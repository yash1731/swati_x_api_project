package com.aixtrade.schemaModel;

import lombok.Data;


@Data
public class ResponseError {

    private Integer timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
