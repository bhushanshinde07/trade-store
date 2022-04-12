package com.barclays.tradesstore.entity;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TRADE_STORE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

	@EmbeddedId
	private TradeId tradeId;
	private Integer version;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date maturity_date;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date created_date;
	private String expired;

}
