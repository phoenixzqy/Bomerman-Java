package server.model.behaviors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * The test for the ImmobileBehavior class
 *
 */
public class ImmobileBehaviorTest 
{
	//A instance of InMobilityBehavior for test
	private ImmobileBehavior inMobilityBehavior;
	
	/**
	 * Set up the instance for the test.
	 */
	@Before
	public void setUp()
	{
		inMobilityBehavior = new ImmobileBehavior();
	}
	
	/**
	 * Test if the canMove() method return false.
	 */
	@Test
	public void canMoveTest()
	{
		assertFalse(inMobilityBehavior.canMove());
	}
	
	/**
	 * Test if the directionToMove() method return null.
	 */
	@Test
	public void directionToMoveTest()
	{
		assertEquals(null,inMobilityBehavior.directionToMove());
	}

	/**
	 * Test if the setPosition() method work correctly.
	 */
	@Test
	public void setPositionTest()
	{
		ImmobileBehavior test = new ImmobileBehavior();
		test.setPosition(5, 8);
		
		assertEquals(5,test.row());
		assertEquals(8,test.column());
		assertEquals(true,test.onBoard());
	}
	

	/**
	 * Test if the removeFromBoard() method work correctly.
	 */
	@Test
	public void removeFromBoardTest()
	{
		ImmobileBehavior test = new ImmobileBehavior();
		test.setPosition(5, 8);
		
		assertEquals(5,test.row());
		assertEquals(8,test.column());
		assertEquals(true,test.onBoard());
		
		test.removeFromBoard();
		assertEquals(-1,test.row());
		assertEquals(-1,test.column());
		assertEquals(false,test.onBoard());
	}

	
	/**
	 * Test startMovingInDirection method will throws an IllegalStateExcpetion.
	 */
	@Test(expected =IllegalStateException.class)
	public void startMovingInDirectionNullPointerException()
	{
		inMobilityBehavior.startMovingInDirection(null);
		
	}

	
	/**
	 * Test stopMovingInDirection method throws an IllegalStateException.
	 */
	@Test(expected = IllegalStateException.class)
	public void stopMovingInDirectionNullPointerException()
	{
		inMobilityBehavior.stopMovingInDirection(null);
		
	}
}
