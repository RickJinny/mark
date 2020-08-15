package com.rickjinny.mark.controller.p11_null.t03_DBNull;

import com.rickjinny.mark.controller.p11_null.t03_DBNull.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "select sum(score) from `user`")
    Long wrong1();

    @Query(nativeQuery = true, value = "select count(score) from `user`")
    Long wrong2();

    @Query(nativeQuery = true, value = "select * from `user` where score = null")
    List<User> wrong3();

    @Query(nativeQuery = true, value = "select nullif(sum(score), 0) from `user`")
    Long right1();

    @Query(nativeQuery = true, value = "select count(*) from `user`")
    Long right2();

    @Query(nativeQuery = true, value = "select * from `user` where score is null")
    List<User> right3();
}
