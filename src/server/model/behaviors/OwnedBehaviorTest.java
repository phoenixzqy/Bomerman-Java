package server.model.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import org.junit.Before;
import org.junit.Test;

import server.model.IGameObject;

/**
 * A class to test OwnedBehavior class
 */
public class OwnedBehaviorTest 
{
	//A instance of the OwnedBehavior for test.
	private OwnedBehavior ownedBehavior;
	
	private IGameObject mockOwner;

	/**
	 * Set up the instance for the entire test
	 */
	@Before
	public void Setup()
	{
		mockOwner = mock(IGameObject.class);
		ownedBehavior = new OwnedBehavior(mockOwner);
	}
	
	/**
	 * Ensures the OwnedBehavior constructor throws a NullPointerException when provided with a null
	 * owner.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorOwnerNull()
	{
		new OwnedBehavior(null);
	}
	
	/**
	 * Test hasOwner() return true
	 */
	@Test
	public void hasOwnertest() 
	{
		assertTrue(ownedBehavior.hasOwner());
	}
	
	/**
	 * Test owner() return correct owner for the object.
	 */
	@Test
	public void ownertest() 
	{
		assertEquals(mockOwner, ownedBehavior.owner());
	}

}
