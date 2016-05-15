package client.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import shared.model.GameObjectType;

/**
 * 
 */
public class GameObjectTest
{
	// a test game object
	private GameObject testGameObject;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		testGameObject = new GameObject(0, GameObjectType.PLAYER, 0, 0);
	}
	
	/**
	 * Ensures the constructor sets all of the properties correctly.
	 */
	@Test
	public void testConstructor()
	{
		GameObject gameObject = new GameObject(1, GameObjectType.PLAYER, 2, 3);
		assertEquals(GameObjectType.PLAYER, gameObject.gameObjectType());
		assertEquals(1, gameObject.identifier());
		assertEquals(2, gameObject.row());
		assertEquals(3, gameObject.column());
	}
	
	/**
	 * Ensures the constructor throws an exception if the game object type is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNullGameObjectType()
	{
		new GameObject(0, null, 0, 0);
	}
	
	/**
	 * Ensures the setRow method sets the row.
	 */
	@Test
	public void testSetRow()
	{
		testGameObject.setRow(1);
		assertEquals(1, testGameObject.row());
		assertEquals(0, testGameObject.column());
	}
	
	/**
	 * Ensures the setColumn method sets the column.
	 */
	@Test
	public void testSetColumn()
	{
		testGameObject.setColumn(1);
		assertEquals(0, testGameObject.row());
		assertEquals(1, testGameObject.column());
	}
}
