package shared.model;

/**
 * This enumerated type contains types for all possible game objects.
 */
public enum GameObjectType {
	
	/**
	 * Represents a player game object.
	 */
	PLAYER,
	
	/**
	 * Represents a bomb game object.
	 */
	BOMB, 
	
	/**
	 * Represents an unbreakable block game object.
	 */
	UNBREAKABLE_BLOCK, 
	
	/**
	 * Represents a breakable block game object.
	 */
	BREAKABLE_BLOCK,
	
	/**
	 * Represents an explosion game object.
	 */
	EXPLOSION
}