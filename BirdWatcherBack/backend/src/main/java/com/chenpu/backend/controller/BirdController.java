package com.chenpu.backend.controller;

import com.chenpu.backend.domain.Bird;
import com.chenpu.backend.domain.ResultObject;
import com.chenpu.backend.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bird")
@CrossOrigin
public class BirdController {
    @Autowired
    private BirdService birdService;

    @PostMapping("/random")
    public ResultObject getRandomBird(){
        Bird bird_info = birdService.getRandomBird();
        ResultObject resultObject = new ResultObject(200,"success", bird_info);
        return resultObject;
    }

    @PostMapping("/name")
    public ResultObject getBirdName(@RequestParam String name){
        try {
            Bird bird_info = birdService.getBirdByName(name);
            if (bird_info == null){
                ResultObject resultObject = new ResultObject(100,"fail", null);
                return resultObject;
            }else {
                ResultObject resultObject = new ResultObject(200,"success", bird_info);
                return resultObject;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/search")
    public ResultObject getBirdWord(@RequestParam String word){
        List<String> birds = birdService.getBirdByKeyWord(word);
        ResultObject resultObject = new ResultObject(200,"success", birds);
        return resultObject;
    }

    @GetMapping("/six")
    public ResultObject getRandomSixBirds(){
        List<Bird> birds = birdService.getSixBirds();
        ResultObject resultObject = new ResultObject(200,"success", birds);
        return resultObject;
    }
}
