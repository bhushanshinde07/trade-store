package com.barclays.tradesstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;

@Repository
public interface TradeRepository extends JpaRepository<Trade, TradeId> {

	@Query(value = "SELECT * FROM TRADE_STORE WHERE maturity_date < CURRENT_DATE() AND (expired IN ('N') OR expired IS NULL)", nativeQuery = true)
	List<Trade> getAllExpiredTradeRecords();

	@Modifying
	@Query(value = "UPDATE TRADE_STORE SET expired='Y' WHERE maturity_date < CURRENT_DATE() AND (expired IN ('N') OR expired IS NULL)", nativeQuery = true)
	Integer updateAllExpiredTradeRecords();

	@Query(value = "SELECT * FROM TRADE_STORE WHERE trade_id = :trade_id AND country_party_id = :country_party_id  AND book_id = :book_id", nativeQuery = true)
	Optional<Trade> findByTradeId(@Param("trade_id") String trade_id,
			@Param("country_party_id") String country_party_id,
			@Param("book_id") String book_id);

}
