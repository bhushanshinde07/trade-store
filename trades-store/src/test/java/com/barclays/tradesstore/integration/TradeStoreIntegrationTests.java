package com.barclays.tradesstore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.barclays.tradesstore.controller.TradeController;
import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;
import com.barclays.tradesstore.exception.LessMaturityDateException;
import com.barclays.tradesstore.exception.LowerVersionException;
import com.barclays.tradesstore.repository.TradeRepository;

@SpringBootTest
public class TradeStoreIntegrationTests {

	@Autowired
	private TradeController controller;

	@Autowired
	private TradeRepository repository;

	@Test
	@DisplayName("Store allow the trade - Success")
	void addTradeTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate, "N");

		Trade tradeResult = controller.addTrade(trade);
		assertEquals(trade.getTradeId().getTrade_id(), tradeResult.getTradeId().getTrade_id());
		assertEquals(trade.getTradeId().getCountry_party_id(), tradeResult.getTradeId().getCountry_party_id());
		assertEquals(trade.getTradeId().getBook_id(), tradeResult.getTradeId().getBook_id());
		assertEquals(trade.getVersion(), tradeResult.getVersion());
		assertEquals(trade.getMaturity_date(), tradeResult.getMaturity_date());
		assertEquals(trade.getCreated_date(), tradeResult.getCreated_date());
		assertEquals(trade.getExpired(), tradeResult.getExpired());
	}

	@Test
	@DisplayName("Store allow to update the existing trade - Success")
	void updateTradeTest() throws Exception {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N")));

		Trade updateTradeRecord = new Trade(new TradeId("T1", "CP-1", "B1"), 2, dateFormat.parse("2035-05-20"),
				currentDate, "N");

		Trade tradeResult = controller.addTrade(updateTradeRecord);
		assertEquals(updateTradeRecord.getTradeId().getTrade_id(), tradeResult.getTradeId().getTrade_id());
		assertEquals(updateTradeRecord.getTradeId().getCountry_party_id(),
				tradeResult.getTradeId().getCountry_party_id());
		assertEquals(updateTradeRecord.getTradeId().getBook_id(), tradeResult.getTradeId().getBook_id());
		assertEquals(updateTradeRecord.getVersion(), tradeResult.getVersion());
		assertEquals(updateTradeRecord.getMaturity_date(), tradeResult.getMaturity_date());
		assertEquals(updateTradeRecord.getCreated_date(), tradeResult.getCreated_date());
		assertEquals(updateTradeRecord.getExpired(), tradeResult.getExpired());
	}

	@Test
	@DisplayName("Store should not allow if given trade version is lower than existing trade version present in store - Rejected")
	void createTradeThrowLowerVersionExceptionTest() throws Exception {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N")));

		Trade updateTradeRecord = new Trade(new TradeId("T1", "CP-1", "B1"), 0, dateFormat.parse("2035-05-20"),
				currentDate, "N");

		Assertions.assertThatExceptionOfType(LowerVersionException.class)
				.isThrownBy(() -> controller.addTrade(updateTradeRecord));
	}

	@Test
	@DisplayName("Store should not allow the trade which has less maturity date than today date - Rejected")
	void createTradeTradeThrowLessMaturityDateExceptionTest() throws Exception {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N")));

		Trade updateTradeRecord = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"),
				currentDate, "N");

		Assertions.assertThatExceptionOfType(LessMaturityDateException.class)
				.isThrownBy(() -> controller.addTrade(updateTradeRecord));
	}

}
