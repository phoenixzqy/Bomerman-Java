package server.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.util.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import server.controller.GameHostingController.GameStepTimerTask;
import server.model.Game;
import shared.controller.INavigator;
import shared.core.ICommand;
import shared.model.communication.IMessageFactory;
import shared.model.communication.ServerCommunicator;

/**
 * Tests the GameHostingController class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GameHostingController.class)
public class GameHostingControllerTest 
{
	// a mock navigator
	private INavigator mockNavigator;

	// a mock controller factory
	private IControllerFactory mockControllerFactory;

	// the controller to test
	private IGameHostingController gameHostingController;

	// a mock command
	private ICommand mockCommand;

	// a mock server communicator
	private ServerCommunicator mockServerCommunicator;

	// a mock Timer
	private Timer mockTimer;

	/**
	 * Sets up the tests.
	 * 
	 * @throws Exception
	 *             this should not happen
	 */
	@Before
	public void setUp() throws Exception
	{
		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		mockCommand = mock(ICommand.class);
		mockServerCommunicator = mock(ServerCommunicator.class);
		PowerMockito.whenNew(ServerCommunicator.class).withParameterTypes(
				IMessageFactory.class).withArguments(anyObject()).thenReturn(
				mockServerCommunicator);
		mockTimer = mock(Timer.class);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(
				mockTimer);
		gameHostingController = new GameHostingController(mockNavigator,
				mockControllerFactory, mockServerCommunicator);
		Game mockGame = mock(Game.class);
		PowerMockito.whenNew(Game.class)
				.withArguments(anyObject(), anyObject()).thenReturn(mockGame);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null navigator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new GameHostingController(null, mockControllerFactory,
				mockServerCommunicator);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when provided with
	 * a null controller factory.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new GameHostingController(mockNavigator, null, mockServerCommunicator);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when provided with
	 * a null server communicator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorServerCommunicatorNull()
	{
		new GameHostingController(mockNavigator, mockControllerFactory, null);
	}

	/**
	 * Ensures the navigator method returns the navigator passed in to the
	 * constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, gameHostingController.navigator());
	}

	/**
	 * Ensures the serverCommunicator method returns the serverCommunicator
	 * passed in to the constructor.
	 */
	@Test
	public void testServerCommunicator()
	{
		assertEquals(mockServerCommunicator, gameHostingController
				.serverCommunicator());
	}

	/**
	 * Ensures the controllerFactory method returns the controller factory
	 * injected in the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, gameHostingController
				.controllerFactory());
	}

	/**
	 * Ensures the startOrStopGameCommand starts the game if the game is not
	 * running.
	 * 
	 * @throws Exception
	 *             this should not happen
	 */
	@Test
	public void testStartGame() throws Exception
	{

		GameStepTimerTask mockGameStepTimerTask = mock(GameStepTimerTask.class);
		PowerMockito.whenNew(GameStepTimerTask.class).withNoArguments()
				.thenReturn(mockGameStepTimerTask);
		mockTimer = mock(Timer.class);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(
				mockTimer);
		gameHostingController = new GameHostingController(mockNavigator,
				mockControllerFactory, mockServerCommunicator);
		verify(mockTimer).schedule(eq(mockGameStepTimerTask), anyLong(),
				anyLong());
		when(mockServerCommunicator.numberOfConnectedCommunicators()).thenReturn(2);
		gameHostingController.startOrStopGame();
		assertTrue(gameHostingController.running());
	}

	/**
	 * Ensures the startOrStopGame method executes the command bound to running
	 * with the correct value when the game is stopped.
	 */
	@Test
	public void testStartOrStopGameExecutesRunningCommandWhenStopped()
	{
		when(mockServerCommunicator.numberOfConnectedCommunicators()).thenReturn(1);
		gameHostingController.bind("running", mockCommand);
		gameHostingController.startOrStopGame();
		verify(mockCommand).execute();
	}

	/**
	 * Ensures the startorStopGame method executes the command bound to running
	 * with the correct value when the game is started.
	 */
	@Test
	public void testStartOrStopGameExecutesRunningCommandWhenStarted()
	{
		when(mockServerCommunicator.numberOfConnectedCommunicators()).thenReturn(1);
		gameHostingController.startOrStopGame();
		gameHostingController.bind("running", mockCommand);
		gameHostingController.startOrStopGame();
		verify(mockCommand).execute();
	}

	/**
	 * Ensures the startOrStopCommand stops the game if the game is already
	 * running.
	 * 
	 * @throws Exception
	 *             When problems occur with PowerMockito.
	 */
	@Test
	public void testStopGame() throws Exception
	{
		GameStepTimerTask mockGameStepTimerTask = mock(GameStepTimerTask.class);
		PowerMockito.whenNew(GameStepTimerTask.class).withNoArguments()
				.thenReturn(mockGameStepTimerTask);
		mockTimer = mock(Timer.class);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(
				mockTimer);
		gameHostingController = new GameHostingController(mockNavigator,
				mockControllerFactory, mockServerCommunicator);
		gameHostingController.startOrStopGame();
		gameHostingController.startOrStopGame();
		assertFalse(gameHostingController.running());
	}

	/**
	 * Ensures the main menu command stops the game if it is currently running
	 * before returning to the main menu.
	 * 
	 * @throws Exception
	 *             Thrown by PowerMockito.
	 */
	@Test
	public void testMainMenuStopsGame() throws Exception
	{
		GameStepTimerTask mockGameStepTimerTask = mock(GameStepTimerTask.class);
		PowerMockito.whenNew(GameStepTimerTask.class).withNoArguments()
				.thenReturn(mockGameStepTimerTask);
		mockTimer = mock(Timer.class);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(
				mockTimer);
		gameHostingController = new GameHostingController(mockNavigator,
				mockControllerFactory, mockServerCommunicator);
		gameHostingController.startOrStopGame();
		gameHostingController.mainMenu();
		assertFalse(gameHostingController.running());
		verify(mockTimer).cancel();
	}

	/**
	 * Ensures the main menu command navigates back to the main menu.
	 */
	@Test
	public void testMainMenuNavigatesBack()
	{
		gameHostingController.mainMenu();
		verify(mockNavigator, times(1)).pop();
	}
}
