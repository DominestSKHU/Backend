package com.dominest.dominestbackend.global.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
@Configuration
public class MongoConfig {
}
