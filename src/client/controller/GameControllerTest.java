package client.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.controller.INavigator;
import shared.core.ICommand;
import shared.model.GameObjectType;
import shared.model.IScore;
import shared.model.Key;
import shared.model.KeyAction;
import shared.model.communication.CommunicationException;
import shared.model.communication.GameObjectCreatedMessage;
import shared.model.communication.GameObjectDestroyedMessage;
import shared.model.communication.GameObjectUpdatedMessage;
import shared.model.communication.ICommunicator;
import shared.model.communication.IMessage;
import shared.model.communication.KeyMessage;
import shared.model.communication.ScoreUpdatedMessage;
import client.controller.GameController.GameTimerTask;
import client.model.IGameObject;

/**
 * Tests game controller.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GameController.class)
public class GameControllerTest
{

	// a mock navigator
	private INavigator mockNavigator;

	// a mock controller factory
	private IControllerFactory mockControllerFactory;

	// a mock communicator
	private ICommunicator mockCommunicator;

	// the test controller
	private IGameController gameController;

	// a mock connection failure controller
	IConnectionFailureController mockConnectionFailureController;

	// a mock timer
	private Timer mockTimer;

	// a mock game timer task
	private GameTimerTask mockGameTimerTask;

	/**
	 * Sets up the tests.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Before
	public void setUp() throws Exception
	{
		// mock the timer so it doesn't actually fire
		mockTimer = mock(Timer.class);
		mockGameTimerTask = mock(GameTimerTask.class);
		PowerMockito.whenNew(GameTimerTask.class).withNoArguments().thenReturn(
				mockGameTimerTask);
		PowerMockito.whenNew(Timer.class).withNoArguments().thenReturn(
				mockTimer);

		mockNavigator = mock(INavigator.class);
		mockControllerFactory = mock(IControllerFactory.class);
		mockCommunicator = mock(ICommunicator.class);
		when(mockCommunicator.connected()).thenReturn(true);
		when(mockCommunicator.receivedMessages()).thenReturn(new IMessage[0]);
		gameController = new GameController(mockNavigator,
				mockControllerFactory, mockCommunicator, 4, 10);

		mockConnectionFailureController = mock(IConnectionFailureController.class);
		when(
				mockControllerFactory.createConnectionFailureController(
						any(INavigator.class), anyString())).thenReturn(
				mockConnectionFailureController);
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null navigator.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNavigatorNull()
	{
		new GameController(null, mockControllerFactory, mockCommunicator, 4, 10);
	}

	/**
	 * Ensures the navigator method returns the navigator passed in to the
	 * constructor.
	 */
	@Test
	public void testNavigator()
	{
		assertEquals(mockNavigator, gameController.navigator());
	}

	/**
	 * Ensures the constructor throws a null pointer exception when provided
	 * with a null controller factory.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorControllerFactoryNull()
	{
		new GameController(mockNavigator, null, mockCommunicator, 4, 10);
	}

	/**
	 * Ensures the navigator method returns the controller factory passed in to
	 * the constructor.
	 */
	@Test
	public void testControllerFactory()
	{
		assertEquals(mockControllerFactory, gameController.controllerFactory());
	}

	/**
	 * Ensures the constructor throws a NullPointerException if the communicator
	 * is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testControllerCommunicatorNull()
	{
		new GameController(mockNavigator, mockControllerFactory, null, 4, 10);
	}

	/**
	 * Ensures the constructor throws an IllegalArgumentException if the
	 * communicator is not connected to a server.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testControllerCommunicatorNotConnected()
	{
		when(mockCommunicator.connected()).thenReturn(false);
		new GameController(mockNavigator, mockControllerFactory,
				mockCommunicator, 4, 10);
	}

	/**
	 * Ensures the communicator method returns the communicator passed in to the
	 * constructor.
	 */
	@Test
	public void testCommunicator()
	{
		assertEquals(mockCommunicator, gameController.communicator());
	}

	/**
	 * Ensures the quit game command takes the application to the multiplayer
	 * screen.
	 */
	@Test
	public void testQuitGameCommandNavigatesToMultiplayer()
	{
		gameController.quitGame();
		verify(mockNavigator, times(2)).pop();
	}

	/**
	 * Ensures the game controller keyEventDidOccur method sends a message to
	 * the server if the event is press up.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurPressUp() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.UP, KeyAction.PRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.UP, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.PRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller keyEventDidOccur method sends a message to
	 * the server if the event is press down.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurPressDown() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.DOWN, KeyAction.PRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.DOWN, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.PRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller keyEventDidOccur method and sends a message
	 * to the server if the event is press left.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurPressLeft() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.LEFT, KeyAction.PRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.LEFT, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.PRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller keyEventDidOccur method sends a message to
	 * the server if the event is press right.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurPressRight() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.RIGHT, KeyAction.PRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.RIGHT, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.PRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller keyEventDidOccur method sends a message to
	 * the server if the event is press space.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurPressSpace() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.SPACE, KeyAction.PRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.SPACE, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.PRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller listens to key depress events and sends a
	 * message to the server if the key is up.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurDepressUp() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.UP, KeyAction.DEPRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.UP, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.DEPRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller listens to key depress events and sends a
	 * message to the server if the key is down.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurDepressDown() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.DOWN, KeyAction.DEPRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.DOWN, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.DEPRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller listens to key depress events and sends a
	 * message to the server if the key is left.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurDepressLeft() throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.LEFT, KeyAction.DEPRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.LEFT, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.DEPRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller listens to key depress events and sends a
	 * message to the server if the key is right.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurDepressRight()
			throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.RIGHT, KeyAction.DEPRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.RIGHT, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.DEPRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller listens to key depress events and sends a
	 * message to the server if the key is space.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurDepressSpace()
			throws CommunicationException
	{
		ArgumentCaptor<IMessage> messageCaptor = ArgumentCaptor
				.forClass(IMessage.class);

		gameController.keyEventDidOccur(Key.SPACE, KeyAction.DEPRESS);
		verify(mockCommunicator).sendMessage(messageCaptor.capture());
		assertTrue(messageCaptor.getValue() instanceof KeyMessage);
		assertEquals(10, ((KeyMessage) messageCaptor.getValue())
				.gameObjectIdentifier());
		assertEquals(Key.SPACE, ((KeyMessage) messageCaptor.getValue()).key());
		assertEquals(KeyAction.DEPRESS, ((KeyMessage) messageCaptor.getValue())
				.action());
	}

	/**
	 * Ensures the game controller navigates to the connection failure screen
	 * with the appropriate error message if a connection failure error occurs
	 * in the keyEventDidOccur method.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testKeyEventDidOccurConnectionFailure()
			throws CommunicationException
	{
		String testMessage = "Test Message";
		doThrow(new CommunicationException(testMessage)).when(mockCommunicator)
				.sendMessage(any(IMessage.class));

		gameController.keyEventDidOccur(Key.UP, KeyAction.PRESS);
		verify(mockControllerFactory).createConnectionFailureController(
				mockNavigator, testMessage);
		verify(mockNavigator).push(mockConnectionFailureController);
	}

	/**
	 * Ensures the keyEventDidOccur method throws a NullPointerException when
	 * provided with a null key.
	 */
	@Test(expected = NullPointerException.class)
	public void testKeyEventDidOccurKeyNull()
	{
		gameController.keyEventDidOccur(null, KeyAction.PRESS);
	}

	/**
	 * Ensures the keyEventDidOccur method throws a NullPointerException when
	 * provided with a null action.
	 */
	@Test(expected = NullPointerException.class)
	public void testKeyEventDidOccurActionNull()
	{
		gameController.keyEventDidOccur(Key.UP, null);
	}

	/**
	 * Ensures the setGameObjects method throws a NullPointerException when null
	 * is provided as an argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetGameObjectsNull()
	{
		gameController.setGameObjects(null);
	}

	/**
	 * Ensures the setGameObjects method calls a bound command.
	 */
	@Test
	public void testSetGameObjectsCallsBoundCommand()
	{
		ICommand mockCommand = mock(ICommand.class);
		gameController.bind("gameObjects", mockCommand);
		verify(mockCommand, never()).execute();
		gameController.setGameObjects(new IGameObject[0]);
		verify(mockCommand).execute();
	}

	/**
	 * Ensures if the step method receives a create game object message, it adds
	 * the object to the game objects.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testStepReceivesCreateMessage() throws CommunicationException
	{
		// set up the mock creation message
		GameObjectCreatedMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessage.row()).thenReturn(1);
		when(mockMessage.column()).thenReturn(2);
		when(mockMessage.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		when(mockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage });

		gameController.step();

		IGameObject[] gameObjects = gameController.gameObjects();
		assertNotNull(gameObjects);
		assertEquals(1, gameObjects.length);
		assertEquals(1, gameObjects[0].row());
		assertEquals(2, gameObjects[0].column());
		assertEquals(3, gameObjects[0].identifier());
		assertEquals(GameObjectType.PLAYER, gameObjects[0].gameObjectType());
	}

	/**
	 * Ensures if the step method receives an update game object message, it
	 * updates the object in the game objects.
	 * 
	 * @throws CommunicationException
	 *             This should never happen.
	 */
	@Test
	public void testStepReceivesUpdateMessage() throws CommunicationException
	{
		// set up the mock creation message
		GameObjectCreatedMessage creationMockMessage = mock(GameObjectCreatedMessage.class);
		when(creationMockMessage.row()).thenReturn(1);
		when(creationMockMessage.column()).thenReturn(2);
		when(creationMockMessage.gameObjectType()).thenReturn(
				GameObjectType.PLAYER);
		when(creationMockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { creationMockMessage });

		gameController.step();

		// set up the update message
		GameObjectUpdatedMessage mockMessage = mock(GameObjectUpdatedMessage.class);
		when(mockMessage.row()).thenReturn(4);
		when(mockMessage.column()).thenReturn(5);
		when(mockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage });

		gameController.step();

		IGameObject[] gameObjects = gameController.gameObjects();
		assertNotNull(gameObjects);
		assertEquals(1, gameObjects.length);
		assertEquals(4, gameObjects[0].row());
		assertEquals(5, gameObjects[0].column());
		assertEquals(3, gameObjects[0].identifier());
		assertEquals(GameObjectType.PLAYER, gameObjects[0].gameObjectType());
	}

	/**
	 * Ensures if the step method receives a destroy game object method, it
	 * removes the object from the game objects.
	 * 
	 * @throws CommunicationException
	 *             This should never happen
	 */
	@Test
	public void testStepReceivesDestroyMessage() throws CommunicationException
	{
		// set up the mock creation message
		GameObjectCreatedMessage creationMockMessage = mock(GameObjectCreatedMessage.class);
		when(creationMockMessage.row()).thenReturn(1);
		when(creationMockMessage.column()).thenReturn(2);
		when(creationMockMessage.gameObjectType()).thenReturn(
				GameObjectType.PLAYER);
		when(creationMockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { creationMockMessage });

		gameController.step();

		// set up the destroy message
		GameObjectDestroyedMessage mockMessage = mock(GameObjectDestroyedMessage.class);
		when(mockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage });

		gameController.step();

		IGameObject[] gameObjects = gameController.gameObjects();
		assertNotNull(gameObjects);
		assertEquals(0, gameObjects.length);
	}

	/**
	 * Ensures if the step method receives a update score message, it
	 * updates the corresponding player's score (assuming the player
	 * with the given game object id has been established).
	 * 
	 * @throws CommunicationException
	 *             This should never happen
	 */
	@Test
	public void testStepReceivesUpdateScoreMessage()
			throws CommunicationException
	{
		// set up the mock score message
		ScoreUpdatedMessage scoreMockMessage = mock(ScoreUpdatedMessage.class);
		when(scoreMockMessage.score()).thenReturn(7);
		when(scoreMockMessage.gameObjectIdentifier()).thenReturn(3);
		
		GameObjectCreatedMessage playerCreatedMockMessage = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage.gameObjectIdentifier()).thenReturn(3);
		when(playerCreatedMockMessage.gameObjectType()).thenReturn(GameObjectType.PLAYER);

		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { playerCreatedMockMessage, scoreMockMessage });

		gameController.step();

		IScore[] scores = gameController.scores();
		assertNotNull(scores);
		assertEquals(7, scores[0].getScore());
	}
	
	/**
	 * Ensures if the step method receives a update score message, it
	 * updates the corresponding player's score with multiple players
	 * (assuming the player with the given game object id has been established).
	 * 
	 * @throws CommunicationException
	 *             This should never happen
	 */
	@Test
	public void testStepReceivesUpdateScoreMessageMultiplePlayers()
			throws CommunicationException
	{
		// set up the mock score message
		ScoreUpdatedMessage scoreMockMessage = mock(ScoreUpdatedMessage.class);
		when(scoreMockMessage.score()).thenReturn(7);
		when(scoreMockMessage.gameObjectIdentifier()).thenReturn(0);
		
		GameObjectCreatedMessage playerCreatedMockMessage1 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage1.gameObjectIdentifier()).thenReturn(5);
		when(playerCreatedMockMessage1.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		
		GameObjectCreatedMessage playerCreatedMockMessage2 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage2.gameObjectIdentifier()).thenReturn(0);
		when(playerCreatedMockMessage2.gameObjectType()).thenReturn(GameObjectType.PLAYER);

		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { playerCreatedMockMessage1, playerCreatedMockMessage2, scoreMockMessage });

		gameController.step();

		IScore[] scores = gameController.scores();
		assertNotNull(scores);
		assertEquals(7, scores[0].getScore());
	}
	
	/**
	 * Ensures if the step method receives a update score message, it
	 * throws an exception if the game object identifier has not been
	 * established as a player.
	 * 
	 * @throws CommunicationException
	 *             This should never happen
	 */
	@Test (expected = IllegalStateException.class)
	public void testStepReceivesUpdateScoreMessageThrowsIllegalStateExceptionUnknownId()
			throws CommunicationException
	{
		// set up the mock score message
		ScoreUpdatedMessage scoreMockMessage = mock(ScoreUpdatedMessage.class);
		when(scoreMockMessage.score()).thenReturn(7);
		when(scoreMockMessage.gameObjectIdentifier()).thenReturn(3);
		
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { scoreMockMessage });

		gameController.step();
	}
	
	/**
	 * Ensures if the step method receives a update score message, it
	 * throws an exception if more players have been established that the
	 * Game was initially constructed with.
	 * 
	 * @throws CommunicationException
	 *             This should never happen
	 */
	@Test (expected = IllegalStateException.class)
	public void testStepReceivesUpdateScoreMessageThrowsIllegalStateExceptionTooManyPlayers()
			throws CommunicationException
	{
		// set up the mock score message
		ScoreUpdatedMessage scoreMockMessage = mock(ScoreUpdatedMessage.class);
		when(scoreMockMessage.score()).thenReturn(7);
		when(scoreMockMessage.gameObjectIdentifier()).thenReturn(4);
		
		GameObjectCreatedMessage playerCreatedMockMessage1 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage1.gameObjectIdentifier()).thenReturn(0);
		when(playerCreatedMockMessage1.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		
		GameObjectCreatedMessage playerCreatedMockMessage2 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage2.gameObjectIdentifier()).thenReturn(1);
		when(playerCreatedMockMessage2.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		
		GameObjectCreatedMessage playerCreatedMockMessage3 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage3.gameObjectIdentifier()).thenReturn(2);
		when(playerCreatedMockMessage3.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		
		GameObjectCreatedMessage playerCreatedMockMessage4 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage4.gameObjectIdentifier()).thenReturn(3);
		when(playerCreatedMockMessage4.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		
		GameObjectCreatedMessage playerCreatedMockMessage5 = mock(GameObjectCreatedMessage.class);
		when(playerCreatedMockMessage5.gameObjectIdentifier()).thenReturn(4);
		when(playerCreatedMockMessage5.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { playerCreatedMockMessage1, playerCreatedMockMessage2, 
						playerCreatedMockMessage3, playerCreatedMockMessage4, playerCreatedMockMessage5,
						scoreMockMessage });

		gameController.step();
	}

	/**
	 * Ensures if the step method receives an unknown message, it throws an
	 * exception.
	 * 
	 * @throws CommunicationException
	 *             This should never happen.
	 */
	@Test(expected = IllegalStateException.class)
	public void testStepReceivesUnknownMessage() throws CommunicationException
	{
		IMessage mockMessage = mock(IMessage.class);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage });

		gameController.step();
	}

	/**
	 * Ensures if the step method receives an update message for an object that
	 * isn't currently in the game, it throws an exception.
	 * 
	 * @throws CommunicationException
	 *             This should never happen.
	 */
	@Test(expected = IllegalStateException.class)
	public void testStepReceivesUpdateMessageForUnknownObject()
			throws CommunicationException
	{
		// set up the update message
		GameObjectUpdatedMessage mockMessage = mock(GameObjectUpdatedMessage.class);
		when(mockMessage.row()).thenReturn(4);
		when(mockMessage.column()).thenReturn(5);
		when(mockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage });

		gameController.step();
	}

	/**
	 * Ensures if the step method receives a destroy message for an object that
	 * isn't currently in the game, it throws an exception.
	 * 
	 * @throws CommunicationException
	 *             This should never happen.
	 */
	@Test(expected = IllegalStateException.class)
	public void testStepReceivesDestroyMessageForUnknownObject()
			throws CommunicationException
	{
		// set up the destroy message
		GameObjectDestroyedMessage mockMessage = mock(GameObjectDestroyedMessage.class);
		when(mockMessage.gameObjectIdentifier()).thenReturn(3);
		when(mockCommunicator.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage });

		gameController.step();
	}

	/**
	 * Ensures step navigates to the connection failure screen if a connection
	 * failure error occurs.
	 * 
	 * @throws CommunicationException
	 *             This should never happen.
	 */
	@Test
	public void testStepReceivesCommunicationExceptionFromCommunicator()
			throws CommunicationException
	{
		// set up the mocks
		String errorMessage = "Test error message.";
		when(mockCommunicator.receivedMessages()).thenThrow(
				new CommunicationException(errorMessage));

		gameController.step();

		verify(mockControllerFactory).createConnectionFailureController(
				mockNavigator, errorMessage);
		verify(mockNavigator).replaceTop(mockConnectionFailureController);
	}

	/**
	 * Tests that the GameTimerTask is scheduled when start() is called.
	 * 
	 * @throws Exception
	 *             If there are errors
	 */
	@Test
	public void testStartSchedulesGameTimerTask() throws Exception
	{
		gameController.start();
		verify(mockTimer).schedule(eq(mockGameTimerTask), anyInt(), anyInt());
	}
}
