package com.example.chenpu.birdaudioapp.entity;

public class BirdShortInfo {
    private String chinese; // 中文名
    private String picture; // 图片链接

    public BirdShortInfo(String chinese, String picture) {
        this.chinese = chinese;
        this.picture = picture;
    }

    public String getChinese() {
        return chinese;
    }

    public String getPicture() {
        return picture;
    }
}
