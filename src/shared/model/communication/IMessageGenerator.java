package shared.model.communication;

/**
 * Generates messages.  This class allows a person to pass in an IMessageGenerator to a method and 
 * let that method generate messages without knowing how the messages are created.  It is a 
 * realization of the command pattern.
 */
public interface IMessageGenerator
{
	/**
	 * Generates a message and returns it.
	 * @return The generated message.
	 */
	public IMessage generateMessage();
}
