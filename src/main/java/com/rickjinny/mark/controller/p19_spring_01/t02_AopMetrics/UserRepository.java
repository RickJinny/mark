package com.rickjinny.mark.controller.p19_spring_01.t02_AopMetrics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByName(String name);

}
