package server.model.behaviors;

/**
 * This class represents the ISolidityBehavior of a solid object.
 * 
 */
public class SolidBehavior implements ISolidityBehavior
{
	/**
	 * {@inheritDoc}
	 */
	public boolean solid()
	{
		return true;
	}
}
