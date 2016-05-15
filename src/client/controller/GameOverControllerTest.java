package client.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;
import shared.model.Score;
import shared.model.communication.CommunicationException;
import shared.model.communication.Communicator;
import shared.model.communication.ICommunicator;
import shared.model.communication.IMessageFactory;
import shared.model.communication.MessageFactory;

/**
 * Tests GameOverController.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GameOverController.class)
public class GameOverControllerTest
{
	// a mock navigator
	private INavigator mockNavigator;

	// a mock controller factory
	private IControllerFactory mockControllerFactory;

	// the test controller
	private IGameOverController gameOverController;

	// a mock communicator
	private Communicator mockCommunicator;

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		mockNavigator = mock(INavigator.class);
		mockCommunicator = mock(Communicator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		gameOverController = new GameOverController(mockNavigator,
				mockControllerFactory, new Score[4], mockCommunicator);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null navigator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new GameOverController(null, mockControllerFactory, new Score[4],
				mockCommunicator);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null scores array.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorScoresNull()
	{
		new GameOverController(mockNavigator, mockControllerFactory, null,
				mockCommunicator);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null communicator
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorCommunicatorNull()
	{
		new GameOverController(mockNavigator, mockControllerFactory,
				new Score[4], null);
	}

	/**
	 * Ensures the navigator method returns the navigator passed in to the
	 * constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, gameOverController.navigator());
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null controller factory.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new GameOverController(mockNavigator, null, new Score[4],
				mockCommunicator);
	}

	/**
	 * Ensures the navigator method returns the controller factory passed in to
	 * the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, gameOverController
				.controllerFactory());
	}

	/**
	 * Ensures the main menu method navigates the application to the main menu
	 * screen.
	 */
	@Test
	public void testMainMenuNavigatesToMainMenu()
	{
		gameOverController.mainMenu();
		verify(mockNavigator, times(2)).pop();
	}

	/**
	 * Ensures the rematch command attempts to reestablish the server
	 * connection.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRematchAttemptsToEstablishServerConnection()
			throws Exception
	{
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);
		when(mockCommunicator.connected()).thenReturn(true);
		gameOverController.rematch();
		PowerMockito.verifyNew(Communicator.class);
	}

	/**
	 * Ensures the rematch command navigates to the game lobby.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRematchNavigatesToGameLobbyIfSuccessful() throws Exception
	{
		GameLobbyController mockGameLobbyController = mock(GameLobbyController.class);

		when(
				mockControllerFactory.createGameLobbyController(
						any(INavigator.class), any(ICommunicator.class)))
				.thenReturn(mockGameLobbyController);
		when(mockCommunicator.connected()).thenReturn(true);
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);
		gameOverController.rematch();
		verify(mockNavigator).push(mockGameLobbyController);
	}

	/**
	 * Ensures the rematch method navigates to the multiplayer screen if the
	 * connection fails when the communicator is not connected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRematchNavigatesToMultiplayerIfConnectionFailsCommunicatorNotConnected()
			throws Exception
	{
		MultiplayerController mockMultiplayerController = mock(MultiplayerController.class);

		when(
				mockControllerFactory
						.createMultiplayerController(any(INavigator.class)))
				.thenReturn(mockMultiplayerController);
		when(mockCommunicator.connected()).thenReturn(false);
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);
		gameOverController.rematch();
		verify(mockNavigator).push(mockMultiplayerController);
	}

	/**
	 * Ensures the rematch method navigates to the multiplayer screen if the
	 * connection fails during construction of communicator.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRematchNavigatesToMultiplayerIfConnectionFailsCommunicatorThrowsException()
			throws Exception
	{
		MultiplayerController mockMultiplayerController = mock(MultiplayerController.class);

		when(
				mockControllerFactory
						.createMultiplayerController(any(INavigator.class)))
				.thenReturn(mockMultiplayerController);

		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenThrow(
				new CommunicationException());
		gameOverController.rematch();
		verify(mockNavigator).push(mockMultiplayerController);
	}

	/**
	 * Ensures the rematch method sets the error message on the multiplayer
	 * screen if the connection fails during construction of communicator.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRematchSetsErrorMessageIfConnectionFailsCommunicatorThrowsException()
			throws Exception
	{
		MultiplayerController mockMultiplayerController = mock(MultiplayerController.class);

		when(
				mockControllerFactory
						.createMultiplayerController(any(INavigator.class)))
				.thenReturn(mockMultiplayerController);

		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenThrow(
				new CommunicationException());
		gameOverController.rematch();
		verify(mockNavigator).push(mockMultiplayerController);
		verify(mockMultiplayerController).setErrorMessage(any(String.class));
	}

	/**
	 * Ensures the rematch method sets the error message on the multiplayer
	 * screen if the connection fails because communicator is not connected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRematchSetsErrorMessageIfConnectionFailsCommunicatorNotConnected()
			throws Exception
	{
		MultiplayerController mockMultiplayerController = mock(MultiplayerController.class);

		when(
				mockControllerFactory
						.createMultiplayerController(any(INavigator.class)))
				.thenReturn(mockMultiplayerController);

		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);
		when(mockCommunicator.connected()).thenReturn(false);
		
		gameOverController.rematch();
		
		verify(mockNavigator).push(mockMultiplayerController);
		verify(mockMultiplayerController).setErrorMessage(any(String.class));
	}
}
