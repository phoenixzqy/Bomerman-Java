package shared.model.communication;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests Communicator as well as it can be tested.  The concurrency portions of communicator really
 * can't be tested in any practical manner.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Communicator.class)
public class CommunicatorTest
{	
	
	// a mock message factory
	private IMessageFactory mockMessageFactory;
	
	// a communicator used for testing
	private Communicator communicator;
	
	// the server address used for testing
	private String testServerAddress;
	
	// a socket mocked for communication
	private Socket mockSocket;
	
	// a mock input stream
	private InputStream mockInputStream;
	
	// a mock output stream
	private OutputStream mockOutputStream;
	
	// a mock thread for receiving messages
	private Thread mockReceiveMessagesThread;
	
	// a mock thread for sending messages
	private Thread mockSendMessagesThread;
	
	/**
	 * Sets up the tests.
	 * @throws Exception This shouldn't happen.
	 */
	@Before
	public void setUp() throws Exception
	{
		mockMessageFactory = mock(IMessageFactory.class);
		testServerAddress = "test server address";
		mockInputStream = mock(InputStream.class);
		mockOutputStream = mock(OutputStream.class);
		
		// mock the socket
		mockSocket = mock(Socket.class);
		PowerMockito.whenNew(Socket.class).withArguments(testServerAddress, Communicator.DEFAULT_PORT)
			.thenReturn(mockSocket);
		when(mockSocket.isConnected()).thenReturn(true);
		when(mockSocket.getInputStream()).thenReturn(mockInputStream);
		when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
		
		// mock the test runnable classes so thread can be mocked
		Communicator.ReceiveMessagesRunnable mockReceiveMessagesRunnable = 
				mock(Communicator.ReceiveMessagesRunnable.class);
		PowerMockito.whenNew(Communicator.ReceiveMessagesRunnable.class).withNoArguments()
				.thenReturn(mockReceiveMessagesRunnable);

		Communicator.SendMessagesRunnable mockSendMessagesRunnable = 
				mock(Communicator.SendMessagesRunnable.class);
		PowerMockito.whenNew(Communicator.SendMessagesRunnable.class).withNoArguments()
				.thenReturn(mockSendMessagesRunnable);
		
		// mock thread so background threads won't mess up tests
		mockReceiveMessagesThread = mock(Thread.class);
		PowerMockito.whenNew(Thread.class.getConstructor(Runnable.class))
			.withArguments(mockReceiveMessagesRunnable)
			.thenReturn(mockReceiveMessagesThread);
		
		mockSendMessagesThread = mock(Thread.class);
		PowerMockito.whenNew(Thread.class.getConstructor(Runnable.class))
			.withArguments(mockSendMessagesRunnable)
			.thenReturn(mockSendMessagesThread);
		
		// set up the test communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when provided with a null message 
	 * factory.
	 * @throws CommunicationException This should not happen.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorMessageFactoryNull() throws CommunicationException
	{
		new Communicator(null, testServerAddress);
	}
	
	/**
	 * Ensures the constructor throws a NullPointerException when provided with a null server address.
	 * @throws CommunicationException This should not happen.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorServerAddressNull() throws CommunicationException
	{
		new Communicator(mockMessageFactory, (String) null);
	}
	
	/**
	 * Ensures the constructor establishes a connection with the server.
	 * @throws Exception This should never happen.
	 */
	@Test
	public void testConstructorEstablishesConnection() throws Exception
	{
		PowerMockito.verifyNew(Socket.class)
			.withArguments(testServerAddress, Communicator.DEFAULT_PORT);
	}
	
	/**
	 * Tests connectedAddress() method returns host address
	 */
	@Test
	public void testConnectedAddress() {
		assertEquals(testServerAddress, communicator.connectedAddress());
	}
	
	/**
	 * Tests connectedAddress() method returns host address after communicator is disconnected
	 * @throws CommunicationException This shouldn't happen.
	 */
	@Test
	public void testConnectedAddressAfterDisconnect() throws CommunicationException {
		communicator.disconnect();
		assertEquals(testServerAddress, communicator.connectedAddress());
	}
	
	
	/**
	 * Ensures the constructor throws a CommunicationException if the socket throws an
	 * UnknownHostException while attempting to connect.
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testConstructorSocketThrowsUnknownHostException() throws Exception
	{
		PowerMockito.whenNew(Socket.class.getConstructor(String.class, int.class))
			.withArguments(anyString(), anyInt())
			.thenThrow(new UnknownHostException());
		
		new Communicator(mockMessageFactory, testServerAddress);
	}
	
	/**
	 * Ensures the constructor throws a CommunicationException if the socket throws an IOException
	 * while attempting to connect to the server.
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testConstructorSocketThrowsIOException() throws Exception
	{
		PowerMockito.whenNew(Socket.class.getConstructor(String.class, int.class))
		.withArguments(anyString(), anyInt())
		.thenThrow(new IOException());
	
		new Communicator(mockMessageFactory, testServerAddress);
	}
	
	/**
	 * Ensures the constructor starts the send messages thread.
	 */
	@Test
	public void testConstructorStartsSendMessagesThread()
	{
		verify(mockSendMessagesThread).start();
	}
	
	/**
	 * Ensures the constructor starts the receive messages thread.
	 */
	@Test
	public void testConstructorStartsReceiveMessagesThread()
	{
		verify(mockReceiveMessagesThread).start();
	}
	
	/**
	 * Ensures the constructor sets the message factory to the provided message factory.
	 * @throws CommunicationException This should never happen.
	 */
	@Test
	public void testConstructorMessageFactory() throws CommunicationException
	{
		Communicator testCommunicator = new Communicator(mockMessageFactory, testServerAddress);
		assertEquals(mockMessageFactory, testCommunicator.messageFactory());
	}
	
	/**
	 * Ensures the communicator throws a CommunicationException when receivedMessages is called
	 * and the communicator is not connected to a server.
	 * @throws CommunicationException This exception should be thrown.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesNotConnected() throws CommunicationException
	{
		when(mockSocket.isConnected()).thenReturn(false);
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages();
	}
	
	/**
	 * Ensures when the communicator receives a message, it parses the message using the message
	 * factory and then returns it when receivedMessages is called.
	 * @throws Exception This should not be thrown.
	 */
	@Test
	public void testReceivedMessages() throws Exception
	{
		// mock the returned message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message")).thenReturn(mockMessage);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("Test Message");
		
		// run the test
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		communicator.addMessageToReceivedMessagesQueue();
		IMessage[] messages = communicator.receivedMessages();
		verify(mockMessageFactory.createMessage("Test Message"));
		assertArrayEquals(new IMessage[] { mockMessage }, messages);
	}
	
	/**
	 * Ensures when the communicator receives a message, it parses the message using the message
	 * factory and throws it away if it is a ConnectionStatusMessage.
	 * @throws Exception This should not be thrown.
	 */
	@Test
	public void testReceivedMessagesConnectionStatusMessage() throws Exception
	{
		// mock the returned message
		IMessage mockMessage = mock(HelloMessage.class);
		when(mockMessageFactory.createMessage("STATUS")).thenReturn(mockMessage);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("STATUS");
		
		// run the test
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		communicator.addMessageToReceivedMessagesQueue();
		IMessage[] messages = communicator.receivedMessages();
		verify(mockMessageFactory.createMessage("STATUS"));
		assertArrayEquals(new IMessage[] { }, messages);
	}
	
	/**
	 * Ensures receivedMessages will parse multiple messages separated by newline characters.
	 * @throws Exception This should not happen.
	 */
	@Test
	public void testReceivedMessagesMultipleMessages() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);

		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();

