package server.model;

/**
 * This class identifies the client.
 */
public interface IClientIdentifier 
{
	/**
	 * Uniquely identifies each client.
	 * @return Returns a unique identifier for this IClientIdentifier.
	 */
	public int identifier();
}