package com.example.catalogue.messaging;

public final class MeubleMessagingConstants {

	private MeubleMessagingConstants() {
	}

	public static final String EXCHANGE = "meuble.topic";
	public static final String RK_ORDER_STOCK_VALIDATE = "order.stock.validate";
	public static final String RK_ORDER_STOCK_RESULT = "order.stock.result";
	public static final String QUEUE_CATALOGUE_STOCK_VALIDATE = "q.catalogue.order.stock.validate";
}
