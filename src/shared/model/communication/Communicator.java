package shared.model.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import shared.core.ArrayUtilities;

/**
 * Client implementation of ICommunicator.
 */
public class Communicator implements ICommunicator
{
	// the factory used to parse messages
	private IMessageFactory messageFactory;

	// the socket used for communicating with the client
	private Socket socket;

	// the socket output stream
	private PrintWriter socketOutputStream;

	// the socket input stream
	private BufferedReader socketInputStream;

	/*
	 * A queue for received messages. A thread-safe queue is not used because
	 * when retrieving received messages, the queue must first be copied to an
	 * array and then cleared, which presents a race condition if another thread
	 * is trying to add a message to the receivedMessageQueue.
	 */
	private Queue<IMessage> receivedMessagesQueue;

	/*
	 * A mutex for receivedMessagesQueue. The purpose of this mutex is to
	 * prevent multiple threads from modifying receivedMessagesQueue at the same
	 * time.
	 */
	private Semaphore receivedMessagesQueueMutex;

	/*
	 * A thread-safe queue for sending messages. The thread-safe queue is okay
	 * in this instance because messages are sent one at a time.
	 */
	private ConcurrentLinkedQueue<IMessage> sendMessagesQueue;

	/*
	 * A semaphore which indicates the sender thread may send a message. Every
	 * time a message is added to sentMessagesQueue, this semaphore should be
	 * signaled, and every time a message is removed from sentMessagesQueue,
	 * this semaphore should wait. This allows the send messages thread to
	 * operate without busy waiting.
	 */
	private Semaphore canSendMessagesSemaphore;
	
	/*
	 * A mutex for sendMessagesQueue.  The purpose of this mutex is to prevent multiple threads from
	 * modifying receivedMessagesQueue at the same time.
	 * 
	 */
	private Semaphore sendMessagesQueueSemaphore;

	// the send messages thread
	private Thread sendMessagesThread;

	// the receive messages thread
	private Thread receiveMessagesThread;
	
	// the address of the machine that this communicator is connected to
	private String connectedAddress;
	
	// the connected status of this communicator
	private boolean isConnected;

