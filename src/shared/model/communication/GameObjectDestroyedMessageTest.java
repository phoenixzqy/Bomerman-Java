package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 */
public class GameObjectDestroyedMessageTest
{
	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		GameObjectDestroyedMessage message = new GameObjectDestroyedMessage(1);
		assertEquals(1, message.gameObjectIdentifier());
	}
	
	/**
	 * Ensures toString() produces a valid string.
	 */
	@Test
	public void testToString()
	{
		GameObjectDestroyedMessage message = new GameObjectDestroyedMessage(1);
		assertEquals("GAME_OBJECT_DESTROYED 1", message.toString());
	}
}
