package com.chenpu.backend.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//表示返回多个结果的多次查询
public class PageObject {
    private Object list;
    private int page_num;
    private int page_size;
    private long total;
}