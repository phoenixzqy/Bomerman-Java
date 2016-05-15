package shared.model.communication;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests GameTimeMessage
 */
public class GameTimeMessageTest
{

	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		GameTimeMessage message = new GameTimeMessage(999);
		assertEquals(999, message.time());
	}
	
	/**
	 * Ensures toString() produces a valid string.
	 */
	@Test
	public void testToString()
	{
		GameTimeMessage message = new GameTimeMessage(999);
		assertEquals("GAME_TIME 999", message.toString());
	}

}
