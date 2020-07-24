package com.rickjinny.mark.controller.p15_serialization.t02_JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRight {
    private String name;
}
