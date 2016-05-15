package client.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import shared.model.Key;
import shared.model.KeyAction;
import client.controller.IGameController;

/**
 * Tests KeyEventAction.
 */
public class KeyEventActionTest
{
	// a mock key
	private Key testKey;

	// a mock action
	private KeyAction testAction;

	// a mock game controller
	private IGameController mockGameController;

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		testKey = Key.DOWN;
		testAction = KeyAction.DEPRESS;
		mockGameController = mock(IGameController.class);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when a null key is
	 * provided.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorKeyNull()
	{
		new KeyPressAction(null, testAction, mockGameController);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when a null action
	 * is provided.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorActionNull()
	{
		new KeyPressAction(testKey, null, mockGameController);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when a null game
	 * controller is provided.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorGameControllerNull()
	{
		new KeyPressAction(testKey, testAction, null);
	}

	/**
	 * Ensures when the actionPerformed method is called, the keyEventDidOccur
	 * method is called with the key and action passed into the constructor.
	 */
	@Test
	public void testActionPerformed()
	{
		KeyPressAction keyEventAction = new KeyPressAction(testKey, testAction,
				mockGameController);
		keyEventAction.actionPerformed(null);
		verify(mockGameController).keyEventDidOccur(testKey, testAction);
	}
}
