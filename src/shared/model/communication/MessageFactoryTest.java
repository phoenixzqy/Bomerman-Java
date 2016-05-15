package shared.model.communication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import shared.model.GameObjectType;
import shared.model.Key;
import shared.model.KeyAction;

/**
 * Tests MessageFactory.
 */
public class MessageFactoryTest
{
	/**
	 * Test instance of MessageFactory.
	 */
	private MessageFactory messageFactory;

	/**
	 * Sets up the tests.
	 */
	@Before
	public void setUp()
	{
		messageFactory = new MessageFactory();
	}

	/**
	 * Tests that createMessage throws a NullPointerException when given a null
	 * argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testCreateMessageNullArgument()
	{
		messageFactory.createMessage(null);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given an
	 * empty string as an argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageEmptyStringArgument()
	{
		messageFactory.createMessage("");
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * whitespace string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageWhitespaceArgument()
	{
		messageFactory.createMessage("          ");
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given an
	 * unknown message type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageUnknownMessageType()
	{
		messageFactory.createMessage("UNKNOWN");
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given an
	 * incorrectly formatted string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageIncorrectlyFormattedStringArgument()
	{
		messageFactory.createMessage("sdlk;fjsadf lk;jsdghqweo qwojadlg;j");
	}

	/**
	 * Tests that createMessage returns the proper GameObjectCreatedMessage
	 * object when given a correctly formatted GameObjectCreatedMessage string.
	 */
	@Test
	public void testCreateMessageGameObjectCreatedMessage()
	{
		String testMessage = "GAME_OBJECT_CREATED 11 " + GameObjectType.PLAYER
				+ " 3 5";
		IMessage message = messageFactory.createMessage(testMessage);
		if (!(message instanceof GameObjectCreatedMessage))
		{
			fail("Message is not an instance of GameObjectCreatedMessage.");
		}
		assertEquals(testMessage, message.toString());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with a lowercase type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageLowercaseType()
	{
		String testMessage = "game_object_created 11 " + GameObjectType.PLAYER
				+ " 3 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageTooFewArguments()
	{
		String testMessage = "GAME_OBJECT_CREATED 11 3";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageTooManyArguments()
	{
		String testMessage = "GAME_OBJECT_CREATED 11 " + GameObjectType.PLAYER
				+ " 3 5 7 15 bob";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with an invalid game object identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageInvalidGameObjectIdentifier()
	{
		String testMessage = "GAME_OBJECT_CREATED 11ab2 "
				+ GameObjectType.PLAYER + " 3 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with an invalid row argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageInvalidRow()
	{
		String testMessage = "GAME_OBJECT_CREATED 11 " + GameObjectType.PLAYER
				+ " .3f 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with an invalid column argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageInvalidColumn()
	{
		String testMessage = "GAME_OBJECT_CREATED 11 " + GameObjectType.PLAYER
				+ " 3 5.61v";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_CREATED string with an invalid GameObjectType.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectCreatedMessageInvalidGameObjectType()
	{
		String testMessage = "GAME_OBJECT_CREATED 11 SUPERPLAYER 3 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage returns the proper GameObjectDestroyedMessage
	 * object when given a correctly formatted GameObjectDestroyedMessage
	 * string.
	 */
	@Test
	public void testCreateMessageGameObjectDestroyedMessage()
	{
		String testMessage = "GAME_OBJECT_DESTROYED 11";
		IMessage message = messageFactory.createMessage(testMessage);
		if (!(message instanceof GameObjectDestroyedMessage))
		{
			fail("Message is not an instance of GameObjectDestroyedMessage.");
		}
		assertEquals(testMessage, message.toString());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_DESTROYED string with a lowercase type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectDestroyedMessageLowercaseType()
	{
		String testMessage = "game_object_destroyed 11";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_DESTROYED string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectDestroyedMessageTooFewArguments()
	{
		String testMessage = "GAME_OBJECT_DESTROYED";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_DESTROYED string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectDestroyedMessageTooManyArguments()
	{
		String testMessage = "GAME_OBJECT_DESTROYED 11 lala 95";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_DESTROYED string with an invalid game object identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectDestroyedMessageInvalidGameObjectIdentifier()
	{
		String testMessage = "GAME_OBJECT_DESTROYED 1.1asb";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage returns the proper ScoreUpdatedMessage object
	 * when given a correctly formatted ScoreUpdatedMessage string.
	 */
	@Test
	public void testCreateMessageScoreUpdatedMessage()
	{
		String testMessage = "SCORE_UPDATED 11 5";
		IMessage message = messageFactory.createMessage(testMessage);
		if (!(message instanceof ScoreUpdatedMessage))
		{
			fail("Message is not an instance of ScoreUpdatedMessage.");
		}
		assertEquals(testMessage, message.toString());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * SCORE_UPDATED string with a lowercase type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageScoreUpdatedMessageLowercaseType()
	{
		String testMessage = "score_updated 11 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * SCORE_UPDATED string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageScoreUpdatedMessageTooFewArguments()
	{
		String testMessage = "SCORE_UPDATED 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * SCORE_UPDATED string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageScoreUpdatedMessageTooManyArguments()
	{
		String testMessage = "SCORE_UPDATED 11 5 kauai 145";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * SCORE_UPDATED string with an invalid player number.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageScoreUpdatedMessageInvalidPlayerNumber()
	{
		String testMessage = "SCORE_UPDATED 1.14 11 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * SCORE_UPDATED string with an invalid game object identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageScoreUpdatedMessageInvalidGameObjectIdentifier()
	{
		String testMessage = "SCORE_UPDATED 1 1.agzvf1 5";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * SCORE_UPDATED string with an invalid score.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageScoreUpdatedMessageInvalidScore()
	{
		String testMessage = "SCORE_UPDATED 1 11 p5.asf";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage returns the proper GameObjectUpdatedMessage
	 * object when given a correctly formatted GameObjectUpdatedMessage string.
	 */
	@Test
	public void testCreateMessageGameObjectUpdatedMessage()
	{
		String testMessage = "GAME_OBJECT_POSITION_UPDATED 11 5 14";
		IMessage message = messageFactory.createMessage(testMessage);
		if (!(message instanceof GameObjectUpdatedMessage))
		{
			fail("Message is not an instance of GameObjectUpdatedMessage.");
		}
		assertEquals(testMessage, message.toString());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_UPDATED string with a lowercase type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectUpdatedMessageLowercaseType()
	{
		String testMessage = "game_object_position_updated 11 5 14";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_UPDATED string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectUpdatedMessageTooFewArguments()
	{
		String testMessage = "GAME_OBJECT_POSITION_UPDATED 11";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_UPDATED string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectUpdatedMessageTooManyArguments()
	{
		String testMessage = "GAME_OBJECT_POSITION_UPDATED 11 5 14 bob kevin 15 susie";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_UPDATED string with an invalid game object identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectUpdatedMessageInvalidGameObjectIdentifier()
	{
		String testMessage = "GAME_OBJECT_POSITION_UPDATED 5.agasdb4 5 14";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_UPDATED string with an invalid row argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectUpdatedMessageInvalidRow()
	{
		String testMessage = "GAME_OBJECT_POSITION_UPDATED 11 5sadgh 14";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_OBJECT_UPDATED string with an invalid column argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameObjectUpdatedMessageInvalidColumn()
	{
		String testMessage = "GAME_OBJECT_POSITION_UPDATED 11 5 1.1a4";
		messageFactory.createMessage(testMessage);
	}

	/**
	 * Tests that createMessage returns the proper KeyMessage object when given
	 * a correctly formatted KeyMessage string.
	 */
	@Test
	public void testCreateMessageKeyMessage()
	{
		String messageString = "KEY 1 UP PRESS";
		IMessage message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		KeyMessage keyMessage = (KeyMessage) message;
		assertEquals(Key.UP, keyMessage.key());
		assertEquals(KeyAction.PRESS, keyMessage.action());

		messageString = "KEY 1 DOWN PRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.DOWN, keyMessage.key());
		assertEquals(KeyAction.PRESS, keyMessage.action());

		messageString = "KEY 1 LEFT PRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.LEFT, keyMessage.key());
		assertEquals(KeyAction.PRESS, keyMessage.action());

		messageString = "KEY 1 RIGHT PRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.RIGHT, keyMessage.key());
		assertEquals(KeyAction.PRESS, keyMessage.action());

		messageString = "KEY 1 SPACE PRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.SPACE, keyMessage.key());
		assertEquals(KeyAction.PRESS, keyMessage.action());

		messageString = "KEY 1 UP DEPRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.UP, keyMessage.key());
		assertEquals(KeyAction.DEPRESS, keyMessage.action());

		messageString = "KEY 1 DOWN DEPRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.DOWN, keyMessage.key());
		assertEquals(KeyAction.DEPRESS, keyMessage.action());

		messageString = "KEY 1 LEFT DEPRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.LEFT, keyMessage.key());
		assertEquals(KeyAction.DEPRESS, keyMessage.action());

		messageString = "KEY 1 RIGHT DEPRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.RIGHT, keyMessage.key());
		assertEquals(KeyAction.DEPRESS, keyMessage.action());

		messageString = "KEY 1 SPACE DEPRESS";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof KeyMessage);
		keyMessage = (KeyMessage) message;
		assertEquals(Key.SPACE, keyMessage.key());
		assertEquals(KeyAction.DEPRESS, keyMessage.action());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with a lower case type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageKeyLowercaseType()
	{
		String messageString = "key UP PRESS";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageKeyMessageTooFewArguments()
	{
		String messageString = "KEY UP";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageKeyMessageTooManyArguments()
	{
		String messageString = "KEY 1 UP PRESS DOWN";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with an invalid Key.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageKeyMessageInvalidKey()
	{
		String messageString = "KEY 1 ENTER PRESS";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with an invalid Action.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageKeyMessageInvalidAction()
	{
		String messageString = "KEY ENTER TAP";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage returns the proper GameMessage object when given
	 * a correctly formatted GameMessage string.
	 */
	@Test
	public void testCreateMessageGameMessage()
	{
		String messageString = "GAME START 3";
		IMessage message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof GameMessage);
		GameMessage gameMessage = (GameMessage) message;
		assertEquals(GameMessage.Action.START, gameMessage.action());
		assertEquals(3, gameMessage.numberOfPlayers());

		messageString = "GAME STOP 3";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof GameMessage);
		gameMessage = (GameMessage) message;
		assertEquals(GameMessage.Action.STOP, gameMessage.action());
		assertEquals(3, gameMessage.numberOfPlayers());

		messageString = "GAME WAITING 3";
		message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof GameMessage);
		gameMessage = (GameMessage) message;
		assertEquals(GameMessage.Action.WAITING, gameMessage.action());
		assertEquals(3, gameMessage.numberOfPlayers());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME string with a lower case type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameMessageLowercaseType()
	{
		String messageString = "game START 5";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameMessageTooFewArguments()
	{
		String messageString = "GAME STOP";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameMessageTooManyArguments()
	{
		String messageString = "GAME WAITING 13 4";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME string with an invalid Action.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameMessageInvalidAction()
	{
		String messageString = "GAME GO 7";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME string with an invalid players argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameMessageInvalidPlayers()
	{
		String messageString = "GAME START 10.8vh*";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage returns the proper
	 * PlayerGameObjectIdentifierMessage object when given a correctly formatted
	 * PlayerGameObjectIdentifierMessage string.
	 */
	@Test
	public void testCreateMessagePlayerGameObjectIdentifierMessage()
	{
		String messageString = "PLAYER_GAME_OBJECT_IDENTIFIER 11";
		IMessage message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof PlayerGameObjectIdentifierMessage);
		PlayerGameObjectIdentifierMessage playerGameObjectIdentifierMessage = (PlayerGameObjectIdentifierMessage) message;
		assertEquals(11, playerGameObjectIdentifierMessage
				.gameObjectIdentifier());

	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * PLAYER_GAME_OBJECT_IDENTIFIER string with a lower case type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayerGameObjectIdentifierMessageLowercaseType()
	{
		String messageString = "player_game_object_identifier 5";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * PLAYER_GAME_OBJECT_IDENTIFIER string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessagePlayerGameObjectIdentifierMessageTooFewArguments()
	{
		String messageString = "PLAYER_GAME_OBJECT_IDENTIFIER";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * PLAYER_GAME_OBJECT_IDENTIFIER string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessagePlayerGameObjectIdentifierMessageTooManyArguments()
	{
		String messageString = "PLAYER_GAME_OBJECT_IDENTIFIER 13 4";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * PLAYER_GAME_OBJECT_IDENTIFIER string with an invalid game object
	 * identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessagePlayerGameObjectIdentifierMessageInvalidGameObjectIdentifier()
	{
		String messageString = "PLAYER_GAME_OBJECT_IDENTIFIER GO";
		messageFactory.createMessage(messageString);
	}
	
	/**
	 * Tests that createMessage returns the proper KeyMessage object when given
	 * a correctly formatted KeyMessage string.
	 */
	@Test
	public void testCreateMessageConnectionStatusMessage()
	{
		String messageString = "STATUS";
		IMessage message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof HelloMessage);
		HelloMessage connectionStatusMessage = (HelloMessage) message;
		assertEquals(messageString, connectionStatusMessage.toString());
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with a lower case type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageConnectionStatusMessageLowercase()
	{
		String messageString = "status";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * KEY string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageConnectionStatusMessageTooManyArguments()
	{
		String messageString = "STATUS BLAHBLAHBLAH";
		messageFactory.createMessage(messageString);
	}
	
	/**
	 * Tests that createMessage returns the proper
	 * GameTimeMessage object when given a correctly formatted
	 * GameTimeMessage string.
	 */
	@Test
	public void testCreateMessageGameTimeMessage()
	{
		String messageString = "GAME_TIME 11";
		IMessage message = messageFactory.createMessage(messageString);
		assertTrue(message instanceof GameTimeMessage);
		GameTimeMessage gameTimeMessage = (GameTimeMessage) message;
		assertEquals(11, gameTimeMessage.time());

	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_TIME string with a lower case type.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateGameTimeMessageLowercaseType()
	{
		String messageString = "game_time 5";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_TIME string with too few arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameTimeMessageTooFewArguments()
	{
		String messageString = "GAME_TIME";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_TIME string with too many arguments.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameTimeMessageTooManyArguments()
	{
		String messageString = "GAME_TIME 13 4";
		messageFactory.createMessage(messageString);
	}

	/**
	 * Tests that createMessage throws an IllegalArgumentException when given a
	 * GAME_TIME string with an invalid game object
	 * identifier.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageGameTimeMessageInvalidGameObjectIdentifier()
	{
		String messageString = "GAME_TIME GO";
		messageFactory.createMessage(messageString);
	}

}
