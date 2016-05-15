package client.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import shared.core.ArrayUtilities;
import shared.model.GameObjectType;
import client.model.IGameObject;

/**
 * A view that draws the IGameObjects that make up the game.
 */
public class GameView extends JPanel implements IGameView {

	/**
	 * The width of game object tiles.
	 */
	public final int GAME_OBJECT_TILE_WIDTH = 32;

	/**
	 * The height of game object tiles.
	 */
	public final int GAME_OBJECT_TILE_HEIGHT = 32;

	/**
	 * ID for serialization necessary for all JPanels.
	 */
	private static final long serialVersionUID = -7799438882394531028L;

	/**
	 * List of all Sprites that have been created. At any given time we will
	 * have a single Sprite for each GameObjectType.
	 */
	private List<Sprite> sprites;

	/**
	 * Mapping from IGameObjects to their corresponding Sprites.
	 */
	private Map<IGameObject, Sprite> spriteMap;

	/**
	 * Mapping from IGameObjects to their last known coordinates.
	 */
	private Map<IGameObject, Rectangle> lastCoordinates;

	/**
	 * Factory for creating Sprites from IGameObjects.
	 */
	private ISpriteFactory factory;

	/**
	 * JPanel for the static background image.
	 */
	private JPanel backgroundPanel;

	/**
	 * JPanel for the moving game sprites.
	 */
	private JPanel foregroundPanel;

	/**
	 * Lock object for sprite map to allow for asynchronous painting.
	 */
	private Object spriteMapLock;

	/**
	 * Constructs a GameView with the given ISpriteFactory.
	 * 
	 * @param factory
	 *            The ISpriteFactory this view will use to obtain Sprites from
	 *            IGameObjects.
	 * @throws IOException
	 *             If Background image cannot be loaded.
	 * @throws IllegalArgumentException
	 *             If factory is null.
	 */
	public GameView(ISpriteFactory factory) throws IOException {
		super();

		if (factory == null) {
			throw new IllegalArgumentException(
					"A GameView cannot be constructed with a null ISpriteFactory.");
		}

		this.factory = factory;
		sprites = new ArrayList<Sprite>();
		spriteMap = new HashMap<IGameObject, Sprite>();
		lastCoordinates = new HashMap<IGameObject, Rectangle>();
		spriteMapLock = new Object();

		backgroundPanel = new JPanel() {
			
			/**
			 * ID for serialization necessary for all JPanels.
			 */
			private static final long serialVersionUID = 6292593509339774839L;

			// image for background panel
			BufferedImage img = ImageIO.read(new File(
					"resources/Background.png"));

			/**
			 * Paints the given clip of the background panel with the static background image.
			 */
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				// repaint the background in the given clip
				Rectangle rect = g.getClipBounds();
				g2d.drawImage(img, rect.x, rect.y, rect.x + rect.width, rect.y
						+ rect.height, rect.x, rect.y, rect.x + rect.width,
						rect.y + rect.height, null);
			}
		};

