package com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata;

import com.rickjinny.mark.controller.p30_sensitivedata.t02_sensitivedata.bean.CipherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CipherRepository extends JpaRepository<CipherData, Long> {

}
