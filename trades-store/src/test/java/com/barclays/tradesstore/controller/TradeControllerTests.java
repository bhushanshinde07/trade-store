package com.barclays.tradesstore.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;
import com.barclays.tradesstore.exception.LessMaturityDateException;
import com.barclays.tradesstore.repository.TradeRepository;
import com.barclays.tradesstore.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TradeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TradeService service;

	@MockBean
	private TradeRepository repository;

	@Test
	@DisplayName("POST /store/trade  [Store allow the trade - Success]")
	void addTradeTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate, "N");

		Mockito.when(service.createTrade(trade)).thenReturn(trade);

		mockMvc.perform(MockMvcRequestBuilders.post("/store/trade").content(asJsonString(trade))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andDo(print());
	}

	@Test
	@DisplayName("POST /store/trade  [Store allow the existing trade - Success]")
	void updateExistingTradeTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade existingTrade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2035-05-20"), currentDate,
				"N");
		Trade updateTradeRequest = new Trade(new TradeId("T1", "CP-1", "B1"), 2, dateFormat.parse("2035-05-20"),
				currentDate, "N");

		Mockito.when(service.createTrade(updateTradeRequest)).thenReturn(existingTrade);

		mockMvc.perform(MockMvcRequestBuilders.post("/store/trade").content(asJsonString(updateTradeRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andDo(print());
	}

	@Test
	@DisplayName("POST /store/trade  [Store should not allow the trade which has less maturity date than today date - Rejected]")
	void addTradeThrowLessMaturityDateExceptionTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat.parse("2020-05-20"), currentDate, "N");

		Mockito.when(service.createTrade(trade))
				.thenThrow(new LessMaturityDateException("Maturity date should not less than current date."));

		mockMvc.perform(MockMvcRequestBuilders.post("/store/trade").content(asJsonString(trade))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andDo(print());
	}

	@Test
	@DisplayName("POST /store/trade  [Store should not allow if given trade version is lower than existing trade version present in store - Rejected]")
	void addTradeThrowLowerVersionExceptionTest() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Trade trade = new Trade(new TradeId("T1", "CP-2", "B1"), 0, dateFormat.parse("2035-05-20"), currentDate, "N");

		Mockito.when(service.createTrade(trade)).thenThrow(new LessMaturityDateException(
				"Given trade version is lower than existing trade version present in store."));

		mockMvc.perform(MockMvcRequestBuilders.post("/store/trade").content(asJsonString(trade))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError()).andDo(print());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
