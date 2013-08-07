package com.joepritzel.feather;

/**
 * Represents a type mismatch between the type a subscriber is subscribed to and
 * the type the subscriber is able to receive.
 * 
 * @author Joe Pritzel
 * 
 */
public class SubscriberTypeMismatchException extends RuntimeException {

	private static final long serialVersionUID = 4715497955655956591L;

	private static final String DEFAULT_MSG = "There is a mismatch between the type the subscriber is bound to and the type the subscriber is able to receive.";

	public SubscriberTypeMismatchException(String msg, Throwable e) {
		super(msg);
		this.setStackTrace(e.getStackTrace());
	}

	public SubscriberTypeMismatchException(Throwable e) {
		super(DEFAULT_MSG);
		this.setStackTrace(e.getStackTrace());
	}

	public SubscriberTypeMismatchException() {
		super(DEFAULT_MSG);
	}
}
