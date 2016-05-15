package shared.core;

/**
 * Represents a rectangle in a two-dimensional coordinate space.
 */
public class Rectangle 
{
	// the x-coordinate of the rectangle
	private double x;
	
	// the y-coordinate of the rectangle
	private double y;
	
	// the width of the rectangle
	private double width;
	
	// the height of the rectangle
	private double height;
	
	/**
	 * Constructor for rectangle.
	 * @param x The x-coordinate of the Rectangle.
	 * @param y The y-coordinate of the Rectangle.
	 * @param width The width of the Rectangle.
	 * @param height The height of the Rectangle.
	 * @throws NullPointerException Thrown if the width or height is less than zero.
	 */
	public Rectangle(double x, double y, double width, double height)
	{
		if (width < 0 || height < 0)
			throw new NullPointerException();
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Returns the x-coordinate if this Rectangle.
	 * @return The x-coordinate if this Rectangle.
	 */
	public double x()
	{
		return x;
	}
	
	/**
	 * Returns the y-coordinate if this Rectangle.
	 * @return The y-coordinate if this Rectangle.
	 */
	public double y()
	{
		return y;
	}
	
	/**
	 * Returns the width of this Rectangle.
	 * @return The width of this Rectangle.
	 */
	public double width()
	{
		return width;
	}
	
	/**
	 * Returns the height of this Rectangle.
	 * @return The height of this Rectangle.
	 */
	public double height()
	{
		return height;
	}
	
	/**
	 * Returns true if the rectangle overlaps the provided rectangle and false otherwise.
	 * @param rectangle The other rectangle to test.
	 * @return True if the rectangle overlaps the provided rectangle and false otherwise.
	 */
	public boolean overlaps(Rectangle rectangle)
	{
		if (rectangle == null)
			throw new NullPointerException();
		
		// check all of the corners
		return (this.x <= rectangle.x + rectangle.width() && this.x + this.width >= rectangle.x &&
			this.y <= rectangle.y + rectangle.height && this.y + this.height >= rectangle.y);
	}
}
