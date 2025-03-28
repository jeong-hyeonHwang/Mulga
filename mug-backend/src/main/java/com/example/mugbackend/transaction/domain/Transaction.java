package com.example.mugbackend.transaction.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
	@Id
	private String id;

	@Field(name = "user_id")
	private String userId;

	private Integer year;
	private Integer month;
	private Integer day;
	private Boolean isCombined;
	private String title;
	private Integer cost;
	private String category;
	private String memo;
	private String vendor;
	private String bank;
	private LocalDateTime time;
	private String paymentMethod;
	private List<Transaction> group;
}
