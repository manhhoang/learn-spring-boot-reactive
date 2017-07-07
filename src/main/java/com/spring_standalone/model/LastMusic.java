package com.spring_standalone.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LastMusic {

    @JsonProperty("albummatches")
    private List<Album> albums;
}
