package com.example.mugbackend.common.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@ReadingConverter
public class DateToLocalDateTimeKstConverter implements Converter<Date, LocalDateTime> {
    @Override
    public LocalDateTime convert(Date source) {
        // MongoDB에서 읽어온 UTC 기준 Date를 LocalDateTime으로 변환한 후,
        // 저장 시 추가된 9시간을 보정하기 위해 9시간을 빼기
        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().minusHours(9);
    }

}