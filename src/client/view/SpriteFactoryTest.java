package client.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import shared.model.GameObjectType;
import client.model.IGameObject;

/**
 * Tests for SpriteFactory.
 * 
 */
public class SpriteFactoryTest
{
	/**
	 * IGameObject whose gameObjectType() method returns null.
	 */
	private IGameObject nullGameObjectType;

	/**
	 * IGameObject whose gameObjectType() method returns PLAYER.
	 */
	private IGameObject player;

	/**
	 * IGameObject whose gameObjectType() method returns BOMB.
	 */
	private IGameObject bomb;

	/**
	 * IGameObject whose gameObjectType() method returns UNBREAKABLE_BLOCK.
	 */
	private IGameObject unbreakableBlock;

	/**
	 * IGameObject whose gameObjectType() method returns BREAKABLE_BLOCK.
	 */
	private IGameObject breakableBlock;
	
	/**
	 * IGameObject whose gameObjectType() method returns EXPLOSION.
	 */
	private IGameObject explosion;

	/**
	 * An instance of SpriteFactory, created with the default constructor.
	 */
	private SpriteFactory factory;
	

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		factory = new SpriteFactory();
		nullGameObjectType = mock(IGameObject.class);
		when(nullGameObjectType.gameObjectType()).thenReturn(null);
		bomb = mock(IGameObject.class);
		when(bomb.gameObjectType()).thenReturn(GameObjectType.BOMB);
		player = mock(IGameObject.class);
		when(player.gameObjectType()).thenReturn(GameObjectType.PLAYER);
		unbreakableBlock = mock(IGameObject.class);
		when(unbreakableBlock.gameObjectType()).thenReturn(
				GameObjectType.UNBREAKABLE_BLOCK);
		breakableBlock = mock(IGameObject.class);
		when(breakableBlock.gameObjectType()).thenReturn(
				GameObjectType.BREAKABLE_BLOCK);
		explosion = mock(IGameObject.class);
		when(explosion.gameObjectType()).thenReturn(GameObjectType.EXPLOSION);
	}

	/**
	 * Tests createSprite() will a null argument.
	 */
	@Test
	public void testCreateSpriteNullGameObject()
	{
		assertNull(factory.createSprite(null));
	}

	/**
	 * Tests createSprite() with an argument whose gameObjectType() method
	 * returns null.
	 */
	@Test
	public void testCreateSpriteGameObjectHasNullGameObjectType()
	{
		assertNull(factory.createSprite(nullGameObjectType));
	}

	/**
	 * Tests createSprite() with an argument whose gameObjectType() method
	 * returns PLAYER.
	 * 
	 * @throws IOException
	 *             When image cannot be loaded.
	 */
	@Test
	public void testCreateSpritePlayerGameObject() throws IOException
	{
		Sprite playerSprite = factory.createSprite(player);
		assertNull(playerSprite);
	}

	/**
	 * Tests createSprite() with an argument whose gameObjectType() method
	 * returns BOMB.
	 * 
	 * @throws IOException
	 *             When image cannot be loaded.
	 */
	@Test
	public void testCreateSpriteBombGameObject() throws IOException
	{
		Sprite bombSprite = factory.createSprite(bomb);
		assertNotNull(bombSprite);
		assertEquals(bomb, bombSprite.getGameObject());
		testImagesEqual(bombSprite, "resources/Bomb.png");
	}

	/**
	 * Tests createSprite() with an argument whose gameObjectType() method
	 * returns UNBREAKABLE_BLOCK.
	 * 
	 * @throws IOException
	 *             When image cannot be loaded.
	 */
	@Test
	public void testCreateSpriteUnbreakableBlockGameObject() throws IOException
	{
		Sprite unbreakableBlockSprite = factory.createSprite(unbreakableBlock);
		assertNotNull(unbreakableBlockSprite);
		assertEquals(unbreakableBlock, unbreakableBlockSprite.getGameObject());
		testImagesEqual(unbreakableBlockSprite,
				"resources/Unbreakable_Block.png");
	}

	/**
	 * Tests createSprite() with an argument whose gameObjectType() method
	 * returns BREAKABLE_BLOCK.
	 * 
	 * @throws IOException
	 *             When image cannot be loaded.
	 */
	@Test
	public void testCreateSpriteBreakableBlockGameObject() throws IOException
	{
		Sprite breakableBlockSprite = factory.createSprite(breakableBlock);
		assertNotNull(breakableBlockSprite);
		assertEquals(breakableBlock, breakableBlockSprite.getGameObject());
		testImagesEqual(breakableBlockSprite, "resources/Breakable_Block.png");
	}
	
	/**
	 * Tests createSprite() with an argument whose gameObjectType() method
	 * returns EXPLOSION.
	 * 
	 * @throws IOException
	 *             When image cannot be loaded.
	 */
	@Test
	public void testCreateSpriteExplosionGameObject() throws IOException
	{
		Sprite explosionSprite = factory.createSprite(explosion);
		assertNotNull(explosionSprite);
		assertEquals(explosion, explosionSprite.getGameObject());
		testImagesEqual(explosionSprite, "resources/Explosion.png");
	}

	/**
	 * Tests createPlayerSprite() with a null gameObject.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayerSpriteNullGameObject()
	{
		factory.createPlayerSprite(null, 2);
	}

	/**
	 * Tests createPlayerSprite() with invalid playerNumbers.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayerSpriteInvalidPlayerNumber()
	{
		factory.createPlayerSprite(player, 5);
		factory.createPlayerSprite(player, 0);
	}

	/**
	 * Tests createPlayerSprite() with breakableBlock.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayerSpriteBreakableBlock()
	{
		factory.createPlayerSprite(breakableBlock, 2);
	}

	/**
	 * Tests createPlayerSprite() with unbreakableBlock.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayerSpriteUnbreakableBlock()
	{
		factory.createPlayerSprite(unbreakableBlock, 2);
	}

	/**
	 * Tests createPlayerSprite() with Bomb.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayerSpriteBomb()
	{
		factory.createPlayerSprite(bomb, 2);
	}

	/**
	 * Tests createPlayerSprite() given valid arguments.
	 * 
	 * @throws IOException
	 *             Problems reading image files.
	 */
	@Test
	public void testCreatePlayerSprite() throws IOException
	{
		Sprite playerSprite = factory.createPlayerSprite(player, 1);
		assertNotNull(playerSprite);
		assertEquals(player, playerSprite.getGameObject());
		testImagesEqual(playerSprite, "resources/BluePlayer.png");

		playerSprite = factory.createPlayerSprite(player, 2);
		assertNotNull(playerSprite);
		assertEquals(player, playerSprite.getGameObject());
		testImagesEqual(playerSprite, "resources/RedPlayer.png");

		playerSprite = factory.createPlayerSprite(player, 3);
		assertNotNull(playerSprite);
		assertEquals(player, playerSprite.getGameObject());
		testImagesEqual(playerSprite, "resources/GreenPlayer.png");

		playerSprite = factory.createPlayerSprite(player, 4);
		assertNotNull(playerSprite);
		assertEquals(player, playerSprite.getGameObject());
		testImagesEqual(playerSprite, "resources/PurplePlayer.png");
	}

	/**
	 * Tests that the BufferedImage for the given Sprite is equal to the
	 * BufferedImage loaded from the given image path.
	 * 
	 * @param sprite
	 *            The Sprite whose BufferedImage is to be verified.
	 * @param imagePath
	 *            The path of the image for the Sprite.
	 * @throws IOException
	 *             If image cannot be loaded from the given imagePath.
	 */
	private void testImagesEqual(Sprite sprite, String imagePath)
			throws IOException
	{
		BufferedImage expected = ImageIO.read(new File(imagePath));
		BufferedImage actual = sprite.getImage();
		assertEquals(expected.getHeight(), actual.getHeight());
		assertEquals(expected.getWidth(), actual.getWidth());

		for (int i = 0; i < actual.getHeight(); i++)
		{
			for (int j = 0; j < actual.getWidth(); j++)
			{
				assertEquals(expected.getRGB(j, i), actual.getRGB(j, i));
			}
		}
	}

}
