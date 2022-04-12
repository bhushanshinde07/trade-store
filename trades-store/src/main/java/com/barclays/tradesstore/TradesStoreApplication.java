package com.barclays.tradesstore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.barclays.tradesstore.entity.Trade;
import com.barclays.tradesstore.entity.TradeId;
import com.barclays.tradesstore.repository.TradeRepository;

@EnableScheduling
@SpringBootApplication
public class TradesStoreApplication implements CommandLineRunner {

	@Autowired
	private TradeRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(TradesStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Insert sample data into table.
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		repository.saveAll(Arrays.asList(
				new Trade(new TradeId("T1", "CP-1", "B1"), 1, dateFormat
						.parse("2020-05-20"), currentDate, "N"),
				new Trade(new TradeId("T2", "CP-2", "B1"), 2, dateFormat
						.parse("2021-05-20"), currentDate, "N"),
				new Trade(new TradeId("T2", "CP-1", "B1"), 1, dateFormat
						.parse("2021-05-20"), dateFormat.parse("2015-03-14"),
						"N"), new Trade(new TradeId("T3", "CP-3", "B2"), 3,
						dateFormat.parse("2014-05-20"), currentDate, "Y")));
	}
}
