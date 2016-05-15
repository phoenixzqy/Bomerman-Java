package server.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.model.GameObjectType;

/**
 * A test class for the Board class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Board.class)
public class BoardTest
{
	
	private int numberOfRows;
	private int numberOfColumns;
	private Board board;
	
	/**
	 * Set up the test
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		numberOfRows = 17;
		numberOfColumns = 23;
		board = new Board(numberOfRows, numberOfColumns);
	}
	
	/**
	 * Test if the constructor will throw an IllegalArgumentException when the number of rows is less
	 * than 0.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBoardConstructorRowIllegalArgument()
	{
		new Board(-1, 0);
	}
	
	/**
	 * Ensures if the board is provided with a negative column, it throws an IllegalArgumentException.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBoardConstructorColumnIllegalArgument()
	{
		new Board(3, -4);
	}
	
	/**
	 * Test if the constructor create a new Board correctly
	 */
	@Test
	public void testBoardConstructor()
	{
		Board testBoard1 = new Board(3, 5);
		assertEquals(3, testBoard1.numberOfRows());
		assertEquals(5, testBoard1.numberOfColumns());
		
		Board testBoard2 = new Board(10, 20);
		assertEquals(10, testBoard2.numberOfRows());
		assertEquals(20, testBoard2.numberOfColumns());
	}
	
	/**
	 * Test if the method can return the correct number of rows.
	 */
	@Test
	public void testnumberOfRows()
	{
		assertEquals(17, board.numberOfRows());
		
	}
	
	/**
	 * Test if the method can return the correct number of columns.
	 */
	@Test
	public void testnumberOfColumns()
	{
		assertEquals(23, board.numberOfColumns());
	}
	
	/**
	 * Test if the method can return the illegalArgumentException when the given row number is
	 * illegal.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testgameObjectsAtSpaceIllegalArgumentException1()
	{
		Board testboard = new Board(50, 50);
		testboard.gameObjectsAtSpace(-1, 5);
	}
	
	/**
	 * Test if the method can return the illegalArgumentException when the given column number is
	 * illegal.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testgameObjectsAtSpaceIllegalArgumentException2()
	{
		Board testboard = new Board(50, 50);
		testboard.gameObjectsAtSpace(8, 55);
	}
	
	/**
	 * Test if the method can return the correct answer.
	 */
	
	@Test
	public void testgameObjectsAtSpace()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		Board testboard = new Board(50, 50);
		
		testboard.cells[2][5].add(temp1);
		testboard.cells[2][5].add(temp2);
		
		IGameObject[] expected = { temp1, temp2 };
		
		assertEquals(2, testboard.gameObjectsAtSpace(2, 5).length);
		assertEquals(new HashSet<IGameObject>(Arrays.asList(expected)),
				new HashSet<IGameObject>(Arrays.asList(testboard.gameObjectsAtSpace(2, 5))));
		
	}
	
	/**
	 * Test if the method return the NullPointerException when the given GameObject is a null.
	 */
	@Test(expected = NullPointerException.class)
	public void testcanMoveToSpaceNullPointerException()
	{
		IGameObject givenObject = null;
		Board testboard = new Board(50, 50);
		testboard.canMoveToSpace(3, 5, givenObject);
	}
	
	/**
	 * Test if the method can return the IllegalArgumentExcpetion when the given row number is illegal
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testcanMoveToSpaceIllegalArgumentRow()
	{
		IGameObject temp = mock(IGameObject.class);
		Board testboard = new Board(50, 50);
		testboard.canMoveToSpace(-1, 4, temp);
	}
	
	/**
	 * Test if the method can return the IllegalArgumentExcpetion when the given column number is
	 * illegal
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testcanMoveToSpaceIllegalArgumentColumn()
	{
		IGameObject temp = mock(IGameObject.class);
		Board testboard = new Board(50, 50);
		testboard.canMoveToSpace(6, 100, temp);
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is solid and the position have one
	 * non-solid object.
	 */
	@Test
	public void testcanMoveToSpaceStoU()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObject = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(true);
		when(positionObject.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][5].add(positionObject);
		assertEquals(true, testboard.canMoveToSpace(3, 5, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is non-solid and the position have
	 * one solid object.
	 */
	@Test
	public void testcanMoveToSpaceUtoS()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObject = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(false);
		when(positionObject.solid()).thenReturn(true);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObject);
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is solid and the position have one
	 * solid object.
	 */
	@Test
	public void testcanMoveToSpaceStoS()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObject = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(true);
		when(positionObject.solid()).thenReturn(true);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObject);
		assertEquals(false, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is non-solid and the position have
	 * one non-solid object.
	 */
	@Test
	public void testcanMoveToSpaceUtoU()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObject = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(false);
		when(positionObject.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObject);
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is non-solid and the position is
	 * empty.
	 */
	@Test
	public void testcanMoveToSpaceUtoE()
	{
		IGameObject moveObject = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].clear();
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is solid and the position has a
	 * solid GameObject and a non-solid GameObject.
	 */
	@Test
	public void testcanMoveToSpaceStoUS()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObjectS = mock(IGameObject.class);
		IGameObject positionObjectU = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(true);
		when(positionObjectS.solid()).thenReturn(true);
		when(positionObjectU.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObjectS);
		testboard.cells[3][3].add(positionObjectU);
		assertEquals(false, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is solid and the position has two
	 * non-solid GameObjects.
	 */
	@Test
	public void testcanMoveToSpaceStoUU()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObjectU = mock(IGameObject.class);
		IGameObject positionObjectU2 = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(true);
		when(positionObjectU2.solid()).thenReturn(false);
		when(positionObjectU.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObjectU2);
		testboard.cells[3][3].add(positionObjectU);
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is non-solid and the position has
	 * a solid GameObject and a non-solid GameObject.
	 */
	@Test
	public void testcanMoveToSpaceUtoUS()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObjectS = mock(IGameObject.class);
		IGameObject positionObjectU = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(false);
		when(positionObjectS.solid()).thenReturn(true);
		when(positionObjectU.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObjectS);
		testboard.cells[3][3].add(positionObjectU);
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is non-solid and the position has
	 * two non-solid GameObjects.
	 */
	@Test
	public void testcanMoveToSpaceUtoUUU()
	{
		IGameObject moveObject = mock(IGameObject.class);
		IGameObject positionObjectS = mock(IGameObject.class);
		IGameObject positionObjectU = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(false);
		when(positionObjectS.solid()).thenReturn(false);
		when(positionObjectU.solid()).thenReturn(false);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(positionObjectS);
		testboard.cells[3][3].add(positionObjectU);
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the method return the correct boolean value to see if the given GameObject can move to
	 * the position with given row and column. The given GameObject is solid and the position empty.
	 */
	@Test
	public void testcanMoveToSpaceStoE()
	{
		IGameObject moveObject = mock(IGameObject.class);
		when(moveObject.solid()).thenReturn(true);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].clear();
		assertEquals(true, testboard.canMoveToSpace(3, 3, moveObject));
	}
	
	/**
	 * Test if the player can put a Bomb at the same place
	 */
	@Test
	public void testcanMoveToSpacePutBomb()
	{
		IGameObject moveObjectBomb = mock(IGameObject.class);
		IGameObject positionObjectS = mock(IGameObject.class);
		when(moveObjectBomb.type()).thenReturn(GameObjectType.BOMB);
		when(moveObjectBomb.solid()).thenReturn(true);
		when(positionObjectS.solid()).thenReturn(true);
		Board testboard = new Board(50,50);
		testboard.cells[5][8].add(positionObjectS);
		
		assertEquals(true, testboard.canMoveToSpace(5, 8, moveObjectBomb));
	}
	
	/**
	 * Test when the player try to put a Bomb at the place when there are already a bomb there
	 */
	@Test
	public void testcanMoveToSpacePutBombFail()
	{
		IGameObject moveObjectBomb = mock(IGameObject.class);
		IGameObject positionObjectS = mock(IGameObject.class);
		IGameObject positionObjectBomb = mock(IGameObject.class);
		
		when(moveObjectBomb.type()).thenReturn(GameObjectType.BOMB);
		when(moveObjectBomb.solid()).thenReturn(true);
		when(positionObjectS.solid()).thenReturn(true);
		when(positionObjectBomb.solid()).thenReturn(true);
		when(positionObjectBomb.type()).thenReturn(GameObjectType.BOMB);
		Board testboard = new Board(50,50);
		testboard.cells[5][8].add(positionObjectS);
		testboard.cells[5][8].add(positionObjectBomb);
		assertEquals(false, testboard.canMoveToSpace(5, 8, moveObjectBomb));
	}
	
	/**
	 * Test if the method return the NullPointerException when the given GameObject is a null.
	 */
	@Test(expected = NullPointerException.class)
	public void testmoveGameObjectToSpaceNullPointerException()
	{
		IGameObject temp = null;
		Board testboard = new Board(50, 50);
		testboard.moveGameObjectToSpace(3, 5, temp);
	}
	
	/**
	 * Test if the method can return the IllegalArgumentExcpetion when the given row number is illegal
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testmoveGameObjectToSpaceIllegalArgumentRow()
	{
		IGameObject temp = mock(IGameObject.class);
		Board testboard = new Board(50, 50);
		testboard.moveGameObjectToSpace(-1, 4, temp);
	}
	
	/**
	 * Test if the method can return the IllegalArgumentExcpetion when the given column number is
	 * illegal
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testmoveGameObjectToSpaceIllegalArgumentColumn()
	{
		IGameObject temp = mock(IGameObject.class);
		Board testboard = new Board(50, 50);
		testboard.moveGameObjectToSpace(6, 100, temp);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position is empty. The given object is not on the board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceStoE()
	{
		IGameObject temp1 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(true);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		Board testboard = new Board(50, 50);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1, times(1)).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is non-solid and the position is empty. The given object is not on the board before it
	 * move.
	 */
	@Test
	public void testmoveGameObjectToSpaceUtoE()
	{
		IGameObject temp1 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		Board testboard = new Board(50, 50);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		verify(temp1, times(1)).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position has a non-solid GameObject. The given object is not on the
	 * board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceStoU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(true);
		when(temp2.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(temp2);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is NON-solid and the position has a non-solid GameObject. The given object is not on the
	 * board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceUtoU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp2.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(temp2);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		verify(temp1).setPosition(3, 3);
		
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is NON-solid and the position has a solid GameObject. The given object is not on the
	 * board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceUtoS()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp2.solid()).thenReturn(true);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(temp2);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position has two non-solid objects. The given object is not on the
	 * board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceStoUU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		IGameObject temp3 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(true);
		when(temp2.solid()).thenReturn(false);
		when(temp3.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		temp.add(temp3);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(temp2);
		testboard.cells[3][3].add(temp3);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		verify(temp1).setPosition(3, 3);
		
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is non-solid and the position has a non-solid and a solid object. The given object is
	 * not on the board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceUtoSU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		IGameObject temp3 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp2.solid()).thenReturn(true);
		when(temp3.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		temp.add(temp3);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(temp2);
		testboard.cells[3][3].add(temp3);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is non-solid and the position has two non-solid objects. The given object is not on the
	 * board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceUtoUU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		IGameObject temp3 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp2.solid()).thenReturn(false);
		when(temp3.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		temp.add(temp3);
		Board testboard = new Board(50, 50);
		testboard.cells[3][3].add(temp2);
		testboard.cells[3][3].add(temp3);
		
		when(temp1.onBoard()).thenReturn(false);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertEquals(temp, testboard.cells[3][3]);
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position is empty. The given object is already on the board before it
	 * move
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardStoE()
	{
		IGameObject temp1 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(true);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		when(temp1.onBoard()).thenReturn(true);
		
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position is empty. The given object is already on the board before it
	 * move
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardUtoE()
	{
		IGameObject temp1 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		when(temp1.onBoard()).thenReturn(true);
		
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
		
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position has a non-solid object. The given object is already on the
	 * board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardStoU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(true);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		when(temp2.solid()).thenReturn(false);
		
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		testboard.cells[3][3].add(temp2);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is non-solid and the position has a solid object. The given object is already on the
	 * board before it move
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardUtoS()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		when(temp2.solid()).thenReturn(true);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		testboard.cells[3][3].add(temp2);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is non-solid and the position has a non-solid object. The given object is already on the
	 * board before it move
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardUtoU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		when(temp2.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		testboard.cells[3][3].add(temp2);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is solid and the position has two non-solid objects. The given object is already on the
	 * board before it move
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardStoUU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		IGameObject temp3 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(true);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		when(temp2.solid()).thenReturn(false);
		when(temp3.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		temp.add(temp3);
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		testboard.cells[3][3].add(temp2);
		testboard.cells[3][3].add(temp3);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position. The given
	 * object is non-solid and the position has a non-solid object and a solid object. The given
	 * object is already on the board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceOnBoardUtoSU()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		IGameObject temp3 = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp1.row()).thenReturn(3);
		when(temp1.column()).thenReturn(2);
		when(temp2.solid()).thenReturn(true);
		when(temp3.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		temp.add(temp2);
		temp.add(temp3);
		Board testboard = new Board(50, 50);
		testboard.cells[3][2].add(temp1);
		testboard.cells[3][3].add(temp2);
		testboard.cells[3][3].add(temp3);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[3][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method can correctly move a given GameObject to the given position when the given
	 * position up , down , left and right are all occupied by a solid GameObject. The given
	 * GameObject is solid and is not on the board before it move.
	 */
	@Test
	public void testmoveGameObjectToSpaceSpecialCase()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject tempUP = mock(IGameObject.class);
		IGameObject tempDOWN = mock(IGameObject.class);
		IGameObject tempLEFT = mock(IGameObject.class);
		IGameObject tempRIGHT = mock(IGameObject.class);
		when(temp1.solid()).thenReturn(false);
		when(temp1.row()).thenReturn(2);
		when(temp1.column()).thenReturn(2);
		when(tempUP.solid()).thenReturn(true);
		when(tempDOWN.solid()).thenReturn(false);
		when(tempLEFT.solid()).thenReturn(false);
		when(tempRIGHT.solid()).thenReturn(false);
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp1);
		Board testboard = new Board(50, 50);
		testboard.cells[2][2].add(temp1);
		testboard.cells[3][2].add(tempUP);
		testboard.cells[3][4].add(tempDOWN);
		testboard.cells[4][3].add(tempLEFT);
		testboard.cells[2][3].add(tempRIGHT);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.moveGameObjectToSpace(3, 3, temp1);
		
		assertTrue(testboard.cells[2][2].isEmpty());
		assertEquals(temp, testboard.cells[3][3]);
		
		verify(temp1).setPosition(3, 3);
	}
	
	/**
	 * Test if the method return the NullPointerException when the given GameObject is null.
	 */
	@Test(expected = NullPointerException.class)
	public void testremoveGameObjectNullPointerException()
	{
		IGameObject temp = null;
		board.removeGameObject(temp);
		
	}
	
	/**
	 * Test if the method return the illegalArgumentException when the given GameObject is not on the
	 * board.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testremoveGameObjectIllegalArgumentException()
	{
		IGameObject temp = mock(IGameObject.class);
		when(temp.onBoard()).thenReturn(false);
		board.removeGameObject(temp);
		
	}
	
	/**
	 * Test if the method can correctly remove the given GameObject. Then given GameObject is solid.
	 */
	@Test
	public void testremoveGameObjectSolid()
	{
		IGameObject temp = mock(IGameObject.class);
		when(temp.row()).thenReturn(10);
		when(temp.column()).thenReturn(10);
		when(temp.solid()).thenReturn(true);
		Board testboard = new Board(50, 50);
		testboard.cells[10][10].add(temp);
		
		when(temp.onBoard()).thenReturn(true);
		testboard.removeGameObject(temp);
		assertTrue(testboard.cells[10][10].isEmpty());
		
		verify(temp).removeFromBoard();
	}
	
	/**
	 * Test if the method can correctly remove the given GameObject. Then given GameObject is
	 * non-solid.
	 */
	@Test
	public void testremoveGameObjectNonSolid()
	{
		IGameObject temp = mock(IGameObject.class);
		when(temp.row()).thenReturn(10);
		when(temp.column()).thenReturn(10);
		when(temp.solid()).thenReturn(false);
		
		Board testboard = new Board(50, 50);
		testboard.cells[10][10].add(temp);
		
		when(temp.onBoard()).thenReturn(true);
		testboard.removeGameObject(temp);
		assertTrue(testboard.cells[10][10].isEmpty());
		
		verify(temp).removeFromBoard();
	}
	
	/**
	 * Test if the method can correctly remove the given GameObject. Then given GameObject is solid
	 * and that position have a nonsolid object.
	 */
	@Test
	public void testremoveGameObjectSolidNonSolid()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.row()).thenReturn(10);
		when(temp1.column()).thenReturn(10);
		when(temp1.solid()).thenReturn(true);
		when(temp2.row()).thenReturn(10);
		when(temp2.column()).thenReturn(10);
		when(temp2.solid()).thenReturn(false);
		
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[10][10].add(temp1);
		testboard.cells[10][10].add(temp2);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.removeGameObject(temp1);
		assertEquals(temp, testboard.cells[10][10]);
		
		verify(temp1).removeFromBoard();
	}
	
	/**
	 * Test if the method can correctly remove the given GameObject. Then given GameObject is solid
	 * and that position already have a nonsolid object.
	 */
	@Test
	public void testremoveGameObjectNonSolidNonSolid()
	{
		IGameObject temp1 = mock(IGameObject.class);
		IGameObject temp2 = mock(IGameObject.class);
		when(temp1.row()).thenReturn(10);
		when(temp1.column()).thenReturn(10);
		when(temp1.solid()).thenReturn(false);
		when(temp2.row()).thenReturn(10);
		when(temp2.column()).thenReturn(10);
		when(temp2.solid()).thenReturn(false);
		
		Set<IGameObject> temp = new HashSet<IGameObject>();
		temp.add(temp2);
		Board testboard = new Board(50, 50);
		testboard.cells[10][10].add(temp1);
		testboard.cells[10][10].add(temp2);
		
		when(temp1.onBoard()).thenReturn(true);
		testboard.removeGameObject(temp1);
		assertEquals(temp, testboard.cells[10][10]);
		
		verify(temp1).removeFromBoard();
	}
	
	/**
	 * Ensures the spaceEmpty method works correctly. 
	 */
	@Test
	public void testSpaceEmpty()
	{
		Answer<Object> setPositionAnswer = new Answer<Object>() {
			
			public Object answer(InvocationOnMock invocation) throws Throwable
			{
				Object[] args = invocation.getArguments();
				IGameObject gameObject = (IGameObject) invocation.getMock();
				
				int row = ((Integer) args[0]).intValue();
				int column = ((Integer) args[1]).intValue();
				
				when(gameObject.row()).thenReturn(row);        
				when(gameObject.column()).thenReturn(column); 
				
				return null;
			}
		};
		
		IGameObject mockGameObject1 = mock(IGameObject.class);
		IGameObject mockGameObject2 = mock(IGameObject.class);
		
		when(mockGameObject1.row()).thenReturn(0);
		when(mockGameObject1.column()).thenReturn(0);
		when(mockGameObject1.onBoard()).thenReturn(true);
		when(mockGameObject1.identifier()).thenReturn(1);
		doAnswer(setPositionAnswer).when(mockGameObject1).setPosition(anyInt(), anyInt());
		
		when(mockGameObject2.row()).thenReturn(0);
		when(mockGameObject2.column()).thenReturn(0);
		when(mockGameObject2.onBoard()).thenReturn(true);
		when(mockGameObject2.identifier()).thenReturn(2);
		doAnswer(setPositionAnswer).when(mockGameObject2).setPosition(anyInt(), anyInt());
		
		for (int row = 0; row < board.numberOfRows(); row++)
		{
			for (int column = 0; column < board.numberOfColumns(); column++)
			{
				assertTrue(board.spaceEmpty(row, column));
				
				board.moveGameObjectToSpace(row, column, mockGameObject1);
				assertFalse(board.spaceEmpty(row, column));
				
				board.moveGameObjectToSpace(row, column, mockGameObject2);
				assertFalse(board.spaceEmpty(row, column));

				board.removeGameObject(mockGameObject2);
				assertFalse(board.spaceEmpty(row, column));

				board.removeGameObject(mockGameObject1);
				assertTrue(board.spaceEmpty(row, column));
			}
		}
	}
	
	/**
	 * Ensures the spaceEmpty method throws an IllegalArgumentException when the row is less than 0.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSpaceEmptyRowLessThanZero()
	{
		board.spaceEmpty(-1, 0);
	}
	
	/**
	 * Ensures the spaceEmpty method throws an IllegalArgumentException when the row is greater than
	 * or equal to the number of rows.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSpaceEmptyRowGreaterThanOrEqualToNumberOfRows()
	{
		board.spaceEmpty(board.numberOfRows(), 0);
	}
	
	/**
	 * Ensures the spaceEmpty method throws an IllegalArgumentException when the column is less than 
	 * 0.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSpaceEmptyColumnLessThanZero()
	{
		board.spaceEmpty(0, -1);
	}
	
	/**
	 * Ensures the spaceEmpty method throws an IllegalArgumentException when the column is greater 
	 * than or equal to the number of columns.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testSpaceEmptyColumnGreaterThanOrEqualToNumberOfColumns()
	{
		board.spaceEmpty(0, board.numberOfColumns());
	}
}
