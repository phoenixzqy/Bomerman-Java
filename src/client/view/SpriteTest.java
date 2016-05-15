package client.view;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import client.model.IGameObject;

/**
 * JUnit4 Test class for Sprite.
 */
public class SpriteTest
{
	/**
	 * An IGameObject usable for testing.
	 */
	private IGameObject testPiece;

	/**
	 * The path of the test image.
	 */
	private String testImagePath;

	/**
	 * An image full of garbage pixels used for testing.
	 */
	private BufferedImage testImage;

	/**
	 * A Sprite usable for testing.
	 */
	private Sprite testSprite;

	/**
	 * Initializes testPiece, testImagePath, and testSprite for testing.
	 * 
	 * @throws Exception
	 *             Throws IOException or IllegalArgumentException if there are
	 *             errors during Sprite construction.
	 */
	@Before
	public void setUp() throws Exception
	{

		// create a test image and write random pixels to it
		testImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Random random = new Random();

		for (int i = 0; i < testImage.getWidth(); i++)
		{
			for (int j = 0; j < testImage.getHeight(); j++)
			{
				testImage.setRGB(i, j, random.nextInt());
			}
		}

		// write the image to a temporary file
		File temporaryFile = File.createTempFile("temporaryImage", ".png");
		testImagePath = temporaryFile.getPath();
		ImageIO.write(testImage, "png", temporaryFile);

		testPiece = mock(IGameObject.class);
		testSprite = new Sprite(testPiece, testImagePath);
	}

	/**
	 * Tests Sprite constructor given valid arguments.
	 * 
	 * @throws IOException
	 *             This should not be thrown if test is successful.
	 */
	@Test
	public void testConstructorValidArguments() throws IOException
	{
		new Sprite(testPiece, testImagePath);
	}

	/**
	 * Tests Sprite constructor given a null IGameObject argument.
	 * 
	 * @throws IOException
	 *             This should be thrown if test is successful.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorThrowsExceptionWhenNullGamePiece()
			throws IOException
	{
		new Sprite(null, testImagePath);
	}

	/**
	 * Tests Sprite constructor given an image source that does not reference a
	 * real file.
	 * 
	 * @throws IOException
	 *             This should be thrown if test is successful.
	 */
	@Test(expected = IOException.class)
	public void testConstructorThrowsExceptionWhenImageNotFound()
			throws IOException
	{
		new Sprite(testPiece, "");
	}

	/**
	 * Tests Sprite constructor given an image source that references a
	 * non-image file.
	 * 
	 * @throws IOException
	 *             This should be thrown if test is successful.
	 */
	@Test(expected = IOException.class)
	public void testConstructorThrowsExceptionWhenSourceSpecifiesNonImage()
			throws IOException
	{
		new Sprite(testPiece, this.getClass().getSimpleName() + ".java");
	}

	/**
	 * Tests Sprite constructor given a null image source argument.
	 * 
	 * @throws IllegalArgumentException
	 *             This should be thrown if test is successful.
	 * @throws IOException
	 *             This exception is ignored.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorThrowsExceptionWhenNullImageSource()
			throws IOException
	{
		new Sprite(testPiece, null);
	}

	/**
	 * Tests that getImage() returns a non-null image.
	 */
	@Test
	public void testGetImageNotNull()
	{
		assertNotNull(testSprite.getImage());
	}

	/**
	 * Tests that getGameObject() returns a non-null IGameObject.
	 */
	@Test
	public void testGetBoundingRectangleNotNull()
	{
		assertNotNull(testSprite.getGameObject());
	}

	/**
	 * Ensures the get image method is equal to the source image.
	 * 
	 * @throws IOException
	 *             This exception is ignored.
	 */
	@Test
	public void testGetImageEqualsSourceImage() throws IOException
	{

		BufferedImage expected = ImageIO.read(new File(testImagePath));
		BufferedImage actual = testSprite.getImage();
		assertEquals(expected.getHeight(), actual.getHeight());
		assertEquals(expected.getWidth(), actual.getWidth());

		for (int i = 0; i < expected.getHeight(); i++)
		{
			for (int j = 0; j < expected.getWidth(); j++)
			{
				int[] expectedData = new int[10];
				int[] actualData = new int[10];
				expected.getData().getPixel(j, i, expectedData);
				actual.getData().getPixel(j, i, actualData);
				assertArrayEquals(expectedData, actualData);
			}
		}
	}

	/**
	 * Tests that getGameObject() returns an IGameObject that matches the
	 * IGameObject used to construct the Sprite.
	 */
	@Test
	public void testInitialGetGameObject()
	{
		assertEquals(testPiece, testSprite.getGameObject());
	}

}
