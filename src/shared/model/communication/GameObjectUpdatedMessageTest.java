package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests GameObjectPositionUpdatedMessage.
 */
public class GameObjectUpdatedMessageTest
{
	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		GameObjectUpdatedMessage message = new GameObjectUpdatedMessage(1, 2, 3);
		assertEquals(1, message.gameObjectIdentifier());
		assertEquals(2, message.row());
		assertEquals(3, message.column());
	}
	
	/**
	 * Ensures the constructor throws an exception when the row is less than 0.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeRowThrowsException()
	{
		new GameObjectUpdatedMessage(0, -1, 0);
	}
	
	/**
	 * Ensures the constructor throws an exception when the column is less than 0.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorNegativeColumnThrowsException()
	{
		new GameObjectUpdatedMessage(0, 0, -1);
	}
	
	/**
	 * Ensures toString() produces a valid string.
	 */
	@Test
	public void testToString()
	{
		GameObjectUpdatedMessage message = new GameObjectUpdatedMessage(1, 2, 3);
		assertEquals("GAME_OBJECT_POSITION_UPDATED 1 2 3", message.toString());
	}
}
