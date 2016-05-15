package client.view;

import java.io.IOException;

import shared.model.GameObjectType;
import client.model.IGameObject;

/**
 * A factory for Sprites.
 * 
 */
public class SpriteFactory implements ISpriteFactory
{
	/**
	 * {@inheritDoc}
	 */
	public Sprite createSprite(IGameObject gameObject)
	{
		// if gameObject is null, do not return a Sprite
		if (gameObject == null)
		{
			return null;
		}

		try
		{
			// return the Sprite with the proper image
			if (gameObject.gameObjectType() == GameObjectType.BOMB)
			{
				return new Sprite(gameObject, "resources/Bomb.png");

			} else if (gameObject.gameObjectType() == GameObjectType.UNBREAKABLE_BLOCK)
			{
				return new Sprite(gameObject, "resources/Unbreakable_Block.png");

			} else if (gameObject.gameObjectType() == GameObjectType.BREAKABLE_BLOCK)
			{
				return new Sprite(gameObject, "resources/Breakable_Block.png");

			} else if (gameObject.gameObjectType() == GameObjectType.EXPLOSION)
			{
				return new Sprite(gameObject, "resources/Explosion.png");

			} else
			{
				return null;
			}
		} catch (IOException e)
		{
			// could not read image, return null
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Sprite createPlayerSprite(IGameObject gameObject, int playerNum)
	{
		// throw IllegalArgumentException if game object is null, not a player, or player number
		// is outside of the range [1, 4]
		if (gameObject == null
				|| gameObject.gameObjectType() != GameObjectType.PLAYER
				|| playerNum > 4 || playerNum < 1)
		{
			throw new IllegalArgumentException();
		}
		
		try
		{
			// return sprite for player with the proper player color
			switch (playerNum)
			{
			case 1:
				return new Sprite(gameObject, "resources/BluePlayer.png");
			case 2:
				return new Sprite(gameObject, "resources/RedPlayer.png");
			case 3:
				return new Sprite(gameObject, "resources/GreenPlayer.png");
			case 4:
				return new Sprite(gameObject, "resources/PurplePlayer.png");
			default:
				// this is unreachable, IllegalArgumentException would already
				// be thrown
				return null;
			}
		} catch (IOException e)
		{
			// image for player sprite could not be loaded
			return null;
		}
	}
}
