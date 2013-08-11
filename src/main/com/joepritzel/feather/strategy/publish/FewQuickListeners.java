package com.joepritzel.feather.strategy.publish;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import com.joepritzel.feather.PredicatedSubscriber;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.UnreadMessage;
import com.joepritzel.feather.internal.Publish;
import com.joepritzel.feather.internal.SubscriberParent;

public class FewQuickListeners implements PublishStrategy {

	/**
	 * The executor to use.
	 */
	private final Executor exec;

	/**
	 * A flag that is used to determine if the hierarchy of types should be
	 * considered.
	 */
	private final boolean considerHierarchy;

	public FewQuickListeners(Executor exec, boolean considerHierarchy) {
		this.exec = exec;
		this.considerHierarchy = considerHierarchy;
	}

	@Override
	public void publish(final Publish p) {
		exec.execute(new Runnable() {
			@Override
			public void run() {
				boolean sent = false;
				if (considerHierarchy) {
					sent = consideringHierarchy(p);
				} else {
					sent = normal(p);
				}
				if (!sent) {
					p.broker.publish(new UnreadMessage(p.message));
				}
			}
		});
	}

	@Override
	public void invokeReceiveO(Subscriber<?> s, Object msg) {
		s.receiveO(msg);
	}

	private boolean consideringHierarchy(Publish p) {
		boolean sent = false;
		for (Entry<Class<?>, List<SubscriberParent>> e : p.mapping.entrySet()) {
			if (e.getKey().isAssignableFrom(p.message.getClass())) {
				for (SubscriberParent parent : e.getValue()) {
					if (!predicateApplies(parent.getSubscriber(), p.message)) {
						continue;
					}
					invokeReceiveO(parent.getSubscriber(), p.message);
					sent = true;
				}
			}
		}
		return sent;
	}

	private boolean normal(Publish p) {
		boolean sent = false;
		List<SubscriberParent> list = p.mapping.get(p.message.getClass());
		if (list == null) {
			return false;
		}
		for (SubscriberParent sp : list) {
			Subscriber<?> s = sp.getSubscriber();
			if (!predicateApplies(s, p.message)) {
				continue;
			}
			invokeReceiveO(s, p.message);
			sent = true;
		}
		return sent;
	}

	private boolean predicateApplies(Subscriber<?> s, Object message) {
		if (s instanceof PredicatedSubscriber
				&& !((PredicatedSubscriber<?>) s).appliesO(message)) {
			return false;
		}
		return true;
	}

}
