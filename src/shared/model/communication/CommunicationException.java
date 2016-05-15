package shared.model.communication;

/**
 * An exception which indicates an error has occurred with a communication between the client and
 * the server.
 */
public class CommunicationException extends Exception
{



	/**
	 * Default constructor.
	 */
	public CommunicationException()
	{
		this("A communication error occurred.");
	}
	
	/**
	 * Constructor which creates a CommunicationException using the provided message.
	 * @param message The message for this CommunicationException.
	 * @throws NullPointerException Thrown if message is null.
	 */
	public CommunicationException(String message)
	{
		super(message);
		
		if (message == null)
			throw new NullPointerException();
	}
}
