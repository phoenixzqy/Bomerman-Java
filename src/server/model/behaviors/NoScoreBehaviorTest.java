package server.model.behaviors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A test class for the NoScoreBehavior class
 *
 */
public class NoScoreBehaviorTest 
{
	//A instance of the NoScoreBehavior for test
	private NoScoreBehavior noScoreBehavior;
	
	/**
	 * Set up the instance for the entire test.
	 */
	@Before
	public void SetUp()
	{
		noScoreBehavior = new NoScoreBehavior();
	}
	
	/**
	 * Test if hasScore() method return false
	 */
	@Test
	public void hasScoreTest() 
	{
		assertFalse(noScoreBehavior.hasScore());
	}
	
	/**
	 * Test if incrementScore() method will throw illegalArgumentException.
	 */
	@Test(expected = IllegalStateException.class)
	public void incrementScoreTest() 
	{
		noScoreBehavior.incrementScore();
	}
	
	
	/**
	 * Test if decrementScore() method will throw illegalArgumentException.
	 */
	@Test(expected = IllegalStateException.class)
	public void decrementScoreTest() 
	{
		noScoreBehavior.decrementScore();
	}

}
