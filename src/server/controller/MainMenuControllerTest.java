package server.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;
import shared.model.communication.CommunicationException;
import shared.model.communication.IMessageFactory;
import shared.model.communication.ServerCommunicator;

/**
 * Tests the MainMenuController class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ MainMenuController.class })
public class MainMenuControllerTest {
	// a mock navigator
	private INavigator mockNavigator;

	// a mock controller factory
	private IControllerFactory mockControllerFactory;

	// the main menu controller used for testing
	private IMainMenuController mainMenuController;

	// a mock communicator used for testing
	private ServerCommunicator mockServerCommunicator;
	// a mock controller
	private IGameHostingController mockGameHostingController;

	/**
	 * Sets up the tests.
	 * 
	 * @throws Exception
	 *             This should never happen.
	 */
	@Before
	public void setUp() throws Exception {
		// mock the communicator
		mockServerCommunicator = mock(ServerCommunicator.class);
		mockGameHostingController = mock(IGameHostingController.class);
		PowerMockito.whenNew(ServerCommunicator.class)
				.withArguments(any(IMessageFactory.class))
				.thenReturn(mockServerCommunicator);

		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		mainMenuController = new MainMenuController(mockNavigator,
				mockControllerFactory);
	}

	/**
	 * Ensures the constructor sets the connected clients property to an empty
	 * array.
	 */
	@Test
	public void testConstructor() {
		new MainMenuController(mockNavigator, mockControllerFactory);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when null is
	 * provided for the navigator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNavigatorNull() {
		new MainMenuController(null, mockControllerFactory);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when provided with
	 * a null controller factory.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorControllerFactoryNull() {
		new MainMenuController(mockNavigator, null);
	}

	/**
	 * Ensures the navigator method returns the navigator passed in to the
	 * constructor.
	 */
	@Test
	public void testNavigator() {
		assertEquals(mockNavigator, mainMenuController.navigator());
	}

	/**
	 * Ensures the controllerFactory method returns the controller factory
	 * injected in the constructor.
	 */
	@Test
	public void testControllerFactory() {
		assertEquals(mockControllerFactory,
				mainMenuController.controllerFactory());
	}

	/**
	 * Ensures the start listening method attempts to start the server
	 * listening.
	 * 
	 * @throws Exception
	 *             this should not happen
	 */
	@Test
	public void testStartListeningStartsListening() throws Exception {
		when(mockControllerFactory.createGameHostingController(
						mockNavigator, mockServerCommunicator)).thenReturn(
				mockGameHostingController);
		mainMenuController = new MainMenuController(mockNavigator,
				mockControllerFactory);
		mainMenuController.startListening();

		verify(mockNavigator).push(mockGameHostingController);
	}

	/**
	 * Ensures the start listening method navigates the application to the start
	 * listening screen.
	 */
	@Test
	public void testStartListeningNavigatesApplicationIfSuccessful() {
		mockGameHostingController = mock(IGameHostingController.class);
		when(mockControllerFactory.createGameHostingController(
						mockNavigator, mockServerCommunicator)).thenReturn(
				mockGameHostingController);
		mainMenuController.startListening();
		//test the navigator actually push the gameHostingController once
		verify(mockNavigator).push(mockGameHostingController);
	}

	/**
	 * Ensures the start listening method displays a message to the user if the
	 * server cannot start listening.
	 * 
	 * @throws Exception
	 *             This should never happen.
	 */
	@Test
	public void testStartListeningDisplaysMessageIfNotSuccessful()
			throws Exception {
		PowerMockito.whenNew(ServerCommunicator.class).withParameterTypes(IMessageFactory.class).withArguments(anyObject())
				.thenThrow(new CommunicationException("Test Exception"));
		// recreate the main menu controller
		mainMenuController = new MainMenuController(mockNavigator,
				mockControllerFactory);

		mainMenuController.startListening();
		assertEquals("Test Exception", mainMenuController.errorMessage());
	}

	/**
	 * Ensures the start listening method does not navigate the application if
	 * the server cannot start listening.
	 * @throws Exception this should not happen
	 */
	@Test
	public void testStartListeningDoesntNavigateIfNotSuccessful() throws Exception {
		PowerMockito.whenNew(ServerCommunicator.class)
		.withArguments(IMessageFactory.class)
		.thenThrow(new CommunicationException("Test Exception"));
		
		mainMenuController = new MainMenuController(mockNavigator,
				mockControllerFactory);
		mainMenuController.startListening();
		verify(mockNavigator,never()).push(mockGameHostingController);
	}

	/**
	 * Ensures the exit method exits the application.
	 */
	@Test
	public void testExitCommandExitsApplication() {
		/*
		 * Instructions for how to do this insanely difficult test are from:
		 * http://loongest.com/powermock/mock-static-void-method/
		 */

		// mock the system class
		PowerMockito.mockStatic(System.class);

		// run the test code
		mainMenuController.exit();

		// make sure System.exit(0) was called
		PowerMockito.verifyStatic(times(1));
		System.exit(0);
	}
}