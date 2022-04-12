package com.barclays.tradesstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.service.TradeService;

@RestController
@RequestMapping("/store")
public class TradeController {

	@Autowired
	private TradeService service;

	@PostMapping("/trade")
	public Trade addTrade(@RequestBody Trade trade) throws Exception {
		return service.createTrade(trade);
	}

}
