package client.controller;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;
import shared.core.ICommand;
import shared.model.communication.CommunicationException;
import shared.model.communication.Communicator;
import shared.model.communication.ICommunicator;
import shared.model.communication.IMessageFactory;
import shared.model.communication.MessageFactory;

/**
 * Tests MultiplayerController.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MultiplayerController.class)
public class MultiplayerControllerTest
{
	// a mock navigator
	private INavigator mockNavigator;

	// a mock controller factory
	private IControllerFactory mockControllerFactory;

	// the test controller
	private IMultiplayerController multiplayerController;

	// a mock command
	private ICommand testCommand;

	// a mock communicator
	private Communicator mockCommunicator;

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		mockCommunicator = mock(Communicator.class);
		multiplayerController = new MultiplayerController(mockNavigator,
				mockControllerFactory);
		testCommand = mock(ICommand.class);
	}

	/**
	 * Ensures the constructor sets the error message and server address
	 * properties to empty strings.
	 */
	@Test
	public void testConstructor()
	{
		assertEquals("", multiplayerController.serverAddress());
		assertEquals("", multiplayerController.errorMessage());
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null navigator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new MultiplayerController(null, mockControllerFactory);
	}

	/**
	 * Ensures the navigator method returns the navigator passed in to the
	 * constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, multiplayerController.navigator());
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null controller factory.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new MultiplayerController(mockNavigator, null);
	}

	/**
	 * Ensures the navigator method returns the controller factory passed in to
	 * the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, multiplayerController
				.controllerFactory());
	}

	/**
	 * Ensures the join method joins the server as a player.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJoinJoinsServer() throws Exception
	{
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);
		multiplayerController.join();
		PowerMockito.verifyNew(Communicator.class);
	}

	/**
	 * Ensures the join method navigates to the game lobby if successful.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJoinNaviagesToGameLobbyIfSuccessful() throws Exception
	{
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);

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

		multiplayerController.join();
		verify(mockNavigator).push(mockGameLobbyController);
	}

	/**
	 * Ensures the observe method sets the controller's error message property
	 * is an error message if not successful.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJoinSetsErrorMessageIfNotSuccessful() throws Exception
	{
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenThrow(
				new CommunicationException());
		multiplayerController.setErrorMessage("reset me!");
		multiplayerController.join();
		assertFalse("reset me!".equals(multiplayerController.errorMessage()));

	}

	/**
	 * Ensures setting the server address property fires the bound command.
	 */
	@Test
	public void testServerAddressPropertyChangeFiresBoundCommand()
	{
		multiplayerController.bind("serverAddress", testCommand);
		multiplayerController.setServerAddress("server address");
		verify(testCommand, times(1)).execute();
	}

	/**
	 * Ensures setting the error message property fires the bound command.
	 */
	@Test
	public void testErrorMessagePropertyChangeFiresBoundCommand()
	{
		multiplayerController.bind("errorMessage", testCommand);
		multiplayerController.setErrorMessage("error message");
		verify(testCommand, times(1)).execute();
	}
}
