package org.example.github.api.client;

import org.example.github.api.client.model.Branch;
import org.example.github.api.client.model.Repository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "github", url = "${github.api.url}")
public interface GithubClient {

    @GetMapping(value = "/users/{username}/repos")
    List<Repository> getRepositories(@PathVariable String username);

    @GetMapping(value = "/repos/{owner}/{repo}/branches")
    List<Branch> getBranches(@PathVariable String owner, @PathVariable String repo);

}
