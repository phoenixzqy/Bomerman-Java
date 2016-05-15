package server.view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import server.controller.IGameHostingController;
import server.controller.IMainMenuController;
import shared.controller.IController;
import shared.core.ICommand;
import shared.view.IMenuViewBuilder;
import shared.view.IViewFactory;

/**
 * Implements IViewFactory for the server.
 */
public class ViewFactory implements IViewFactory
{
	// the menu view builder
	private final IMenuViewBuilder menuViewBuilder;
	
	/**
	 * Constructor.
	 * @param menuViewBuilder The menuViewBuilder used to build the views.
	 */
	public ViewFactory(IMenuViewBuilder menuViewBuilder)
	{
		if (menuViewBuilder == null)
			throw new NullPointerException();
		
		this.menuViewBuilder = menuViewBuilder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public JPanel createView(IController controller)
	{
		if (controller == null)
			throw new NullPointerException();
		
		// create and return the proper view depending on the type of controller
		if (controller instanceof IMainMenuController)
		{
			return createMainMenuView((IMainMenuController) controller);
		}
		else if (controller instanceof IGameHostingController)
		{
			return createGameHostingView((IGameHostingController) controller);
		}
		else
		{
			// controller type not supported
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Creates a main menu view.
	 * @param mainMenuController The controller for the view.
	 * @return A main menu view.
	 */
	private JPanel createMainMenuView(final IMainMenuController mainMenuController)
	{
		// label for title text
		JLabel bombermanLabel = menuViewBuilder.buildTitleLabel("Bomberman");
		
		// label for diplaying error messages, defaulted to no error message
		final JLabel errorMessageLabel = menuViewBuilder.buildLabel("Waiting to Host Game");
		
		// bind to errorMessage property to update label text
		mainMenuController.bind("errorMessage", new ICommand(){

			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// set the text to match the error message
				errorMessageLabel.setText(mainMenuController.errorMessage());
			}
			
		});
		
		// button to start listening for connetions from clients
		JButton startListeningButton = menuViewBuilder.buildButton("Host a Game", new ICommand()
			{
				/**
				 * {@inheritDoc}
				 */
				public void execute()
				{
					// call mainMenuController's method to startListening and navigate to the game hosting screen
					mainMenuController.startListening();
				}
			});
		
		// button to exit the application
		JButton exitButton = menuViewBuilder.buildButton("Exit", new ICommand()
			{
				/**
				 * {@inheritDoc}
				 */
				public void execute()
				{
					// calls mainMenuController's method to exit the application
					mainMenuController.exit();
				}
			});
		
		JComponent[] components = { bombermanLabel, errorMessageLabel, startListeningButton, exitButton };
		
		// use the menuViewBuilder to create a view from these components
		return menuViewBuilder.buildMenu(components);
	}
	
	/**
	 * Creates a game hosting view.
	 * @param gameHostingController The controller for the view.
	 * @return A game hosting view.
	 */
	private JPanel createGameHostingView(final IGameHostingController gameHostingController)
	{
		// label with title text
		final JLabel hostAGameLabel = menuViewBuilder.buildTitleLabel("Host a Game");
		
		// label with game status
		final JLabel statusLabel = menuViewBuilder.buildLabel("Accepting connections from clients.");
		
		// label to display error messages in communicating with connected clients
		final JLabel errorMessageLabel = menuViewBuilder.buildLabel("Waiting to Start Game");
		
		// bind to "errorMessage" property to update errorMessageLabel
		gameHostingController.bind("errorMessage", new ICommand(){

			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// sets the label text to match the error message
				errorMessageLabel.setText(gameHostingController.errorMessage());
			}
			
		});
		
		// button to start or stop the game
		final JButton startGameStopGameButton = menuViewBuilder.buildButton("Start Game", 
			new ICommand()
			{
				/**
				 * {@inheritDoc}
				 */
				public void execute()
				{
					// calls the gameHostingController's method to start or stop the game
					gameHostingController.startOrStopGame();
				}
			});
		
		// bind to "running" property of the gameHostingController to change the button text on the Start Game/Stop Game button
		gameHostingController.bind("running", new ICommand()
		{
			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				String text;
				if (gameHostingController.running()) {
					// set the button label text to "Stop Game" if the game is currently running
					text = "Stop Game";
				} else {
					// set the button label text to "Start Game" if the game is NOT currently running
					text = "Start Game";
				}
				// set the button label text
				startGameStopGameButton.setText(text);
			}
		});
		
		// label for the number of currently connected players
		final JLabel connectedPlayersLabel = menuViewBuilder
				.buildLabel("Connected Players: "
						+ gameHostingController.connectedPlayers());
		
		// bind to "connectedPlayers" property to update the label
		gameHostingController.bind("connectedPlayers", new ICommand()
		{
			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// set the label text to match the number of currently connected players
				connectedPlayersLabel.setText("Connected Players: "
						+ gameHostingController.connectedPlayers());

			}

		});
		
		// button to return to the main menu
		JButton mainMenuButton = menuViewBuilder.buildButton("Main Menu", new ICommand()
			{
				/**
				 * {@inheritDoc}
				 */
				public void execute()
				{
					// call gameHostingController's method to return to the main menu
					gameHostingController.mainMenu();
				}
			});
		
		JComponent[] components = 
			{ 
				hostAGameLabel, 
				statusLabel,
				connectedPlayersLabel,
				errorMessageLabel,
				startGameStopGameButton, 
				mainMenuButton 
			};
		
		// use menu view builder to build a view from the created components
		return menuViewBuilder.buildMenu(components);
	}
}
