package server.model.behaviors;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Tests CanPlaceBombBehavior
 */
public class CanPlaceBombBehaviorTest
{
	// a test CanPlaceBombBehavior
	private CanPlaceBombBehavior testCanPlaceBombBehavior;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		testCanPlaceBombBehavior = new CanPlaceBombBehavior(3);
	}
	
	/**
	 * Ensures canPlaceBomb returns true.
	 */
	@Test
	public void testCanPlaceBomb()
	{
		assertEquals(true, testCanPlaceBombBehavior.canPlaceBomb());
	}
	
	/**
	 * Ensures the constructor sets up CanPlaceBombBehavior correctly.
	 */
	@Test
	public void testConstructor()
	{
		assertEquals(false, testCanPlaceBombBehavior.placeBomb());
	}
	
	/**
	 * Tests the placeBomb and setPlaceBomb methods.
	 */
	@Test
	public void testPlaceBombAndSetPlaceBomb()
	{
		assertEquals(false, testCanPlaceBombBehavior.placeBomb());
		
		testCanPlaceBombBehavior.setPlaceBomb(true);
		assertEquals(true, testCanPlaceBombBehavior.placeBomb());
		
		testCanPlaceBombBehavior.setPlaceBomb(true);
		assertEquals(true, testCanPlaceBombBehavior.placeBomb());
		
		testCanPlaceBombBehavior.setPlaceBomb(false);
		assertEquals(false, testCanPlaceBombBehavior.placeBomb());
		
		testCanPlaceBombBehavior.setPlaceBomb(false);
		assertEquals(false, testCanPlaceBombBehavior.placeBomb());
	}
	
	/**
	 * Test the CountBomb method works correctly
	 */
	@Test
	public void testCountBomb()
	{
		assertEquals(3,testCanPlaceBombBehavior.countBomb());
	}
	
	/**
	 * Test the DecrementBomb method works correctly
	 */
	@Test
	public void testDecrementBomb()
	{
		testCanPlaceBombBehavior.decrementBomb();
		assertEquals(2,testCanPlaceBombBehavior.countBomb());
		
		testCanPlaceBombBehavior.decrementBomb();
		assertEquals(1,testCanPlaceBombBehavior.countBomb());
	}
	
	/**
	 * Test the DecrementBomb method will throw IllegalStateException when the bomb number is already 0
	 */
	@Test (expected = IllegalStateException.class)
	public void testDecrementBombTo0()
	{
		testCanPlaceBombBehavior.decrementBomb();
		testCanPlaceBombBehavior.decrementBomb();
		testCanPlaceBombBehavior.decrementBomb();
		assertEquals(0,testCanPlaceBombBehavior.countBomb());
		testCanPlaceBombBehavior.decrementBomb();
	}
	
	/**
	 * Test the incrementBomb method works correctly
	 */
	@Test
	public void testIncrementBomb()
	{
		testCanPlaceBombBehavior.decrementBomb();
		assertEquals(2,testCanPlaceBombBehavior.countBomb());
		
		testCanPlaceBombBehavior.decrementBomb();
		assertEquals(1,testCanPlaceBombBehavior.countBomb());
		
		testCanPlaceBombBehavior.incrementBomb();
		assertEquals(2,testCanPlaceBombBehavior.countBomb());
		
		testCanPlaceBombBehavior.incrementBomb();
		assertEquals(3,testCanPlaceBombBehavior.countBomb());
	}
	
	/**
	 * Test the incrementBomb method will throw IllegalStateException when the number of bomb is
	 * already the max.
	 */
	@Test (expected = IllegalStateException.class)
	public void testIncrementBombToMax()
	{
		assertEquals(3,testCanPlaceBombBehavior.countBomb());
		testCanPlaceBombBehavior.incrementBomb();
	}
}
