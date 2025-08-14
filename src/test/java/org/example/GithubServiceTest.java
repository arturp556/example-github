package org.example;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.github.api.client.GithubClient;
import org.example.service.GithubService;
import org.example.service.model.BranchDto;
import org.example.service.model.GithubUserRepositoriesResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WireMockTest(httpPort = 9999)
public class GithubServiceTest {

    @Autowired
    private GithubService githubService;

    @Autowired
    private GithubClient githubClient;

    @Autowired
    private TestClient testClient;

    @Test
    @DisplayName("Get non fork repositories with branches")
    public void getNonForkRepositoriesWithBranches() throws IOException {

//        given:
        final String userName = "zio";
        final String expectedRepositoryName = "zio-aws";
        final String expectedBranchName = "gh-pages";
        final String expectedLastCommitSHA = "5283ed8e3f3f35477d6aa95c02d7c2237d8b9ef8";
        final String forkedRepositoryName = "FrameworkBenchmarks";

        stubFor(get(urlEqualTo("/users/zio/repos"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/vnd.github.v3+json")
                .withBody(IOUtils.resourceToString("/github/repos-zio.json", StandardCharsets.UTF_8))));

        stubFor(get(urlEqualTo("/repos/zio/zio-aws/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/vnd.github.v3+json")
                        .withBody(IOUtils.resourceToString("/github/repos-zio-branches-aws.json", StandardCharsets.UTF_8))));


//        when:
        final List<GithubUserRepositoriesResponseDto> repositories = testClient.getRepositories(userName);
        repositories.forEach(System.out::println);


//        then:
        assertFalse(repositories.isEmpty(), "Response should return user repositories");
        final Optional<GithubUserRepositoriesResponseDto> repositoryWithExpectedNameOpt = repositories.stream()
                .filter(dto -> dto.name().equals(expectedRepositoryName))
                .findAny();
        assertTrue(repositoryWithExpectedNameOpt.isPresent(), "Response should containt a repository with expected name");
        final Optional<BranchDto> expectedBranch = repositoryWithExpectedNameOpt.get().branches().stream()
                .filter(branchDto -> branchDto.name().equals(expectedBranchName))
                .findAny();
        assertTrue(expectedBranch.isPresent(), "Response should containt a branch with expected name");
        assertEquals(expectedLastCommitSHA, expectedBranch.get().lastCommitSHA(), "Response should containt a branch with expected commit SHA");
    }
}

