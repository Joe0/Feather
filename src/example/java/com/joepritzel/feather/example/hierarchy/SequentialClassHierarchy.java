package com.joepritzel.feather.example.hierarchy;

import com.joepritzel.feather.PSBroker;
import com.joepritzel.feather.PSBrokerBuilder;
import com.joepritzel.feather.Subscriber;
import com.joepritzel.feather.strategy.publish.Sequential;

/**
 * An example showing how to consider the class hierarchy while executing
 * everything sequentially.
 * 
 * @author Joe Pritzel
 * 
 */
public class SequentialClassHierarchy {

	public static void main(String[] args) throws InterruptedException {
		// We can't use the default settings, so we need to specify
		// the publish strategy. This us to specify whether
		// the class hierarchy should be considered or not.
		PSBroker broker = new PSBrokerBuilder().publishStrategy(
				new Sequential(true)).build();
		broker.subscribe(new TestInterfaceClass(), ITest.class);
		broker.subscribe(new TestAbstractClass(), ATest.class);
		broker.publish(new ITestImpl());
		broker.publish(new ATestImpl());
	}

	private interface ITest {
		public void print();
	}

	private static class ITestImpl implements ITest {

		@Override
		public void print() {
			System.out.println("ITestImpl message");
		}

	}

	private static class TestInterfaceClass extends Subscriber<ITest> {

		@Override
		public void receive(ITest message) {
			System.out.println("TestInterfaceClass message");
			message.print();
		}

	}

	private static abstract class ATest {
		public abstract void print();
	}

	private static class ATestImpl extends ATest {

		@Override
		public void print() {
			System.out.println("ATestImpl message");
		}

	}

	private static class TestAbstractClass extends Subscriber<ATest> {

		@Override
		public void receive(ATest message) {
			System.out.println("TestAbstractClass message");
			message.print();
		}

	}
}
