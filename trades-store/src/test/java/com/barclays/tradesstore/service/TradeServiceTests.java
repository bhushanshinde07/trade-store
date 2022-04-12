package com.barclays.tradesstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;
import com.barclays.tradesstore.exception.LessMaturityDateException;
import com.barclays.tradesstore.exception.LowerVersionException;
import com.barclays.tradesstore.repository.TradeRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TradeServiceTests {

	@InjectMocks
	private TradeService service;

	@Mock
	private TradeRepository repository;

	@Test
	@DisplayName("Store allow the trade - Success")
	void createTradeTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate, "N");
		Mockito.when(repository.save(trade)).thenReturn(trade);

		Trade tradeRecord = service.createTrade(trade);
		assertEquals(trade.getTradeId().getTrade_id(), tradeRecord.getTradeId().getTrade_id());
		assertEquals(trade.getTradeId().getCountry_party_id(), tradeRecord.getTradeId().getCountry_party_id());
		assertEquals(trade.getTradeId().getBook_id(), tradeRecord.getTradeId().getBook_id());
		assertEquals(trade.getVersion(), tradeRecord.getVersion());
		assertEquals(trade.getMaturity_date(), tradeRecord.getMaturity_date());
		assertEquals(trade.getCreated_date(), tradeRecord.getCreated_date());
		assertEquals(trade.getExpired(), tradeRecord.getExpired());
	}

	@Test
	@DisplayName("Store allow to update the existing trade - Success")
	void updateTradeTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate, "N");

		Trade updateTradeRecord = new Trade(new TradeId("T1", "CP-1", "B1"), 2, dateFormat.parse("2035-05-20"),
				currentDate, "N");
		Mockito.when(repository.save(trade)).thenReturn(trade);
		Mockito.when(repository.save(updateTradeRecord)).thenReturn(updateTradeRecord);

		Trade tradeRecord = service.createTrade(updateTradeRecord);
		assertEquals(updateTradeRecord.getTradeId().getTrade_id(), tradeRecord.getTradeId().getTrade_id());
		assertEquals(updateTradeRecord.getTradeId().getCountry_party_id(), tradeRecord.getTradeId().getCountry_party_id());
		assertEquals(updateTradeRecord.getTradeId().getBook_id(), tradeRecord.getTradeId().getBook_id());
		assertEquals(updateTradeRecord.getVersion(), tradeRecord.getVersion());
		assertEquals(updateTradeRecord.getMaturity_date(), tradeRecord.getMaturity_date());
		assertEquals(updateTradeRecord.getCreated_date(), tradeRecord.getCreated_date());
		assertEquals(updateTradeRecord.getExpired(), tradeRecord.getExpired());
	}

	@Test
	@DisplayName("Store should not allow if given trade version is lower than existing trade version present in store - Rejected")
	void createTradeThrowLowerVersionExceptionTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Optional<Trade> existingTradeRecord = Optional.ofNullable(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate, "N"));
		Mockito.when(repository.findByTradeId("T1", "CP-1", "B1")).thenReturn(existingTradeRecord);

		Trade lowVersiontrade = new Trade(new TradeId("T1", "CP-1", "B1"), 0, dateFormat.parse("2035-05-20"),
				currentDate, "N");

		Exception exception = assertThrows(LowerVersionException.class, () -> service.createTrade(lowVersiontrade));
		assertTrue(exception.getMessage()
				.contains("Given trade version is lower than existing trade version present in store."));
	}

	@Test
	@DisplayName("Store should not allow if given trade version is lower than existing trade version present in store - Rejected")
	void createTradeTradeThrowLessMaturityDateExceptionTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade lessMaturityDatetrade = new Trade(new TradeId("T1", "CP-1", "B1"), 0, dateFormat.parse("2020-05-20"),
				currentDate, "N");

		Exception exception = assertThrows(LessMaturityDateException.class,
				() -> service.createTrade(lessMaturityDatetrade));
		assertTrue(exception.getMessage().contains("Maturity date should not less than current date."));
	}

}
