package shared.model.communication;

import shared.model.GameObjectType;
import shared.model.Key;
import shared.model.KeyAction;
import shared.model.communication.GameMessage.Action;

/**
 * Implementation of IMessageFactory.
 * 
 */
public class MessageFactory implements IMessageFactory
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMessage createMessage(String messageString)
	{
		if (messageString == null)
		{
			throw new NullPointerException("createMessage cannot be invoked with a null messageString.");
		}

		if (messageString == "")
		{
			throw new IllegalArgumentException("createMessage cannot be invoked with an empty string.");
		}

		// split the message string with space character as a delimeter
		String[] substrings = messageString.split(" ");

		// messages cannot have zero arguments
		if (substrings.length == 0)
		{
			throw new IllegalArgumentException(
					"createMessage cannot be invoked with a string argument that is all white space.");
		}

		// use the first argument to determine what type of message to create and return
		if (substrings[0].equals("GAME_OBJECT_CREATED"))
		{
			return createGameObjectCreatedMessage(substrings);

		} else if (substrings[0].equals("GAME_OBJECT_DESTROYED"))
		{
			return createGameObjectDestroyedMessage(substrings);

		} else if (substrings[0].equals("SCORE_UPDATED"))
		{
			return createScoreUpdatedMessage(substrings);

		} else if (substrings[0].equals("GAME_OBJECT_POSITION_UPDATED"))
		{
			return createGameObjectUpdatedMessage(substrings);

		} else if (substrings[0].equals("KEY"))
		{
			return createKeyMessage(substrings);
			
		} else if (substrings[0].equals("GAME"))
		{
			return createGameMessage(substrings);
			
		} else if (substrings[0].equals("PLAYER_GAME_OBJECT_IDENTIFIER"))
		{
			return createPlayerGameObjectIdentifierMessage(substrings);
		} else if (substrings[0].equals("STATUS"))
		{
			return createConnectionStatusMessage(substrings);
		} else if (substrings[0].equals("GAME_TIME")) {
			return createGameTimeMessage(substrings);
		} else
		{
			// message type argument invalid
			throw new IllegalArgumentException(
					"messageString is of improper format.  The first word must be GAME_OBJECT_CREATED, GAME_OBJECT_DESTROYED, "
							+ "GAME_OBJECT_SCORE_UPDATED, or GAME_OBJECT_POSITION_UPDATED");
		}

	}

	/**
	 * Creates a GameTimeMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a
	 *            GameTImeMessage.
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new GameTimeMessage, created from the
	 *         arguments in substrings
	 */
	private IMessage createGameTimeMessage(String[] substrings)
	{
		// message string must have two arguments
		if (substrings.length != 2)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode time argument
		int time;
		try
		{
			time = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's time could not be decoded.");
		}

		return new GameTimeMessage(time);
	}

	/**
	 * Creates a ConnectionStatusMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a
	 *            ConnectionStatusMessage.
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new ConnectionStatusMessage, created from the
	 *         arguments in substrings
	 */
	private IMessage createConnectionStatusMessage(String[] substrings)
	{
		// message string must have one argument
		if (substrings.length != 1) {
			throw new IllegalArgumentException(
			"Incorrect number of arguments in messageString.");
		}
		
		return new HelloMessage();
	}

	/**
	 * Creates a PlayerGameObjectIdentifierMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a
	 *            PlayerGameObjectIdentifierMessage.
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new PlayerGameObjectIdentifierMessage, created from the
	 *         arguments in substrings
	 */
	private IMessage createPlayerGameObjectIdentifierMessage(String[] substrings)
	{
		// message must have two arguments
		if (substrings.length != 2)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode game object identifier argument
		int gameObjectId;
		try
		{
			gameObjectId = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's game object identifier could not be decoded.");
		}

		return new PlayerGameObjectIdentifierMessage(gameObjectId);
	}

	/**
	 * Creates a GameObjectScoreUpdatedMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a
	 *            GameObjectScoreUpdatedMessage
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new GameObjectScoreUpdatedMessage, created from the arguments
	 *         in substrings
	 */
	private ScoreUpdatedMessage createScoreUpdatedMessage(String[] substrings)
	{
		// message must have 3 arguments
		if (substrings.length != 3)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode game object identifier argument
		int gameObjectId;
		try
		{
			gameObjectId = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's game object identifier could not be decoded.");
		}

		// decode score argument
		int score;
		try
		{
			score = Integer.decode(substrings[2]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's score could not be decoded.");
		}

		return new ScoreUpdatedMessage(gameObjectId, score);
	}

	/**
	 * Creates a GameObjectDestroyedMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a GameObjectDestroyedMessage
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new GameObjectDestroyedMessage, created from the arguments in
	 *         substrings
	 */
	private GameObjectDestroyedMessage createGameObjectDestroyedMessage(
			String[] substrings)
	{
		// message must have two arguments
		if (substrings.length != 2)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode game object identifier argument
		int gameObjectId;
		try
		{
			gameObjectId = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's game object identifier could not be decoded.");
		}

		return new GameObjectDestroyedMessage(gameObjectId);
	}

	/**
	 * Creates a GameObjectCreatedMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a GameObjectCreatedMessage
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new GameObjectCreatedMessage, created from the arguments in
	 *         substrings
	 */
	private GameObjectCreatedMessage createGameObjectCreatedMessage(
			String[] substrings)
	{
		// message must have 5 arguments
		if (substrings.length != 5)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode game object identifier argument
		int gameObjectId;
		try
		{
			gameObjectId = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's game object identifier could not be decoded.");
		}

		// decode game object type argument
		GameObjectType gameObjectType;
		try
		{
			gameObjectType = GameObjectType.valueOf(substrings[2]);
		} catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(
					"messageString's GameObjectType could not be decoded.");
		}

		// decode the row argument
		int row;
		try
		{
			row = Integer.decode(substrings[3]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's row could not be decoded.");
		}

		// decode the column argument
		int column;
		try
		{
			column = Integer.decode(substrings[4]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's column could not be decoded.");
		}

		return new GameObjectCreatedMessage(gameObjectId, gameObjectType, row,
				column);

	}

	/**
	 * Creates a GameObjectUpdatedMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a GameObjectUpdatedMessage
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new GameObjectUpdatedMessage, created from the arguments in
	 *         substrings
	 */
	private GameObjectUpdatedMessage createGameObjectUpdatedMessage(
			String[] substrings)
	{
		// message must have 4 arguments
		if (substrings.length != 4)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode game object identifier argument
		int gameObjectId;
		try
		{
			gameObjectId = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's game object identifier could not be decoded.");
		}

		// decode row argument
		int row;
		try
		{
			row = Integer.decode(substrings[2]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's row could not be decoded.");
		}

		// decode column argument
		int column;
		try
		{
			column = Integer.decode(substrings[3]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's column could not be decoded.");
		}

		return new GameObjectUpdatedMessage(gameObjectId, row, column);
	}

	/**
	 * Creates a KeyMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a KeyMessage
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new KeyMessage, created from the arguments in substrings
	 */
	private KeyMessage createKeyMessage(String[] substrings)
	{
		// message must have 4 arguments
		if (substrings.length != 4)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode game object identifier argument
		int gameObjectId;
		try
		{
			gameObjectId = Integer.decode(substrings[1]);
		} catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"messageString's game object identifier could not be decoded.");
		}

		// decode key argument
		Key key;
		try
		{
			key = Key.valueOf(substrings[2]);
		} catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(
					"messageString's Key could not be decoded.");
		}

		// decode key action argument
		KeyAction action;
		try
		{
			action = KeyAction.valueOf(substrings[3]);
		} catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(
					"messageString's Action could not be decoded.");
		}

		return new KeyMessage(gameObjectId, key, action);
	}

	/**
	 * Creates a GameMessage from the given String array.
	 * 
	 * @param substrings
	 *            array of arguments for creating a GameMessage
	 * @throws IllegalArgumentException
	 *             If the arguments are in the improper format.
	 * @return A new GameMessage, created from the arguments in substrings
	 */
	private GameMessage createGameMessage(String[] substrings)
	{
		// message must have 3 arguments
		if (substrings.length != 3)
		{
			throw new IllegalArgumentException(
					"Incorrect number of arguments in messageString.");
		}

		// decode action argument
		Action action;
		try
		{
			action = Action.valueOf(substrings[1]);
		} catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(
					"messageString's Action could not be decoded.");
		}

		// decode number of players argument
		int players;
		try
		{
			players = Integer.valueOf(substrings[2]);
		} catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException(
					"messageString's players argument could not be decoded.");
		}

		return new GameMessage(action, players);
	}

}
