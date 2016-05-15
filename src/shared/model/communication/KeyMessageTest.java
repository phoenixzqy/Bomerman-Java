package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import shared.model.Key;
import shared.model.KeyAction;

/**
 * Tests KeyMessage.
 */
public class KeyMessageTest
{
	/**
	 * Ensures the constructor and accessor methods work correctly.
	 */
	@Test
	public void sanityCheck()
	{
		KeyMessage message = new KeyMessage(17, Key.UP, KeyAction.PRESS);
		assertEquals(Key.UP, message.key());
		assertEquals(KeyAction.PRESS, message.action());
	}

	/**
	 * Ensures the constructor throws an exception if the key is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorKeyNull()
	{
		new KeyMessage(1, null, KeyAction.PRESS);
	}

	/**
	 * Ensures the constructor throws an exception if the action is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorActionNull()
	{
		new KeyMessage(1, Key.UP, null);
	}

	/**
	 * Ensures toString produces a valid string.
	 */
	@Test
	public void testToString()
	{
		KeyMessage message = new KeyMessage(1, Key.UP, KeyAction.PRESS);
		assertEquals("KEY 1 UP PRESS", message.toString());

		message = new KeyMessage(1, Key.DOWN, KeyAction.PRESS);
		assertEquals("KEY 1 DOWN PRESS", message.toString());

		message = new KeyMessage(1, Key.LEFT, KeyAction.PRESS);
		assertEquals("KEY 1 LEFT PRESS", message.toString());

		message = new KeyMessage(1, Key.RIGHT, KeyAction.PRESS);
		assertEquals("KEY 1 RIGHT PRESS", message.toString());

		message = new KeyMessage(1, Key.SPACE, KeyAction.PRESS);
		assertEquals("KEY 1 SPACE PRESS", message.toString());

		message = new KeyMessage(1, Key.UP, KeyAction.DEPRESS);
		assertEquals("KEY 1 UP DEPRESS", message.toString());

		message = new KeyMessage(1, Key.DOWN, KeyAction.DEPRESS);
		assertEquals("KEY 1 DOWN DEPRESS", message.toString());

		message = new KeyMessage(1, Key.LEFT, KeyAction.DEPRESS);
		assertEquals("KEY 1 LEFT DEPRESS", message.toString());

		message = new KeyMessage(1, Key.RIGHT, KeyAction.DEPRESS);
		assertEquals("KEY 1 RIGHT DEPRESS", message.toString());

		message = new KeyMessage(1, Key.SPACE, KeyAction.DEPRESS);
		assertEquals("KEY 1 SPACE DEPRESS", message.toString());
	}
}
