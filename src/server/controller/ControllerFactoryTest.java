package server.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import shared.controller.INavigator;
import shared.model.communication.IServerCommunicator;

/**
 * Tests ControllerFactory.
 */
public class ControllerFactoryTest
{
	// the controller factory
	IControllerFactory controllerFactory;
	
	// a mock navigator
	private INavigator mockNavigator;
	
	// a mock server communicator
	private IServerCommunicator mockServerCommunicator;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		controllerFactory = new ControllerFactory();
		mockServerCommunicator = mock(IServerCommunicator.class);
		mockNavigator = mock(INavigator.class);
	}
	
	/**
	 * Ensures the create main menu controller method returns a new main menu controller.
	 */
	@Test
	public void testCreateMainMenuController()
	{
		IMainMenuController controller = controllerFactory.createMainMenuController(mockNavigator);
		assertNotNull(controller);
		assertTrue(controller instanceof IMainMenuController);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
	}
	
	/**
	 * Ensures the create main menu controller method throws an exception when the navigator is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateMainMenuControllerNavigatorNullThrowsException()
	{
		controllerFactory.createMainMenuController(null);
	}
	
	/**
	 * Ensures the create game hosting controller method return a new game hosting controller.
	 */
	@Test
	public void testCreateGameHostingController()
	{
		IGameHostingController controller = controllerFactory.
			createGameHostingController(mockNavigator, mockServerCommunicator);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
		assertEquals(mockServerCommunicator, controller.serverCommunicator());
	}
	
	/**
	 * Ensures the create game hosting controller method throws an exception when the navigator is
	 * null.
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateGameHostingControllerNavigatorNullThrowsException()
	{
		controllerFactory.createGameHostingController(null, mockServerCommunicator);
	}
	
	/**
	 * Ensures the create game hosting controller method throws an exception when the server
	 * communicator is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateGameHostingControllerServerCommunicatorNull()
	{
		controllerFactory.createGameHostingController(mockNavigator, null);
	}
}
