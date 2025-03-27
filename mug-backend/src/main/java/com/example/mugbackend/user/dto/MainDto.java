package com.example.mugbackend.user.dto;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainDto {
    private int monthTotal;
    private int remainingBudget;
    private Transaction lastTransaction;
}
/*
{
    "monthTotal": 33534,
    "remainingBudget": 234234,
    "lastTransaction": {
        "_id": {
            "$oid": "67e241927be4585f3d4c7c47"
        },
        "year": 2025,
        "month": 3,
        "day": 25,
        "isCombined": false,
        "title": "아이스아메리카노",
        "cost": -3000,
        "category": "cafe",
        "memo": "hello",
        "vendor": "바나프레소",
        "time": "2025-03-25T14:45:00Z",
        "paymentMethod": "네이버페이",
        "group": []
    }
}
 */