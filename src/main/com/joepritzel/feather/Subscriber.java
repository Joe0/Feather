package com.joepritzel.feather;

/**
 * 
 * @author Joe Pritzel
 * 
 * @param <T>
 *            - The type of message to listen for.
 */
public abstract class Subscriber<T> {

	/**
	 * The method that will be invoked when the subscriber receives a message.
	 * 
	 * @param message
	 *            - The message.
	 * @exception SubscriberTypeMismatchException
	 *                the type the subscriber was bound to is incompatible with
	 *                the type that is being received.
	 */
	public abstract void receive(T message);

	/**
	 * A method that casts the message and calls the method that uses generics.<br>
	 * Do not overide.
	 * 
	 * @param o
	 *            - The message that will be received by the subscriber.
	 * @exception SubscriberTypeMismatchException
	 *                the type the subscriber was bound to is incompatible with
	 *                the type that is being received.
	 */
	public void receiveO(Object o) {
		try {
			@SuppressWarnings("unchecked")
			T o2 = (T) o;
			receive(o2);
		} catch (ClassCastException e) {
			throw new SubscriberTypeMismatchException(e);
		}
	}

}
