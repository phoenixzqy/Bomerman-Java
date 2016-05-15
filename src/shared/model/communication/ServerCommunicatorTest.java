package shared.model.communication;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests ServerCommunicator.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ServerCommunicator.class)
public class ServerCommunicatorTest
{
	// a mock message factory
	private IMessageFactory mockMessageFactory;

	// a test server communicator
	private ServerCommunicator testServerCommunicator;

	// a mock server socket
	private ServerSocket mockServerSocket;

	// a mock accept runnable
	private ServerCommunicator.AcceptRunnable mockAcceptRunnable;

	// a mock thread
	private Thread mockThread;

	// mock sockets
	private Socket mockSocket1;
	private Socket mockSocket2;
	private Socket mockSocket3;

	// mock communicators
	private Communicator mockCommunicator1;
	private Communicator mockCommunicator2;
	private Communicator mockCommunicator3;

	// mock messages
	private IMessage mockMessage1;
	private IMessage mockMessage2;
	private IMessage mockMessage3;
	private IMessage mockMessage4;
	private IMessage mockMessage5;
	private IMessage mockMessage6;

	// a mock message generator
	IMessageGenerator mockMessageGenerator;

	/**
	 * Sets up the tests.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Before
	public void setUp() throws Exception
	{
		mockMessageFactory = mock(IMessageFactory.class);

		// mock the messages
		mockMessage1 = mock(IMessage.class);
		mockMessage2 = mock(IMessage.class);
		mockMessage3 = mock(IMessage.class);
		mockMessage4 = mock(IMessage.class);
		mockMessage5 = mock(IMessage.class);
		mockMessage6 = mock(IMessage.class);

		// mock the sockets
		mockSocket1 = mock(Socket.class);
		mockSocket2 = mock(Socket.class);
		mockSocket3 = mock(Socket.class);

		// mock the communicators
		mockCommunicator1 = mock(Communicator.class);
		mockCommunicator2 = mock(Communicator.class);
		mockCommunicator3 = mock(Communicator.class);

		when(mockCommunicator1.connected()).thenReturn(true);
		when(mockCommunicator2.connected()).thenReturn(true);
		when(mockCommunicator3.connected()).thenReturn(true);

		PowerMockito.whenNew(Communicator.class).withArguments(
				mockMessageFactory, mockSocket1).thenReturn(mockCommunicator1);

		PowerMockito.whenNew(Communicator.class).withArguments(
				mockMessageFactory, mockSocket2).thenReturn(mockCommunicator2);

		PowerMockito.whenNew(Communicator.class).withArguments(
				mockMessageFactory, mockSocket3).thenReturn(mockCommunicator3);

		// mock the server socket
		mockServerSocket = mock(ServerSocket.class);
		PowerMockito.whenNew(ServerSocket.class).withArguments(
				Communicator.DEFAULT_PORT).thenReturn(mockServerSocket);

		// mock the accept runnable
		mockAcceptRunnable = mock(ServerCommunicator.AcceptRunnable.class);
		PowerMockito.whenNew(ServerCommunicator.AcceptRunnable.class)
				.withNoArguments().thenReturn(mockAcceptRunnable);

		// mock the thread
		mockThread = mock(Thread.class);
		PowerMockito.whenNew(Thread.class).withArguments(mockAcceptRunnable)
				.thenReturn(mockThread);

		// create the test server communicator
		testServerCommunicator = new ServerCommunicator(mockMessageFactory);

		// mock a message generator which returns a different mocked message
		mockMessageGenerator = mock(IMessageGenerator.class);
		when(mockMessageGenerator.generateMessage()).thenReturn(mockMessage1,
				mockMessage2, mockMessage3, mockMessage4, mockMessage5,
				mockMessage6);
	}

	/**
	 * Ensures the constructor works properly.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test
	public void testConstructor() throws Exception
	{
		// verify the message factory was set properly
		assertEquals(mockMessageFactory, testServerCommunicator
				.messageFactory());

		// verify the thread was started
		verify(mockThread).start();

		// verify the server socket was set up properly
		PowerMockito.verifyNew(ServerSocket.class).withArguments(
				Communicator.DEFAULT_PORT);
	}

	/**
	 * Ensures the constructor throws a NullPointerException when provided with
	 * a null message factory.
	 * 
	 * @throws CommunicationException
	 *             This should not happen.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNullMessageFactory()
			throws CommunicationException
	{
		new ServerCommunicator(null);
	}

	/**
	 * Ensures the ServerCommunicator throws a CommunicationException if the
	 * socket server constructor throws an IOException.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test(expected = CommunicationException.class)
	public void testConstructorServerSocketThrowsException() throws Exception
	{
		PowerMockito.whenNew(ServerSocket.class).withArguments(
				Communicator.DEFAULT_PORT).thenThrow(new IOException());
		new ServerCommunicator(mockMessageFactory);
	}

	/**
	 * Ensures sendMessages throws a NullPointerException when provided with a
	 * null message.
	 * 
	 * @throws Exception
	 *             This should be a NullPointerException
	 */
	@Test(expected = NullPointerException.class)
	public void testSendMessagesMessageNull() throws Exception
	{
		testServerCommunicator.sendMessages(null);
	}

