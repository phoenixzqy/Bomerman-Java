package shared.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests Rectangle.
 */
public class RectangleTest 
{
	// the error tolerance level for double comparisons
	private static final double EPSILON = 0.00000001;
	
	// a test rectangle
	private Rectangle rectangle;
	
	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		rectangle = new Rectangle(0, 0, 2, 2);
	}
	
	/**
	 * Sanity check for creating a rectangle.
	 */
	@Test
	public void sanityCheck() 
	{
		new Rectangle(1, 1, 1, 1);
	}
	
	/**
	 * Ensures the constructor allows zero for x.
	 */
	@Test
	public void constructorShouldAllowZeroForX()
	{
		new Rectangle(0, 1, 1, 1);
	}
	
	/**
	 * Ensures the constructor allows zero for y.
	 */
	@Test
	public void constructorShouldAllowZeroForY()
	{
		new Rectangle(1, 0, 1, 1);
	}
	
	/**
	 * Ensures the constructor allows zero for the width.
	 */
	@Test
	public void constructorShouldAllowZeroForWidth()
	{
		new Rectangle(1, 1, 0, 1);
	}
	
	/**
	 * Ensures the constructor allows zero for the height.
	 */
	@Test
	public void constructorShouldAllowZeroForHeight()
	{
		new Rectangle(1, 1, 1, 0);
	}
	
	/**
	 * Ensures the constructor allows negative values for x.
	 */
	@Test
	public void constructorShouldAllowNegativeValuesForX()
	{
		new Rectangle(-1, 1, 1, 1);
	}
	
	/**
	 * Ensures the constructor allows negative values for y.
	 */
	@Test
	public void constructorShouldAllowNegativeValuesForY()
	{
		new Rectangle(1, -1, 1, 1);
	}
	
	/**
	 * Ensures the constructor throws an exception when a negative width is provided.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorShouldThrowAnExceptionForNegativeWidth()
	{
		new Rectangle(1, 1, -1, 1);
	}
	
	/**
	 * Ensures the constructor throws an exception when a negative height is provided.
	 */
	@Test(expected=NullPointerException.class)
	public void constructorShouldThrowAnExceptionForNegativeHeight()
	{
		new Rectangle(1, 1, 1, -1);
	}
	
	

	/**
	 * Ensures the x() method return's the rectangle's x coordinate.
	 */
	@Test
	public void testX()
	{
		assertEquals(2, new Rectangle(2, 1, 1, 1).x(), EPSILON);
	}

	/**
	 * Ensures the y() method return's the rectangle's y coordinate.
	 */
	@Test
	public void testY()
	{
		assertEquals(2, new Rectangle(1, 2, 1, 1).y(), EPSILON);
	}

	/**
	 * Ensures the width() method return's the rectangle's width.
	 */
	@Test
	public void testWidth()
	{
		assertEquals(2, new Rectangle(1, 1, 2, 1).width(), EPSILON);
	}

	/**
	 * Ensures the height() method return's the rectangle's height.
	 */
	@Test
	public void testHeight()
	{
		assertEquals(2, new Rectangle(1, 1, 1, 2).height(), EPSILON);
	}
	
	
	
	/**
	 * Ensures the overlaps() method throws an exception when the provided rectangle is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testoverlapsNullArgument()
	{
		rectangle.overlaps(null);
	}
	
	
	/**
	 * Ensures two rectangles are overlaps when the bottom left corner of one rectangle is inside another rectangle.
	 */
	@Test
	public void overlapsWhenBottomLeftInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, -1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom right corner of one rectangle is inside another rectangle.
	 */
	@Test
	public void overlapsWhenBottomRightInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(1, -1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top left corner of one rectangle is inside another rectangle.
	 */
	@Test
	public void overlapsWhenTopLeftInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, 1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top right corner of one rectangle is inside another rectangle.
	 */
	@Test
	public void overlapsWhenTopRightInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(1, 1, 2, 2)));
	}
	
	
	
	/**
	 * Ensures two rectangles are overlaps when the right side of one rectangle overlaps the left side of the other rectangle.
	 */
	@Test
	public void overlapsWhenRightSideOverlapsLeftSideOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, 0, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the left side of one rectangle overlaps the right side of the other rectangle.
	 */
	@Test
	public void overlapsWhenLeftSideOverlapsRightSideOfOther() 
	{
		assertTrue(rectangle.overlaps(new Rectangle(-2, 0, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top side of one rectangle overlaps the bottom side of the other rectangle.
	 */
	@Test
	public void overlapsWhenTopSideOverlapsBottomSideOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(0, 2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom side of one rectangle overlaps the top side of the other rectangle.
	 */
	@Test
	public void overlapsWhenBottomSideOverlapsTopSideOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(0, -2, 2, 2)));
	}
	
	
	/**
	 * Ensures two rectangles are overlaps when the right side of one rectangle is inside the other rectangle.
	 */
	@Test
	public void overlapsWhenRightSideInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(1, -1, 2, 4)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the left side of one rectangle is inside the other rectangle.
	 */
	@Test
	public void overlapsWhenLeftSideInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, -1, 2, 4)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top side of one rectangle is inside the other rectangle.
	 */
	@Test
	public void overlapsWhenTopSideInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, 1, 4, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom side of one rectangle is inside other rectangle.
	 */
	@Test
	public void overlapsWhenBottomSideInsideOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, -1, 4, 2)));
	}
	
	
	
	/**
	 * Ensures two rectangles are overlaps when the bottom left corner of one rectangle overlaps the top right corner of another.
	 */
	@Test
	public void overlapsWhenBottomLeftOverlapsTopRightOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-2, -2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom right corner of one rectangle overlaps the top left corner of another.
	 */
	@Test
	public void overlapsWhenBottomRightOverlapsTopLeftOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, -2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top left corner of one rectangle overlaps the bottom right corner of another.
	 */
	@Test
	public void overlapsWhenTopLeftOverlapsBottomRightOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, -2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top right corner of one rectangle overlaps the bottom left corner of another.
	 */
	@Test
	public void overlapsWhenTopRightOverlapsBottomLeftOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, 2, 2, 2)));
	}
	
	

	/**
	 * Ensures two rectangles are overlaps when the bottom left corner of one rectangle overlaps top of the other rectangle.
	 */
	@Test
	public void overlapsWhenBottomLeftOverlapsTopOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, -2, 2, 2)));
	}

	/**
	 * Ensures two rectangles are overlaps when the bottom left corner of one rectangle overlaps right of the other rectangle.
	 */
	@Test
	public void overlapsWhenBottomLeftOverlapsRightOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-2, -1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom right corner of one rectangle overlaps the left corner of another.
	 */
	@Test
	public void overlapsWhenBottomRightOverlapsTopOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(1, -2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom right corner of one rectangle overlaps the top corner of another.
	 */
	@Test
	public void overlapsWhenBottomRightOverlapsLeftOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, -1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top left corner of one rectangle overlaps the bottom corner of another.
	 */
	@Test
	public void overlapsWhenTopLeftOverlapsBottomOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, 2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top left corner of one rectangle overlaps the right corner of another.
	 */
	@Test
	public void overlapsWhenTopLeftOverlapsRightOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, -1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top right corner of one rectangle overlaps the left corner of another.
	 */
	@Test
	public void overlapsWhenTopRightOverlapsLeftOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(1, 2, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top right corner of one rectangle overlaps the bottom corner of another.
	 */
	@Test
	public void overlapsWhenTopRightOverlapsBottomOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(2, 1, 2, 2)));
	}

	
	/**
	 * Ensures two rectangles are overlaps when the right side of this rectangle overlaps the middle of another rectangle but no points of this rectangle are contained
	 * within the provided rectangle.
	 */
	@Test
	public void overlapsWhenRightSideOverlapsMiddleOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(1.5, 0.5, 1, 1)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the left side of this rectangle overlaps the middle of another rectangle but no points of this rectangle are contained
	 * within the provided rectangle.
	 */
	@Test
	public void overlapsWhenLeftSideOverlapsMiddleOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-0.5, 0.5, 1, 1)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the top side of this rectangle overlaps the middle of another rectangle but no points of this rectangle are contained
	 * within the provided rectangle.
	 */
	@Test
	public void overlapsWhenTopSideOverlapsMiddleOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(0.5, 1.5, 1, 1)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the bottom side of this rectangle overlaps the middle of another rectangle but no points of this rectangle are contained
	 * within the provided rectangle.
	 */
	@Test
	public void overlapsWhenBottomSideOverlapsMiddleOfOther()
	{
		assertTrue(rectangle.overlaps(new Rectangle(0.5, -0.5, 1, 1)));
	}
	
	/**
	 * Ensures two rectangles overlap when only their middles overlap.
	 */
	@Test
	public void overlapsWhenMiddlesOverlap()
	{
		assertTrue(new Rectangle(0, 1, 3, 1).overlaps(new Rectangle(1, 0, 1, 3)));
	}
	
	
	
	/**
	 * Ensures two rectangles are overlaps when the calling rectangle is completely inside the other rectangle.
	 */
	@Test
	public void overlapsWhenCallerIsInsideTheOtherRectangle()
	{
		assertTrue(rectangle.overlaps(new Rectangle(-1, -1, 4, 4)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the other rectangle is completely inside the other rectangle.
	 */
	@Test
	public void overlapsWhenOtherRectangleIsInsideCaller()
	{
		assertTrue(rectangle.overlaps(new Rectangle(0.5, 0.5, 1 ,1)));
	}
	
	/**
	 * Ensures two rectangles are overlaps when the rectangles are equal.
	 */
	@Test
	public void colldingWhenEqual()
	{
		assertTrue(rectangle.overlaps(rectangle));
	}
	
	
	
	/**
	 * Ensures two rectangles are not overlaps when the x-coordinate is inside the other rectangle but the y is not.
	 */
	@Test
	public void doesntOverlapWhenXOverlapsButYDoesNot()
	{
		assertFalse(rectangle.overlaps(new Rectangle(1, 3, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(1, -3, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(-1, 3, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(-1, -3, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are not overlaps when the y-coordinate is inside the other rectangle but the x is not.
	 */
	@Test
	public void doesntOverlapWhenYOverlapsButXDoesNot()
	{
		assertFalse(rectangle.overlaps(new Rectangle(3, 1, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(-3, 1, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(3, -1, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(-3, -1, 2, 2)));
	}
	
	/**
	 * Ensures two rectangles are not overlaps when neither the x-coordinate or the y-coordinate do not overlap.
	 */
	@Test
	public void doesntOverlapWhenDoesntOverlap()
	{
		assertFalse(rectangle.overlaps(new Rectangle(3, 3, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(-3, -3, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(3, -3, 2, 2)));
		assertFalse(rectangle.overlaps(new Rectangle(-3, 3, 2, 2)));
	}
}
