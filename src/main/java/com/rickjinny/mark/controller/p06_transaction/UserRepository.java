package com.rickjinny.mark.controller.p06_transaction;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    List<UserEntity> findByName(String name);
}
