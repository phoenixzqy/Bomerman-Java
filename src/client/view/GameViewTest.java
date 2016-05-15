package client.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import client.model.IGameObject;

/**
 * Contains tests for GameView.
 * 
 */
public class GameViewTest
{

	/**
	 * An implementation of ISpriteFactory mocked by Mockito.
	 */
	private ISpriteFactory mockedFactory;

	/**
	 * A list to maintain the clip rectangles from calls to paint in the
	 * TestGameView.
	 */
	private final List<Rectangle> rects = new ArrayList<Rectangle>();

	/**
	 * A test GameView that overrides it's implementation of repaint for the
	 * purpose of verifying the rectangular clips that are being redrawn.
	 * 
	 */
	private class TestGameView extends GameView
	{
		/**
		 * Generated ID for serialization. This is necessary to avoid compiler
		 * warnings, as all JPanels are serializable.
		 */
		private static final long serialVersionUID = -2841927344338964377L;

		private TestGameView(ISpriteFactory factory) throws IOException
		{
			super(factory);
		}

		@Override
		public void repaint(Rectangle rect)
		{
			rects.add(rect);
		}
	}

	/**
	 * An instance of the TestGameView.
	 */
	private TestGameView testView;

	/**
	 * Sets up instance variables for the tests.
	 * 
	 * @throws IOException
	 *             When background image for GameView cannot be loaded.
	 */
	@Before
	public void setUp() throws IOException
	{
		mockedFactory = mock(ISpriteFactory.class);
		rects.clear();
		testView = new TestGameView(mockedFactory);
	}

	/**
	 * Tests creation of GameView given valid arguments.
	 * 
	 * @throws IOException
	 *             When background image for GameView cannot be loaded.
	 */
	@Test
	public void testGameViewConstructorValidArgument() throws IOException
	{
		ISpriteFactory factory = mock(ISpriteFactory.class);
		new GameView(factory);

	}

	/**
	 * Tests creation of GameView throws an IllegalArgumentException given a
	 * null ISpriteFactory argument.
	 * 
	 * @throws IOException
	 *             When background image for GameView cannot be loaded.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGameViewConstructorNullFactory() throws IOException
	{
		new GameView(null);
	}

	/**
	 * Tests drawGameObject throws an IllegalArgumentException given a null
	 * argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDrawGameObjectNullArgument()
	{
		testView.drawGameObject(null);
	}

	/**
	 * Tests boundingBox throws an IllegalArgumentException given a null
	 * argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBoundingBoxNullArgument()
	{
		testView.boundingBox(null);
	}

	/**
	 * Sanity check that boundingBox throws no exceptions given valid arguments.
	 */
	@Test
	public void testBoundingBoxValidArgument()
	{
		IGameObject gameObject = mock(IGameObject.class);
		testView.boundingBox(gameObject);
	}

	/**
	 * Tests that boundingBox returrns a rectangle with coordinates to match the
	 * IGameObject.
	 */
	@Test
	public void testBoundingBoxReturnsRectangleWithCorrectCoordinates()
	{
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(3);
		when(gameObject.row()).thenReturn(5);
		Rectangle rect = testView.boundingBox(gameObject);
		assertEquals(rect, new Rectangle(3 * testView.GAME_OBJECT_TILE_WIDTH,
				5 * testView.GAME_OBJECT_TILE_HEIGHT,
				testView.GAME_OBJECT_TILE_WIDTH,
				testView.GAME_OBJECT_TILE_HEIGHT));
	}

