package com.chenpu.backend.service;

import com.chenpu.backend.domain.Bird;
import com.chenpu.backend.domain.Infer;

import java.util.List;

public interface BirdService {
    public Bird getBirdByName(String name) throws Exception;
    public List<String> getBirdByKeyWord(String word);
    public int insertBird(Bird bird_info);
    public Bird getRandomBird();
    public List<Bird> getSixBirds();
    public Infer inferBird();
}
