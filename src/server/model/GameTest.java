package server.model;

import static org.mockito.Matchers.any; 
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.model.*;
import shared.model.communication.*;

/**
 * A test class to test all the Game class functions. Currently it is only a
 * draft test that can only test the key message functions.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Game.class)
public class GameTest {
	
	// a counter used to create unique game object identifiers
	private static int gameObjectIdentifierCounter = 0;
	
	// a mock server communicator
	private IServerCommunicator mockServerCommunicator;
	
	// a mock game object factory
	private IGameObjectFactory mockGameObjectFactory;
	
	// the test game
	private Game game;
	
	// the mock IBoard
	private Board mockBoard;
	
	/**
	 * Set up all the conditions that needed by the tests.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Before
	public void setUp() throws Exception {
		
		// mock the server communicator
		mockServerCommunicator = mock(IServerCommunicator.class);
		when(mockServerCommunicator.listening()).thenReturn(false);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[0]);
		
		
		// mock the game object factory
		mockGameObjectFactory = mock(IGameObjectFactory.class);
		IGameObject mockGameObjectFiller = createMockGameObject();
		when(mockGameObjectFactory.createPlayer()).thenReturn(mockGameObjectFiller);
		when(mockGameObjectFactory.createBreakableBlock()).thenReturn(mockGameObjectFiller);
		when(mockGameObjectFactory.createUnbreakableBlock()).thenReturn(mockGameObjectFiller);
		when(mockGameObjectFactory.createBomb(any(IGameObject.class))).thenReturn(mockGameObjectFiller);
		when(mockGameObjectFactory.createExplosion(any(IGameObject.class))).
		thenReturn(mockGameObjectFiller);
		
		// mock IBoard then pass it to the game
		mockBoard = mock(Board.class);
		when(mockBoard.canMoveToSpace(anyInt(), anyInt(), any(IGameObject.class))).thenReturn(true);
		when(mockBoard.gameObjectsAtSpace(anyInt(), anyInt())).thenReturn(new IGameObject[0]);
		PowerMockito.whenNew(Board.class).withArguments(anyInt(), anyInt()).thenReturn(mockBoard);
		
		// initialize a game to test
		game = new Game(mockServerCommunicator, mockGameObjectFactory, null);
	}
	
	/**
	 * Test the Game constructor will throw a NullPointerException when a null
	 * communicator is given to it.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test(expected = NullPointerException.class)
	public void testGameConstructorNull() throws CommunicationException {
		new Game(null, mockGameObjectFactory);
	}
	
	/**
	 * Ensures the Game constructor will throw a NullPointerException when the
	 * game object factory is null.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test(expected = NullPointerException.class)
	public void testGameConstructorGameObjectFactoryNull()
			throws CommunicationException {
		new Game(mockServerCommunicator, null);
	}
	
	/**
	 * Ensures the Game constructor throws a NullPointerException when the
	 * provided communicator is not listening.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGameConstructorShouldStartListening()
			throws CommunicationException {
		when(mockServerCommunicator.listening()).thenReturn(true);
		new Game(mockServerCommunicator, mockGameObjectFactory);
	}
	
	/**
	 * Ensures the constructor creates an unbreakable block in the correct
	 * places on the board.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testConstructorCreatesUnbreakableBlocks() throws Exception {
		
		PowerMockito.whenNew(Board.class).withArguments(anyInt(), anyInt())
		.thenReturn(mockBoard);
		
		// mock the unbreakable block
		IGameObject mockUnbreakableBlock = createMockGameObject();
		when(mockUnbreakableBlock.type()).thenReturn(GameObjectType.UNBREAKABLE_BLOCK);
		
		// mock a filler object
		IGameObject mockGameObjectFiller = createMockGameObject();
		when(mockGameObjectFiller.type()).thenReturn(GameObjectType.BREAKABLE_BLOCK);
		
		// make the game object factory
		when(mockGameObjectFactory.createUnbreakableBlock()).thenReturn(mockUnbreakableBlock);
		when(mockGameObjectFactory.createBreakableBlock()).thenReturn(mockGameObjectFiller);
		when(mockGameObjectFactory.createPlayer()).thenReturn(mockGameObjectFiller);
		
		// recreate the game
		game = new Game(mockServerCommunicator, mockGameObjectFactory);
		
		class IsUnbreakableBlock extends ArgumentMatcher<IGameObject> {
			public boolean matches(Object gameObject) {
				return ((IGameObject) gameObject).type() == GameObjectType.UNBREAKABLE_BLOCK;
			}
		}
		
		// verify the top and bottom rows
		for (int column = 0; column < Game.DEFAULT_NUMBER_OF_COLUMNS; column++) {
			verify(mockBoard).moveGameObjectToSpace(eq(0), eq(column),
					argThat(new IsUnbreakableBlock()));
			verify(mockBoard).moveGameObjectToSpace(
					eq(Game.DEFAULT_NUMBER_OF_ROWS - 1), eq(column),
					argThat(new IsUnbreakableBlock()));
		}
		
		// verify the left and right columns
		for (int row = 0; row < Game.DEFAULT_NUMBER_OF_ROWS; row++) {
			verify(mockBoard).moveGameObjectToSpace(eq(row), eq(0),
					argThat(new IsUnbreakableBlock()));
			verify(mockBoard).moveGameObjectToSpace(eq(row),
					eq(Game.DEFAULT_NUMBER_OF_COLUMNS - 1),
					argThat(new IsUnbreakableBlock()));
		}
		
		// verify the center blocks
		for (int row = 2; row < Game.DEFAULT_NUMBER_OF_ROWS - 2; row += 2) {
			for (int column = 2; column < Game.DEFAULT_NUMBER_OF_COLUMNS - 2; column += 2) {
				verify(mockBoard).moveGameObjectToSpace(eq(row), eq(column),
						argThat(new IsUnbreakableBlock()));
			}
		}
		
		// make sure none of the odd rows are not occupied by unbreakable blocks
		for (int row = 1; row < Game.DEFAULT_NUMBER_OF_ROWS; row += 2) {
			for (int column = 1; column < Game.DEFAULT_NUMBER_OF_COLUMNS - 1; column++) {
				verify(mockBoard, never()).moveGameObjectToSpace(eq(row),
						eq(column), argThat(new IsUnbreakableBlock()));
			}
		}
		
		// make sure none of the odd columns are not occupied by unbreakable
		// blocks
		for (int column = 1; column < Game.DEFAULT_NUMBER_OF_COLUMNS; column += 2) {
			for (int row = 1; row < Game.DEFAULT_NUMBER_OF_ROWS - 1; row++) {
				verify(mockBoard, never()).moveGameObjectToSpace(eq(row),
						eq(column), argThat(new IsUnbreakableBlock()));
			}
		}
	}
	
	/**
	 * Ensures the constructor creates an breakable blocks in the random places
	 * on the board.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testConstructorCreatesBreakableBlocks() throws Exception {
		
		class IsBreakableBlock extends ArgumentMatcher<IGameObject> {
			public boolean matches(Object gameObject) {
				return ((IGameObject) gameObject).type() == GameObjectType.BREAKABLE_BLOCK;
			}
		}
		
		IGameObject mockBreakableBlock = createMockGameObject();
		when(mockBreakableBlock.type()).thenReturn(GameObjectType.BREAKABLE_BLOCK);
		when(mockGameObjectFactory.createBreakableBlock()).thenReturn(mockBreakableBlock);
		
		game = new Game(mockServerCommunicator, mockGameObjectFactory);
		
		// make sure the board has 130 breakable blocks creates
		verify(mockBoard, times(130)).moveGameObjectToSpace(anyInt(), anyInt(), 
				argThat(new IsBreakableBlock()));
	}
	
	/**
	 * Ensures the Game calls the sendUniqueMessageToEachConnectedCommunicator.
	 * It should send a generator which creates a
	 * PlayerGameObjectIdentifierMessage.
	 * 
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testConstructorGameObjectsSetsUpPlayers() throws CommunicationException {
		
		game = new Game(mockServerCommunicator, mockGameObjectFactory);
		
		verify(mockServerCommunicator).sendUniqueMessageToEachConnectedCommunicator(
				any(IMessageGenerator.class));
		verify(mockBoard, atLeastOnce()).moveGameObjectToSpace(anyInt(), anyInt(), any(IGameObject.class));
	}
	
	/**
	 * Ensures the game constructor sends a start game message to the connected
	 * clients.
	 * 
	 * @throws CommunicationException
	 *             This should not happen.
	 */
	@Test
	public void testConstructorSendsStartGameMessage()
			throws CommunicationException {
		
		game = new Game(mockServerCommunicator, mockGameObjectFactory);
		
		final ArgumentCaptor<GameMessage> argumentCaptor = ArgumentCaptor
				.forClass(GameMessage.class);
		
		verify(mockServerCommunicator).sendMessages(
				argumentCaptor.capture());
	}
	
	/**
	 * Ensures when a press left key message is received the provided game
	 * object starts moving left.
	 * 
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testOneMessagePressLeft() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.LEFT, 
				KeyAction.PRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		game.step();
		
		verify(mockPlayer1).startMovingInDirection(Direction.LEFT);
		verify(mockPlayer1, never()).stopMovingInDirection(any(Direction.class));
		verify(mockPlayer2, never()).startMovingInDirection(any(Direction.class));
		verify(mockPlayer2, never()).stopMovingInDirection(any(Direction.class));
}
	
	/**
	 * Ensures when a press right key message is received the provided game
	 * object starts moving right.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessagePressRight() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.RIGHT, 
				KeyAction.PRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		
		game.step();
		
		verify(mockPlayer1).startMovingInDirection(Direction.RIGHT);
		verify(mockPlayer1, never())
		.stopMovingInDirection(any(Direction.class));
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensures when a press up key message is received the provided game object
	 * starts moving up.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessagePressUp() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.UP, 
				KeyAction.PRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		
		game.step();
		
		verify(mockPlayer1).startMovingInDirection(Direction.UP);
		verify(mockPlayer1, never())
		.stopMovingInDirection(any(Direction.class));
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensures when a press down key message is received the provided game
	 * object starts moving down.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessagePressDown() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.DOWN, 
				KeyAction.PRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		
		game.step();
		
		verify(mockPlayer1).startMovingInDirection(Direction.DOWN);
		verify(mockPlayer1, never())
		.stopMovingInDirection(any(Direction.class));
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensures when a depress left key message is received the provided game
	 * object stops moving left.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessageDepressLeft() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.LEFT, 
				KeyAction.DEPRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		game.step();
		
		verify(mockPlayer1, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer1).stopMovingInDirection(Direction.LEFT);
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensures when a depress right key message is received the provided game
	 * object stops moving right.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessageDepressRight() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.RIGHT, 
				KeyAction.DEPRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		game.step();
		
		verify(mockPlayer1, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer1).stopMovingInDirection(Direction.RIGHT);
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensures when a depress up key message is received the provided game
	 * object stops moving up.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessageDepressUp() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.UP, 
				KeyAction.DEPRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		game.step();
		
		verify(mockPlayer1, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer1).stopMovingInDirection(Direction.UP);
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensures when a depress down key message is received the provided game
	 * object stops moving down.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testOneMessageDepressDown() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.DOWN, 
				KeyAction.DEPRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		game.step();
		
		verify(mockPlayer1, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer1).stopMovingInDirection(Direction.DOWN);
		verify(mockPlayer2, never()).startMovingInDirection(
				any(Direction.class));
		verify(mockPlayer2, never())
		.stopMovingInDirection(any(Direction.class));
		
	}
	
	/**
	 * Ensure when the game receive a PRESS SPACE message, it does nothing
	 * @throws CommunicationException This shouldn't happen.
	 */
	@Test
	public void testPressSpacePlacesBomb() throws CommunicationException {
		
		IGameObject mockPlayer1 = createMockGameObject();
		IGameObject mockPlayer2 = createMockGameObject();
		when(mockPlayer1.onBoard()).thenReturn(true);
		when(mockPlayer2.onBoard()).thenReturn(true);
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		IMessage mockKeyMessage = createMockKeyMessage(mockPlayer1.identifier(), Key.SPACE, 
				KeyAction.PRESS);
		when(mockServerCommunicator.receivedMessages()).thenReturn(new IMessage[] { mockKeyMessage });
		
		game.step();
		
		verify(mockPlayer1).setPlaceBomb(true);
		verify(mockPlayer2,never()).setPlaceBomb(anyBoolean());
	}
	
	/**
	 * Ensures when the game object returns a direction to move of NONE it is
	 * not moved.
	 * @throws CommunicationException 
	 */
	@Test
	public void testGameObjectDirectionToMoveNone() throws CommunicationException 
	{
		
		IGameObject mockPlayer1 = createMockGameObject();
		addMobilityToMockObject(mockPlayer1, Direction.NONE);
		IGameObject mockPlayer2 = createMockGameObject();
		addMobilityToMockObject(mockPlayer2, Direction.NONE);
		
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };	
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		game.step();
		
		verify(mockBoard, never()).canMoveToSpace(mockPlayer1.row(),
				mockPlayer1.column(), mockPlayer1);
		verify(mockBoard, never()).moveGameObjectToSpace(mockPlayer1.row(),
				mockPlayer1.column(), mockPlayer1);
		
		verify(mockBoard, never()).canMoveToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		verify(mockBoard, never()).moveGameObjectToSpace(anyInt(), anyInt(), eq(mockPlayer2));
	}
	
	/**
	 * Ensures when the game object returns a direction to move of LEFT it is
	 * moved left.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testGameObjectDirectionToMoveLeft() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		addMobilityToMockObject(mockPlayer1, Direction.LEFT);
		IGameObject mockPlayer2 = createMockGameObject();
		addMobilityToMockObject(mockPlayer2, Direction.NONE);
		when(mockPlayer1.onBoard()).thenReturn(true);
		when(mockPlayer2.onBoard()).thenReturn(true);
		
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		game.step();
		
		verify(mockBoard).canMoveToSpace(mockPlayer1.row(),
				mockPlayer1.column() - 1, mockPlayer1);
		verify(mockBoard).moveGameObjectToSpace(mockPlayer1.row(),
				mockPlayer1.column() - 1, mockPlayer1);
		
		verify(mockBoard, never()).canMoveToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		verify(mockBoard, never()).moveGameObjectToSpace(anyInt(), anyInt(), eq(mockPlayer2));
	}
	
	
	/**
	 * Ensures when the game object returns a direction to move of RIGHT it is
	 * moved left.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testGameObjectDirectionToMoveRight() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		addMobilityToMockObject(mockPlayer1, Direction.RIGHT);
		IGameObject mockPlayer2 = createMockGameObject();
		addMobilityToMockObject(mockPlayer2, Direction.NONE);
		when(mockPlayer1.onBoard()).thenReturn(true);
		when(mockPlayer2.onBoard()).thenReturn(true);
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		game.step();
		
		verify(mockBoard).canMoveToSpace(mockPlayer1.row(),
				mockPlayer1.column() + 1, mockPlayer1);
		verify(mockBoard).moveGameObjectToSpace(mockPlayer1.row(),
				mockPlayer1.column() + 1, mockPlayer1);
		
		verify(mockBoard, never()).canMoveToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		verify(mockBoard, never()).moveGameObjectToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		
	}
	
	/**
	 * Ensures when the game object returns a direction to move of UP it is
	 * moved left.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testGameObjectDirectionToMoveUp() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		addMobilityToMockObject(mockPlayer1, Direction.UP);
		IGameObject mockPlayer2 = createMockGameObject();
		addMobilityToMockObject(mockPlayer2, Direction.NONE);
		when(mockPlayer1.onBoard()).thenReturn(true);
		when(mockPlayer2.onBoard()).thenReturn(true);
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		when(mockPlayer1.directionToMove()).thenReturn(Direction.UP);
		when(mockBoard.canMoveToSpace(9, 20, mockPlayer1)).thenReturn(true);
		
		game.step();
		
		verify(mockBoard).canMoveToSpace(mockPlayer1.row() - 1,
				mockPlayer1.column(), mockPlayer1);
		verify(mockBoard).moveGameObjectToSpace(mockPlayer1.row() - 1,
				mockPlayer1.column(), mockPlayer1);
		
		verify(mockBoard, never()).canMoveToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		verify(mockBoard, never()).moveGameObjectToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		
	}
	
	/**
	 * Ensures when the game object returns a direction to move of DOWN it is
	 * moved left.
	 * 
	 * @throws Exception
	 *             this is not going to happen
	 */
	@Test
	public void testGameObjectDirectionToMoveDown() throws Exception {
		
		IGameObject mockPlayer1 = createMockGameObject();
		addMobilityToMockObject(mockPlayer1, Direction.DOWN);
		IGameObject mockPlayer2 = createMockGameObject();
		addMobilityToMockObject(mockPlayer2, Direction.NONE);
		when(mockPlayer1.onBoard()).thenReturn(true);
		when(mockPlayer2.onBoard()).thenReturn(true);
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		game.step();
		
		verify(mockBoard).canMoveToSpace(mockPlayer1.row() + 1,
				mockPlayer1.column(), mockPlayer1);
		verify(mockBoard).moveGameObjectToSpace(mockPlayer1.row() + 1,
				mockPlayer1.column(), mockPlayer1);
		
		verify(mockBoard, never()).canMoveToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		verify(mockBoard, never()).moveGameObjectToSpace(anyInt(), anyInt(), eq(mockPlayer2));
		
	}
	
	/**
	 * Ensures when the player should place a bomb a bomb is placed at the player's position.
	 * @throws CommunicationException This shouldn't happen.
	 */
	@Test
	public void testPlaceBomb() throws CommunicationException {
		
		IGameObject mockPlayer1 = createMockGameObject();
		
		when(mockPlayer1.canPlaceBomb()).thenReturn(true);
		when(mockPlayer1.placeBomb()).thenReturn(true);
		when(mockPlayer1.bombCount()).thenReturn(1);
		
		IGameObject mockPlayer2 = createMockGameObject();
		when(mockPlayer2.canPlaceBomb()).thenReturn(true);
		when(mockPlayer1.onBoard()).thenReturn(true);
		when(mockPlayer2.onBoard()).thenReturn(true);
		IGameObject mockBomb = createMockGameObject();
		when(mockBomb.type()).thenReturn(GameObjectType.BOMB);
		when(mockBomb.row()).thenReturn(5);
		when(mockBomb.column()).thenReturn(13);
		when(mockGameObjectFactory.createBomb(any(IGameObject.class))).thenReturn(mockBomb);
		
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		
		game.step();
		
		// check the game object factory
		verify(mockGameObjectFactory).createBomb(mockPlayer1);
		verify(mockGameObjectFactory, never()).createBomb(mockPlayer2);
		
		// check the game board
		verify(mockBoard).canMoveToSpace(mockPlayer1.row(), mockPlayer1.column(), mockBomb);
		verify(mockBoard).moveGameObjectToSpace(mockPlayer1.row(), mockPlayer1.column(), mockBomb);
		
		// ensure the bomb count is decremented
		verify(mockPlayer1).decrementBombCount();
		verify(mockPlayer2, never()).decrementBombCount();
		
		// ensure a create game object message is sent for the game object
		verifyMessageSent(new GameObjectCreatedMessage(mockBomb.identifier(), mockBomb.type(), 
				mockBomb.row(), mockBomb.column()));
	}
	
	/**
	 * test the breakable block will disappear after an explosion destroys it 
	 * @throws CommunicationException This should not happen
	 * 
	 */
	@Test
	public void testBreakableBlockDisapearAction() throws CommunicationException{
		IGameObject mockBreakableBlock=createMockGameObject();
		when(mockBreakableBlock.type()).thenReturn(GameObjectType.BREAKABLE_BLOCK);
		when(mockBreakableBlock.destructionAction()).thenReturn(DestructionAction.DISAPPEAR);
		when(mockBreakableBlock.destructible()).thenReturn(true);
		when(mockBreakableBlock.row()).thenReturn(10);
		when(mockBreakableBlock.column()).thenReturn(10);
		
		IGameObject mockExplosion=createMockGameObject();
		when(mockExplosion.type()).thenReturn(GameObjectType.EXPLOSION);
		when(mockExplosion.destructionAction()).thenReturn(DestructionAction.DISAPPEAR);
		when(mockExplosion.row()).thenReturn(10);
		when(mockExplosion.column()).thenReturn(10);
		
		IGameObject mockOwner=createMockGameObject();
		when(mockExplosion.owner()).thenReturn(mockOwner);
		// set up the game
		IGameObject[] gameObjects = { mockBreakableBlock,mockExplosion };
		when(mockBoard.gameObjectsAtSpace(eq(10), eq(10))).thenReturn(gameObjects);
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		game.step();
		verify(mockBoard).removeGameObject(mockBreakableBlock);
		// ensure a create game object message is sent for the game object
		verifyMessageSent(new GameObjectDestroyedMessage(mockBreakableBlock.identifier()));
	}
	
	/**
	 * test the player checkRespawn call the decrementNumberOfStepUntilRespawn method once;
	 * @throws CommunicationException this should not happen
	 */
	@Test
	public void testThePlayerRespawnDecrement() throws CommunicationException{
		IGameObject mockPlayer1 = createMockGameObject();
		when(mockPlayer1.destructionAction()).thenReturn(DestructionAction.RESPAWN);
		when(mockPlayer1.onBoard()).thenReturn(false);
		// set up the game
		IGameObject[] gameObjects = { mockPlayer1 };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);
		game.step();
		verify(mockPlayer1).decrementNumberOfStepUntilRespawn();
		
	}
	
	/**
	 * Ensures score is incremented for the owner of an explosion when the explosion destroys a player
	 * and the player is different from the owner. 
	 * @throws CommunicationException 
	 */
	@Test
	public void testExplosionDestroysPlayerIncrementsScore() throws CommunicationException
	{
		IGameObject mockPlayer1 = createMockGameObject();
		when(mockPlayer1.type()).thenReturn(GameObjectType.PLAYER);
		when(mockPlayer1.destructible()).thenReturn(true);
		
		IGameObject mockPlayer2 = createMockGameObject();
		when(mockPlayer2.type()).thenReturn(GameObjectType.PLAYER);
		when(mockPlayer2.row()).thenReturn(2);
		when(mockPlayer2.row()).thenReturn(3);
		when(mockPlayer2.destructible()).thenReturn(true);
		
		IGameObject mockExplosion = createMockGameObject();
		when(mockExplosion.type()).thenReturn(GameObjectType.EXPLOSION);
		when(mockExplosion.row()).thenReturn(2);
		when(mockExplosion.row()).thenReturn(3);
		addOwnerToMockGameObject(mockExplosion, mockPlayer1);
		
		// mock the game board
		when(mockBoard.gameObjectsAtSpace(2, 3)).thenReturn(new IGameObject[] { mockPlayer2, 
				mockExplosion });

		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2, mockExplosion };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);

		game.step();

		verify(mockPlayer1).incrementScore();
		verify(mockPlayer1, never()).decrementScore();
		verify(mockPlayer2, never()).incrementScore();
		verify(mockPlayer2, never()).decrementScore();
	}
	
	/**
	 * Ensures score is decremented for the owner of an explosion when the explosion destroys the 
	 * owner.
	 * @throws CommunicationException 
	 */
	@Test
	public void testExplosionDestroysOwnerDecrementsScore() throws CommunicationException
	{
		IGameObject mockPlayer1 = createMockGameObject();
		when(mockPlayer1.type()).thenReturn(GameObjectType.PLAYER);
		when(mockPlayer1.row()).thenReturn(2);
		when(mockPlayer1.row()).thenReturn(3);
		when(mockPlayer1.destructible()).thenReturn(true);
		
		IGameObject mockPlayer2 = createMockGameObject();
		when(mockPlayer2.type()).thenReturn(GameObjectType.PLAYER);
		when(mockPlayer2.destructible()).thenReturn(true);
		
		IGameObject mockExplosion = createMockGameObject();
		when(mockExplosion.type()).thenReturn(GameObjectType.EXPLOSION);
		when(mockExplosion.row()).thenReturn(2);
		when(mockExplosion.row()).thenReturn(3);
		addOwnerToMockGameObject(mockExplosion, mockPlayer1);
		
		// mock the game board
		when(mockBoard.gameObjectsAtSpace(2, 3)).thenReturn(new IGameObject[] { mockPlayer1, 
				mockExplosion });

		// set up the game
		IGameObject[] gameObjects = { mockPlayer1, mockPlayer2, mockExplosion };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);

		game.step();

		verify(mockPlayer1, never()).incrementScore();
		verify(mockPlayer1).decrementScore();
		verify(mockPlayer2, never()).incrementScore();
		verify(mockPlayer2, never()).decrementScore();
	}
	
	/**
	 * Ensures the score is not incremented when the object being destroyed is not a player.
	 * @throws CommunicationException 
	 */
	@Test
	public void testExplosionDestroysNonPlayerDoesntChangeScore() throws CommunicationException
	{
		IGameObject mockPlayer = createMockGameObject();
		when(mockPlayer.type()).thenReturn(GameObjectType.PLAYER);
		
		IGameObject mockUnbreakableBlock = createMockGameObject();
		when(mockUnbreakableBlock.type()).thenReturn(GameObjectType.UNBREAKABLE_BLOCK);
		when(mockUnbreakableBlock.destructible()).thenReturn(true);
		when(mockUnbreakableBlock.row()).thenReturn(2);
		when(mockUnbreakableBlock.row()).thenReturn(3);
		
		IGameObject mockExplosion = createMockGameObject();
		when(mockExplosion.type()).thenReturn(GameObjectType.EXPLOSION);
		when(mockExplosion.row()).thenReturn(2);
		when(mockExplosion.row()).thenReturn(3);
		addOwnerToMockGameObject(mockExplosion, mockPlayer);
		
		// mock the game board
		when(mockBoard.gameObjectsAtSpace(2, 3)).thenReturn(new IGameObject[] { mockUnbreakableBlock, 
				mockExplosion });

		// set up the game
		IGameObject[] gameObjects = { mockPlayer, mockUnbreakableBlock, mockExplosion };
		game = new Game(mockServerCommunicator, mockGameObjectFactory, gameObjects);

		game.step();

		verify(mockPlayer, never()).incrementScore();
		verify(mockPlayer, never()).decrementScore();
		verify(mockUnbreakableBlock, never()).incrementScore();
		verify(mockUnbreakableBlock, never()).decrementScore();
	}
	
	/**
	 * Helper method which creates a mock IGameObject which should pass through game's methods without
	 * throwing an exception.
	 * @return
	 */
	private IGameObject createMockGameObject()
	{
		IGameObject mockGameObject = mock(IGameObject.class);
		
		// mock the identifier
		when(mockGameObject.identifier()).thenReturn(gameObjectIdentifierCounter);
		gameObjectIdentifierCounter++;
		
		// mock the game object type
		when(mockGameObject.type()).thenReturn(GameObjectType.UNBREAKABLE_BLOCK);
		
		// mock the position attributes
		when(mockGameObject.onBoard()).thenReturn(false);
		when(mockGameObject.row()).thenReturn(-1);
		when(mockGameObject.column()).thenReturn(-1);
		
		// mock the solidity properties
		when(mockGameObject.solid()).thenReturn(false);
		
		// mock the ownership behaviors
		when(mockGameObject.hasOwner()).thenReturn(false);
		
		// mock the score properties
		when(mockGameObject.hasScore()).thenReturn(false);
		
		// mock the destruction properties
		when(mockGameObject.destructible()).thenReturn(false);
		when(mockGameObject.destructionTimed()).thenReturn(false);
		when(mockGameObject.destructionAction()).thenReturn(DestructionAction.DISAPPEAR);
		
		// mock the mobility properties
		when(mockGameObject.canMove()).thenReturn(false);
		
		// mock the bomb properties
		when(mockGameObject.placeBomb()).thenReturn(false);
		when(mockGameObject.canPlaceBomb()).thenReturn(false);
		
		return mockGameObject;
	}
	
	/**
	 * Helper method which adds mobility to a mock object.
	 * @param mockGameObject The game object to add mobility to.
	 * @param directionToMove The direction the mock game object should move.
	 */
	private void addMobilityToMockObject(IGameObject mockGameObject, Direction directionToMove)
	{
		when(mockGameObject.canMove()).thenReturn(true);
		when(mockGameObject.row()).thenReturn(10);
		when(mockGameObject.column()).thenReturn(20);
		when(mockGameObject.directionToMove()).thenReturn(directionToMove);
	}
	
	/**
	 * Adds an owner to the provided mock game object.
	 * @param mockGameObject The object to add an owner to.
	 * @param owner The owner game object.
	 */
	private void addOwnerToMockGameObject(IGameObject mockGameObject, IGameObject owner)
	{
		when(mockGameObject.hasOwner()).thenReturn(true);
		when(mockGameObject.owner()).thenReturn(owner);
	}
	
	/**
	 * Helper method which creates a mock key message.
	 * @param gameObjectIdentifier The identifier for the key message.
	 * @param key The key for the key message.
	 * @param keyAction The action for the key message.
	 * @return A mock key message.
	 */
	private IMessage createMockKeyMessage(int gameObjectIdentifier, Key key, KeyAction keyAction)
	{
		KeyMessage mockKeyMessage = mock(KeyMessage.class);
		when(mockKeyMessage.key()).thenReturn(key);
		when(mockKeyMessage.action()).thenReturn(keyAction);
		when(mockKeyMessage.gameObjectIdentifier()).thenReturn(gameObjectIdentifier);
		return mockKeyMessage;
	}
	
	/**
	 * Verifies a message equal to the provided message was sent.
	 * @param message The message to check.
	 * @throws CommunicationException This shouldn't happen.
	 */
	private void verifyMessageSent(final IMessage message) throws CommunicationException
	{
		class IsExpectedMessage extends ArgumentMatcher<IMessage> {
			public boolean matches(Object matchingMessage) {
				return (message.toString().equals(matchingMessage.toString()));
			}
		}
		
		verify(mockServerCommunicator).sendMessages(argThat(new IsExpectedMessage()));
	}
}
