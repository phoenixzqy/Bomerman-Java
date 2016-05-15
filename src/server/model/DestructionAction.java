package server.model;

/**
 * An action which should occur when an IGameObject is destroyed.
 */
public enum DestructionAction
{
	/**
	 * The IGameObject should respawn.
	 */
	RESPAWN,
	
	/**
	 * The IGameObject should explode.
	 */
	EXPLODE,
	
	/**
	 * The IGameObject should disappear when destroyed.
	 */
	DISAPPEAR,
}
