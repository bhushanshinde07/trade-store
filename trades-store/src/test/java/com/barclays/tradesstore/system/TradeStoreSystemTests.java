package com.barclays.tradesstore.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;
import com.barclays.tradesstore.repository.TradeRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TradeStoreSystemTests {

	@Autowired
	private TradeRepository repository;

	private static final String SERVER_URL = "http://localhost:9090/store/trade";

	private static final RestTemplate REST_TEMPLATE = new RestTemplate();

	@Test
	@DisplayName("Store allow the trade - Success")
	void addTradeTest() throws Exception {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate, "N");

		ResponseEntity<Trade> entity = REST_TEMPLATE.postForEntity(SERVER_URL, trade, Trade.class);

		assertEquals(trade.getTradeId().getTrade_id(), entity.getBody().getTradeId().getTrade_id());
		assertEquals(trade.getTradeId().getCountry_party_id(), entity.getBody().getTradeId().getCountry_party_id());
		assertEquals(trade.getTradeId().getBook_id(), entity.getBody().getTradeId().getBook_id());
		assertEquals(trade.getVersion(), entity.getBody().getVersion());
		assertEquals(trade.getMaturity_date(), entity.getBody().getMaturity_date());
		assertEquals(trade.getCreated_date(), entity.getBody().getCreated_date());
		assertEquals(trade.getExpired(), entity.getBody().getExpired());
		assertEquals(HttpStatus.OK, entity.getStatusCode());

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

		ResponseEntity<Trade> entity = REST_TEMPLATE.postForEntity(SERVER_URL, updateTradeRecord, Trade.class);

		assertEquals(updateTradeRecord.getTradeId().getTrade_id(), entity.getBody().getTradeId().getTrade_id());
		assertEquals(updateTradeRecord.getTradeId().getCountry_party_id(),
				entity.getBody().getTradeId().getCountry_party_id());
		assertEquals(updateTradeRecord.getTradeId().getBook_id(), entity.getBody().getTradeId().getBook_id());
		assertEquals(updateTradeRecord.getVersion(), entity.getBody().getVersion());
		assertEquals(updateTradeRecord.getMaturity_date(), entity.getBody().getMaturity_date());
		assertEquals(updateTradeRecord.getCreated_date(), entity.getBody().getCreated_date());
		assertEquals(updateTradeRecord.getExpired(), entity.getBody().getExpired());
		assertEquals(HttpStatus.OK, entity.getStatusCode());
	}

	@Test
	@DisplayName("Store should not allow if given trade version is lower than existing trade version present in store - Rejected")
	void createTradeThrowLowerVersionExceptionTest() throws ParseException {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N")));

		Trade updateTradeRecord = new Trade(new TradeId("T1", "CP-1", "B1"), 0, dateFormat.parse("2035-05-20"),
				currentDate, "N");
		HttpClientErrorException e = assertThrows(HttpClientErrorException.class, () -> {
			REST_TEMPLATE.postForEntity(SERVER_URL, updateTradeRecord, Trade.class);
		});

		assertEquals(true,
				e.getMessage().contains("Given trade version is lower than existing trade version present in store."));
		assertEquals(true, e.getMessage().contains("400"));

	}

	@Test
	@DisplayName("Store should not allow if given trade version is lower than existing trade version present in store - Rejected")
	void createTradeTradeThrowLessMaturityDateExceptionTest() throws Exception {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N");

		HttpClientErrorException e = assertThrows(HttpClientErrorException.class, () -> {
			REST_TEMPLATE.postForEntity(SERVER_URL, trade, Trade.class);
		});

		assertEquals(true, e.getMessage().contains("Maturity date should not less than current date."));
		assertEquals(true, e.getMessage().contains("400"));

	}

}
