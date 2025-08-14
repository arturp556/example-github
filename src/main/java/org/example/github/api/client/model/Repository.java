package org.example.github.api.client.model;

import java.util.List;

public record Repository(
        String name,
        Owner owner,
        List<Branch> branches,
        boolean fork
) {}
