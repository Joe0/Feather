package com.joepritzel.feather;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.MethodSorters;

/**
 * Tests the default PSBroker's ability to handle topical messages.
 * 
 * @author Joe Pritzel
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TopicalSubscriberTest {

	private static PSBroker broker;
	private static UnreadMessageSubscriber unreadSub;
	private static SubscriberTest topicalSub;

	/**
	 * Set up the PSBroker.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		if (broker == null) {
			broker = new PSBrokerBuilder().build();
			unreadSub = new UnreadMessageSubscriber();
			topicalSub = new SubscriberTest("/test/");
		}
	}

	/**
	 * Ensure that things can subscribe without exceptions being thrown.
	 */
	@Test
	public void aTestValidSubscribe() {
		broker.subscribe(unreadSub);
		broker.subscribe(topicalSub);
	}

	/**
	 * Test publishing integers.
	 */
	@Test
	public void baTestPublishTest() {
		broker.publish(new TopicalMessage<Integer>("/test/", 1));
		assertTrue(topicalSub.received == 1);
		assertFalse(unreadSub.received);
		broker.publish(new TopicalMessage<Integer>("/", 1));
		assertTrue(topicalSub.received == 1);
		assertTrue(unreadSub.received);
	}

	private class SubscriberTest extends TopicSubscriber<Integer> {

		public SubscriberTest(String path) {
			super(path);
		}

		public int received = 0;

		@Override
		public void received(String path, Integer message) {
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
