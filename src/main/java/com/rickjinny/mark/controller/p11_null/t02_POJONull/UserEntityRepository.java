package com.rickjinny.mark.controller.p11_null.t02_POJONull;

import com.rickjinny.mark.controller.p11_null.t02_POJONull.bean.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

}
