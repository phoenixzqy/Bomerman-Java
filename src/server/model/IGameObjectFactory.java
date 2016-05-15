package server.model;

/**
 * A factory for game objects.
 */
public interface IGameObjectFactory 
{
	/**
	 * Creates a player game object.
	 * @return A new player game object.
	 */
	public IGameObject createPlayer();
	
	/**
	 * Creates an unbreakable block object.
	 * @return A new unbreakable block object.
	 */
	public IGameObject createUnbreakableBlock();
	
	/**
	 * Creates a breakable block object.
	 * @return A new breakable block object.
	 */
	public IGameObject createBreakableBlock();
	
	/**
	 * Creates a bomb object.
	 * @param owner  The owner of the new bomb object.
	 * @return A new bomb object.
	 * @throws NullPointerException If owner is null
	 */
	public IGameObject createBomb(IGameObject owner);
	
	/**
	 * Creates a new explosion object.
	 * @param owner  The owner of the new explosion object.
	 * @return A new explosion object.
	 * @throws NullPointerException If owner is null
	 */
	public IGameObject createExplosion(IGameObject owner);
}