	/**
	 * Ensures the sendMessages method sends the message to a connected
	 * communicator.
	 * 
	 * @throws IOException
	 *             This shouldn't happen.
	 * @throws CommunicationException
	 *             This shouldn't happen.
	 */
	@Test
	public void testSendMessagesOneConnectedCommunicator() throws IOException,
			CommunicationException
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		// perform the test
		testServerCommunicator.sendMessages(mockMessage1);
		verify(mockCommunicator1).sendMessage(mockMessage1);
	}

	/**
	 * Ensures the sendMessages methods sends the message to multiple connected
	 * communicators.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testSendMessagesMultipleConnectedCommunicators()
			throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();

		// perform the test
		testServerCommunicator.sendMessages(mockMessage1);
		verify(mockCommunicator1).sendMessage(mockMessage1);
		verify(mockCommunicator2).sendMessage(mockMessage1);
	}

	/**
	 * Ensures the sendMessages method sends the message only to the connected
	 * communicators when multiple communicators are connected and then one is
	 * disconnected.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testSendMessagesDisconnectedCommunicator() throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();
		when(mockCommunicator1.connected()).thenReturn(false);

		// perform the test
		testServerCommunicator.sendMessages(mockMessage1);
		verify(mockCommunicator1, never()).sendMessage(mockMessage1);
		verify(mockCommunicator2).sendMessage(mockMessage1);
	}

	/**
	 * Ensures the sendMessages method still sends messages to connected
	 * communicators after the server communicator stops listening.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testSendMessagesStopListening() throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();
		testServerCommunicator.stopListening();

		// perform the test
		testServerCommunicator.sendMessages(mockMessage1);
		verify(mockCommunicator1).sendMessage(mockMessage1);
		verify(mockCommunicator2).sendMessage(mockMessage1);
	}

	/**
	 * Ensures sendMessages throws a CommunicationException if an error occurs
	 * with the connection.
	 * 
	 * @throws Exception
	 *             This should be a CommunicationException.
	 */
	@Test(expected = CommunicationException.class)
	public void testSendMessagesCommunicatorError() throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		doThrow(new CommunicationException()).when(mockCommunicator1)
				.sendMessage(mockMessage1);

		// perform the test
		testServerCommunicator.sendMessages(mockMessage1);
	}

	/**
	 * Ensures receiveMessages returns an empty array when no communicators are
	 * connected.
	 * 
	 * @throws Exception
	 *             This souldn't happen.
	 */
	@Test
	public void testReceiveMessagesNoConnectedCommunicators() throws Exception
	{
		assertArrayEquals(new IMessage[0], testServerCommunicator
				.receivedMessages());
	}

	/**
	 * Ensures receiveMessages returns the messages received from a single
	 * connected communicator.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testReceiveMessagesOneConnectedCommunicator() throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockCommunicator1.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage1, mockMessage2 });

		// perform the test
		IMessage[] messages = testServerCommunicator.receivedMessages();
		verify(mockCommunicator1).receivedMessages();
		assertArrayEquals(new IMessage[] { mockMessage1, mockMessage2 },
				messages);
	}

	/**
	 * Ensures receiveMessages returns the messages received from multiple
	 * connected communicators. The messages should be in the order returned by
	 * the connected communicators and should be concatenated into an array in
	 * the order the communicators connected to the server communicator.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testReceiveMessagesMultipleConnectedCommunicators()
			throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket3);
		testServerCommunicator.accept();

		when(mockCommunicator1.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage1, mockMessage2 });
		when(mockCommunicator2.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage3 });
		when(mockCommunicator3.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage4, mockMessage5, mockMessage6 });

		// perform the test
		IMessage[] messages = testServerCommunicator.receivedMessages();
		IMessage[] expectedMessages = new IMessage[] { mockMessage1,
				mockMessage2, mockMessage3, mockMessage4, mockMessage5,
				mockMessage6, };
		verify(mockCommunicator1).receivedMessages();
		assertArrayEquals(expectedMessages, messages);
	}

	/**
	 * Ensures receiveMessages does not receive messages from a disconnected
	 * communicator.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testReceiveMessagesDisconnectedCommunicator() throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();
		when(mockCommunicator1.connected()).thenReturn(false);

		when(mockCommunicator1.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage1, mockMessage2 });
		when(mockCommunicator2.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage3, mockMessage4 });

		// perform the test
		IMessage[] messages = testServerCommunicator.receivedMessages();
		verify(mockCommunicator1, never()).receivedMessages();
		verify(mockCommunicator2).receivedMessages();
		assertArrayEquals(new IMessage[] { mockMessage3, mockMessage4 },
				messages);
	}

	/**
	 * Ensures the receiveMessages method still receives messages after the
	 * serverCommunicator stops listening.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testReceiveMessagesStopListening() throws Exception
	{
		// get the communicator into the test state
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();
		testServerCommunicator.stopListening();

		when(mockCommunicator1.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage1, mockMessage2 });
		when(mockCommunicator2.receivedMessages()).thenReturn(
				new IMessage[] { mockMessage3, mockMessage4 });

		// perform the test
		IMessage[] messages = testServerCommunicator.receivedMessages();
		verify(mockCommunicator1).receivedMessages();
		verify(mockCommunicator2).receivedMessages();
		IMessage[] expectedMessages = { mockMessage1, mockMessage2,
				mockMessage3, mockMessage4 };
		assertArrayEquals(expectedMessages, messages);
	}

	/**
	 * Ensures receiveMessages throws a CommunicationException if an error
	 * occurs with the connection.
	 * 
	 * @throws Exception
	 *             This should be a CommunicationException.
	 */
	@Test(expected = CommunicationException.class)
	public void testReceiveMessagesCommunicationError() throws Exception
	{
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		when(mockCommunicator1.receivedMessages()).thenThrow(
				new CommunicationException());

		testServerCommunicator.receivedMessages();
	}

	/**
	 * Ensures numberOfConnectedCommunicators works properly.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testNumberOfConnectedCommuicators() throws Exception
	{
		assertEquals(0, testServerCommunicator.numberOfConnectedCommunicators());

		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();
		assertEquals(1, testServerCommunicator.numberOfConnectedCommunicators());

		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();
		assertEquals(2, testServerCommunicator.numberOfConnectedCommunicators());

		when(mockServerSocket.accept()).thenReturn(mockSocket3);
		testServerCommunicator.accept();
		assertEquals(3, testServerCommunicator.numberOfConnectedCommunicators());

		when(mockCommunicator1.connected()).thenReturn(false);
		assertEquals(2, testServerCommunicator.numberOfConnectedCommunicators());

		when(mockCommunicator2.connected()).thenReturn(false);
		assertEquals(1, testServerCommunicator.numberOfConnectedCommunicators());

		when(mockCommunicator3.connected()).thenReturn(false);
		assertEquals(0, testServerCommunicator.numberOfConnectedCommunicators());
	}

	/**
	 * Ensures stopListening prevents any other connections from being
	 * established.
	 */
	@Test
	public void testStopListening()
	{
		testServerCommunicator.stopListening();
		verify(mockThread).interrupt();
	}

	/**
	 * Ensures stopListening throws an IllegalStateException if the server
	 * communicator has already stopped listening.
	 */
	@Test(expected = IllegalStateException.class)
	public void testStopListeningAlreadyStopped()
	{
		testServerCommunicator.stopListening();
		testServerCommunicator.stopListening();
	}

	/**
	 * Ensures the listening method works properly.
	 */
	@Test
	public void testListening()
	{
		assertTrue(testServerCommunicator.listening());
		testServerCommunicator.stopListening();
		assertFalse(testServerCommunicator.listening());
	}

	/**
	 * Ensures accept accepts an incoming connection.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testAccept() throws Exception
	{
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		assertTrue(testServerCommunicator.accept());
		verify(mockServerSocket).accept();
		PowerMockito.verifyNew(Communicator.class).withArguments(
				mockMessageFactory, mockSocket1);
	}

	/**
	 * Ensures accept returns false if the server socket throws an IOException
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testAcceptServerSocketThrowsIOException() throws Exception
	{
		when(mockServerSocket.accept()).thenThrow(new IOException());
		assertFalse(testServerCommunicator.accept());
	}

	/**
	 * Ensures accept returns false if the mutex throws an InterruptedException.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testAcceptSemaphoreThrowsInterruptedException()
			throws Exception
	{
		Semaphore mockSemaphore = mock(Semaphore.class);
		PowerMockito.whenNew(Semaphore.class).withArguments(1).thenReturn(
				mockSemaphore);
		doThrow(new InterruptedException()).when(mockSemaphore).acquire();
	}

	/**
	 * Ensures accept returns false if the communicator throws a
	 * CommunicationException.
	 * 
	 * @throws Exception
	 *             This shouldn't happen.
	 */
	@Test
	public void testAcceptCommunicatorThrowsCommunicationException()
			throws Exception
	{
		PowerMockito.whenNew(Communicator.class).withArguments(
				mockMessageFactory, mockSocket1).thenThrow(
				new CommunicationException());
	}

	/**
	 * Ensures sendUniqueMessageToEachConnectedCommunicator sends a unique
	 * message to each connected communicator.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test
	public void testSendUniqueMessagesToEachConnectedCommunicator()
			throws Exception
	{
		// connect the communicators
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket3);
		testServerCommunicator.accept();

		// run the test
		testServerCommunicator
				.sendUniqueMessageToEachConnectedCommunicator(mockMessageGenerator);

		verify(mockCommunicator1).sendMessage(mockMessage1);
		verify(mockCommunicator2).sendMessage(mockMessage2);
		verify(mockCommunicator3).sendMessage(mockMessage3);
	}

	/**
	 * Ensures sendUniqueMessageToEachConnectedCommunicator throws a
	 * NullPointerException when provided with a null message generator.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test(expected = NullPointerException.class)
	public void testSendUniqueMessagesToEachConnectedCommunicatorMessageGeneratorNull()
			throws Exception
	{
		// connect the communicators
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator
				.sendUniqueMessageToEachConnectedCommunicator(null);
	}

	/**
	 * Ensures sendUniqueMessageToEachConnectedCommunicator throws an
	 * IllegalArgumentException when the provided message generator returns
	 * null.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSendUniqueMessagesToEachConnectedCommunicatorMessageGeneratorReturnsNull()
			throws Exception
	{
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		// run the test
		when(mockMessageGenerator.generateMessage()).thenReturn(null);
		testServerCommunicator
				.sendUniqueMessageToEachConnectedCommunicator(mockMessageGenerator);
	}

	/**
	 * Ensures sendUniqueMessageToEachConnectedCommunicator throws a
	 * communication exception if any of the connected communicators throws an
	 * exception when sending the message, but still attempts to send the
	 * messages to the other connected communicators.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test
	public void testSendUniqueMessagesToEachConnectedCommunicatorConnectedCommunicatorThrowsExcpetion()
			throws Exception
	{
		// connect the communicators
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket3);
		testServerCommunicator.accept();

		// make the second communication throw an exception
		doThrow(new CommunicationException()).when(mockCommunicator2)
				.sendMessage(mockMessage2);

		// run the test
		boolean exceptionWasThrown = false;

		try
		{
			testServerCommunicator
					.sendUniqueMessageToEachConnectedCommunicator(mockMessageGenerator);
		} catch (CommunicationException e)
		{
			exceptionWasThrown = true;
		}

		assertTrue(exceptionWasThrown);
		verify(mockCommunicator1).sendMessage(mockMessage1);
		verify(mockCommunicator2).sendMessage(mockMessage2);
		verify(mockCommunicator3).sendMessage(mockMessage3);
	}

	/**
	 * Ensures the sendUniqueMessageToEachConnectedCommunicator sends the
	 * messages in the order in which the clients connected.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test
	public void testSendUniqueMessagesToEachConnectedCommunicatorSendsInOrder()
			throws Exception
	{
		// connect the communicators
		when(mockServerSocket.accept()).thenReturn(mockSocket3);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		// run the test
		testServerCommunicator
				.sendUniqueMessageToEachConnectedCommunicator(mockMessageGenerator);

		verify(mockCommunicator3).sendMessage(mockMessage1);
		verify(mockCommunicator2).sendMessage(mockMessage2);
		verify(mockCommunicator1).sendMessage(mockMessage3);
	}

	/**
	 * Ensures the sendUniqueMessageToEachConnectedCommunicator does not send a
	 * message to a communicator which has disconnected.
	 * 
	 * @throws Exception
	 *             This should not happen.
	 */
	@Test
	public void testSendUniqueMessagesToEachConnectedCommunicatorDoesntSendToDisconnectedCommunicator()
			throws Exception
	{
		// connect the communicators
		when(mockServerSocket.accept()).thenReturn(mockSocket1);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket2);
		testServerCommunicator.accept();

		when(mockServerSocket.accept()).thenReturn(mockSocket3);
		testServerCommunicator.accept();

		when(mockCommunicator2.connected()).thenReturn(false);

		// run the test
		testServerCommunicator
				.sendUniqueMessageToEachConnectedCommunicator(mockMessageGenerator);

		verify(mockCommunicator1).sendMessage(mockMessage1);
		verify(mockCommunicator2, never()).sendMessage(any(IMessage.class));
		verify(mockCommunicator3).sendMessage(mockMessage2);
	}
}
