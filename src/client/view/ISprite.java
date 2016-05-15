package client.view;

import java.awt.image.BufferedImage;

import client.model.IGameObject;


/**
 * This class represents a graphical sprite.
 */
public interface ISprite {
	/**
	 * Returns the BufferedImage corresponding to this Sprite.
	 * 
	 * @return The BufferedImage corresponding to this Sprite.
	 */
	public BufferedImage getImage();

	/**
	 * Returns the IGameObject represented by this Sprite.
	 * 
	 * @return The IGameObject represented by this Sprite.
	 */
	public IGameObject getGameObject();

}
