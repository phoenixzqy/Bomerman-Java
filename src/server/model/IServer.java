package server.model;

/**
 * Sends and receives messages from multiple clients.
 */
public interface IServer 
{
	/**
	 * Returns all messages received by this server since the last call to receivedMessages().
	 * @return All messages received by this server since the last call to receivedMessages().
	 */
	public String[] receivedMessages();
	
	/**
	 * Sends a message to all connected clients.
	 * @param message The message to send.
	 */
	public void sendMessage(String message);
	
	/**
	 * Sends the message to the specific client.
	 * @param client The client identifier to send the message to.
	 * @param message The message to send.
	 */
	public void sendMessage(IClientIdentifier client, String message);
}
