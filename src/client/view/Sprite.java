package client.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import client.model.IGameObject;


/**
 * This class represents a graphical sprite. It's x and y coordinates correspond
 * to it's associated IGameObject.
 */
public class Sprite implements ISprite {
	/**
	 * The IGameObject to be graphically represented by this Sprite.
	 */
	private IGameObject gamePiece;

	/**
	 * The Image to graphically represent this Sprite.
	 */
	private BufferedImage image;

	/**
	 * Constructs a Sprite with the given IGameObject and an Image with the
	 * given source.
	 * 
	 * @param gamePiece
	 *            The IGameObject to provide the x and y coordinates for this
	 *            Sprite. This must not be null.
	 * @param imageSource
	 *            The path to the image to graphically represent this Sprite.
	 *            This must not be null.
	 * @throws IOException
	 *             Thrown if an error occurs when reading from the image source
	 *             file.
	 * @throws IllegalArgumentException
	 *             Thrown if the IGameObject argument or imageSource argument is
	 *             null.
	 */
	public Sprite(IGameObject gamePiece, String imageSource) throws IOException {
		
		if (gamePiece == null) {
			throw new IllegalArgumentException(
					"The IGameObject argument must not be null.");
		}
		
		if (imageSource == null) {
			throw new IllegalArgumentException(
					"The Image Source argument must not be null.");
		}
		
		// read in image from source file
		image = ImageIO.read(new File(imageSource));
		this.gamePiece = gamePiece;
	}

	/**
	 * {@inheritDoc}
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * {@inheritDoc}
	 */
	public IGameObject getGameObject() {
		return gamePiece;
	}
}
