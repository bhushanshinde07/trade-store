package com.barclays.tradesstore.scheduler;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.barclays.tradesstore.repository.TradeRepository;

@Component
public class Scheduler {

	@Autowired
	private TradeRepository repository;

	@Scheduled(cron = "0 * * ? * *")
	@Transactional
	public void updateTradeExpireFlag() {
		Integer totalExpiredRecords = repository.updateAllExpiredTradeRecords();
		System.out.println("Total updated expired trade records ::"
				+ totalExpiredRecords);
	}

}
