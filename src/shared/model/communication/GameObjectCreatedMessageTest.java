package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import shared.model.GameObjectType;

/**
 * Tests GameObjectCreatedMessage.
 */
public class GameObjectCreatedMessageTest
{
	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		GameObjectCreatedMessage message = new GameObjectCreatedMessage(1, GameObjectType.BOMB, 2, 3);
		assertEquals(GameObjectType.BOMB, message.gameObjectType());
		assertEquals(1, message.gameObjectIdentifier());
		assertEquals(2, message.row());
		assertEquals(3, message.column());
	}
	
	/**
	 * Ensures the constructor throws an exception if the game object type is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNullGameObjectType()
	{
		new GameObjectCreatedMessage(0, null, 0, 0);
	}
	
	/**
	 * Ensures the constructor throws an exception when the row is less than 0.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeRowThrowsException()
	{
		new GameObjectCreatedMessage(0, GameObjectType.BOMB, -1, 0);
	}
	
	/**
	 * Ensures the constructor throws an exception when the column is less than 0.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeColumnThrowsException()
	{
		new GameObjectCreatedMessage(0, GameObjectType.BOMB, 0, -1);
	}
	
	/**
	 * Ensures toString() produces a valid string.
	 */
	@Test
	public void testToString()
	{
		GameObjectCreatedMessage message = new GameObjectCreatedMessage(1, GameObjectType.BOMB, 2, 3);
		assertEquals("GAME_OBJECT_CREATED 1 " + GameObjectType.BOMB.toString() + " 2 3", 
			message.toString());
	}
}
