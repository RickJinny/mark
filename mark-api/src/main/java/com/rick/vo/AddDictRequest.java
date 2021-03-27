package com.rick.vo;

import lombok.Data;

@Data
public class AddDictRequest {

    private Long dictId;

    private String dictType;

    private String dictCode;

    private String dictValue;

}
