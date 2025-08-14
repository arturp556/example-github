package org.example.service;

import org.example.exception.UserNotFoundException;
import org.example.github.api.client.GithubClient;
import org.example.github.api.client.model.Repository;
import org.example.service.model.BranchDto;
import org.example.service.model.GithubUserRepositoriesResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class GithubService {

    private static final Logger log = LoggerFactory.getLogger(GithubService.class);

    private final GithubClient githubClient;

    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    /**
     * @param username
     * @return list of user's github repositories which are not forks
     * @throws UserNotFoundException
     */
    public List<GithubUserRepositoriesResponseDto> getRepositories(final String username) throws UserNotFoundException {
        log.info("Get user: {} repositories", username);
        final List<Repository> repositories;
        try {
            repositories = githubClient.getRepositories(username);
        } catch (Exception e) {
            throw new UserNotFoundException(MessageFormat.format("User: {0} not found", username));
        }
        return repositories.stream()
                .filter(repository -> !repository.fork())
                .map(repository -> {
                    List<BranchDto> branches = githubClient.getBranches(username, repository.name())
                            .stream()
                            .map(branch -> new BranchDto(branch.name(), branch.commit().sha()))
                            .toList();
                    return new GithubUserRepositoriesResponseDto(repository.name(), repository.owner().login(), branches);
                })
                .toList();
    }
}
