package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import shared.model.communication.GameMessage.Action;

/**
 * JUnit tests for GameMessage.
 * 
 */
public class GameMessageTest
{

	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		GameMessage message = new GameMessage(Action.START, 4);
		assertEquals(4, message.numberOfPlayers());
		assertEquals(Action.START, message.action());

		message = new GameMessage(Action.STOP, 4);
		assertEquals(4, message.numberOfPlayers());
		assertEquals(Action.STOP, message.action());
		
		message = new GameMessage(Action.WAITING, 4);
		assertEquals(4, message.numberOfPlayers());
		assertEquals(Action.WAITING, message.action());
	}

	/**
	 * Ensures toString() produces a valid string.
	 */
	@Test
	public void testToString()
	{
		GameMessage message = new GameMessage(Action.START, 4);
		assertEquals("GAME START 4", message.toString());
	}

}
