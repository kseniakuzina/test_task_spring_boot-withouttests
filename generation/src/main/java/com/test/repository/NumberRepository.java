package com.test.repository;

import com.test.entity.GeneratedNumber;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository extends KeyValueRepository<GeneratedNumber,String> {
}
