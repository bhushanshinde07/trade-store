package com.barclays.tradesstore.repository;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TradeRepositoryTests {

	@Autowired
	private TradeRepository repository;

	@Test
	@DisplayName("Get all expired trade records")
	void getAllExpiredTradeRecordsTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N"),
				new Trade(new TradeId("T2", "CP-2", "B1"), 2, dateFormat.parse("2021-05-20"), currentDate, "N"),
				new Trade(new TradeId("T2", "CP-1", "B1"), 1, dateFormat.parse("2021-05-20"),
						dateFormat.parse("2015-03-14"), "N"),
				new Trade(new TradeId("T3", "CP-3", "B2"), 3, dateFormat.parse("2014-05-20"), currentDate, "Y")));

		List<Trade> allExpiredTradeRecords = repository.getAllExpiredTradeRecords();
		Assertions.assertEquals(3, allExpiredTradeRecords.size());
	}

	@Test
	@DisplayName("Update all expired trade records")
	@Transactional
	void updateAllExpiredTradeRecordsTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N"),
				new Trade(new TradeId("T2", "CP-2", "B1"), 2, dateFormat.parse("2021-05-20"), currentDate, "N"),
				new Trade(new TradeId("T2", "CP-1", "B1"), 1, dateFormat.parse("2021-05-20"),
						dateFormat.parse("2015-03-14"), "N"),
				new Trade(new TradeId("T3", "CP-3", "B2"), 3, dateFormat.parse("2014-05-20"), currentDate, "Y")));

		Assertions.assertEquals(3, repository.updateAllExpiredTradeRecords());
	}

	@Test
	@DisplayName("Find trade by id")
	void findByTradeIdTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N")));

		Optional<Trade> tradeRecord = repository.findByTradeId("T1", "CP-1", "B1");
		Assertions.assertEquals(true, tradeRecord.isPresent());
		Assertions.assertEquals("T1", tradeRecord.get().getTradeId().getTrade_id());
		Assertions.assertEquals("CP-1", tradeRecord.get().getTradeId().getCountry_party_id());
		Assertions.assertEquals("B1", tradeRecord.get().getTradeId().getBook_id());
		Assertions.assertEquals(1, tradeRecord.get().getVersion());
		Assertions.assertEquals(dateFormat.parse("2020-05-20"), tradeRecord.get().getMaturity_date());
		Assertions.assertEquals(currentDate, tradeRecord.get().getCreated_date());
		Assertions.assertEquals("N", tradeRecord.get().getExpired());
	}
}
