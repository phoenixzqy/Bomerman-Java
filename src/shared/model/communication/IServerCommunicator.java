package shared.model.communication;

/**
 * Allows other communicators to connect and sends and receives messages.
 */
public interface IServerCommunicator
{
	/**
	 * An asynchronous method which sends a message to all of the connected clients.
	 * @param message The message to send.
	 * @throws NullPointerException Thrown if the message is null.
	 * @throws CommunicationException Thrown if an error occurs with the connection.
	 */
	public void sendMessages(IMessage message) throws CommunicationException;
	
	/**
	 * An asynchronous method which returns all of the messages received since this 
	 * IServerCommunicator was connected or since the last call to this message.
	 * @throws CommunicationException Thrown if an error occurs with the connection.
	 * @return All of the messages received since this ICommunicator was connected or since the last 
	 * call to this method.
	 */
	public IMessage[] receivedMessages() throws CommunicationException;
	
	/**
	 * Returns the number of connected communicators.
	 * @return The number of connected communicators.
	 */
	public int numberOfConnectedCommunicators();
	
	/**
	 * Returns the message factory for this IServerCommunicator.
	 * @return The message factory for this IServerCommunicator.
	 */
	public IMessageFactory messageFactory();
	
	/**
	 * Stops this IServerCommunicator for accepting incoming connections.
	 * @throws IllegalStateException Thrown if this IServerCommunicator has already stopped listening
	 * for incoming connections.
	 */
	public void stopListening();
	
	/**
	 * Returns true if this IServerCommunicator is listening for incoming connections and false 
	 * otherwise.
	 * @return True if this IServerCommunicator is listening for incoming connections and false 
	 * otherwise.
	 */
	public boolean listening();
	
	/**
	 * Sends a unique message created using the provided message generator to each connected 
	 * communicator.  These messages a guaranteed to be sent in the same order that the communicators
	 * connected.
	 * @param messageGenerator The generator used to create the message.
	 * @throws NullPointerException Thrown if messageGenerator is null.  Or if its
	 * generateMessage method return null.
	 * @throws IllegalArgumentException Thrown if messageGenerator returns null for one of its 
	 * messages.
	 * @throws CommunicationException Thrown if a communication error occurs with any of the connected
	 * clients.  This method will attempt to send messages to all of the connected clients, regardless
	 * of the order in which the clients receive the message.
	 */
	public void sendUniqueMessageToEachConnectedCommunicator(IMessageGenerator messageGenerator)
		throws CommunicationException;
	
	/**
	 * Disconnectes this server communicator from all connected communicators and closes socket.
	 * @throws CommunicationException  Thrown if a communication error occurs with any of the connected clients.
	 */
	public void disconnect() throws CommunicationException;
}
