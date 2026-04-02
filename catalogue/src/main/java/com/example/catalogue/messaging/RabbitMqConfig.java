package com.example.catalogue.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Bean
	TopicExchange meubleTopicExchange() {
		return new TopicExchange(MeubleMessagingConstants.EXCHANGE, true, false);
	}

	@Bean
	Queue catalogueStockValidateQueue() {
		return new Queue(MeubleMessagingConstants.QUEUE_CATALOGUE_STOCK_VALIDATE, true);
	}

	@Bean
	Binding catalogueStockValidateBinding(Queue catalogueStockValidateQueue, TopicExchange meubleTopicExchange) {
		return BindingBuilder.bind(catalogueStockValidateQueue)
				.to(meubleTopicExchange)
				.with(MeubleMessagingConstants.RK_ORDER_STOCK_VALIDATE);
	}

	@Bean
	MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
