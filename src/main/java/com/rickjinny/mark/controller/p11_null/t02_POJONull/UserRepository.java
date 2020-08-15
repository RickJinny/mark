package com.rickjinny.mark.controller.p11_null.t02_POJONull;

import com.rickjinny.mark.controller.p11_null.t02_POJONull.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