		verify(mockMessageFactory).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		IMessage[] messages = communicator.receivedMessages();
		assertArrayEquals(new IMessage[] { mockMessage1, mockMessage2 }, messages);
	}
	
	/**
	 * Ensures receivedMessages will return an empty array when no messages have been received.
	 * @throws CommunicationException This should never happen.
	 */
	@Test
	public void testReceivedMessagesNoReceivedMessages() throws CommunicationException
	{
		assertArrayEquals(new IMessage[0], communicator.receivedMessages());
	}
	
	/**
	 * Ensures receivedMessages will only return the messages received since the last call to 
	 * receivedMessages.
	 * @throws Exception This should not happen 
	 */
	@Test
	public void testReceivedMessagesOnlyReceivesCurrentMessages() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages();
		
		// mock the buffered reader again
		reset(mockMessageFactory);
		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		
		// verify createMessage is only called with the second message
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();

		verify(mockMessageFactory, never()).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		IMessage[] messages = communicator.receivedMessages();
		assertArrayEquals(new IMessage[] { mockMessage2 }, messages);
	}
	
	/**
	 * Ensures receivedMessages throws a CommunicationException if the buffered reader throws
	 * an IOException when reading the message. 
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesBufferedReaderThrowsIOException() throws Exception
	{
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenThrow(new IOException());
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// attempt to read a message
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages();
	}
	
	/**
	 * Ensures receivedMessages throws a CommunicationException if the message factory throws an
	 * IllegalArgumentException.
	 * @throws CommunicationException This should be thrown.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesMessageFactoryThrowsIllegalArgumentException() throws 
		CommunicationException
	{
		when(mockMessageFactory.createMessage(anyString())).thenThrow(new IllegalArgumentException());
		
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages();
	}
	
	/**
	 * Ensures receivedMessages throws a CommunicationException if the semaphore throws an
	 * InterruptedException.
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesSemaphoreThrowsInterruptedException() throws Exception
	{
		// mock the semaphore
		Semaphore mockSemaphore = mock(Semaphore.class);
		PowerMockito.whenNew(Semaphore.class.getConstructor(int.class))
			.withArguments(anyInt())
			.thenReturn(mockSemaphore);
		doThrow(new InterruptedException()).when(mockSemaphore).acquire();
			
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
			
		// attempt to send the message
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages();
	}
	
	/**
	 * Ensures the communicator throws a CommunicationException when receivedMessages(int) is called
	 * and the communicator is not connected to a server.
	 * @throws CommunicationException This exception should be thrown.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesMaxNotConnected() throws CommunicationException
	{
		when(mockSocket.isConnected()).thenReturn(false);
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages(10);
	}
	
	/**
	 * Ensures when the communicator receives a message, it parses the message using the message
	 * factory and then returns it when receivedMessages(int) is called.
	 * @throws Exception This should not be thrown.
	 */
	@Test
	public void testReceivedMessagesMax() throws Exception
	{
		// mock the returned message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message")).thenReturn(mockMessage);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("Test Message");
		
		// run the test
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		communicator.addMessageToReceivedMessagesQueue();
		IMessage[] messages = communicator.receivedMessages(1);
		verify(mockMessageFactory.createMessage("Test Message"));
		assertArrayEquals(new IMessage[] { mockMessage }, messages);
	}
	
	/**
	 * Ensures receivedMessages(int) throws an exception when provided with a negative number of
	 * messages.
	 * @throws Exception This should be an IllegalArgumentException.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testReceivedMessagesMaxNegativeMaxThrowsException() throws Exception
	{
		communicator.receivedMessages(-1);
	}
	
	/**
	 * Ensures receivedMessages(int) will parse multiple messages separated by newline characters.
	 * @throws Exception This should not happen.
	 */
	@Test
	public void testReceivedMessagesMaxMultipleMessages() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);

		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();

		verify(mockMessageFactory).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		IMessage[] messages = communicator.receivedMessages(2);
		assertArrayEquals(new IMessage[] { mockMessage1, mockMessage2 }, messages);
	}
	
	/**
	 * Ensures receivedMessages(int) will return all of the received messages when the number of
	 * received messages is less than the provided maximum number of messages.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testReceivedMessagesMaxMultipleMessagesLessThanMax() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);

		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();
		
		verify(mockMessageFactory).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		IMessage[] messages = communicator.receivedMessages(3);
		assertArrayEquals(new IMessage[] { mockMessage1, mockMessage2 }, messages);
	}
	
	/**
	 * Ensures receivedMessages(int) will return will return only the maximum number of messages when
	 * the number of received messages is greater than the provided maximum number of messages.  This
	 * test also ensures the rest of the messages will not be removed from the received messages 
	 * queue.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testReceivedMessagesMaxMultipleMessagesGreaterThanMax() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		IMessage mockMessage3 = mock(GameObjectDestroyedMessage.class);
		IMessage mockMessage4 = mock(KeyMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		when(mockMessageFactory.createMessage("Test Message 3")).thenReturn(mockMessage3);
		when(mockMessageFactory.createMessage("Test Message 4")).thenReturn(mockMessage4);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);

		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 3");
		communicator.addMessageToReceivedMessagesQueue();
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 4");
		communicator.addMessageToReceivedMessagesQueue();

		verify(mockMessageFactory).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		verify(mockMessageFactory).createMessage("Test Message 3");
		verify(mockMessageFactory).createMessage("Test Message 4");
		
		IMessage[] messages = communicator.receivedMessages(2);
		assertArrayEquals(new IMessage[] { mockMessage1, mockMessage2 }, messages);
		messages = communicator.receivedMessages();
		assertArrayEquals(new IMessage[] { mockMessage3, mockMessage4 }, messages);
	}
	
	/**
	 * Ensures receivedMessages(int) will return an empty array when no messages have been received.
	 * @throws CommunicationException This should never happen.
	 */
	@Test
	public void testReceivedMessagesMaxNoReceivedMessages() throws CommunicationException
	{
		assertArrayEquals(new IMessage[0], communicator.receivedMessages(1));
	}
	
	/**
	 * Ensures receivedMessages(int) will only return the messages received since the last call to 
	 * receivedMessages.
	 * @throws Exception This should not happen 
	 */
	@Test
	public void testReceivedMessagesMaxOnlyReceivesCurrentMessages() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages(1);
		
		// mock the buffered reader again
		reset(mockMessageFactory);
		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		
		// verify createMessage is only called with the second message
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();

		verify(mockMessageFactory, never()).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		IMessage[] messages = communicator.receivedMessages(1);
		assertArrayEquals(new IMessage[] { mockMessage2 }, messages);
	}
	
	/**
	 * Ensures receivedMessages(int) will not remove any messages from the message queue when
	 * when 0 is provided an an argument.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testReceivedMessagesMaxZeroMax() throws Exception
	{
		// mock the returned message
		IMessage mockMessage1 = mock(GameObjectCreatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 1")).thenReturn(mockMessage1);

		IMessage mockMessage2 = mock(GameObjectUpdatedMessage.class);
		when(mockMessageFactory.createMessage("Test Message 2")).thenReturn(mockMessage2);
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// receive the messages
		when(mockBufferedReader.readLine()).thenReturn("Test Message 1");
		communicator.addMessageToReceivedMessagesQueue();
		when(mockBufferedReader.readLine()).thenReturn("Test Message 2");
		communicator.addMessageToReceivedMessagesQueue();
		
		// verify receivedMessages(0) does what it's supposed to do
		IMessage[] messages = communicator.receivedMessages(0);
		assertArrayEquals(new IMessage[0], messages);
		verify(mockMessageFactory).createMessage("Test Message 1");
		verify(mockMessageFactory).createMessage("Test Message 2");
		
		// mock the buffered reader again
		reset(mockMessageFactory);

		messages = communicator.receivedMessages(2);
		verify(mockMessageFactory, never()).createMessage("Test Message 1");
		verify(mockMessageFactory, never()).createMessage("Test Message 2");
		assertArrayEquals(new IMessage[] { mockMessage1, mockMessage2 }, messages);
	}
	
	/**
	 * Ensures receivedMessages(int) throws a CommunicationException if the buffered reader throws
	 * an IOException when reading the message. 
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesMaxBufferedReaderThrowsIOException() throws Exception
	{
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(InputStreamReader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenThrow(new IOException());
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// attempt to read a message
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages(1);
	}
	
	/**
	 * Ensures receivedMessages throws a CommunicationException if the message factory throws an
	 * IllegalArgumentException.
	 * @throws CommunicationException This should be thrown.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesMaxMessageFactoryThrowsIllegalArgumentException() throws 
		CommunicationException
	{
		when(mockMessageFactory.createMessage(anyString())).thenThrow(new IllegalArgumentException());
		
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages(1);
	}
	
	/**
	 * Ensures receivedMessages throws a CommunicationException if the semaphore throws an
	 * InterruptedException.
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testReceivedMessagesMaxSemaphoreThrowsInterruptedException() throws Exception
	{
		// mock the semaphore
		Semaphore mockSemaphore = mock(Semaphore.class);
		PowerMockito.whenNew(Semaphore.class.getConstructor(int.class))
			.withArguments(anyInt())
			.thenReturn(mockSemaphore);
		doThrow(new InterruptedException()).when(mockSemaphore).acquire();
			
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
			
		// attempt to send the message
		communicator.addMessageToReceivedMessagesQueue();
		communicator.receivedMessages(1);
	}
	
	/**
	 * Ensures disconnect disconnects the communicator from the server.
	 * @throws CommunicationException This should not happen.
	 */
	@Test
	public void testDisconnect() throws CommunicationException
	{
		communicator.disconnect();
		assertEquals(false, communicator.connected());
	}
	
	/**
	 * Ensures disconnect throws a CommunicationException if the connected socket throws an 
	 * IOExcpetion.
	 * @throws Exception This should be a CommunicationException.
	 */
	@Test(expected=CommunicationException.class)
	public void testDiconnectedSocketIOException() throws Exception
	{
		doThrow(new IOException()).when(mockSocket).close();
		communicator.disconnect();
	}
	
	/**
	 * Ensures disconnect throws an IllegalStateException if the communicator is already 
	 * disconnected.
	 * @throws CommunicationException This should not be thrown.
	 */
	@Test(expected=IllegalStateException.class)
	public void testDisconnectAlreadyDisconnected() throws CommunicationException
	{
		communicator.disconnect();
		communicator.disconnect();
	}
	
	/**
	 * Ensures sendMessage sends the message to the server.
	 * @throws Exception This should not happen.
	 */
	@Test
	public void testSendMessage() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessage.toString()).thenReturn("Test Message String");
		
		// mock the print writer
		PrintWriter mockPrintWriter = mock(PrintWriter.class);
		PowerMockito.whenNew(PrintWriter.class.getConstructor(OutputStream.class, boolean.class))
			.withArguments(any(OutputStream.class), anyBoolean())
			.thenReturn(mockPrintWriter);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// send the message
		communicator.sendMessage(mockMessage);
		communicator.sendMessage();
		
		verify(mockPrintWriter).println("Test Message String");
	}
	
	/**
	 * Ensures sendMessage throws a NullPointerException when the provided message is null.
	 * @throws CommunicationException This should not happen.
	 */
	@Test(expected=NullPointerException.class)
	public void testSendMessageMessageNull() throws CommunicationException
	{
		communicator.sendMessage(null);
	}
	
	/**
	 * Ensures sendMessage throws a CommunicationException on the next call if an error occurs while
	 * sending a message.
	 * @throws Exception This should never happen.
	 */
	@Test(expected=CommunicationException.class)
	public void testSendMessageError() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(IMessage.class);
		
		// mock the semaphore
		Semaphore mockSemaphore = mock(Semaphore.class);
		PowerMockito.whenNew(Semaphore.class.getConstructor(int.class))
			.withArguments(anyInt())
			.thenReturn(mockSemaphore);
		doThrow(new InterruptedException()).when(mockSemaphore).acquire();
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// attempt to send the message
		communicator.sendMessage(mockMessage);
		communicator.sendMessage();
		communicator.sendMessage(mockMessage);
	}
	
	/**
	 * Ensures addMessageToReceivedMessagesQueue method adds the message to the received message 
	 * queue.
	 * @throws Exception This shouldn't happen. 
	 */
	@Test
	public void testAddMessageToReceivedMessagesQueue() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessage.toString()).thenReturn("Test Message String");
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(Reader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("Test Read Line");
		
		when(mockMessageFactory.createMessage(anyString())).thenReturn(mockMessage);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// receive the message
		assertTrue(communicator.addMessageToReceivedMessagesQueue());
		
		verify(mockMessageFactory.createMessage("Test Read Line"));
		assertArrayEquals(new IMessage[] { mockMessage }, communicator.receivedMessages());
	}
	
	/**
	 * Ensures ddMessageToReceivedMessagesQueue method throws away a HelloMessage.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testAddMessageToReceivedMessageQueueHelloMessage() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(HelloMessage.class);
		when(mockMessage.toString()).thenReturn("Test Message String");
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(Reader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("Test Read Line");
		
		when(mockMessageFactory.createMessage(anyString())).thenReturn(mockMessage);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// receive the message
		assertTrue(communicator.addMessageToReceivedMessagesQueue());
		
		verify(mockMessageFactory.createMessage("Test Read Line"));
		assertArrayEquals(new IMessage[0], communicator.receivedMessages());
	}
	
	/**
	 * Ensures addMessageToReceivedMessageQueue method returns false if an IllegalArgumentException
	 * occurs.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testAddMessageToReceivedMessageQueueIllegalArgumentException() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessage.toString()).thenReturn("Test Message String");
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(Reader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("Test Read Line");
		
		when(mockMessageFactory.createMessage(anyString())).thenThrow(new IllegalArgumentException());
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// receive the message
		assertFalse(communicator.addMessageToReceivedMessagesQueue());
	}
	
	/**
	 * Ensures addMessageToReceivedMessageQueue method returns false if an InterruptedException
	 * occurs.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testAddMessageToReceivedMessageQueueInterruptedException() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessage.toString()).thenReturn("Test Message String");
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(Reader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenReturn("Test Read Line");
		
		when(mockMessageFactory.createMessage(anyString())).thenReturn(mockMessage);
		
		Semaphore mockSemaphore = mock(Semaphore.class);
		PowerMockito.whenNew(Semaphore.class.getConstructor(int.class))
			.withArguments(anyInt())
			.thenReturn(mockSemaphore);
		doThrow(new InterruptedException()).when(mockSemaphore).acquire();
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// receive the message
		assertFalse(communicator.addMessageToReceivedMessagesQueue());
	}
	
	/**
	 * Ensures addMessageToReceivedMessageQueue method returns false if an IOException occurs.
	 * @throws Exception This shouldn't happen.
	 */
	@Test
	public void testAddMessageToReceivedMessageQueueIOException() throws Exception
	{
		// mock the message
		IMessage mockMessage = mock(GameObjectCreatedMessage.class);
		when(mockMessage.toString()).thenReturn("Test Message String");
		
		// mock the buffered reader
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		PowerMockito.whenNew(BufferedReader.class.getConstructor(Reader.class))
			.withArguments(any(Reader.class))
			.thenReturn(mockBufferedReader);
		when(mockBufferedReader.readLine()).thenThrow(new IOException());
		
		when(mockMessageFactory.createMessage(anyString())).thenReturn(mockMessage);
		
		// recreate the communicator
		communicator = new Communicator(mockMessageFactory, testServerAddress);
		
		// receive the message
		assertFalse(communicator.addMessageToReceivedMessagesQueue());
	}
}
