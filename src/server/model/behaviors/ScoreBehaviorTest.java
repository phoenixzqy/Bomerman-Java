package server.model.behaviors;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A class to test ScoreBehavior class.
 *
 */
public class ScoreBehaviorTest 
{
	// A test ScoreBehavior
	private ScoreBehavior scoreBehavior;
	
	/**
	 * Set up a new test
	 */
	@Before
	public void setUp()
	{
		scoreBehavior = new ScoreBehavior();
	}
	
	/**
	 * Test whether hasScore() mehtod return true
	 */
	@Test
	public void hasScoreTest() 
	{
		assertTrue(scoreBehavior.hasScore());
	}
	
	/**
	 * Test constructor to initialize the score to 0.
	 */
	@Test
	public void constructorTest() 
	{
		assertEquals(0, scoreBehavior.score());
	}
	
	/**
	 * Test if incrementScore() method work correctly.
	 */
	@Test
	public void incrementScoretest() 
	{
		scoreBehavior.incrementScore();
		assertEquals(1, scoreBehavior.score());
		
		scoreBehavior.incrementScore();
		assertEquals(2, scoreBehavior.score());
	}
	
	/**
	 * Test if decrementScore() method work correctly.
	 */
	@Test
	public void decrementScoreTest() 
	{
		scoreBehavior.decrementScore();
		assertEquals(-1, scoreBehavior.score());
		
		scoreBehavior.incrementScore();
		assertEquals(0, scoreBehavior.score());
		
		scoreBehavior.incrementScore();
		assertEquals(1, scoreBehavior.score());
		
		scoreBehavior.incrementScore();
		assertEquals(2, scoreBehavior.score());
		
		scoreBehavior.decrementScore();
		assertEquals(1, scoreBehavior.score());
	}
}
