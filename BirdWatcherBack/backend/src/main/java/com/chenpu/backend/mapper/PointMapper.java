package com.chenpu.backend.mapper;

import com.chenpu.backend.domain.Bird;
import com.chenpu.backend.domain.Point;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PointMapper {
    @Select("SELECT name,lat,ing" +
            " FROM point")
    List<Point> findAllPoint();

    @Select("SELECT name, lat, ing " +
            "FROM point " +
            "WHERE name LIKE CONCAT('%', #{name}, '%') " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    Point findPointByName(String name);

    @Insert("INSERT INTO point " +
            "(name,lat,ing) " +
            "VALUES " +
            "(#{name},#{lat},#{ing})")
    int insertPoint(Point point_info);
}
