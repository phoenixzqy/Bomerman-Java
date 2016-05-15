package client.controller;

import java.util.Timer;

import shared.model.IScore;
import shared.model.Key;
import shared.model.KeyAction;
import shared.model.communication.ICommunicator;
import client.model.IGameObject;

/**
 * Controls the game screen.
 */
public interface IGameController extends IController
{
	/**
	 * Quits the game and navigates the application to the multiplayer scene.
	 */
	public void quitGame();

	/**
	 * Returns the communicator for this IGameController.
	 * 
	 * @return The communicator for this IGameController.
	 */
	public ICommunicator communicator();

	/**
	 * Returns the game objects. This is a bindable property.
	 * 
	 * @return The game objects. This is a bindable property.
	 */
	public IGameObject[] gameObjects();

	/**
	 * Sets the game objects. This is a bindable property.
	 * 
	 * @param gameObjects
	 *            The game objects. This array will be copied.
	 * @throws NullPointerException
	 *             Thrown if gameObjects is null.
	 */
	public void setGameObjects(IGameObject[] gameObjects);

	/**
	 * Returns the timer for this game controller.
	 * 
	 * @return The timer for this game controller.
	 */
	public Timer timer();

	/**
	 * Steps the controller one step forward. This method is called internally
	 * by a timer and is only exposed for testing purposes. This method will
	 * iterate received messages in order. This means if one message is
	 * dependent on another, and the dependent message was received after its
	 * dependency, then this method will execute without a problem.
	 * 
	 * @throws IllegalStateException
	 *             Thrown if a receive message is unknown or invalid.
	 */
	public void step();

	/**
	 * This method may be called when a key event occurred.
	 * 
	 * @param key
	 *            The key.
	 * @param action
	 *            The action.
	 * @throws NullPointerException
	 *             Thrown if the key or action is null.
	 */
	public void keyEventDidOccur(Key key, KeyAction action);

	/**
	 * Returns the player Scores. This is a bindable property.
	 * 
	 * @return The player Scores. This is a bindable property.
	 */
	public IScore[] scores();

	/**
	 * Returns the GameObject identifier of the player. This is a bindable
	 * property.
	 * 
	 * @return The GameObject identifier of the player. This is a bindable
	 *         property.
	 */
	public int playerGameObjectId();

	/**
	 * Starts the game.
	 */
	public void start();

	/**
	 * Returns the amount of time remaining in the game (in seconds).
	 * 
	 * @return The amount of time remaining in the game (in seconds).
	 */
	public int time();
}
