package com.chenpu.backend.mapper;

import com.chenpu.backend.domain.Bird;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BirdMapper {
    @Select("SELECT name, introduce, picture, protonym, geo, chinese " +
            "FROM bird " +
            "WHERE name LIKE CONCAT('%', #{name}, '%') " +
            "OR protonym LIKE CONCAT('%', #{name}, '%') " +
            "OR chinese LIKE CONCAT('%', #{name}, '%') " +
            "LIMIT 1")
    Bird findByName(String name);


    @Insert("INSERT INTO bird" +
            "name,introduce,picture,protonym,geo,chinese " +
            "VALUES" +
            "(#{name},#{introduce},#{picture},#{protonym},#{geo},#{chinese})")
    int insert(Bird birds);

    @Select("SELECT name,introduce,picture,protonym,geo,chinese" +
            " FROM bird ORDER BY RAND() LIMIT 1")
    Bird getRandom();

    @Select("SELECT DISTINCT name, introduce, picture, protonym, geo, chinese " +
            "FROM bird ORDER BY RAND() LIMIT 6")
    List<Bird> getSixRandomBirds();
}
