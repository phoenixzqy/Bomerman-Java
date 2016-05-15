package shared.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Set of JUnit tests for Score.
 * 
 */
public class ScoreTest
{
	/**
	 * Tests that the constructor creates a new Score with the given id and
	 * score.
	 */
	@Test
	public void constructorTest()
	{
		int id = 1542;
		int score = 7;
		Score scoreObject = new Score(id, score);
		assertEquals(id, scoreObject.getGameObjectId());
		assertEquals(score, scoreObject.getScore());
	}
}
