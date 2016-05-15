package server.model.behaviors;

import static org.junit.Assert.*;
import org.junit.*;
import server.model.DestructionAction;

/**
 * Tests DestructibleBehavior.
 */
public class DestructibleBehaviorTest
{

	// a test destructible behavior
	private DestructibleBehavior testDestructibleBehavior;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		testDestructibleBehavior = new DestructibleBehavior(10, DestructionAction.RESPAWN);
	}
	
	/**
	 * Ensure the constructor sets the destructible behavior to the provided destructible behavior.
	 */
	@Test
	public void testConstructor()
	{
		assertEquals(DestructionAction.RESPAWN, testDestructibleBehavior.destructionAction());
		testDestructibleBehavior = new DestructibleBehavior(10, DestructionAction.DISAPPEAR);
		assertEquals(DestructionAction.DISAPPEAR, testDestructibleBehavior.destructionAction());
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when provided with a null destructible
	 * behavior.
	 */
	@Test (expected= NullPointerException.class)
	public void testConstructorNullPointerExcpetion()
	{
		new DestructibleBehavior(10, null);
	}
	
	/**
	 * Ensures destructible returns true.
	 */
	@Test
	public void testDestructible()
	{
		assertEquals(true, testDestructibleBehavior.destructible());
	}
	
	/**
	 * Ensures decrementNumberOfStepsUntilDestruction doesn't do anything.
	 */
	@Test
	public void testDecremetNumberOfStepsUnitilDestruction()
	{
		testDestructibleBehavior.decrementNumberOfStepsUntilDestruction();
	}
	
	/**
	 * Ensures destructionTimed returns false.
	 */
	@Test
	public void testDestructionTimed()
	{
		assertEquals(false, testDestructibleBehavior.destructionTimed());
	}
	
	/**
	 * Ensures numberOfStepUntilRespawn will return the correct number.
	 */
	@Test
	public void testNumberOfStepUntilRespawn()
	{
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
	}
	
	/**
	 * Ensures decreemntNumberOfStepUntilRespawn will reduce the numberOfStepUntilRespawn by one
	 */
	@Test
	public void testDecrementNumberOfStepUntilRespawn()
	{
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		assertEquals(9, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		assertEquals(8, testDestructibleBehavior.numberOfStepUntilRespawn());
	}
	
	/**
	 * Ensures resetNumberOfStepUntilRespawn will correctly reset the numberOfStepUntilRespawn
	 * to its initial value.
	 */
	@Test
	public void testResetNumberOfStepUntilRespawn()
	{
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		assertEquals(9, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		assertEquals(8, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.resetNumberOfStepUntilRespawn();
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
	}
	
	/**
	 * Test if the decrementNumberOfStepUntilRespawn will throw IllegalStateException when the numberOfStepUntilRespawn
	 * reduce to 0.
	 */
	@Test (expected = IllegalStateException.class)
	public void testDecrementNumberOfStepUntilRespawnTo0()
	{
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
		for (int i =0; i<10 ; i++)
		{
			testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		}
		
		assertEquals(0, testDestructibleBehavior.numberOfStepUntilRespawn());
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		
	}
	
	/**
	 * Ensures increemntNumberOfStepUntilRespawn will increase the numberOfStepUntilRespawn by one
	 */
	@Test
	public void testIncrementNumberOfStepUntilRespawn()
	{
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		assertEquals(9, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.decrementNumberOfStepUntilRespawn();
		assertEquals(8, testDestructibleBehavior.numberOfStepUntilRespawn());
		
		testDestructibleBehavior.incrementNumberOfStepUntilRespawn();
		assertEquals(9, testDestructibleBehavior.numberOfStepUntilRespawn());
		testDestructibleBehavior.incrementNumberOfStepUntilRespawn();
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
	}
	
	
	/**
	 * Test if the incrementNumberOfStepUntilRespawn will throw IllegalStateException when the numberOfStepUntilRespawn
	 * is already max.
	 */
	@Test (expected = IllegalStateException.class)
	public void testIncrementNumberOfStepUntilRespawnToMax()
	{
		assertEquals(10, testDestructibleBehavior.numberOfStepUntilRespawn());
		testDestructibleBehavior.incrementNumberOfStepUntilRespawn();
		
	}
}