		foregroundPanel = new JPanel() {
			/**
			 * ID for serialization necessary for all JPanels.
			 */
			private static final long serialVersionUID = -5520859733654480744L;

			/**
			 * Paints the game objects on this panel overlapping with the given clip.
			 */
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				Rectangle clip = g2d.getClipBounds();

				// for each gameObject
				IGameObject[] gameObjects;
				synchronized (spriteMapLock) {
					gameObjects = spriteMap.keySet()
							.toArray(new IGameObject[0]);
					for (IGameObject gameObject : gameObjects) {
						// get its bounding box
						Rectangle componentBounds = boundingBox(gameObject);

						// if bounding box intersects area to draw
						if (componentBounds.intersects(clip)) {
							// redraw sprite
							BufferedImage image;
							image = spriteMap.get(gameObject).getImage();
							g2d.drawImage(image, componentBounds.x,
									componentBounds.y, componentBounds.width,
									componentBounds.height, null);
						}
					}
				}
			}

		};

		// set the size and properties of the panels
		foregroundPanel.setOpaque(false);
		foregroundPanel.setSize(736, 544);
		backgroundPanel.setSize(736, 544);

		// set the size of this GameView
		setPreferredSize(new Dimension(736, 544));
		setSize(736, 544);
		add(backgroundPanel, 0);
		add(foregroundPanel, 1);
	}

	@Override
	public void paint(Graphics g) {
		backgroundPanel.paint(g);
		foregroundPanel.paint(g);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawGameObject(IGameObject gameObject) {
		if (gameObject == null) {
			throw new IllegalArgumentException(
					"A null IGameObject cannot be drawn.");
		}

		Sprite matchingSprite;
		synchronized (spriteMapLock) {
			matchingSprite = spriteMap.get(gameObject);
		}

		// if game object has already been drawn
		if (matchingSprite != null) {
			// repaint given section of grid
			repaint(boundingBox(gameObject));

			// repaint the section of the grid where the sprite previously was
			Rectangle oldLoc = lastCoordinates.get(gameObject);
			if (oldLoc != null) {
				repaint(oldLoc);
			}

		} else {
			// prioritize gameObject with matching id for different
			// colored players
			for (Sprite sprite : sprites) {
				if (sprite.getGameObject().identifier() == gameObject
						.identifier()) {
					matchingSprite = sprite;
				}
			}

			if (matchingSprite == null) {
				// the game object has not already been drawn
				// Look for Sprite with matching GameObjectType
				for (Sprite sprite : sprites) {
					if (sprite.getGameObject().gameObjectType() == gameObject
							.gameObjectType()) {
						matchingSprite = sprite;

						break;
					}
				}
			}

			// if Sprite doesn't exist for this gameObject's type, create new
			// Sprite
			if (matchingSprite == null) {
				matchingSprite = factory.createSprite(gameObject);
				sprites.add(matchingSprite);
			}

			// Map to corresponding Sprite
			synchronized (spriteMapLock) {
				spriteMap.put(gameObject, matchingSprite);
			}

			// paint the section of the grid where the gameObject is to appear
			repaint(boundingBox(gameObject));
		}

		// update lastCoordinates
		lastCoordinates.put(gameObject, boundingBox(gameObject));

	}

	/**
	 * {@inheritDoc}
	 */
	public void drawGameObjects(IGameObject[] gameObjects) {
		if (gameObjects == null) {
			throw new IllegalArgumentException(
					"drawGameObjects cannot be called with a null gameObjects argument.");
		}

		// Don't need cloned gameObjects, just a new array so sorting doesn't
		// change ordering.
		Arrays.sort(gameObjects, new Comparator<IGameObject>() {

			public int compare(IGameObject lhs, IGameObject rhs) {
				return lhs.identifier() - rhs.identifier();
			}
		});

		// load sprites for players based on color
		int playerCount = 0;
		for (IGameObject gameObject : gameObjects) {
			synchronized (spriteMapLock) {
				if (gameObject.gameObjectType() == GameObjectType.PLAYER) {
					playerCount++;

					// if player does not already have a sprite
					if (!spriteMap.containsKey(gameObject)) {
						Sprite sprite = factory.createPlayerSprite(gameObject,
								playerCount);
						sprites.add(sprite);
						spriteMap.put(gameObject, sprite);
					}
				}
			}
		}

		// draw all game objects
		for (IGameObject gameObject : gameObjects) {
			drawGameObject(gameObject);
		}

		// update the last coordinates and remove game objects that have been
		// drawn but where not specified in this call
		Set<IGameObject> drawnGameObjects = lastCoordinates.keySet();
		Iterator<IGameObject> iter = drawnGameObjects.iterator();
		while (iter.hasNext()) {
			IGameObject gameObject = iter.next();
			// remove game objects not specified in this call
			if (!ArrayUtilities.contains(gameObjects, gameObject)) {
				synchronized (spriteMapLock) {
					spriteMap.remove(gameObject);
				}
				iter.remove();
				// schedule a repaint of previous coordinates
				repaint(boundingBox(gameObject));
			}
		}
	}

	/**
	 * Private helper method which calculates a bounding box for a game object.
	 * 
	 * @param gameObject
	 *            The object for which the bounding box is calculated.
	 * @return The bounding box for the provided game object.
	 */
	public Rectangle boundingBox(IGameObject gameObject) {
		if (gameObject == null) {
			throw new IllegalArgumentException(
					"The IGameObject parameter cannot be null.");
		}

		// use width and height of game tile to calculate bounding box of game object
		int x = GAME_OBJECT_TILE_WIDTH * gameObject.column();
		int y = GAME_OBJECT_TILE_HEIGHT * gameObject.row();
		return new Rectangle(x, y, GAME_OBJECT_TILE_WIDTH,
				GAME_OBJECT_TILE_HEIGHT);
	}

}
