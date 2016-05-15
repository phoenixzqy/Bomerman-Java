package client.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import shared.model.Key;
import shared.model.KeyAction;
import client.controller.IGameController;

/**
 * An action which calls the game controller keyEventDidOccur method whenever
 * the action is performed.
 */
public class KeyPressAction extends AbstractAction
{
	/**
	 * A serial version ID, required by Java.
	 */
	private static final long serialVersionUID = 1L;

	// the key
	private final Key key;

	// the action
	private final KeyAction action;

	// the game controller
	private final IGameController gameController;

	/**
	 * Creates a new KeyEventAction with the provided parameters.
	 * 
	 * @param key
	 *            The key for the KeyEventAction.
	 * @param action
	 *            The KeyAction for the KeyEventAction.
	 * @param gameController
	 *            The game controller which will be notified when the action is
	 *            performed.
	 */
	public KeyPressAction(Key key, KeyAction action,
			IGameController gameController)
	{
		if (key == null || action == null || gameController == null)
			throw new NullPointerException();

		this.key = key;
		this.action = action;
		this.gameController = gameController;
	}

	/**
	 * Notifies the game controller of the action that has occurred.
	 */
	public void actionPerformed(ActionEvent e)
	{
		gameController.keyEventDidOccur(key, action);
	}
}
