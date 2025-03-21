package com.chenpu.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InferBirdInfo {
    String label;
    double score;
    String name;
    String protonym;
    String chinese;
    String geo;

    public InferBirdInfo(Bird bird,Infer infer){
        this.label = infer.getLabel();
        this.score = infer.getScore();
        this.name = bird.getName();
        this.protonym = bird.getProtonym();
        this.chinese = bird.getChinese();
        this.geo = bird.getGeo();
    }
}
