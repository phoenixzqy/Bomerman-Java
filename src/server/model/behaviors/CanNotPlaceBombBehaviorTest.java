package server.model.behaviors;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * 
 */
public class CanNotPlaceBombBehaviorTest
{
	// a test CanNotPlaceBombBehavior
	private CanNotPlaceBombBehavior testCanNotPlaceBombBehavior;
	
	/**
	 * Sets up the test.
	 */
	@Before
	public void setUp()
	{
		testCanNotPlaceBombBehavior = new CanNotPlaceBombBehavior(); 
	}
	
	/**
	 * Ensures the canPlaceBomb method returns false.
	 */
	@Test
	public void testCanPlaceBomb()
	{
		assertFalse(testCanNotPlaceBombBehavior.canPlaceBomb());
	}
	
	/**
	 * Ensures the placeBomb method returns false.
	 */
	@Test
	public void testPlaceBomb()
	{
		assertFalse(testCanNotPlaceBombBehavior.placeBomb());
	}
	
	/**
	 * Ensures the setPlaceBomb method throws an IllegalStateException.
	 */
	@Test(expected=IllegalStateException.class)
	public void testSetPlaceBomb()
	{
		testCanNotPlaceBombBehavior.setPlaceBomb(false);
	}
	
	/**
	 * Test the CountBomb method works correctly
	 */
	@Test
	public void testCountBomb()
	{
		//Do nothing
		assertEquals(0,testCanNotPlaceBombBehavior.countBomb());
	}

	/**
	 * Test the DecrementBomb method works correctly
	 */
	@Test 
	public void testDecrementBomb()
	{
		//Do nothing
		testCanNotPlaceBombBehavior.decrementBomb();
	}
	
	/**
	 * Test the incrementBomb method works correctly
	 */
	@Test
	public void testIncrementBomb()
	{
		//Do nothing
		testCanNotPlaceBombBehavior.incrementBomb();
	}
}
