package client.view;

import client.model.IGameObject;

/**
 * Provides Sprites for given IGameObjects.
 * 
 */
public interface ISpriteFactory
{
	/**
	 * Creates a Sprite for the given IGameObject.
	 * 
	 * @param gameObject
	 *            The IGameObject for which to obtain a Sprite.
	 * @return The Sprite for the given IGameObject.
	 */
	public Sprite createSprite(IGameObject gameObject);

	/**
	 * Creates a Sprite for the given player.
	 * 
	 * @param gameObject
	 *            The player for which to create a Sprite.
	 * @param playerNum
	 *            The number of the player for which to create a Sprite.
	 * @return The Sprite for the given IGameObject.
	 * @throws IllegalArgumentException
	 *             Thrown if gameObject is not a player or playerNum is greater
	 *             than 4 or less than 1.
	 */
	public Sprite createPlayerSprite(IGameObject gameObject, int playerNum);

}
