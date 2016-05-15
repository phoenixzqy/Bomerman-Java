package server.model.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import shared.model.Direction;

/**
 * A Test class for the MobilityBehavior class
 *
 */
public class MobileBehaviorTest 
{
	//a instance of MobilityBehavior class for test
	private MobileBehavior mobilityBehavior;

	/**
	 * Set up the instance for entire class
	 */
	@Before
	public void Setup()
	{
		mobilityBehavior = new MobileBehavior();
	}
	
	/**
	 * Test canMove() return true.
	 */
	@Test
	public void canMovetest() 
	{
		assertTrue(mobilityBehavior.canMove()); 
	}
	
	/**
	 * Test setPosition method work correctly
	 */
	@Test
	public void setPostitionTest()
	{
		mobilityBehavior.setPosition(10, 8);
		
		assertEquals(10, mobilityBehavior.row());
		assertEquals(8, mobilityBehavior.column());
		assertTrue(mobilityBehavior.onBoard());
	}
	
	/**
	 * Test removeFromBoard method work correctly
	 */
	@Test
	public void removeFromBardTest()
	{
		mobilityBehavior.setPosition(5, 5);
		
		mobilityBehavior.removeFromBoard();
		
		assertEquals(-1, mobilityBehavior.row());
		assertEquals(-1, mobilityBehavior.column());
		assertFalse(mobilityBehavior.onBoard());
	}
	
	/**
	 * Test startMovingInDirection method will throw NullPointerException corretly
	 */
	@Test(expected = NullPointerException.class)
	public void startMovingInDirectionNullPointerException()
	{
		mobilityBehavior.startMovingInDirection(null);
		
	}
	
	/**
	 * Test startMovingInDirection method will throw IllegalArgumentException correctly
	 */
	@Test(expected = IllegalArgumentException.class)
	public void startMovingInDirectionIllegalArgumentException()
	{
		mobilityBehavior.startMovingInDirection(Direction.NONE);
		
	}

	/**
	 * Test startMovingInDirection method work correctly when the initial direction is NONE and
	 * the target direction is LEFT
	 */
	@Test
	public void startMovingInDirectionNtoL()
	{
		MobileBehavior test = new MobileBehavior();
		test.startMovingInDirection(Direction.LEFT);
		
		assertEquals(Direction.LEFT, test.directionToMove());
	}
	
	/**
	 * Test startMovingInDirection method work correctly when the initial direction is RIGHT and
	 * the target direction is LEFT
	 */
	@Test
	public void startMovingInDirectionRtoL()
	{
		MobileBehavior test = new MobileBehavior();
		
		test.startMovingInDirection(Direction.RIGHT);
		assertEquals(Direction.RIGHT, test.directionToMove());
		
		test.startMovingInDirection(Direction.LEFT);
		assertEquals(Direction.NONE, test.directionToMove());
		
	}
	
	/**
	 * Test stopMovingInDirection method will throw NullPointerException correctly
	 */
	@Test(expected = NullPointerException.class)
	public void stopMovingInDirectionNullPointerException()
	{
		mobilityBehavior.stopMovingInDirection(null);
	}
	
	/**
	 * Test stopMovingInDirection method will throw IllegalArgumentException correctly
	 */
	@Test(expected = IllegalArgumentException.class)
	public void stopMovingInDirectionIllegalArgumentException()
	{
		mobilityBehavior.stopMovingInDirection(Direction.NONE);
	}
	
	/**
	 * Test stopMovingInDirection method work correctly when the initial direction is LEFT
	 * and the stop direction is LEFT.
	 */
	@Test
	public void stopMovingInDirectionLtoN()
	{
		MobileBehavior test = new MobileBehavior();
		test.startMovingInDirection(Direction.LEFT);
		assertEquals(Direction.LEFT, test.directionToMove());
		
		test.stopMovingInDirection(Direction.LEFT);
		assertEquals(Direction.NONE, test.directionToMove());
	}
	
	
	/**
	 * Test stopMovingInDirection method work correctly when the initial direction is multiple LEFT and RIGHT
	 * and the stop direction is LEFT.
	 */
	@Test
	public void stopMovingInDirectionLRtoL()
	{
		MobileBehavior test = new MobileBehavior();
		test.startMovingInDirection(Direction.LEFT);
		test.startMovingInDirection(Direction.RIGHT);
		assertEquals(Direction.NONE, test.directionToMove());
		
		test.stopMovingInDirection(Direction.LEFT);
		assertEquals(Direction.RIGHT, test.directionToMove());
	}
	
	/**
	 * Test stopMovingInDirection method work correctly when the initial direction is multiple LEFT and RIGHT
	 * and the stop direction is UP.
	 */
	@Test
	public void stopMovingInDirectionLRtoLR()
	{
		MobileBehavior test = new MobileBehavior();
		test.startMovingInDirection(Direction.LEFT);
		test.startMovingInDirection(Direction.RIGHT);
		assertEquals(Direction.NONE, test.directionToMove());
		
		test.stopMovingInDirection(Direction.UP);
		assertEquals(Direction.NONE, test.directionToMove());
	}
	
	/**
	 * Test stopMovingInDirection method work correctly when the initial direction is LEFT
	 * and the stop direction is RIGHT.
	 */
	@Test
	public void stopMovingInDirectionLtoR()
	{
		MobileBehavior test = new MobileBehavior();
		test.startMovingInDirection(Direction.LEFT);
		assertEquals(Direction.LEFT, test.directionToMove());
		
		test.stopMovingInDirection(Direction.RIGHT);
		assertEquals(Direction.LEFT, test.directionToMove());
	}
}
