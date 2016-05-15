package client.view;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import client.controller.IGameController;

/**
 * JUnit test cases for KeyBinder.
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JComponent.class)
public class KeyBinderTest
{
	/**
	 * Mocked JComponent.
	 */
	private JComponent mockComponent;

	/**
	 * Mocked IGameController.
	 */
	private IGameController mockController;

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		mockComponent = PowerMockito.mock(JComponent.class);
		mockController = mock(IGameController.class);
		when(mockComponent.getActionMap()).thenReturn(new ActionMap());
		when(mockComponent.getInputMap(anyInt())).thenReturn(new InputMap());
	}

	/**
	 * Tests that KeyBinder is successfully constructed and it's action map is
	 * not populated before setGameObjectId is called.
	 */
	@Test
	public void testKeyBinderConstructor()
	{
		new KeyBinder(mockComponent, mockController);
		assertNotNull(mockComponent.getActionMap().get("pressedUp"));
		assertNotNull(mockComponent.getActionMap().get("pressedDown"));
		assertNotNull(mockComponent.getActionMap().get("pressedLeft"));
		assertNotNull(mockComponent.getActionMap().get("pressedRight"));
		assertNotNull(mockComponent.getActionMap().get("releasedUp"));
		assertNotNull(mockComponent.getActionMap().get("releasedDown"));
		assertNotNull(mockComponent.getActionMap().get("releasedLeft"));
		assertNotNull(mockComponent.getActionMap().get("releasedRight"));
	}

}