	/**
	 * A runnable which, when run, will continuously receive messages from a
	 * connected server. This class is intended to be private and is only
	 * exposed for testing purposes. THIS CLASS SHOULD NEVER BE USED OUTSIDE OF
	 * THE COMMUNICATOR CLASS!
	 */
	protected class ReceiveMessagesRunnable implements Runnable
	{
		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
			// continuously receive messages from the server until an error
			// occurs
			while (addMessageToReceivedMessagesQueue())
			{
				// do nothing but wait on an error
			}
		}
	}

	/**
	 * A runnable which, when run, will continuously send messages to a
	 * connected server. This class is intended to be private and is only
	 * exposed for testing purposes. THIS CLASS SHOULD NEVER BE USED OUTSIDE OF
	 * THE COMMUNICATOR CLASS!
	 */
	protected class SendMessagesRunnable implements Runnable
	{
		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
			// continuously send messages to the server until an error occurs
			while (sendMessage())
			{
				// do nothing but wait on an error
			}
		}
	}

	/**
	 * An error message for a communication error. This may be set by a separate
	 * thread, and should be thrown when calling a method which utilizes another
	 * thread (such as receivedMessages). If null, then no error has occurred.
	 */
	private String communicationErrorMessage;

	/**
	 * The port for the socket connection.
	 */
	public final static int DEFAULT_PORT = 45000;

	/**
	 * Creates a communicator connected to a server.
	 * 
	 * @param messageFactory
	 *            The factory used to parse received message strings.
	 * @param serverAddress
	 *            The address of a server to connect to.
	 * @throws NullPointerException
	 *             Thrown if messageFactory or serverAddress is null.
	 * @throws CommunicationException
	 *             Thrown if any error occurs when connecting to the server.
	 */
	public Communicator(IMessageFactory messageFactory, String serverAddress)
			throws CommunicationException
	{
		try
		{
			// create the socket and initialize this Communicator
			Socket connectedSocket = new Socket(serverAddress, DEFAULT_PORT);
			initialize(messageFactory, connectedSocket);
			connectedAddress = serverAddress;
		} catch (UnknownHostException exception)
		{
			throw new CommunicationException(
					"The provided server address could not be connected to.");
		} catch (IOException exception)
		{
			throw new CommunicationException(
					"An error occurred while communicating with the server.");
		}
	}

	/**
	 * Creates a communicator connected to a server.
	 * 
	 * @param messageFactory
	 *            The factory used to parse received message strings.
	 * @param socket
	 *            A socket which is already connected to a server.
	 * @throws NullPointerException
	 *             Thrown if messageFactory or socket is null.
	 * @throws IllegalArgumentException
	 *             Thrown if the provided socket is not connected to a server.
	 * @throws CommunicationException
	 *             Thrown if any error occurs when connecting to the server.
	 */
	protected Communicator(IMessageFactory messageFactory, Socket socket)
			throws CommunicationException
	{
		// initialize this Communicator
		initialize(messageFactory, socket);
		connectedAddress = socket.getInetAddress().getHostAddress();
	}

	/**
	 * {@inheritDoc}
	 */
	public void disconnect() throws CommunicationException
	{
		if (socket == null)
			throw new IllegalStateException();

		// Kill the send and receive messages threads.

		sendMessagesThread.interrupt();
		receiveMessagesThread.interrupt();

		try
		{
			socket.close();
		} catch (IOException exception)
		{
			throw new CommunicationException(
					"An error occurred while communicating with the server.");
		}

		// nullify the properties to prevent them from being used
		socket = null;
		socketOutputStream = null;
		socketInputStream = null;
		receivedMessagesQueue = null;
		sendMessagesQueue = null;
		receivedMessagesQueue = null;
		sendMessagesThread = null;
		receiveMessagesThread = null;
		isConnected = false;

		// set up the communication exception message in case anything else
		// tries to use the class
		communicationErrorMessage = "The connection has closed.";
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendMessage(IMessage message) throws CommunicationException
	{
		// check to see if an error has occurred
		if (communicationErrorMessage != null)
			throw new CommunicationException(communicationErrorMessage);

		if (message == null)
			throw new NullPointerException();
		
		try
		{
			// acquire the send messages queue lock
			sendMessagesQueueSemaphore.acquire();

			// add the message to the sent messages waiting queue
			sendMessagesQueue.add(message);
			
			// release the send messages queue lock
			sendMessagesQueueSemaphore.release();

			// increment the send messages semaphore to indicate a message can be sent
			canSendMessagesSemaphore.release();
		}
		catch (InterruptedException exception)
		{
			// do nothing
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public IMessage[] receivedMessages() throws CommunicationException
	{
		return receivedMessages(Integer.MAX_VALUE);
	}

	/**
	 * {@inheritDoc}
	 */
	public IMessage[] receivedMessages(int maximumNumberOfMessages)
			throws CommunicationException
	{
		if (maximumNumberOfMessages < 0)
			throw new IllegalArgumentException();

		// check to see if an error has occurred
		if (communicationErrorMessage != null)
			throw new CommunicationException(communicationErrorMessage);

		// acquire the mutex lock
		try
		{
			receivedMessagesQueueMutex.acquire();
		} catch (InterruptedException exception)
		{
			throw new CommunicationException(
					"An error occurred while communicating with the server.");
		}

		IMessage[] messages;

		if (receivedMessagesQueue.size() <= maximumNumberOfMessages)
		{
			messages = receivedMessagesQueue.toArray(new IMessage[0]);
			receivedMessagesQueue.clear();
		} else
		{
			// get the requested number of messages from the queue
			List<IMessage> messagesList = new LinkedList<IMessage>();

			for (int i = 0; i < maximumNumberOfMessages; i++)
				messagesList.add(receivedMessagesQueue.poll());

			messages = messagesList.toArray(new IMessage[0]);
		}

		// release the mutex lock
		receivedMessagesQueueMutex.release();

		return messages;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean connected()
	{
		return isConnected;
	}

	/**
	 * {@inheritDoc}
	 */
	public IMessageFactory messageFactory()
	{
		return messageFactory;
	}

	/**
	 * A private helper method which initializes this Communicator. This logic
	 * would normally be stored in a constructor, but in order for the
	 * constructor to create a socket and catch any exceptions, this code must
	 * instead be moved to a helper method.
	 * 
	 * @param theMessageFactory
	 *            The message factory.
	 * @param theSocket
	 *            The socket.
	 * @throws NullPointerException
	 *             Thrown if messageFactory or socket are null.
	 * @throws IllegalArgumentException
	 *             Thrown if the provided socket is not connected to a server.
	 * @throws CommunicationException
	 *             Thrown if any error occurs while communicating over the
	 *             provided socket.
	 */
	private void initialize(IMessageFactory theMessageFactory, Socket theSocket)
			throws CommunicationException
	{
		if (theMessageFactory == null || theSocket == null)
			throw new NullPointerException();

		if (!theSocket.isConnected())
			throw new IllegalArgumentException();

		this.messageFactory = theMessageFactory;
		this.socket = theSocket;

		// set up the reader and writer
		try
		{
			socketOutputStream = new PrintWriter(theSocket.getOutputStream(), true);
			socketInputStream = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
		} 
		catch (IOException exception)
		{
			throw new CommunicationException(
					"An error occurred when communicating with the server.");
		}
		
		isConnected = true;

		// set up the message queues
		receivedMessagesQueue = new LinkedList<IMessage>();
		sendMessagesQueue = new ConcurrentLinkedQueue<IMessage>();

		// set up the semaphores
		receivedMessagesQueueMutex = new Semaphore(1);
		canSendMessagesSemaphore = new Semaphore(0);
		sendMessagesQueueSemaphore = new Semaphore(1);

		// set up the threads
		sendMessagesThread = new Thread(new SendMessagesRunnable());
		receiveMessagesThread = new Thread(new ReceiveMessagesRunnable());
		sendMessagesThread.start();
		receiveMessagesThread.start();
	}

	/**
	 * A helper method which receives a message from the server. THIS METHOD
	 * SHOULD NOT BE CALLED DIRECTLY WHEN USING THE COMMUNICATOR. It is only
	 * exposed for testing purposes. This method will block until a message is
	 * received.
	 * 
	 * @return Returns true if the message was received without error and false
	 *         otherwise.
	 */
	protected boolean addMessageToReceivedMessagesQueue()
	{

		try
		{
			// read and parse the message
			String stringMessage = socketInputStream.readLine();
			
			// If the message is null, throw IllegalArgumentException.
			if (stringMessage == null)
				return false;
			
			IMessage message = messageFactory.createMessage(stringMessage);
			
			if (message instanceof HelloMessage) {
				// throw away
				return true;
			}

			// acquire the mutex
			receivedMessagesQueueMutex.acquire();

			// add the messages to the message queue so it can be read by receivedMessages
			receivedMessagesQueue.add(message);

			// release the mutex
			receivedMessagesQueueMutex.release();

			return true;
		} 
		catch (IllegalArgumentException exception)
		{
			// an error occurred when trying to parse the received message
			communicationErrorMessage = "An error occurred when communicating.";
			return false;
		} 
		catch (InterruptedException exception)
		{
			// quit working when interrupted
			return false;
		}
		catch (IOException exception)
		{
			// the buffered reader encountered a problem
			communicationErrorMessage = "An error occurred when communicating.";
			return false;
		}
	}

	/**
	 * A helper method which sends a message to the server. THIS METHOD SHOULD
	 * NOT BE CALLED DIRECTLY WHEN USING THE COMMUNICATOR. It is only exposed
	 * for testing purposes. This method will block until a message is placed on
	 * sentMessagesQueue and the message is sent. This method sends multiple messages separated
	 * by newlines if multiple message in the message queue.
	 * 
	 * @return Returns true if the message was sent without error and false
	 *         otherwise.
	 */
	protected boolean sendMessage()
	{
		try
		{
			// wait for at least one message to be sent
			canSendMessagesSemaphore.acquire();
			
			// acquire the mutex lock for the sendMessagesQueue
			sendMessagesQueueSemaphore.acquire();
			
			// determine the number of messages to send
			int numberOfMessages = sendMessagesQueue.size();
			
			// release the mutex lock for the sendMessagesQueue
			sendMessagesQueueSemaphore.release();
			
			// acquire the canSendMessagesSemaphore for the rest of the messages to send
			for (int i = 0; i < numberOfMessages - 1; i++)
				canSendMessagesSemaphore.acquire();
			
			// acquire the send messages queue lock
			sendMessagesQueueSemaphore.acquire();

			// get numberOfMessages messages from the sendMessagesQueue
			String[] messages = new String[numberOfMessages];
			for (int i = 0; i < numberOfMessages; i++)
				messages[i] = sendMessagesQueue.poll().toString();
			
			// release the send messages queue lock
			sendMessagesQueueSemaphore.release();
			
			// create a really big message string
			String concatenatedMessageString = ArrayUtilities.join(messages, "\n");

			// send the message
			socketOutputStream.println(concatenatedMessageString);
			
			if (socketOutputStream.checkError()) {
				isConnected = false;
			}

			// indicate a success
			return true;
		} 
		catch (InterruptedException exception)
		{
			// quit working when interrupted
			communicationErrorMessage = "An error occurred when communicating with the server.";
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String connectedAddress() {
		return connectedAddress;
	}
}
