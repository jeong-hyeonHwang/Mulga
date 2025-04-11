package com.example.mugbackend.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomTransactionRepositoryImpl implements CustomTransactionRepository {
	private final MongoTemplate mongoTemplate;

	@Override
	public Optional<Transaction> updateTransaction(CustomUserDetails userDetails, Transaction transaction) {
		Criteria criteria = Criteria.where("_id").is(transaction.getId()).and("user_id").is(userDetails.id());
		Update update = toUpdate(transaction);

		Transaction updatedTransaction = mongoTemplate.findAndModify(
			new Query(criteria),
			update,
			new FindAndModifyOptions().returnNew(true),
			Transaction.class
		);

		return Optional.ofNullable(updatedTransaction);
	}

	@Override
	public void updateGroupAndCostById(String transactionId, List<Transaction> newGroup, Integer newCost) {
		Query query = new Query(Criteria.where("_id").is(transactionId));

		Update update = new Update()
			.set("group", newGroup) // Update the `group` field
			.set("cost", newCost);  // Update the `cost` field

		mongoTemplate.updateFirst(query, update, Transaction.class);
	}

	private Update toUpdate(Transaction transaction) {
		Update update = new Update();
		Integer year = transaction.getYear();
		Integer month = transaction.getMonth();
		Integer day = transaction.getDay();
		String title = transaction.getTitle();
		Integer cost = transaction.getCost();
		String category = transaction.getCategory();
		String memo = transaction.getMemo();
		String vendor = transaction.getVendor();
		LocalDateTime time = transaction.getTime();
		String paymentMethod = transaction.getPaymentMethod();

		if (year != null) update.set("year", year);
		if (month != null) update.set("month", month);
		if (day != null) update.set("day", day);
		if (title != null) update.set("title", title);
		if (cost != null) update.set("cost", cost);
		if (category != null) update.set("category", category);
		if (memo != null) update.set("memo", memo);
		if (vendor != null) update.set("vendor", vendor);
		if (time != null) update.set("time", time);
		if (paymentMethod != null) update.set("paymentMethod", paymentMethod);

		return update;
	}
}
