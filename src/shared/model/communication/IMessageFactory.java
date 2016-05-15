package shared.model.communication;


/**
 * Allows messages to be created from strings.
 */
public interface IMessageFactory
{
	/**
	 * Creates a message using the provided message string.
	 * @param messageString The string from which the message is created.
	 * @return A message created from the provided message string.
	 * @throws NullPointerException Thrown if messageString is null.
	 * @throws IllegalArgumentException Thrown if the message is in an unrecognized format.
	 */
	public IMessage createMessage(String messageString);
}
