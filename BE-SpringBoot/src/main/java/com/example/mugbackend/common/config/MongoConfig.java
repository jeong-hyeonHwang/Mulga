package com.example.mugbackend.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions(
            LocalDateTimeToDateKstConverter localDateTimeToDateKstConverter,
            DateToLocalDateTimeKstConverter dateToLocalDateTimeKstConverter
    ) {
        return new MongoCustomConversions(
                List.of(localDateTimeToDateKstConverter, dateToLocalDateTimeKstConverter)
        );
    }

}