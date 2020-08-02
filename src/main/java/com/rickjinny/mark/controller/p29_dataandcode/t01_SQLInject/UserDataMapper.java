package com.rickjinny.mark.controller.p29_dataandcode.t01_SQLInject;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDataMapper {

    @Select("select id, name from `userdata` where name like '%${name}%'")
    List<UserData> findByNameWrong(@Param("name") String name);

    List<UserData> findByNamesWrong(@Param("names") String names);

    @Select("select id, name from `userdata` where name like concat('%', #{name}, '%')")
    List<UserData> findByNameRight(@Param("name") String name);

    List<UserData> findByNamesRight(@Param("names") List<String> names);

}
