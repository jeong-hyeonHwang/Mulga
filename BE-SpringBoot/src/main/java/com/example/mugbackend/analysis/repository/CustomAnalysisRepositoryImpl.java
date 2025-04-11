package com.example.mugbackend.analysis.repository;

import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAnalysisRepositoryImpl implements CustomAnalysisRepository{
	private final MongoTemplate mongoTemplate;

	@Override
	@Transactional
	public void updateAnalysis(Analysis analysis, Transaction transaction) {
		Update update = new Update();
		applyChangesToUpdate(update, analysis, transaction);

		Query query = new Query(Criteria.where("_id").is(analysis.getId()));

		mongoTemplate.updateFirst(query, update, Analysis.class);
	}

	@Override
	public void updateAnalysis (
		Analysis beforeAnalysis,
		Analysis currentAnalysis,
		Transaction beforeTransaction,
		Transaction currentTransaction
	) {
		Update update1 = new Update();
		Update update2 = new Update();

		Query query1 = new Query(Criteria.where("_id").is(beforeAnalysis.getId()));
		Query query2 = new Query(Criteria.where("_id").is(currentAnalysis.getId()));

		applyChangesToUpdate(update1, beforeAnalysis, beforeTransaction);
		applyChangesToUpdate(update2, currentAnalysis, currentTransaction);

		BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, Analysis.class);
		bulkOps.updateOne(query1, update1);
		bulkOps.updateOne(query2, update2);

		bulkOps.execute();
	}

	@Override
	public void updateAnalysis(
		Analysis analysis,
		Transaction beforeTransaction,
		Transaction currentTransaction
	) {
		Update update = new Update();
		Query query = new Query(Criteria.where("_id").is(analysis.getId()));

		applyChangesToUpdate(update, analysis, beforeTransaction);
		applyChangesToUpdate(update, analysis, currentTransaction);

		mongoTemplate.updateFirst(query, update, Analysis.class);
	}

	private void applyChangesToUpdate(Update update, Analysis analysis, Transaction transaction) {
		Integer cost = transaction.getCost();
		String category = transaction.getCategory();
		int day = transaction.getDay();
		String paymentMethod = transaction.getPaymentMethod();

		Analysis.DailyAmount dailyAmount = analysis.getDaily().get(day);
		Integer dailyExpense = dailyAmount.getExpense();
		Integer dailyIncome = dailyAmount.getIncome();

		if(cost < 0) {
			update.set("monthTotal", analysis.getMonthTotal());
			update.set("category." + category, analysis.getCategory().get(category));
			update.set("daily." + day + ".expense", dailyExpense);

			if(analysis.getPaymentMethod().get(paymentMethod) == null) {
				update.unset("paymentMethod." + paymentMethod);
			}
			else {
				update.set("paymentMethod." + paymentMethod, analysis.getPaymentMethod().get(paymentMethod));
			}
		}
		else {
			update.set("daily." + day + ".income", dailyIncome);
		}

		if(dailyIncome == 0 && dailyExpense == 0) {
			update.set("daily." + day + ".isValid", false);
		}
		else {
			update.set("daily." + day + ".isValid", true);
		}
	}
}
