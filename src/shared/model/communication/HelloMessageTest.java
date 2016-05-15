package shared.model.communication;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests HelloMessage.
 */
public class HelloMessageTest
{
	/**
	 * Ensures the hello message can be created.
	 */
	@Test
	public void sanityCheck() {
		assertEquals("STATUS", new HelloMessage().toString());
	}

}
