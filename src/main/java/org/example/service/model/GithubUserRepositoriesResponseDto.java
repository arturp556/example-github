package org.example.service.model;

import org.example.github.api.client.model.Branch;

import java.util.List;

public record GithubUserRepositoriesResponseDto(String name, String ownerLogin, List<BranchDto> branches) {

}
