package com.joepritzel.feather;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Tests the default PSBroker setup to ensure everything works correctly.
 * 
 * @author Joe Pritzel
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OverallCorrectnessTest {

	private static PSBroker broker;
	private static IntegerSubscriber intSub;
	private static UnreadMessageSubscriber unreadSub;
	private static PredicatedIntegerSubscriber pIntSub;

	/**
	 * Set up the PSBroker.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		if (broker == null) {
			broker = new PSBrokerBuilder().build();
			intSub = new IntegerSubscriber();
			unreadSub = new UnreadMessageSubscriber();
			pIntSub = new PredicatedIntegerSubscriber();
		}
	}

	/**
	 * Ensure that things can subscribe without exceptions being thrown.
	 */
	@Test
	public void aTestValidSubscribe() {
		broker.subscribe(intSub);
		broker.subscribe(unreadSub);
		broker.subscribe(pIntSub);
	}

	/**
	 * Ensure exceptions are thrown when types mismatch.
	 */
	@Test(expected = SubscriberTypeMismatchException.class)
	public void aTestInvalidSubscribe() {
		broker.subscribe(new IntegerSubscriber(), Boolean.class);
	}

	/**
	 * Test publishing integers.
	 */
	@Test
	public void baTestPublishTest() {
		broker.publish(0);
		assertTrue(intSub.received == 1);
		assertTrue(pIntSub.received == 0);
		assertFalse(unreadSub.received);
	}

	/**
	 * Test UnreadMessage publishing.
	 */
	@Test
	public void bbTestPublishTest() {
		broker.publish(false);
		assertTrue(unreadSub.received);
		assertTrue(pIntSub.received == 0);
	}

	/**
	 * Tests PredicatedSubscribers publishing.
	 */
	@Test
	public void bcTestPublishTest() {
		broker.publish(1);
		assertTrue(intSub.received == 2);
		assertTrue(pIntSub.received == 1);
		broker.publish(2);
		assertTrue(intSub.received == 3);
		assertTrue(pIntSub.received == 1);
	}

	private class IntegerSubscriber extends Subscriber<Integer> {

		public int received = 0;

		@Override
		public void receive(Integer message) {
			received++;
		}

	}

	private class PredicatedIntegerSubscriber extends
			PredicatedSubscriber<Integer> {

		public int received = 0;

		@Override
		public boolean applies(Integer message) {
			return message.intValue() == 1;
		}

		@Override
		public void receive(Integer message) {
			received++;
		}

	}

	private class UnreadMessageSubscriber extends Subscriber<UnreadMessage> {

		public boolean received = false;

		@Override
		public void receive(UnreadMessage message) {
			received = true;
		}

	}

}
