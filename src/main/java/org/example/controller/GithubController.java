package org.example.controller;

import org.example.service.GithubService;
import org.example.service.model.GithubUserRepositoriesResponseDto;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class GithubController {
    private final GithubService githubService;

    GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/repositories/{username}")
    List<GithubUserRepositoriesResponseDto> getRepositories(@PathVariable final String username) throws HttpMediaTypeNotSupportedException {
        return githubService.getRepositories(username);
    }
}
