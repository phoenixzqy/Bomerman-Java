package server.model.behaviors;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * Set of JUnit tests for UnsolidBehavior.
 * 
 */
public class UnsolidBehaviorTest
{
	/**
	 * An instance of UnsolidBehavior for use across tests.
	 */
	private UnsolidBehavior unsolidBehavior;

	/**
	 * Sets up instance variables for the tests.
	 */
	@Before
	public void setUp()
	{
		unsolidBehavior = new UnsolidBehavior();
	}

	/**
	 * Tests that isSolid() returns false.
	 */
	@Test
	public void testIsSolid()
	{
		assertFalse(unsolidBehavior.solid());
	}
}
