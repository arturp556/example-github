package org.example.github.api.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Branch(
        String name,
        //@JsonProperty("last_commit")
        Commit commit
) {}
