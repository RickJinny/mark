package com.rickjinny.mark.controller.p06_transaction.repository;

import com.rickjinny.mark.controller.p06_transaction.bean.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByName(String name);
}
