package server.model;

import java.util.HashSet;
import java.util.Set;

import shared.model.GameObjectType;


/**
 * Implements the IBoard interface.
 */
public class Board implements IBoard
{
	//Number of row for the board
	private int numberOfRows; 
	
	//Number of columns for the board
	private int numberOfColumns;
	
	//the two-array set to save all the GameObjects in the board
	protected Set<IGameObject>[][] cells;
	
	
	/**
	 * Create a new Board.
	 * @param numberOfRows the number of rows for the new Board.
	 * @param numberOfColumns the number of columns for the new Board.
	 * @throws IllegalArgumentException Throw when the input number of rows or the number of columns are less than zero.
	 */
	@SuppressWarnings("unchecked")
	public Board(int numberOfRows, int numberOfColumns) 
	{
		if (numberOfRows <= 0 || numberOfColumns <= 0)
		{
			throw new IllegalArgumentException();
		}

		// initialize the size of board
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		
		// initialize the cells of board
		cells = new HashSet[numberOfRows][numberOfColumns];
		for (int i =0; i< numberOfRows ; i++)
		{
			for (int j = 0; j < numberOfColumns; j++)
			{
				cells[i][j] = new HashSet<IGameObject>();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int numberOfRows() {
		
		return numberOfRows;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int numberOfColumns() {
		
		return numberOfColumns;
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameObject[] gameObjectsAtSpace(int row, int column) 
	{
		if ((row <0 || row >= numberOfRows) || (column <0 || column >= numberOfColumns))
		{
			throw new IllegalArgumentException();
		}
		
		// return the GameObjects at the certain position with array style
		return cells[row][column].toArray(new IGameObject[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canMoveToSpace(int row, int column, IGameObject gameObject) 
	{
		if (gameObject == null)
		{
			throw new NullPointerException();
		}
		if ((row < 0 || row >= numberOfRows) || (column < 0 || column >= numberOfColumns))
		{
			throw new IllegalArgumentException();
		}
		
		// check if the given position is available
		if (gameObject.solid())
		{
			// check if the GameObject is bomb or not
			if (gameObject.type() == GameObjectType.BOMB)
			{
				for (IGameObject position : gameObjectsAtSpace(row, column))
				{
					if (position.type() == GameObjectType.BOMB)
					{
						return false;
					}
				}
				return true;
			}
			
			// check if two solid GameObjects on the same position
			for (IGameObject position : gameObjectsAtSpace(row, column))
			{
				if (position.solid())
				{
					return false;
				}
			}
			
		}
        
		return true;
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void moveGameObjectToSpace(int row, int column,
			IGameObject gameObject) 
	{
		if (gameObject == null)
		{
			throw new NullPointerException();
		}
		
		if ((row <0 || row > numberOfRows) || (column <0 || column > numberOfColumns))
		{
			throw new IllegalArgumentException();
		}
		
		if (canMoveToSpace(row, column, gameObject))
		{
			// check if the GameObject already on the board
			if (gameObject.onBoard())
			{
				removeGameObject(gameObject);
			}
			
			// move the GameObject to the position
			gameObject.setPosition(row, column);
			cells[row][column].add(gameObject);
		}
		else
		{
			throw new IllegalArgumentException();
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeGameObject(IGameObject gameObject) 
	{
		if (gameObject == null)
		{
			throw new NullPointerException();
		}
		
		if (gameObject.onBoard())
		{
			// remove the GameObject from board
			cells[gameObject.row()][gameObject.column()].remove(gameObject);
			gameObject.removeFromBoard();
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean spaceEmpty(int row, int column)
	{
		return gameObjectsAtSpace(row, column).length == 0;
	}
}
