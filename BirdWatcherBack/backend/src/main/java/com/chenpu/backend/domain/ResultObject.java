package com.chenpu.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//存放返回信息
public class ResultObject {

    private int code;
    private String msg;
    //success or fail
    private Object result;
    //对应的实体类信息
}
