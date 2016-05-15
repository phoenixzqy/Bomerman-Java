package client.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.util.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;
import shared.model.Score;
import shared.model.communication.ICommunicator;
import client.controller.GameLobbyController.CheckForMessagesFromHostTimerTask;

/**
 * Tests ControllerFactory.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GameLobbyController.class)
public class ControllerFactoryTest
{
	// the controller factory
	IControllerFactory controllerFactory;

	// a mock navigator
	private INavigator mockNavigator;

	// a mock communicator
	private ICommunicator mockCommunicator;

	// a test error message
	private String testErrorMessage;

	/**
	 * Sets up the tests.
	 * @throws Exception Should not happen.
	 */
	@Before
	public void setUp() throws Exception
	{
		controllerFactory = new ControllerFactory();
		mockNavigator = mock(INavigator.class);
		mockCommunicator = mock(ICommunicator.class);
		testErrorMessage = "Test error message.";
		stub(mockCommunicator.connected()).toReturn(true);
		
	}

	/**
	 * Ensures the createConnectionFailureController method returns a new
	 * IConnectionFailureController.
	 */
	@Test
	public void testCreateConnectionFailureController()
	{
		IConnectionFailureController controller = controllerFactory
				.createConnectionFailureController(mockNavigator,
						testErrorMessage);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
		assertEquals(testErrorMessage, controller.errorMessage());
	}

	/**
	 * Ensures the createConnectionFailureController method throws an exception
	 * when the navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateConnectionFailureControllerNavigatorNull()
	{
		controllerFactory.createConnectionFailureController(null,
				testErrorMessage);
	}

	/**
	 * Ensures the createConnectionFailureController method throws an exception
	 * when the error message is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateConnectionFailureControllerErrorMessageNull()
	{
		controllerFactory
				.createConnectionFailureController(mockNavigator, null);
	}

	/**
	 * Ensures the createCreditsController method returns a new
	 * ICreditsController.
	 */
	@Test
	public void testCreateCreditsController()
	{
		ICreditsController controller = controllerFactory
				.createCreditsController(mockNavigator);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
	}

	/**
	 * Ensures the createCreditsController method throws an exception when the
	 * navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateCreditsControllerNavigatorNull()
	{
		controllerFactory.createCreditsController(null);
	}

	/**
	 * Ensures the createGameController method returns a new IGameController.
	 */
	@Test
	public void testCreateGameController()
	{
		IGameController controller = controllerFactory.createGameController(
				mockNavigator, mockCommunicator, 4, 10);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
		assertEquals(4, controller.scores().length);
		assertEquals(10, controller.playerGameObjectId());
	}

	/**
	 * Ensures the createGameController method throws an exception when the
	 * navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameControllerNavigatorNull()
	{
		controllerFactory.createGameController(null, mockCommunicator, 4, 10);
	}

	/**
	 * Ensures the createGameController method throws an exception when the
	 * communicator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameControllerCommunicatorNull()
	{
		controllerFactory.createGameController(mockNavigator, null, 4, 10);
	}

	/**
	 * Ensures the createGameController method throws an exception when the
	 * communicator is not connected.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateGameControllerCommunicatorNotConnected()
	{
		when(mockCommunicator.connected()).thenReturn(false);
		controllerFactory.createGameController(mockNavigator, mockCommunicator,
				4, 10);
	}

	/**
	 * Ensures the createGameLobbyController method returns a new
	 * IGameLobbyController.
	 * @throws Exception Exceptions with PowerMockito. This should not happen
	 */
	@Test
	public void testCreateGameLobbyController() throws Exception
	{
		// ensures timers are not started in creation of controllers
		Timer mockTimer = mock(Timer.class);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(mockTimer);
		
		CheckForMessagesFromHostTimerTask mockTimerTask = mock(CheckForMessagesFromHostTimerTask.class);
		PowerMockito.whenNew(CheckForMessagesFromHostTimerTask.class).withNoArguments().thenReturn(mockTimerTask);
		
		IGameLobbyController controller = controllerFactory
				.createGameLobbyController(mockNavigator, mockCommunicator);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
	}

	/**
	 * Ensures the createGameLobbyController method throws an exception when the
	 * navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameLobbyControllerNavigatorNull()
	{
		controllerFactory.createGameLobbyController(null, mockCommunicator);
	}

	/**
	 * Ensures the createGameLobbyController method throws an exception when the
	 * communicator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameLobbyControllerThrowsExceptionCommunicatorNull()
	{
		controllerFactory.createGameLobbyController(mockNavigator, null);
	}

	/**
	 * Ensures the createGameLobbyController method throws an exception when the
	 * communicator is not connected.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateGameLobbyControllerThrowsExceptionCommunicatorNotConnected()
	{
		stub(mockCommunicator.connected()).toReturn(false);
		controllerFactory.createGameLobbyController(mockNavigator,
				mockCommunicator);
	}

	/**
	 * Ensures the createGameOverController method returns a new
	 * IGameOverController.
	 */
	@Test
	public void testCreateGameOverController()
	{
		IGameOverController controller = controllerFactory
				.createGameOverController(mockNavigator, new Score[4], mockCommunicator);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
	}

	/**
	 * Ensures the createGameOverController method throws an exception when the
	 * navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameOverControllerNavigatorNull()
	{
		controllerFactory.createGameOverController(null, new Score[4], mockCommunicator);
	}

	/**
	 * Ensures the createGameOverController method throws an exception when
	 * scores is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameOverControllerScoresNull()
	{
		controllerFactory.createGameOverController(mockNavigator, null, mockCommunicator);
	}
	
	/**
	 * Ensures the createGameOverController method throws an exception when
	 * communicator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateGameOverControllerCommunicatorNull()
	{
		controllerFactory.createGameOverController(mockNavigator, new Score[4], null);
	}

	/**
	 * Ensures the createMainMenuController method returns a new
	 * IMainMenuController.
	 */
	@Test
	public void testCreateMainMenuController()
	{
		IMainMenuController controller = controllerFactory
				.createMainMenuController(mockNavigator);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
	}

	/**
	 * Ensures the createMainMenuController method throws an exception when the
	 * navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateMainMenuControllerNavigatorNull()
	{
		controllerFactory.createMainMenuController(null);
	}

	/**
	 * Ensures the createMultiplayerController method returns a new
	 * IMultiplayerController.
	 */
	@Test
	public void testCreateMultiplayerController()
	{
		IMultiplayerController controller = controllerFactory
				.createMultiplayerController(mockNavigator);
		assertNotNull(controller);
		assertEquals(mockNavigator, controller.navigator());
		assertEquals(controllerFactory, controller.controllerFactory());
	}

	/**
	 * Ensures the createMultiplayerController method throws an exception when
	 * the navigator is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateMultiplayerControllerNavigatorNull()
	{
		controllerFactory.createMultiplayerController(null);
	}
}
