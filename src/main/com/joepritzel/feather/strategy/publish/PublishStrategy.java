package com.joepritzel.feather.strategy.publish;

import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.internal.Publish;

public interface PublishStrategy {
	public void publish(Publish message);

	public void invokeReceiveO(Subscriber<?> s, Object msg);
}
