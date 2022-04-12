package com.barclays.tradesstore.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.exception.LessMaturityDateException;
import com.barclays.tradesstore.exception.LowerVersionException;
import com.barclays.tradesstore.repository.TradeRepository;

@Service
public class TradeService {

	@Autowired
	private TradeRepository repository;

	public Trade createTrade(Trade trade) {
		// validate version
		validateTradeVersion(trade);
		// validate maturity date
		validateMaturityDate(trade);
		return repository.save(trade);
	}

	private void validateMaturityDate(Trade trade) {
		Date currentDate = new Date();
		int dateComparison = trade.getMaturity_date().compareTo(currentDate);
		if (dateComparison == -1) {
			throw new LessMaturityDateException("Maturity date should not less than current date.");
		}
	}

	private void validateTradeVersion(Trade trade) {
		Optional<Trade> existingTradeRecord = repository.findByTradeId(trade.getTradeId().getTrade_id(),
				trade.getTradeId().getCountry_party_id(), trade.getTradeId().getBook_id());
		if (existingTradeRecord.isPresent() && trade.getVersion() < existingTradeRecord.get().getVersion()) {
			throw new LowerVersionException(
					"Given trade version is lower than existing trade version present in store.");
		}
	}
}
