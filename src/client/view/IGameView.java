package client.view;

import client.model.IGameObject;

/**
 * Contains logic for drawing the game view.
 */
public interface IGameView
{
	/**
	 * Draws the specified gameObject.
	 * 
	 * @param gameObject
	 *            The game object to draw.
	 * @throws IllegalArgumentException
	 *             If the given IGameObject is null.
	 */
	public void drawGameObject(IGameObject gameObject);

	/**
	 * Draws the specified gameObjects. If a gameObject that has been previously
	 * drawn is not in this array, it will be erased.
	 * 
	 * @param gameObjects
	 *            The game objects to be drawn.
	 * @throws IllegalArgumentException
	 *             If the given IGameObject array is null.
	 */
	public void drawGameObjects(IGameObject[] gameObjects);

}
