package client.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import java.util.Timer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import shared.controller.INavigator;
import shared.core.ICommand;
import shared.model.communication.*;
import client.controller.GameLobbyController.CheckForMessagesFromHostTimerTask;

/**
 * Tests GameLobbyController.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GameLobbyController.class)
public class GameLobbyControllerTest
{
	// a mock navigator
	private INavigator mockNavigator;

	// a mock communicator
	private Communicator mockCommunicator;

	// a mock controller factory
	private IControllerFactory mockControllerFactory;

	// the test controller
	private IGameLobbyController gameLobbyController;

	// the test command
	private ICommand testCommand;

	/**
	 * Sets up the tests.
	 * 
	 * @throws Exception
	 *             If an unexpected exception is thrown during a test.
	 */
	@Before
	public void setUp() throws Exception
	{
		// ensures timers are not started in creation of controllers
		Timer mockTimer = mock(Timer.class);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(mockTimer);
		
		CheckForMessagesFromHostTimerTask mockTimerTask = mock(CheckForMessagesFromHostTimerTask.class);
		PowerMockito.whenNew(CheckForMessagesFromHostTimerTask.class).withNoArguments().thenReturn(mockTimerTask);
		
		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		mockCommunicator = mock(Communicator.class);
		stub(mockCommunicator.connected()).toReturn(true);
		gameLobbyController = new GameLobbyController(mockNavigator,
				mockControllerFactory, mockCommunicator);
		testCommand = mock(ICommand.class);

	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null navigator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new GameLobbyController(null, mockControllerFactory, mockCommunicator);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null communicator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorCommunicatorNull()
	{
		new GameLobbyController(mockNavigator, mockControllerFactory, null);
	}

	/**
	 * Ensures the constructor throws an illegal state exception when provided
	 * with a communicator that is not connected to a server.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorCommunicatorNotConnected()
	{
		stub(mockCommunicator.connected()).toReturn(false);
		new GameLobbyController(mockNavigator, mockControllerFactory,
				mockCommunicator);
	}

	/**
	 * Ensures the navigator method returns the navigator passed in to the
	 * constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, gameLobbyController.navigator());
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null controller factory.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new GameLobbyController(mockNavigator, mockControllerFactory, null);
	}

	/**
	 * Ensures the navigator method returns the controller factory passed in to
	 * the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, gameLobbyController
				.controllerFactory());
	}

	/**
	 * Ensures the communicator method returns the communicator passed in to the
	 * constructor
	 */
	@Test
	public void testCommunicator()
	{
		assertEquals(mockCommunicator, gameLobbyController.communicator());
	}

	/**
	 * Ensures the cancel method navigates the application to the multiplayer
	 * screen.
	 */
	@Test
	public void testCancelCommandNavigatesMultiplayer()
	{
		gameLobbyController.cancel();
		verify(mockNavigator, times(1)).pop();
	}

	/**
	 * Ensures the cancel method disconnects the application from the server.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCancelCommandDisconnectsApplicationFromServer()
			throws Exception
	{
		PowerMockito.whenNew(Communicator.class).withParameterTypes(
				IMessageFactory.class, String.class).withArguments(
				any(MessageFactory.class), any(String.class)).thenReturn(
				mockCommunicator);
		when(mockCommunicator.receivedMessages(anyInt())).thenReturn(new IMessage[0]);
		gameLobbyController.cancel();
		verify(mockCommunicator).disconnect();

	}

	/**
	 * Ensures setting the player number property fires the bound command.
	 */
	@Test
	public void testServerAddressPropertyChangeFiresBoundCommand()
	{
		gameLobbyController.bind("numberOfPlayers", testCommand);
		gameLobbyController.setNumberOfPlayers(4);
		verify(testCommand, times(1)).execute();
	}
}
