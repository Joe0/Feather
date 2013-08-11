package com.joepritzel.feather;

import java.util.concurrent.Executor;

import com.joepritzel.feather.internal.MutablePSBroker;
import com.joepritzel.feather.strategy.publish.PublishStrategy;

public class PSBrokerBuilder {

	private MutablePSBroker broker;

	public PSBrokerBuilder(Executor exec) {
		broker = new MutablePSBroker();
		broker.exec = exec;
	}

	public PSBrokerBuilder considerClassHierarchy(boolean considerHierarchy) {
		broker.considerHierarchy = considerHierarchy;
		return this;
	}
	
	public PSBrokerBuilder publishStrategy(PublishStrategy strategy) {
		broker.publishStrategy = strategy;
		return this;
	}	

	public PSBroker build() {
		return broker.toPSBroker();
	}
}
