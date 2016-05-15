package server.model.behaviors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A class to test UnOwnedBeavior class
 *
 */
public class UnownedBehaviorTest 
{
	private UnownedBehavior unOwnedBehavior;
	
	/**
	 * Sets up instance variables for the tests.
	 */
	@Before
	public void setUp()
	{
		unOwnedBehavior = new UnownedBehavior();
	}
	
	/**
	 * Tests that hasOwner() method returns false.
	 */
	@Test
	public void hasOwnertest() 
	{
		assertFalse(unOwnedBehavior.hasOwner());
	}
	
	
	/**
	 * Tests that OwnerTest() returns null.
	 */
	@Test
	public void OwnerTest()
	{
		assertEquals(null, unOwnedBehavior.owner());
	}
}
