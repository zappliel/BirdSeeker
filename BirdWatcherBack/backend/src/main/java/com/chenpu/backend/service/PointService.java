package com.chenpu.backend.service;

import com.chenpu.backend.domain.Point;

import java.util.List;

public interface PointService {
    public int insertPoint(Point point_info);
    public List<Point> getAllPoint();
    public Point getPointByName(String name);
}
