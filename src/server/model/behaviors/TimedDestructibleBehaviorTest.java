package server.model.behaviors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import server.model.DestructionAction;

/**
 * Set of JUnit tests for TimedDestructableBehavior.
 * 
 */
public class TimedDestructibleBehaviorTest 
{
	private TimedDestructibleBehavior timedDestructibleBehavior;
	
	/**
	 *  Sets up instance variables for the tests.
	 */
	@Before
	public void Setup()
	{
		int numberOfStepsUntilDestruction = 5;
		DestructionAction destructionAction = DestructionAction.EXPLODE;
		timedDestructibleBehavior = new TimedDestructibleBehavior(numberOfStepsUntilDestruction, 
				destructionAction);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException if provided with a null
	 * DestructionAction.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorDestructionActionNull()
	{
		new TimedDestructibleBehavior(1, null);
	}
	
	/**
	 * Ensures the constructor throws an IllegalArgumentException if provided with a negative number
	 * of steps until destruction.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNumberOfStepsUntilDestructionNegative()
	{
		new TimedDestructibleBehavior(-1, DestructionAction.EXPLODE);
	}
	
	/**
	 * Test the numberOfStepsUntilDestruction() return the correct number of steps.
	 */
	@Test
	public void numberOfStepsUntilDestructionTest()
	{
		assertEquals(5, timedDestructibleBehavior.numberOfStepsUntilDestruction());
	}
	
	/**
	 * Test the destructionAction() return the correct action for the object.
	 */
	@Test
	public void destructionActionTest()
	{
		assertEquals(DestructionAction.EXPLODE, timedDestructibleBehavior.destructionAction());
	}
	
	
	
	/**
	 * Test the destructible() method return true.
	 */
	@Test
	public void destructibleTest()
	{
		assertTrue(timedDestructibleBehavior.destructible());
	}

	/**
	 * Ensures the decrementNumberOfStepsUntilDestruction method decrements the number of steps until
	 * destruction. 
	 */
	@Test
	public void testDecrmentNumberOfStepsUntilDestruction()
	{
		assertEquals(5, timedDestructibleBehavior.numberOfStepsUntilDestruction());
		timedDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
		assertEquals(4, timedDestructibleBehavior.numberOfStepsUntilDestruction());
		timedDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
		assertEquals(3, timedDestructibleBehavior.numberOfStepsUntilDestruction());
		timedDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
		assertEquals(2, timedDestructibleBehavior.numberOfStepsUntilDestruction());
		timedDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
		assertEquals(1, timedDestructibleBehavior.numberOfStepsUntilDestruction());
		timedDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
		assertEquals(0, timedDestructibleBehavior.numberOfStepsUntilDestruction());
	}
	
	/**
	 * Ensures the decrementNumberOfStepsUntilDestruction method throws an IllegalStateException if
	 * the number of steps until destruction is already 0.
	 */
	@Test(expected=IllegalStateException.class)
	public void testDecrementNumberOfStepsAlreadyZero()
	{
		timedDestructibleBehavior = new TimedDestructibleBehavior(0, DestructionAction.EXPLODE);
		timedDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
	}
	
	/**
	 * Ensures the destructionTimed method returns true.
	 */
	@Test
	public void testDestructionTimed()
	{
		assertTrue(timedDestructibleBehavior.destructionTimed());
	}
		
	/**
	 * Ensures numberOfStepUntilRespawn will return 0.
	 */
	@Test
	public void testNumberOfStepUntilRespawn()
	{
		assertEquals(0, timedDestructibleBehavior.numberOfStepUntilRespawn());
	}
	
	/**
	 * Ensures decreemntNumberOfStepUntilRespawn will do nothing
	 */
	@Test
	public void testDecrementNumberOfStepUntilRespawn()
	{
		// make sure nothing bad happens
		timedDestructibleBehavior.decrementNumberOfStepUntilRespawn();
	}
	
	/**
	 * Ensures resetNumberOfStepUntilRespawn will do nothing
	 */
	@Test
	public void testResetNumberOfStepUntilRespawn()
	{
		// make sure nothing bad happens
		timedDestructibleBehavior.resetNumberOfStepUntilRespawn();
	}
	
	/**
	 * Ensures increemntNumberOfStepUntilRespawn will do nothing
	 */
	@Test
	public void testIncrementNumberOfStepUntilRespawn()
	{
		// make sure nothing bad happens
		timedDestructibleBehavior.incrementNumberOfStepUntilRespawn();
	}
}
