package com.barclays.tradesstore.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TradeId implements Serializable{

	private String trade_id;
	private String country_party_id;
	private String book_id;

}
