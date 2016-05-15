package shared.model.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Implementation of IServerCommunicator.
 */
public class ServerCommunicator implements IServerCommunicator
{
	// the factory used for creating messages
	private final IMessageFactory messageFactory;
	
	// a server socket used for accepting incoming connections
	private ServerSocket serverSocket;
	
	// a list of connected communicators
	private final List<ICommunicator> communicators;
	
	// the thread which accepts for incoming connections
	private Thread acceptThread;
	
	// a mutex for the connected communicators
	private Semaphore communicatorsMutex;
	
	/**
	 * A runnable which, when run, will continuously incoming connections.  This class is intended to 
	 * be private and is only exposed for testing purposes.  THIS CLASS SHOULD NEVER BE USED DIRECTLY
	 * OUTSIDE OF THE SERVER COMMUNICATOR CLASS!
	 */
	protected class AcceptRunnable implements Runnable
	{
		/**
		 * {@inheritDoc}
		 */
		public void run()
		{
			// continuously accept incoming connections until an error occurs
			while (accept())
			{
				// do nothing
			}
		}
	}
	
	/**
	 * Creates a new instance of ServerCommunicator which automatically starts listening for incoming
	 * connections.
	 * @param messageFactory The message factory.
	 * @throws NullPointerException Thrown if messageFactory is null.
	 * @throws CommunicationException Thrown if an error occurs while communicating.
	 */
	public ServerCommunicator(IMessageFactory messageFactory) throws CommunicationException
	{
		if (messageFactory == null)
			throw new NullPointerException();
		
		this.messageFactory = messageFactory;
		
		try
		{
			// set up the communicators set
			communicators = new ArrayList<ICommunicator>();
			
			// set up the communicators semaphore
			communicatorsMutex = new Semaphore(1);
			
			// set up the server socket
			serverSocket = new ServerSocket(Communicator.DEFAULT_PORT);
			
			// start the accept thread
			acceptThread = new Thread(new AcceptRunnable());
			acceptThread.start();
			
		}
		catch (IOException exception)
		{
			throw new CommunicationException();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendMessages(IMessage message) throws CommunicationException
	{
		if (message == null)
			throw new NullPointerException();
		
		updateConnectedCommunicators();
		
		try
		{
			// acquire the communicators mutex lock
			communicatorsMutex.acquire();
			
			// send the message to each communicator
			for (ICommunicator communicator : communicators)
			{
				communicator.sendMessage(message);
			}
			
			// release the communicators mutex lock
			communicatorsMutex.release();
		}
		catch (InterruptedException exception)
		{
			// do nothing, because this shouldn't happen
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void sendUniqueMessageToEachConnectedCommunicator(IMessageGenerator messageGenerator)
		throws CommunicationException
	{
		if (messageGenerator == null)
			throw new NullPointerException();
		
		updateConnectedCommunicators();
		
		try
		{
			// acquire the communicators mutex lock
			communicatorsMutex.acquire();
			
			boolean shouldThrowException = false;
			
			// send the message to each communicator
			for (ICommunicator communicator : communicators)
			{
				IMessage message = messageGenerator.generateMessage();
				
				if (message == null)
					throw new IllegalArgumentException();
				
				try
				{
					communicator.sendMessage(message);
				}
				catch (CommunicationException e)
				{
					shouldThrowException = true;
				}
			}
			
			// release the communicators mutex lock
			communicatorsMutex.release();
			
			if (shouldThrowException == true)
				throw new CommunicationException("An error occurred while sending messages.");
		}
		catch (InterruptedException exception)
		{
			// do nothing, because this shouldn't happen
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IMessage[] receivedMessages() throws CommunicationException
	{
		updateConnectedCommunicators();
		
		List<IMessage> messages = new LinkedList<IMessage>();
		
		try
		{
			// acquire the communicators mutex lock
			communicatorsMutex.acquire();
			
			// add the received messages from all of the connected communicators to the messages list
			for (ICommunicator communicator : communicators)
				for (IMessage message : communicator.receivedMessages())
					messages.add(message);
			
			// release the communicators mutex lock
			communicatorsMutex.release();
		}
		catch (InterruptedException exception)
		{
			// do nothing, because this shouldn't happen
		}
		
		return messages.toArray(new IMessage[0]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int numberOfConnectedCommunicators()
	{
		updateConnectedCommunicators();
		
		try
		{
			// acquire the communicators mutex lock
			communicatorsMutex.acquire();
			
			int numberOfConnectedCommunicators = communicators.size();
			
			// release the communicators mutex lock
			communicatorsMutex.release();
			
			return numberOfConnectedCommunicators;
		}
		catch (InterruptedException exception)
		{
			// do nothing, because this shouldn't happen
			return 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IMessageFactory messageFactory()
	{
		return messageFactory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void stopListening()
	{
		if (!listening())
			throw new IllegalStateException();
		
		// kill the accept thread
		acceptThread.interrupt();
		
		// close the server socket
		try
		{
			serverSocket.close();
		}
		catch (IOException exception)
		{
			// do nothing
		}
		
		// nullify all of the values
		serverSocket = null;
		acceptThread = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean listening()
	{
		return serverSocket != null && !serverSocket.isClosed();
	}
	
	/**
	 * Private helper method which removes any disconnected communicators from the set of connected
	 * communicators.
	 */
	private void updateConnectedCommunicators()
	{
		try
		{
			// acquire the communicators mutex lock
			communicatorsMutex.acquire();
			
			// iterate through the communicators and remove them if they are not connected
			ICommunicator[] communicatorsArray = communicators.toArray(new ICommunicator[0]);
			
			for (ICommunicator communicator : communicatorsArray)
			{
				if (!communicator.connected())
					communicators.remove(communicator);
			}
			
			// release the communicators mutex lock
			communicatorsMutex.release();
		}
		catch (InterruptedException exception)
		{
			// do nothing, because this shouldn't happen
		}
	}
	
	/**
	 * Accepts an incoming connection, if the maximum amount of connections aren't already accepted.
	 * This method is intended to be private and is only exposed for testing purposes.  THIS METHOD
	 * SHOULD NEVER BE CALLED DIRECTLY OUTSIDE OF THE SERVER COMMUNICATOR CLASS!
	 * @return Returns true if no error occurs and false otherwise.
	 */
	protected boolean accept()
	{
		if (!listening())
			return false;
		
		try
		{
			Socket clientSocket = serverSocket.accept();
			ICommunicator communicator = new Communicator(messageFactory, clientSocket);
			
			// acquire the communicators mutex
			communicatorsMutex.acquire();
			
			communicators.add(communicator);
			
			// release the communicators mutex
			communicatorsMutex.release();
		}
		catch (IOException exception)
		{
			return false;
		}
		catch (CommunicationException exception)
		{
			return false;
		}
		catch (InterruptedException exception)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public void disconnect() throws CommunicationException
	{
		// acquire the communicators mutex lock
		try
		{
			communicatorsMutex.acquire();
		
		// iterate through the communicators and remove them if they are not connected
		ICommunicator[] communicatorsArray = communicators.toArray(new ICommunicator[0]);
		
		for (ICommunicator communicator : communicatorsArray)
		{
			communicators.remove(communicator);
			communicator.disconnect();
		}
		
		// release the communicators mutex lock
		communicatorsMutex.release();
		
		} catch (InterruptedException e)
		{
			// do nothing
		}
		
		try
		{
			serverSocket.close();
		} catch (IOException e)
		{
			throw new CommunicationException("Could not close socket.");
		}
	}
}
