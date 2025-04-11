package com.example.mugbackend.common.config;



import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@WritingConverter
public class LocalDateTimeToDateKstConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(LocalDateTime source) {
        // 저장 전, LocalDateTime 값에 9시간을 더해서 보정
        return Timestamp.valueOf(source.plusHours(9));
    }

}
