package client.view;

import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.*;
import shared.model.*;
import client.controller.IGameController;

/**
 * Intelligently sets up bindings between the provided JComponent key events and
 * the provided game controller. Also abstracts away the rapid pressing which
 * occurs with the platform
 */
public class KeyBinder
{
	/**
	 * A private action which calls the keyPressed method with the provided key.
	 */
	private class KeyEventAction extends AbstractAction
	{
		/**
		 * A serial version ID, required by Java.
		 */
		private static final long serialVersionUID = 1L;

		// the key
		private final Key key;

		// the action
		private final KeyAction action;

		/**
		 * Creates a new KeyPressAction with the provided key.
		 * 
		 * @param key
		 *            The key for the KeyPressAction.
		 * @param action
		 *            The action for the KeyPressAction.
		 */
		public KeyEventAction(Key key, KeyAction action)
		{
			this.key = key;
			this.action = action;

		}

		/**
		 * Generates a key pressed event if the action was a key press
		 * or a key depressed event if the action was a key depress.
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (action == KeyAction.PRESS)
				keyPressed(key);
			else
				keyDepressed(key);
		}
	}

	// the game controller
	private final IGameController gameController;

	// a map of keys which correspond to boolean values that indicate when a key
	// is pressed
	private final Map<Key, Boolean> keyPressedMap;

	/**
	 * Creates a new key binder which automatically binds the provided component
	 * to the provided game controller.
	 * 
	 * @param component
	 *            The component whose key events are bound to.
	 * @param gameController
	 *            The game controller which will be called when key events fire.
	 * 
	 */
	public KeyBinder(JComponent component, IGameController gameController)
	{
		// check the parameters
		if (component == null || gameController == null)
			throw new NullPointerException();

		// set the member variables
		this.gameController = gameController;

		// get the input map
		InputMap inputMap = component
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		// add the event strings to the input map
		inputMap.put(KeyStroke.getKeyStroke("pressed UP"), "pressedUp");
		inputMap.put(KeyStroke.getKeyStroke("pressed DOWN"), "pressedDown");
		inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "pressedLeft");
		inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "pressedRight");
		inputMap.put(KeyStroke.getKeyStroke("pressed SPACE"), "pressedSpace");
		inputMap.put(KeyStroke.getKeyStroke("released UP"), "releasedUp");
		inputMap.put(KeyStroke.getKeyStroke("released DOWN"), "releasedDown");
		inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "releasedLeft");
		inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "releasedRight");
		inputMap.put(KeyStroke.getKeyStroke("released SPACE"), "releasedSpace");

		// create new actions
		Action keyPressedUpAction = new KeyEventAction(Key.UP, KeyAction.PRESS);
		Action keyPressedDownAction = new KeyEventAction(Key.DOWN,
				KeyAction.PRESS);
		Action keyPressedLeftAction = new KeyEventAction(Key.LEFT,
				KeyAction.PRESS);
		Action keyPressedRightAction = new KeyEventAction(Key.RIGHT,
				KeyAction.PRESS);
		Action keyPressedSpaceAction = new KeyEventAction(Key.SPACE,
				KeyAction.PRESS);
		Action keyReleasedUpAction = new KeyEventAction(Key.UP,
				KeyAction.DEPRESS);
		Action keyReleasedDownAction = new KeyEventAction(Key.DOWN,
				KeyAction.DEPRESS);
		Action keyReleasedLeftAction = new KeyEventAction(Key.LEFT,
				KeyAction.DEPRESS);
		Action keyReleasedRightAction = new KeyEventAction(Key.RIGHT,
				KeyAction.DEPRESS);
		Action keyReleasedSpaceAction = new KeyEventAction(Key.SPACE,
				KeyAction.DEPRESS);

		// add new actions
		component.getActionMap().put("pressedUp", keyPressedUpAction);
		component.getActionMap().put("pressedDown", keyPressedDownAction);
		component.getActionMap().put("pressedLeft", keyPressedLeftAction);
		component.getActionMap().put("pressedRight", keyPressedRightAction);
		component.getActionMap().put("pressedSpace", keyPressedSpaceAction);
		component.getActionMap().put("releasedUp", keyReleasedUpAction);
		component.getActionMap().put("releasedDown", keyReleasedDownAction);
		component.getActionMap().put("releasedLeft", keyReleasedLeftAction);
		component.getActionMap().put("releasedRight", keyReleasedRightAction);
		component.getActionMap().put("releasedSpace", keyReleasedSpaceAction);

		// set up the key press map
		keyPressedMap = new HashMap<Key, Boolean>();
		keyPressedMap.put(Key.UP, false);
		keyPressedMap.put(Key.DOWN, false);
		keyPressedMap.put(Key.LEFT, false);
		keyPressedMap.put(Key.RIGHT, false);
		keyPressedMap.put(Key.SPACE, false);
	}

	/**
	 * Method which is called whenever a key is pressed.
	 * 
	 * @param key
	 *            The key which was pressed
	 */
	private synchronized void keyPressed(Key key)
	{
		// if key was pressed, ignore this key press event
		if (keyPressedMap.get(key))
			return;

		// mark key as pressed and notify game controller of key event
		keyPressedMap.put(key, true);
		gameController.keyEventDidOccur(key, KeyAction.PRESS);
	}

	/**
	 * Method which is called whenever a key is depressed.
	 * 
	 * @param key
	 *            The key which is depressed.
	 */
	private synchronized void keyDepressed(Key key)
	{
		// mark key as depressed and notify game controller of key event
		keyPressedMap.put(key, false);
		gameController.keyEventDidOccur(key, KeyAction.DEPRESS);
	}
}
