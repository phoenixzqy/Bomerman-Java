package server.model.behaviors;

/**
 * Behavior which determines the solidity of the current IGameObject.
 */
public interface ISolidityBehavior 
{
	/**
	 * Returns true if the IGameObject to which this behavior belongs is solid and false otherwise.
	 * @return True if the IGameObject to which this behavior belongs is solid and false otherwise.
	 */
	public boolean solid();
}
