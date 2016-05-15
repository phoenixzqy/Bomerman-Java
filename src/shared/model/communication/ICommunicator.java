package shared.model.communication;

/**
 * Communicates with the server application.
 */
public interface ICommunicator
{
	/**
	 * An asynchronous method which sends a message to the server.
	 * 
	 * @param message
	 *            The message to send.
	 * @throws NullPointerException
	 *             Thrown if the message is null.
	 * @throws IllegalStateException
	 *             Thrown if this ICommunicator is not connected to a server.
	 * @throws CommunicationException
	 *             Thrown if an error occurs with the connection.
	 */
	public void sendMessage(IMessage message) throws CommunicationException;

	/**
	 * An asynchronous method which returns all of the messages received since
	 * this ICommunicator was connected or since the last call to this method or
	 * receiveMessage().
	 * 
	 * @throws IllegalStateException
	 *             Thrown if this ICommunicator is not connected to a server.
	 * @throws CommunicationException
	 *             Thrown if an error occurs with the connection.
	 * @return All of the messages received since this ICommunicator was
	 *         connected or since the last call to this method.
	 */
	public IMessage[] receivedMessages() throws CommunicationException;

	/**
	 * An asynchronous method which returns all of the messages received since
	 * this ICommunicator was connected or since the last call to this method or
	 * receiveMessage().
	 * 
	 * @param maximumNumberOfMessages
	 *            The maximum number of messages which will be received.
	 * @throws IllegalStateException
	 *             Thrown if this ICommunicator is not connected to a server.
	 * @throws CommunicationException
	 *             Thrown if an error occurs with the connection.
	 * @return Returns the messages received since this ICommunicator was
	 *         connected or since the last call to this messages up to the
	 *         provided maximum number of messages.
	 */
	public IMessage[] receivedMessages(int maximumNumberOfMessages)
			throws CommunicationException;

	/**
	 * A synchronous message which disconnects this ICommunicator from the
	 * connected server.
	 * 
	 * @throws IllegalStateException
	 *             Thrown if this ICommunicator is already disconnected from the
	 *             server.
	 * @throws CommunicationException
	 *             Thrown if an error occurs with the connection.
	 */
	public void disconnect() throws CommunicationException;

	/**
	 * Returns true if this ICommunicator is currently connected to a server and
	 * false otherwise.
	 * 
	 * @return True if this ICommunicator is currently connected to a server and
	 *         false otherwise.
	 */
	public boolean connected();

	/**
	 * Returns the message factory for this ICommunicator.
	 * 
	 * @return The message factory for this ICommunicator.
	 */
	public IMessageFactory messageFactory();

	/**
	 * If connected, returns the address that this communicator is connected to.
	 * Otherwise returns the address that this communicator was last connected
	 * to.
	 * 
	 * @return The address that this communicator is connected to.
	 */
	public String connectedAddress();
}