	/**
	 * Tests drawGameObject throws no exceptions with valid arguments.
	 * 
	 * @throws IOException
	 *             If the image with the given path cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectValidArguments() throws IOException
	{
		IGameObject gameObject1 = mock(IGameObject.class);
		IGameObject gameObject2 = mock(IGameObject.class);
		IGameObject gameObject3 = mock(IGameObject.class);
		when(gameObject1.row()).thenReturn(0);
		when(gameObject1.column()).thenReturn(0);
		when(gameObject2.row()).thenReturn(3);
		when(gameObject2.column()).thenReturn(0);
		when(gameObject3.row()).thenReturn(0);
		when(gameObject3.column()).thenReturn(3);

		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		testView.drawGameObject(gameObject1);
		testView.drawGameObject(gameObject2);
		testView.drawGameObject(gameObject3);
	}

	/**
	 * Tests drawGameObject draws gameObject for the first time.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectNewGameObject() throws IOException
	{
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(5);
		when(gameObject.row()).thenReturn(3);
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(gameObject, "resources/blank.png"));
		testView.drawGameObject(gameObject);
		assertTrue(rects.contains(new Rectangle(
				5 * testView.GAME_OBJECT_TILE_WIDTH,
				3 * testView.GAME_OBJECT_TILE_HEIGHT,
				testView.GAME_OBJECT_TILE_WIDTH,
				testView.GAME_OBJECT_TILE_HEIGHT)));
	}

	/**
	 * Tests drawGameObject redraws gameObject for the second time, including
	 * it's current location and previous location.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectRedrawExistingGameObjectRedrawsCurrentAndPrevious()
			throws IOException
	{
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(5);
		when(gameObject.row()).thenReturn(3);
		Rectangle firstRect = testView.boundingBox(gameObject);

		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(gameObject, "resources/blank.png"));

		testView.drawGameObject(gameObject);
		rects.clear();
		when(gameObject.column()).thenReturn(15);
		when(gameObject.row()).thenReturn(13);
		Rectangle secondRect = testView.boundingBox(gameObject);
		testView.drawGameObject(gameObject);

		assertTrue(rects.contains(firstRect));
		assertTrue(rects.contains(secondRect));
	}

	/**
	 * Tests drawGameObject doesn't throw any exceptions when the GameView's
	 * ISpriteFactory returns null for the given IGameObject.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectNullSpriteFromFactory() throws IOException
	{
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				null);
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(5);
		when(gameObject.row()).thenReturn(3);
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				null);
		testView.drawGameObject(gameObject);
	}

	/**
	 * Tests that drawGameObjects executes without any exceptions with valid
	 * arguments.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectsValidArguments() throws IOException
	{
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		IGameObject[] gameObjects = new IGameObject[3];

		gameObjects[0] = mock(IGameObject.class);
		when(gameObjects[0].column()).thenReturn(5);
		when(gameObjects[0].row()).thenReturn(3);

		gameObjects[1] = mock(IGameObject.class);
		when(gameObjects[1].column()).thenReturn(5);
		when(gameObjects[1].row()).thenReturn(3);

		gameObjects[2] = mock(IGameObject.class);
		when(gameObjects[2].column()).thenReturn(5);
		when(gameObjects[2].row()).thenReturn(3);

		testView.drawGameObjects(gameObjects);
	}

	/**
	 * Tests that drawGameObjects throws an IllegalArgumentException when given
	 * a null argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDrawGameObjectsNullArgument()
	{
		testView.drawGameObjects(null);
	}

	/**
	 * Tests that drawGameObjects redraws all gameObjects that have been
	 * previously drawn.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectsRedrawsAllGameObjects() throws IOException
	{
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		IGameObject[] gameObjects = new IGameObject[3];

		gameObjects[0] = mock(IGameObject.class);
		when(gameObjects[0].column()).thenReturn(5);
		when(gameObjects[0].row()).thenReturn(3);

		gameObjects[1] = mock(IGameObject.class);
		when(gameObjects[1].column()).thenReturn(15);
		when(gameObjects[1].row()).thenReturn(13);

		gameObjects[2] = mock(IGameObject.class);
		when(gameObjects[2].column()).thenReturn(9);
		when(gameObjects[2].row()).thenReturn(9);

		testView.drawGameObjects(gameObjects);
		rects.clear();

		testView.drawGameObjects(gameObjects);

		assertTrue(rects.contains(new Rectangle(
				5 * testView.GAME_OBJECT_TILE_WIDTH,
				3 * testView.GAME_OBJECT_TILE_HEIGHT,
				testView.GAME_OBJECT_TILE_WIDTH,
				testView.GAME_OBJECT_TILE_HEIGHT)));

		assertTrue(rects.contains(new Rectangle(
				15 * testView.GAME_OBJECT_TILE_WIDTH,
				13 * testView.GAME_OBJECT_TILE_HEIGHT,
				testView.GAME_OBJECT_TILE_WIDTH,
				testView.GAME_OBJECT_TILE_HEIGHT)));

		assertTrue(rects.contains(new Rectangle(
				9 * testView.GAME_OBJECT_TILE_WIDTH,
				9 * testView.GAME_OBJECT_TILE_HEIGHT,
				testView.GAME_OBJECT_TILE_WIDTH,
				testView.GAME_OBJECT_TILE_HEIGHT)));
	}

	/**
	 * Tests that drawGameObjects erases all gameObjects that have been
	 * previously drawn, but are not specified by the call.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testDrawGameObjectsErasesMissingObjects() throws IOException
	{
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		IGameObject[] gameObjects = new IGameObject[3];

		gameObjects[0] = mock(IGameObject.class);
		when(gameObjects[0].column()).thenReturn(5);
		when(gameObjects[0].row()).thenReturn(3);

		gameObjects[1] = mock(IGameObject.class);
		when(gameObjects[1].column()).thenReturn(15);
		when(gameObjects[1].row()).thenReturn(13);

		gameObjects[2] = mock(IGameObject.class);
		when(gameObjects[2].column()).thenReturn(9);
		when(gameObjects[2].row()).thenReturn(9);

		IGameObject[] gameObjectsErased = new IGameObject[1];
		gameObjectsErased[0] = gameObjects[2];

		testView.drawGameObjects(gameObjects);
		rects.clear();

		testView.drawGameObjects(gameObjectsErased);

		assertTrue(rects.contains(new Rectangle(
				9 * testView.GAME_OBJECT_TILE_WIDTH,
				9 * testView.GAME_OBJECT_TILE_HEIGHT,
				testView.GAME_OBJECT_TILE_WIDTH,
				testView.GAME_OBJECT_TILE_HEIGHT)));

	}

	/**
	 * Tests that paint draws a single game object overlapping with the clip.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testPaintRedrawsObjectOverlappingWithClip() throws IOException
	{
		// Create mock Graphics with a fixed clip
		Graphics2D g2d = mock(Graphics2D.class);
		when(g2d.getClipBounds()).thenReturn(
				new Rectangle(16, 16, 10 * testView.GAME_OBJECT_TILE_WIDTH,
						10 * testView.GAME_OBJECT_TILE_HEIGHT));

		// mock some game objects
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(0);
		when(gameObject.row()).thenReturn(0);
		Rectangle rect = testView.boundingBox(gameObject);

		// mock the sprite factory
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		// draw the mocks
		testView.drawGameObject(gameObject);

		// manually call paint with the mock Graphics2D and verify that the
		// overlapping game object is completely redrawn.
		testView.paint(g2d);
		verify(g2d).drawImage(any(Image.class), eq(rect.x), eq(rect.y),
				eq(rect.height), eq(rect.width), any(ImageObserver.class));
	}

	/**
	 * Tests that paint draws a single game object completely contained within
	 * the clip.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testPaintRedrawsObjectCompletelyContainedWithinClip()
			throws IOException
	{
		// Create mock Graphics with a fixed clip
		Graphics2D g2d = mock(Graphics2D.class);
		when(g2d.getClipBounds()).thenReturn(
				new Rectangle(16, 16, 10 * testView.GAME_OBJECT_TILE_WIDTH,
						10 * testView.GAME_OBJECT_TILE_HEIGHT));

		// mock some game objects
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(5);
		when(gameObject.row()).thenReturn(5);
		Rectangle rect = testView.boundingBox(gameObject);

		// mock the sprite factory
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		// draw the mocks
		testView.drawGameObject(gameObject);

		// manually call paint with the mock Graphics2D and verify that each
		// overlapping game object is completely redrawn.
		testView.paint(g2d);
		verify(g2d).drawImage(any(Image.class), eq(rect.x), eq(rect.y),
				eq(rect.height), eq(rect.width), any(ImageObserver.class));
	}

	/**
	 * Tests that paint draws nothing if no GameObjects have been drawn.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testPaintDrawsNoObjectsWhenNoGameObjectsHaveBeenDrawn()
			throws IOException
	{
		// Create mock Graphics with a fixed clip
		Graphics2D g2d = mock(Graphics2D.class);
		when(g2d.getClipBounds()).thenReturn(
				new Rectangle(testView.GAME_OBJECT_TILE_WIDTH,
						testView.GAME_OBJECT_TILE_HEIGHT,
						10 * testView.GAME_OBJECT_TILE_WIDTH,
						10 * testView.GAME_OBJECT_TILE_HEIGHT));

		// mock the sprite factory
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		// manually call paint with the mock Graphics2D and verify that nothing
		// is drawn
		testView.paint(g2d);
		verify(g2d, never()).drawImage(any(Image.class), anyInt(), anyInt(),
				anyInt(), anyInt(), any(ImageObserver.class));
	}

	/**
	 * Tests that paint draws if no objects overlap with the clip.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testPaintDrawsNoObjectsWhenNoDrawnObjectsOverlapWithClip()
			throws IOException
	{
		// Create mock Graphics with a fixed clip
		Graphics2D g2d = mock(Graphics2D.class);
		when(g2d.getClipBounds()).thenReturn(
				new Rectangle(testView.GAME_OBJECT_TILE_WIDTH,
						testView.GAME_OBJECT_TILE_HEIGHT,
						10 * testView.GAME_OBJECT_TILE_WIDTH,
						10 * testView.GAME_OBJECT_TILE_HEIGHT));

		// mock some game objects
		IGameObject gameObject = mock(IGameObject.class);
		when(gameObject.column()).thenReturn(0);
		when(gameObject.row()).thenReturn(0);

		// mock the sprite factory
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		// draw the mocks
		testView.drawGameObject(gameObject);

		// manually call paint with the mock Graphics2D and verify that nothing
		// is drawn
		testView.paint(g2d);
		verify(g2d, never()).drawImage(any(Image.class), anyInt(), anyInt(),
				anyInt(), anyInt(), any(ImageObserver.class));
	}

	/**
	 * Tests that paint draws all IGameObjects that overlap with the clip.
	 * 
	 * @throws IOException
	 *             If the test image cannot be loaded.
	 */
	@Test
	public void testPaintRedrawsAllObjectsOverlappingWithClip()
			throws IOException
	{
		// Create mock Graphics with a fixed clip
		Graphics2D g2d = mock(Graphics2D.class);
		when(g2d.getClipBounds()).thenReturn(
				new Rectangle(16, 16, 10 * testView.GAME_OBJECT_TILE_WIDTH,
						10 * testView.GAME_OBJECT_TILE_HEIGHT));

		// mock some game objects
		IGameObject gameObject1 = mock(IGameObject.class);
		when(gameObject1.column()).thenReturn(0);
		when(gameObject1.row()).thenReturn(0);
		Rectangle rect1 = testView.boundingBox(gameObject1);

		IGameObject gameObject2 = mock(IGameObject.class);
		when(gameObject2.column()).thenReturn(10);
		when(gameObject2.row()).thenReturn(10);
		Rectangle rect2 = testView.boundingBox(gameObject2);

		IGameObject gameObject3 = mock(IGameObject.class);
		when(gameObject3.column()).thenReturn(5);
		when(gameObject3.row()).thenReturn(5);
		Rectangle rect3 = testView.boundingBox(gameObject3);

		// mock the sprite factory
		when(mockedFactory.createSprite(any(IGameObject.class))).thenReturn(
				new Sprite(mock(IGameObject.class), "resources/blank.png"));

		// draw the mocks
		testView.drawGameObject(gameObject1);
		testView.drawGameObject(gameObject2);
		testView.drawGameObject(gameObject3);

		// manually call paint with the mock Graphics2D and verify that each
		// overlapping game object is completely redrawn.
		testView.paint(g2d);
		verify(g2d).drawImage(any(Image.class), eq(rect1.x), eq(rect1.y),
				eq(rect1.height), eq(rect1.width), any(ImageObserver.class));
		verify(g2d).drawImage(any(Image.class), eq(rect2.x), eq(rect2.y),
				eq(rect2.height), eq(rect2.width), any(ImageObserver.class));
		verify(g2d).drawImage(any(Image.class), eq(rect3.x), eq(rect3.y),
				eq(rect3.height), eq(rect3.width), any(ImageObserver.class));
	}

	/**
	 * Tests that the the entirety of the clip is redrawn by the background.
	 */
	@Test
	public void testPaintRedrawsBackground()
	{
		Graphics2D g2d = mock(Graphics2D.class);
		when(g2d.getClipBounds()).thenReturn(
				new Rectangle(16, 16, 10 * testView.GAME_OBJECT_TILE_WIDTH,
						10 * testView.GAME_OBJECT_TILE_HEIGHT));

		testView.paint(g2d);
		verify(g2d).drawImage(any(Image.class), eq(16), eq(16),
				eq(16 + 10 * testView.GAME_OBJECT_TILE_WIDTH),
				eq(16 + 10 * testView.GAME_OBJECT_TILE_HEIGHT), eq(16), eq(16),
				eq(16 + 10 * testView.GAME_OBJECT_TILE_WIDTH),
				eq(16 + 10 * testView.GAME_OBJECT_TILE_HEIGHT),
				any(ImageObserver.class));
	}
}
