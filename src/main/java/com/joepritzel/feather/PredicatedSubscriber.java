package com.joepritzel.feather;

/**
 * A subscriber that gets messages if they apply to it.
 * 
 * @author Joe Pritzel
 * 
 * @param <T>
 *            - The type of messages to subscribe to.
 */
public abstract class PredicatedSubscriber<T> extends Subscriber<T> {

	/**
	 * Returns true if the message applies to this subscriber. Otherwise, false.
	 * 
	 * @param message
	 *            - The message in question.
	 * @exception SubscriberTypeMismatchException
	 *                the type the subscriber was bound to is incompatible with
	 *                the type that is being received.
	 */
	public abstract boolean applies(T message);

	/**
	 * A method that casts the message and calls the method that uses generics.<br>
	 * Do not override.
	 * 
	 * @param o
	 *            - The message.
	 * @return Returns the same as the applies method.
	 * 
	 * @exception SubscriberTypeMismatchException
	 *                the type the subscriber was bound to is incompatible with
	 *                the type that is being received.
	 */
	public boolean appliesO(Object o) {
		try {
			@SuppressWarnings("unchecked")
			T o2 = (T) o;
			return applies(o2);
		} catch (ClassCastException e) {
			throw new SubscriberTypeMismatchException(e);
		}
	}
}
