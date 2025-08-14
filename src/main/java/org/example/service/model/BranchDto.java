package org.example.service.model;

public record BranchDto (
        String name,
        String lastCommitSHA
) {}
