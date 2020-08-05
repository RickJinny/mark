package com.rickjinny.mark.controller.p30_sensitivedata.t01_StorePassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {

}
