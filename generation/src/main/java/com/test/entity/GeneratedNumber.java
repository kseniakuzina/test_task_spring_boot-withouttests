package com.test.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("generated_number")
public class GeneratedNumber{
    @Id
    private String id;
    private String number;

    public GeneratedNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
