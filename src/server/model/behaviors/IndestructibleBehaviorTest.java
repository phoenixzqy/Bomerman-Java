package server.model.behaviors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * Set of JUnit tests for InDestructableBehavior.
 * 
 */
public class IndestructibleBehaviorTest 
{
	//An instance of InDestructableBehavior for test.
	private IndestructibleBehavior indestructibleBehavior;
	
	/**
	 *  Sets up instance variables for the tests.
	 */
	@Before
	public void setUp()
	{
		indestructibleBehavior = new IndestructibleBehavior();
	}
	
	/**
	 * Test the destructionAction() return null.
	 */
	@Test
	public void destructionActionTest()
	{
		assertEquals(null, indestructibleBehavior.destructionAction());
	}
	
	/**
	 * Test the destructible() method return false.
	 */
	@Test
	public void destructibleTest()
	{
		assertFalse(indestructibleBehavior.destructible());
	}

	/**
	 * Ensures the decrementNumberOfStepsUntilDestruciton method doens't do anything.
	 */
	@Test
	public void testDecrementNumberOfStepsUntilDestruction()
	{
		// make sure nothing bad happens
		indestructibleBehavior.decrementNumberOfStepsUntilDestruction();
	}
	
	/**
	 * Ensures numberOfStepUntilRespawn will return 0.
	 */
	@Test
	public void testNumberOfStepUntilRespawn()
	{
		assertEquals(0, indestructibleBehavior.numberOfStepUntilRespawn());
	}
	
	/**
	 * Ensures decreemntNumberOfStepUntilRespawn will do nothing
	 */
	@Test
	public void testDecrementNumberOfStepUntilRespawn()
	{
		// make sure nothing bad happens
		indestructibleBehavior.decrementNumberOfStepUntilRespawn();
	}
	
	/**
	 * Ensures resetNumberOfStepUntilRespawn will do nothing
	 */
	@Test
	public void testResetNumberOfStepUntilRespawn()
	{
		// make sure nothing bad happens
		indestructibleBehavior.resetNumberOfStepUntilRespawn();
	}
	
	/**
	 * Ensures increemntNumberOfStepUntilRespawn will do nothing
	 */
	@Test
	public void testIncrementNumberOfStepUntilRespawn()
	{
		// make sure nothing bad happens
		indestructibleBehavior.incrementNumberOfStepUntilRespawn();
	}
}
