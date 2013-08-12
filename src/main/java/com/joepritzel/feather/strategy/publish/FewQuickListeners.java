package com.joepritzel.feather.strategy.publish;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import com.joepritzel.feather.PredicatedSubscriber;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.UnreadMessage;
import com.joepritzel.feather.internal.Publish;
import com.joepritzel.feather.internal.SubscriberParent;

/**
 * This publish strategy is optimized for few listeners that are not
 * computationally expensive.
 * 
 * @author Joe Pritzel
 * 
 */
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

	/**
	 * @param exec
	 *            - The executor to be used.
	 * @param considerHierarchy
	 *            - If the class hierarchy should be considered when determining
	 *            types a subscriber is subscribed to.
	 */
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
				if (!sent && p.mapping.containsKey(UnreadMessage.class)) {
					p.broker.publish(new UnreadMessage(p.message));
				}
			}
		});
	}

	/**
	 * Iterates through the subscribers, considering the type hierarchy, and
	 * invokes the receiveO method.
	 * 
	 * @param p
	 *            - The publish object.
	 * @return If a message was published to at least one subscriber, then it
	 *         will return true. Otherwise, false.
	 */
	private boolean consideringHierarchy(Publish p) {
		boolean sent = false;
		for (Entry<Class<?>, List<SubscriberParent>> e : p.mapping.entrySet()) {
			if (e.getKey().isAssignableFrom(p.message.getClass())) {
				for (SubscriberParent parent : e.getValue()) {
					if (!predicateApplies(parent.getSubscriber(), p.message)) {
						continue;
					}
					parent.getSubscriber().receiveO(p.message);
					sent = true;
				}
			}
		}
		return sent;
	}

	/**
	 * Iterates through the subscribers invoking the receiveO method.
	 * 
	 * @param p
	 *            - The publish object.
	 * @return If a message was published to at least one subscriber, then it
	 *         will return true. Otherwise, false.
	 */
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
			s.receiveO(p.message);
			sent = true;
		}
		return sent;
	}

	/**
	 * Checks to see if the subscriber is a predicated subscriber, and if it
	 * applies.
	 * 
	 * @param s
	 *            - The subscriber to check.
	 * @param message
	 *            - The message to check.
	 * @return If the subscriber is not predicated or it is and applies, then it
	 *         returns true. If it's a predicated subscriber, and it doesn't
	 *         apply, then it returns false.
	 */
	private boolean predicateApplies(Subscriber<?> s, Object message) {
		if (s instanceof PredicatedSubscriber
				&& !((PredicatedSubscriber<?>) s).appliesO(message)) {
			return false;
		}
		return true;
	}

}
