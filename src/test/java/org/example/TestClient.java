package org.example;

import org.example.service.model.GithubUserRepositoriesResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "test", url = "localhost:${server.port}")
public interface TestClient {

    @GetMapping("/repositories/{username}")
    List<GithubUserRepositoriesResponseDto> getRepositories(@PathVariable final String username);

}
