package com.chenpu.backend.controller;

import com.chenpu.backend.domain.Point;
import com.chenpu.backend.domain.ResultObject;
import com.chenpu.backend.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@CrossOrigin
public class PointController {
    @Autowired
    private PointService pointService;

    @GetMapping("/map")
    public ResultObject getAllPoints(){
        List<Point> point_info = pointService.getAllPoint();
        ResultObject resultObject = new ResultObject(200,"success", point_info);
        return resultObject;
    }

    @GetMapping("/gain")
    public ResultObject getPointsByName(@RequestParam String name){
        Point point_info = pointService.getPointByName(name);
        ResultObject resultObject = new ResultObject(200,"success", point_info);
        return resultObject;
    }

    @PostMapping("/add")
    public ResultObject insertPoint(@RequestParam("name") String name,
                                    @RequestParam("lat") String lat,
                                    @RequestParam("ing") String ing){
        Point point_info = new Point(name,lat,ing);
        int result = pointService.insertPoint(point_info);
        ResultObject resultObject = new ResultObject(200,"success", result);
        return resultObject;
    }
}
