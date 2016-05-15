package server.model.behaviors;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Set of JUnit tests for SolidBehavior.
 * 
 */
public class SolidBehaviorTest
{
	/**
	 * An instance of SolidBehavior for use across tests.
	 */
	private SolidBehavior solidBehavior;

	/**
	 * Sets up instance variables for the tests.
	 */
	@Before
	public void setUp()
	{
		solidBehavior = new SolidBehavior();
	}

	/**
	 * Tests that isSolid() returns true.
	 */
	@Test
	public void testIsSolid()
	{
		assertTrue(solidBehavior.solid());
	}

}
