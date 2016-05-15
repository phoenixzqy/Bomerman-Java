package server.model.behaviors;

/**
 * This class represents the ISolidityBehavior of an unsolid object.
 * 
 */
public class UnsolidBehavior implements ISolidityBehavior
{
	/**
	 * {@inheritDoc}
	 */
	public boolean solid()
	{
		return false;
	}
}
