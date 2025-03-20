package com.chenpu.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bird {
    String name;
    String introduce;
    String picture;
    String protonym;
    String chinese;
    String geo;
}
