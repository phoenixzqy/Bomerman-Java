package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for PlayerGameObjectIdentifierMessage.
 * 
 */
public class PlayerGameObjectIdentifierMessageTest
{

	/**
	 * Sets up the test.
	 * 
	 * @throws Exception
	 *             If any exceptions are thrown during a test.
	 */
	@Before
	public void setUp() throws Exception
	{
		// DO NOTHING
	}

	/**
	 * Tests constructor and accessors.
	 */
	@Test
	public void testPlayerGameObjectIdentifierMessageConstructor()
	{
		PlayerGameObjectIdentifierMessage message = new PlayerGameObjectIdentifierMessage(
				10);
		assertEquals(10, message.gameObjectIdentifier());
	}

	/**
	 * Tests string representation of message matches desired format.
	 */
	@Test
	public void testToString()
	{
		PlayerGameObjectIdentifierMessage message = new PlayerGameObjectIdentifierMessage(
				10);
		assertEquals("PLAYER_GAME_OBJECT_IDENTIFIER 10", message.toString());
	}

}
