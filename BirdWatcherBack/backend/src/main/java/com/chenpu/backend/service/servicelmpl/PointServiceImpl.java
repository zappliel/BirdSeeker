package com.chenpu.backend.service.servicelmpl;

import com.chenpu.backend.domain.Point;
import com.chenpu.backend.mapper.PointMapper;
import com.chenpu.backend.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {
    @Autowired
    private PointMapper pointMapper;
    @Override
    public int insertPoint(Point point_info){
        return pointMapper.insertPoint(point_info);
    }

    @Override
    public List<Point> getAllPoint() {
        return pointMapper.findAllPoint();
    }

    @Override
    public Point getPointByName(String name) {
        return pointMapper.findPointByName(name);
    }
}
