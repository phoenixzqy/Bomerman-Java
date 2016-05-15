package shared.model.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 */
public class CommunicationExceptionTest
{
	/**
	 * Ensures the default constructor creates a valid CommunicationException.
	 */
	@Test
	public void testDefaultConstructor()
	{
		Exception exception = new CommunicationException();
		assertEquals("A communication error occurred.", exception.getMessage());
	}
	
	/**
	 * Ensures the message constructor creates a CommunicationException with the provided message. 
	 */
	@Test
	public void testConstructorMessage()
	{
		String message = "Test message.";
		Exception exception = new CommunicationException(message);
		assertEquals(message, exception.getMessage());
	}
	
	/**
	 * Ensures the message constructor throws a NullPointerException if the message is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorMessageNull()
	{
		new CommunicationException(null);
	}
}
