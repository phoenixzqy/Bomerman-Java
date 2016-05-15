package server.model;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.*;

import server.model.behaviors.*;
import shared.model.*;

/**
 * Tests GameObject.
 */
public class GameObjectTest
{
	// a game object to test
	private IGameObject testGameObject;
	
	// a mock mobility behavior
	private IMobilityBehavior mockMobilityBehavior;
	
	// a mock solidity behavior
	private ISolidityBehavior mockSolidityBehavior;
	
	// a mock ownership behavior
	private IOwnershipBehavior mockOwnershipBehavior;
	
	// a mock score behavior
	private IScoreBehavior mockScoreBehavior;
	
	// a mock destruction behavior
	private IDestructionBehavior mockDestructionBehavior;
	
	// a mock bomb behavior
	private IBombBehavior mockBombBehavior;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		// mock the behaviors
		mockMobilityBehavior = mock(IMobilityBehavior.class);
		mockSolidityBehavior = mock(ISolidityBehavior.class);
		mockOwnershipBehavior = mock(IOwnershipBehavior.class);
		mockScoreBehavior = mock(IScoreBehavior.class);
		mockDestructionBehavior = mock(IDestructionBehavior.class);
		mockBombBehavior = mock(IBombBehavior.class);
		
		// set up the test game object
		testGameObject = new GameObject(GameObjectType.BOMB, mockMobilityBehavior, mockSolidityBehavior,
				mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null type is 
	 * provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorGameObjectTypeNull()
	{
		new GameObject(null, mockMobilityBehavior, mockSolidityBehavior,
			mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null IMobilityBehavior is 
	 * provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorMobilityBehaviorNull()
	{
		new GameObject(GameObjectType.BOMB, null, mockSolidityBehavior,
				mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null ISolidityBehavior is 
	 * provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorSolidityBehaviorNull()
	{
		new GameObject(GameObjectType.BOMB, mockMobilityBehavior, null,
				mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null IOwnershipBehavior is 
	 * provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorOwnershipBehaviorNull()
	{
		new GameObject(GameObjectType.BOMB, mockMobilityBehavior, mockSolidityBehavior,
				null, mockScoreBehavior, mockOwnershipBehavior, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null IScoreBehavior is 
	 * provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorScoreBehaviorNull()
	{
		new GameObject(GameObjectType.BOMB, mockMobilityBehavior, mockSolidityBehavior,
				mockDestructionBehavior, null, mockOwnershipBehavior, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null IDestructionBehavior is 
	 * provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorDestructionBehaviorNull()
	{
		new GameObject(GameObjectType.BOMB, mockMobilityBehavior, mockSolidityBehavior,
				mockDestructionBehavior, mockScoreBehavior, null, mockBombBehavior);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when a null IBombBehavior is provided.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorBombBehaviorNull()
	{
		new GameObject(GameObjectType.BOMB, mockMobilityBehavior, mockSolidityBehavior,
				mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior, null);
	}
	
	/**
	 * Ensures the game object type is set to the provided game object type.
	 */
	@Test
	public void testGameObjectType()
	{
		assertEquals(GameObjectType.BOMB, testGameObject.type());
		testGameObject = new GameObject(GameObjectType.PLAYER, mockMobilityBehavior, 
				mockSolidityBehavior, mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior,
				mockBombBehavior);
		assertEquals(GameObjectType.PLAYER, testGameObject.type());
	}
	
	/**
	 * Ensures the game object has a unique identifier.
	 */
	@Test
	public void testGameObjectIdentifier()
	{
		final IGameObject gameObject1 = new GameObject(GameObjectType.BOMB, mockMobilityBehavior, 
				mockSolidityBehavior, mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior,
				mockBombBehavior);
		final IGameObject gameObject2 = new GameObject(GameObjectType.BOMB, mockMobilityBehavior, 
				mockSolidityBehavior, mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior,
				mockBombBehavior);
		final IGameObject gameObject3 = new GameObject(GameObjectType.BOMB, mockMobilityBehavior, 
				mockSolidityBehavior, mockDestructionBehavior, mockScoreBehavior, mockOwnershipBehavior, 
				mockBombBehavior);

		assertFalse(gameObject1.identifier() == gameObject2.identifier());
		assertFalse(gameObject3.identifier() == gameObject2.identifier());
		assertFalse(gameObject1.identifier() == gameObject3.identifier());
	}
	
	/**
	 * Ensures the row method defers to the provided behavior.
	 */
	@Test
	public void testRow()
	{
		when(mockMobilityBehavior.row()).thenReturn(5);
		int row = testGameObject.row();
	  verify(mockMobilityBehavior).row();
	  assertEquals(5, row);
	}

	/**
	 * Ensures the column method defers to the provided behavior.
	 */
	@Test
	public void testColumn()
	{
		when(mockMobilityBehavior.column()).thenReturn(5);
	  int column = testGameObject.column();
	  verify(mockMobilityBehavior).column();
	  assertEquals(5, column);
	}

	/**
	 * Ensures the setPosition method defers to the provided behavior.
	 */
	@Test
	public void testSetPosition()
	{
	  testGameObject.setPosition(1, 2);
	  verify(mockMobilityBehavior).setPosition(1, 2);
	}

	/**
	 * Ensures the onBoard method defers to the provided behavior.
	 */
	@Test
	public void testOnBoard()
	{
		when(mockMobilityBehavior.onBoard()).thenReturn(true);
	  boolean onBoard = testGameObject.onBoard();
	  verify(mockMobilityBehavior).onBoard();
	  assertEquals(true, onBoard);
	}

	/**
	 * Ensures the removeFromBoard method defers to the provided behavior.
	 */
	@Test
	public void testRemoveFromBoard()
	{
	  testGameObject.removeFromBoard();
	  verify(mockMobilityBehavior).removeFromBoard();
	}

	/**
	 * Ensures the solid method defers to the provided behavior.
	 */
	@Test
	public void testSolid()
	{
		when(mockSolidityBehavior.solid()).thenReturn(true);
	  boolean solid = testGameObject.solid();
	  verify(mockSolidityBehavior).solid();
	  assertEquals(true, solid);
	}

	/**
	 * Ensures the hasOwner method defers to the provided behavior.
	 */
	@Test
	public void testHasOwner()
	{
		when(mockOwnershipBehavior.hasOwner()).thenReturn(true);
	  boolean hasOwner = testGameObject.hasOwner();
	  verify(mockOwnershipBehavior).hasOwner();
	  assertEquals(true, hasOwner);
	}

	/**
	 * Ensures the owner method defers to the provided behavior.
	 */
	@Test
	public void testOwner()
	{
		IGameObject mockOwner = mock(IGameObject.class);
		when(mockOwnershipBehavior.owner()).thenReturn(mockOwner);
	  IGameObject owner = testGameObject.owner();
	  verify(mockOwnershipBehavior).owner();
	  assertEquals(mockOwner, owner);
	}

	/**
	 * Ensures the hasScore method defers to the provided behavior.
	 */
	@Test
	public void testHasScore()
	{
		when(mockScoreBehavior.hasScore()).thenReturn(true);
	  boolean hasScore = testGameObject.hasScore();
	  verify(mockScoreBehavior).hasScore();
	  assertEquals(true, hasScore);
	}

	/**
	 * Ensures the score method defers to the provided behavior.
	 */
	@Test
	public void testScore()
	{
		when(mockScoreBehavior.score()).thenReturn(5);
	  int score = testGameObject.score();
	  verify(mockScoreBehavior).score();
	  assertEquals(5, score);
	}

	/**
	 * Ensures the incrementScore method defers to the provided behavior.
	 */
	@Test
	public void testIncrementScore()
	{
	  testGameObject.incrementScore();
	  verify(mockScoreBehavior).incrementScore();
	}

	/**
	 * Ensures the decrementScore method defers to the provided behavior.
	 */
	@Test
	public void testdecrementScore()
	{
	  testGameObject.decrementScore();
	  verify(mockScoreBehavior).decrementScore();
	}

	/**
	 * Ensures the destructible method defers to the provided behavior.
	 */
	@Test
	public void testDestructible()
	{
		when(mockDestructionBehavior.destructible()).thenReturn(true);
	  boolean destructable = testGameObject.destructible();
	  verify(mockDestructionBehavior).destructible();
	  assertEquals(true, destructable);
	}

	/**
	 * Ensures the numberOfStepsUntilDestruction method defers to the provided behavior.
	 */
	@Test
	public void testNumberOfStepsUntilDestruction()
	{
		when(mockDestructionBehavior.numberOfStepsUntilDestruction()).thenReturn(5);
	  assertEquals(5, testGameObject.numberOfStepsUntilDestruction());
	  verify(mockDestructionBehavior).numberOfStepsUntilDestruction();
	}

	/**
	 * Ensures the ructionAction destructionAction method defers to the provided behavior.
	 */
	@Test
	public void testDestructionAction()
	{
		when(mockDestructionBehavior.destructionAction()).thenReturn(DestructionAction.RESPAWN);
	  assertEquals(DestructionAction.RESPAWN, testGameObject.destructionAction());
	  verify(mockDestructionBehavior).destructionAction();
	}

	/**
	 * Ensures the canMove method defers to the provided behavior.
	 */
	@Test
	public void testCanMove()
	{
		when(mockMobilityBehavior.canMove()).thenReturn(true);
	  boolean canMove = testGameObject.canMove();
	  verify(mockMobilityBehavior).canMove();
	  assertEquals(canMove, true);
	}

	/**
	 * Ensures the startMovingInDirection method defers to the provided behavior.
	 */
	@Test
	public void testStartMovingInDirection()
	{
		testGameObject.startMovingInDirection(Direction.RIGHT);
	  verify(mockMobilityBehavior).startMovingInDirection(Direction.RIGHT);
	}

	/**
	 * Ensures the stopMovingInDirection method defers to the provided behavior.
	 */
	@Test
	public void testStopMovingInDirection()
	{
	  testGameObject.stopMovingInDirection(Direction.UP);
	  verify(mockMobilityBehavior).stopMovingInDirection(Direction.UP);
	}

	/**
	 * Ensures the directionToMove method defers to the provided behavior.
	 */
	@Test
	public void testDirectionToMove()
	{
		when(mockMobilityBehavior.directionToMove()).thenReturn(Direction.DOWN);
	  Direction directionToMove = testGameObject.directionToMove();
	  verify(mockMobilityBehavior).directionToMove();
	  assertEquals(directionToMove, Direction.DOWN);
	}

	/**
	 * Ensures the decrementNumberOfStepsUntilDestruction method defers to the provided behavior.
	 */
	@Test
	public void testDecrementNumberOfStepsUntilDestruction()
	{
		testGameObject.decrementNumberOfStepsUntilDestruction();
		verify(mockDestructionBehavior).decrementNumberOfStepsUntilDestruction();
	}
	
	/**
	 * Ensures the placeBomb method defers to the provided behavior.
	 */
	@Test
	public void testPlaceBomb()
	{
		when(mockBombBehavior.placeBomb()).thenReturn(true);
	  assertEquals(true, testGameObject.placeBomb());
	  verify(mockBombBehavior).placeBomb();
	}
	
	/**
	 * Ensures the canPlaceBomb method defers to the provided behavior.
	 */
	@Test
	public void testCanPlaceBomb()
	{
		when(mockBombBehavior.canPlaceBomb()).thenReturn(true);
	  assertEquals(true, testGameObject.canPlaceBomb());
	  verify(mockBombBehavior).canPlaceBomb();
	}
	
	/**
	 * Ensures the setPlaceBomb method defers to the provided behavior.
	 */
	@Test
	public void testSetPlaceBomb()
	{
	  testGameObject.setPlaceBomb(true);
	  verify(mockBombBehavior).setPlaceBomb(true);
	}
	
	/**
	 * Ensures the destructionTimed method defers to the provided behavior.
	 */
	@Test
	public void testDestructionTimed()
	{
		when(mockDestructionBehavior.destructionTimed()).thenReturn(true);
	  assertEquals(true, testGameObject.destructionTimed());
	  verify(mockDestructionBehavior).destructionTimed();
	}
	
	/**
	 * Test the CountBomb method works correctly
	 */
	@Test
	public void testCountBomb()
	{
		when(mockBombBehavior.countBomb()).thenReturn(3);
		assertEquals(3,testGameObject.bombCount());
		verify(mockBombBehavior).countBomb();
	}
	
	/**
	 * Test the DecrementBomb method works correctly
	 */
	@Test
	public void testDecrementBomb()
	{
		testGameObject.decrementBombCount();
		verify(mockBombBehavior).decrementBomb();

	}
	
	
	/**
	 * Test the incrementBomb method works correctly
	 */
	@Test
	public void testIncrementBomb()
	{
		testGameObject.incrementBomb();
		verify(mockBombBehavior).incrementBomb();
	}
	
	/**
	 * Ensures numberOfStepUntilRespawn will return the correct number.
	 */
	@Test
	public void testNumberOfStepUntilRespawn()
	{
		when(mockDestructionBehavior.numberOfStepUntilRespawn()).thenReturn(10);
		assertEquals(10, testGameObject.numberOfStepUntilRespawn());
	}
	
	/**
	 * Ensures decrementNumberOfStepUntilRespawn works
	 */
	@Test
	public void testDecrementNumberOfStepUntilRespawn()
	{
		testGameObject.decrementNumberOfStepUntilRespawn();
		verify(mockDestructionBehavior).decrementNumberOfStepUntilRespawn();
	}
	
	/**
	 * Ensures ResetNumberOfStepUntilRespawn works
	 */
	@Test
	public void testResetNumberOfStepUntilRespawn()
	{
		testGameObject.resetNumberOfStepUntilRespawn();
		verify(mockDestructionBehavior).resetNumberOfStepUntilRespawn();
	}
	
	/**
	 * Ensures incrementNumberOfStepUntilRespawn works
	 */
	@Test
	public void testIncrementNumberOfStepUntilRespawn()
	{
		testGameObject.incrementNumberOfStepUntilRespawn();
		verify(mockDestructionBehavior).incrementNumberOfStepUntilRespawn();
	}
}
