package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests GameObjectScoreUpdatedMessage.
 */
public class ScoreUpdatedMessageTest
{
	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		ScoreUpdatedMessage message = new ScoreUpdatedMessage(2, 3);
		assertEquals(2, message.gameObjectIdentifier());
		assertEquals(3, message.score());
	}

	/**
	 * Ensures toString() produces a valid string.
	 */
	@Test
	public void testToString()
	{
		ScoreUpdatedMessage message = new ScoreUpdatedMessage(2, 3);
		assertEquals("SCORE_UPDATED 2 3", message.toString());
	}
}
