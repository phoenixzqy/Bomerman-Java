package client.view;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import shared.controller.IController;
import shared.core.*;
import shared.model.IScore;
import shared.view.*;
import client.controller.*;

/**
 * Implements IViewFactory for the client.
 */
public class ViewFactory implements IViewFactory
{
	// the menu view builder
	private final IMenuViewBuilder menuViewBuilder;

	/**
	 * Constructor.
	 * 
	 * @param menuViewBuilder
	 *            The menuViewBuilder used to build the views.
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

		// returns the view for the given controller type
		if (controller instanceof IConnectionFailureController)
		{
			return createConnectionFailureView((IConnectionFailureController) controller);
		} else if (controller instanceof ICreditsController)
		{
			return createCreditsView((ICreditsController) controller);
		} else if (controller instanceof IGameController)
		{
			try
			{
				return createGameView((IGameController) controller);
			} catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		} else if (controller instanceof IGameLobbyController)
		{
			return createGameLobbyView((IGameLobbyController) controller);
		} else if (controller instanceof IGameOverController)
		{
			return createGameOverView((IGameOverController) controller);
		} else if (controller instanceof IMainMenuController)
		{
			return createMainMenuView((IMainMenuController) controller);
		} else if (controller instanceof IMultiplayerController)
		{
			return createMultiplayerView((IMultiplayerController) controller);
		} else
		{
			// not a valid controller type
			throw new IllegalArgumentException();
		}
	}

	/**
	 * This is a helper method that creates a view for the connection failure screen.
	 * 
	 * @param connectionFailureController The controller for which to create the view.
	 * 
	 * @return The JPanel for the connection failure view
	 */
	private JPanel createConnectionFailureView(
			final IConnectionFailureController connectionFailureController)
	{
		// label for title text
		JLabel connectionFailureLabel = menuViewBuilder
				.buildTitleLabel("Connection Failure");

		// label for error message, defaulted to the controller's errorMessage()
		JLabel errorMessage = menuViewBuilder
				.buildLabel(connectionFailureController.errorMessage());

		// button for returning to the MultiplayerMenu
		JButton multiplayerButton = menuViewBuilder.buildButton("Multiplayer",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// calls method in conection failure controller to navigate
						// to multiplayer view
						connectionFailureController.multiplayer();
					}
				});

		JComponent[] components = { connectionFailureLabel, errorMessage,
				multiplayerButton };

		// build the view using the menu view builder and the created components
		return menuViewBuilder.buildMenu(components);
	}

	/**
	 * This is a helper method that creates a view for the credits screen.
	 * 
	 * @param creditsController The controller for which to create the view.
	 * 
	 * @return The JPanel for the credits view
	 */
	private JPanel createCreditsView(final ICreditsController creditsController)
	{
		// label for title text
		JLabel creditsLabel = menuViewBuilder.buildTitleLabel("Credits");

		// label for team name
		JLabel teamName = menuViewBuilder.buildLabel("Team: E1");
		
		// label for first line of team members
		JLabel teamMember = menuViewBuilder
				.buildLabel("Team Member: Schropp,Landon   Zhao,Qiyu");
		
		// label for second line of team members
		JLabel teamMember1 = menuViewBuilder
				.buildLabel("                             Brouwer,Daniel   Tan,Sen ");

		// button for opening readme file
		JButton readmeButton = menuViewBuilder.buildButton("Open README",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// executes credits controller's method to open readme file
						creditsController.readme();
					}
				});

		// button for returning to the main menu
		JButton mainMenuButton = menuViewBuilder.buildButton("Main Menu",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// executes credits controller's method to navigate to the main menu
						creditsController.mainMenu();
					}
				});

		JComponent[] components = { creditsLabel, teamName, teamMember,
				teamMember1, readmeButton, mainMenuButton };

		// create the credits view with the menu view builder and the created components
		return menuViewBuilder.buildMenu(components);
	}

	/**
	 * This is a helper method that creates a view for the game screen.
	 * 
	 * @param gameController The controller for which to create the view.
	 * 
	 * @return The JPanel for the game view
	 */
	private JPanel createGameView(final IGameController gameController)
			throws IOException
	{

		IScore[] scores = gameController.scores();
		final JLabel[] playerScoreLabels = new JLabel[scores.length];

		// create score labels and bind them to "scores" property
		for (int i = 0; i < scores.length; i++)
		{
			playerScoreLabels[i] = new JLabel("Player " + (i + 1) + ": "
					+ scores[i].getScore());
			playerScoreLabels[i].setForeground(new Color(0x551A8B));
			
			// necessary for anonymous ICommand's execute method
			final int j = i;
			// bind to the scores property of the game controller to update scores diplay
			gameController.bind("scores", new ICommand()
			{

				/**
				 * {@inheritDoc}
				 */
				public void execute()
				{
					// updates score label
					playerScoreLabels[j].setText("Player " + (j + 1) + ": "
							+ gameController.scores()[j].getScore());

				}

			});
		}

		// create container JPanel for GameView, score labels, exit button, and time label
		JPanel gameContainer = new JPanel();
		
		// button for exiting the game
		JButton button = new JButton("Exit");
		button.addActionListener(new ActionListener()
		{

			/**
			 * {@inheritDoc}
			 */
			public void actionPerformed(ActionEvent arg0)
			{
				// calls the game controller method to quit the game
				gameController.quitGame();
			}
		});

		// creates the GameView with a new SpriteFactory
		final GameView gameView = new GameView(new SpriteFactory());

		// binds the GameView to the gameObjects property of the GameController
		// so that it can draw the updated status of game objects
		gameController.bind("gameObjects", new ICommand()
		{
			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// calls GameView method for drawing the game controller's game objects
				gameView.drawGameObjects(gameController.gameObjects());
			}

		});

		// label for remaining game time, initialized to 0:00
		final JLabel timeLabel = new JLabel("0:00");
		// bind the time label to the remaining time property of the game controller
		gameController.bind("time", new ICommand()
		{

			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// get the remaining minutes and seconds
				int minutes = gameController.time() / 60;
				int seconds = gameController.time() % 60;
				
				if (seconds < 10)
				{
					// prepend a '0' to the number of seconds
					timeLabel.setText(minutes + ":0" + seconds);
				} else
				{
					// use seconds as is
					timeLabel.setText(minutes + ":" + seconds);
				}
			}

		});

		// set game container size and layout
		gameContainer.setSize(new Dimension(ViewStack.DEFAULT_FRAME_WIDTH,
				ViewStack.DEFAULT_FRAME_HEIGHT));
		gameContainer.setLayout(null);

		// set button dimensions and add button
		Dimension size = button.getPreferredSize();
		button.setBounds(ViewStack.DEFAULT_FRAME_WIDTH - size.width, 3,
				size.width, size.height);
		button.setFocusable(false);
		gameContainer.add(button);
		
		// space between top of view and components in the top row
		int componentY = 7;
		
		// set time label size and bounds
		size = timeLabel.getPreferredSize();
		timeLabel.setBounds(10, componentY, size.width, size.height);
		timeLabel.setForeground(new Color(245, 245, 245));
		
		// add time label to game container
		gameContainer.add(timeLabel);

		// set score label colors and dimensions
		if (playerScoreLabels.length >= 1)
		{
			// set size and bounds of player 1's score label
			Rectangle prevBounds = timeLabel.getBounds();
			size = playerScoreLabels[0].getPreferredSize();
			playerScoreLabels[0].setBounds(
					prevBounds.x + prevBounds.width + 20, componentY, size.width + 10,
					size.height);
			
			// set color of player 1's score label to blue
			playerScoreLabels[0].setForeground(new Color(99, 167, 244));
			
			// add player 1's score label to game container
			gameContainer.add(playerScoreLabels[0]);
		}
		if (playerScoreLabels.length >= 2)
		{
			// set size and bounds of player 2's score label
			size = playerScoreLabels[1].getPreferredSize();
			Rectangle prevBounds = playerScoreLabels[0].getBounds();
			playerScoreLabels[1].setBounds(
					prevBounds.x + prevBounds.width + 20, componentY, size.width + 10,
					size.height);
			
			// set color of player 2's score label to red
			playerScoreLabels[1].setForeground(new Color(196, 65, 58));
			
			// add player 2's score label to game container
			gameContainer.add(playerScoreLabels[1]);
		}
		if (playerScoreLabels.length >= 3)
		{
			// set size and bounds of player 3's score label
			size = playerScoreLabels[2].getPreferredSize();
			Rectangle prevBounds = playerScoreLabels[1].getBounds();
			playerScoreLabels[2].setBounds(
					prevBounds.x + prevBounds.width + 20, componentY, size.width + 10,
					size.height);
			
			// set color of player 3's score label to green
			playerScoreLabels[2].setForeground(new Color(77, 164, 75));
			
			// add player 3's score label to game container
			gameContainer.add(playerScoreLabels[2]);
		}
		if (playerScoreLabels.length >= 4)
		{
			// set size and bounds of player 4's score label
			size = playerScoreLabels[3].getPreferredSize();
			Rectangle prevBounds = playerScoreLabels[2].getBounds();
			playerScoreLabels[3].setBounds(
					prevBounds.x + prevBounds.width + 20, componentY, size.width + 10,
					size.height);
			
			// set color of player 4's score label to purple
			playerScoreLabels[3].setForeground(new Color(186, 101, 245));
			
			// add player 4's score label to game container
			gameContainer.add(playerScoreLabels[3]);
		}

		// set game view size and bounds
		gameView.setSize(new Dimension(736, 544));
		gameView.setBounds(0, 32, 736, 544);
		gameContainer.add(gameView);

		// create the key listener and bind it to the gameController
		new KeyBinder(gameContainer, gameController);
		return gameContainer;

	}

	/**
	 * This is a helper method that creates a view for the game lobby screen.
	 * 
	 * @param gameLobbyController The controller for which to create the view.
	 * 
	 * @return The JPanel for the game lobby view
	 */
	private JPanel createGameLobbyView(
			final IGameLobbyController gameLobbyController)
	{
		// label for the title text
		JLabel gameLobbyLabel = menuViewBuilder.buildTitleLabel("Game Lobby");

		// label for the waiting for game start text
		JLabel waitingLabel = menuViewBuilder
				.buildLabel("Waiting for the game to start.");

		// label for the number of players currently connected to the host
		final JLabel connectedPlayersLabel = menuViewBuilder
				.buildLabel("Connected Players: "
						+ gameLobbyController.numberOfPlayers());

		// bind to the the numberOfPlayers property in gameLobbyController
		// to update label for number of players connected to host
		gameLobbyController.bind("numberOfPlayers", new ICommand()
		{
			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// sets the label text to the game lobby's number of connected players
				connectedPlayersLabel.setText("Connected Players: "
						+ gameLobbyController.numberOfPlayers());

			}

		});

		// button for canceling out of the game lobby
		JButton cancelButton = menuViewBuilder.buildButton("Cancel",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// calls the cancel method in the game lobby controller to
						// navigate back to the multiplayer menu
						gameLobbyController.cancel();
					}
				});

		JComponent[] components = { gameLobbyLabel, waitingLabel,
				connectedPlayersLabel, cancelButton };

		// use the menu view builder to build the game lobby view from the created components
		return menuViewBuilder.buildMenu(components);
	}

	/**
	 * This is a helper method that creates a view for the game over screen.
	 * 
	 * @param gameController The controller for which to create the view.
	 * 
	 * @return The JPanel for the game over view
	 */
	private JPanel createGameOverView(
			final IGameOverController gameOverController)
	{
		// label for the title text
		JLabel gameOverLabel = menuViewBuilder.buildTitleLabel("Game Over");

		// get score objects for the players
		IScore[] scores = gameOverController.scores();
		
		// create the table data
		String[] columnNames = { "Rank", "Player", "Kills" };
		String[][] data = new String[scores.length][3];

		// map the score objects to their index in the scores array to for reference after sorting
		Map<IScore, Integer> scoreMap = new HashMap<IScore, Integer>();
		for (int i = 0; i < scores.length; i++)
		{
			scoreMap.put(scores[i], i);
		}
		
		// sort the scores array by the integer score value
		Arrays.sort(scores, new Comparator<IScore>()
		{
			/**
			 * {@inheritDoc}
			 */
			public int compare(IScore lhs, IScore rhs)
			{
				// sorts from greatest to least
				return rhs.getScore() - lhs.getScore();
			}
		});

		// set the player rankings, colors, and scores in the table data
		for (int i = 0; i < scores.length; i++)
		{
			// set player ranking
			data[i][0] = "" + (i + 1);
			
			// set player color
			switch (scoreMap.get(scores[i]))
			{
			case 0:
				data[i][1] = "BLUE";
				break;
			case 1:
				data[i][1] = "RED";
				break;
			case 2:
				data[i][1] = "GREEN";
				break;
			case 3:
				data[i][1] = "PURPLE";
				break;
			}

			// sets the player score
			data[i][2] = "" + scores[i].getScore();
		}

		// display table data as series of JLabels
		// set column labels
		JLabel columnLabels = menuViewBuilder.buildLabel(ArrayUtilities.join(
				columnNames, "          "));
		
		// set row one label
		JLabel rowOne;
		if (data.length >= 1)
		{
			rowOne = menuViewBuilder.buildLabel(ArrayUtilities.join(data[0],
					"          "));
		} else
		{
			rowOne = menuViewBuilder.buildLabel("");
		}
		
		// set row two label
		JLabel rowTwo;
		if (data.length >= 2)
		{
			rowTwo = menuViewBuilder.buildLabel(ArrayUtilities.join(data[1],
					"          "));
		} else
		{
			rowTwo = menuViewBuilder.buildLabel("");
		}
		
		// set row three label
		JLabel rowThree;
		if (data.length >= 3)
		{
			rowThree = menuViewBuilder.buildLabel(ArrayUtilities.join(data[2],
					"          "));
		} else
		{
			rowThree = menuViewBuilder.buildLabel("");
		}
		
		// set row four label
		JLabel rowFour;
		if (data.length >= 4)
		{
			rowFour = menuViewBuilder.buildLabel(ArrayUtilities.join(data[3],
					"          "));
		} else
		{
			rowFour = menuViewBuilder.buildLabel("");
		}

		// button for rejoining a server for a rematch
		JButton rematchButton = menuViewBuilder.buildButton("Rematch",
				new ICommand()
				{
			
				/**
				 * @{inheritDoc}
				 */
					public void execute()
					{
						// calls game over controller's method for
						// reconnecting to the host for a rematch
						gameOverController.rematch();
					}
				});

		// button for returning to the main menu
		JButton mainMenuButton = menuViewBuilder.buildButton("Main Menu",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// calls game over controller's method for navigating to the main menu
						gameOverController.mainMenu();
					}
				});

		JComponent[] components = { gameOverLabel, columnLabels, rowOne,
				rowTwo, rowThree, rowFour, rematchButton, mainMenuButton };

		// use the menu view builder to create the view with the created components
		return menuViewBuilder.buildMenu(components);
	}

	/**
	 * This is a helper method that creates a view for the main menu screen.
	 * 
	 * @param mainMenuController The controller for which to create the view.
	 * 
	 * @return The JPanel for the main menu view
	 */
	private JPanel createMainMenuView(
			final IMainMenuController mainMenuController)
	{
		// load image for title screen
		BufferedImage myPicture;
		try
		{
			myPicture = ImageIO.read(new File("resources/Title_Image.png"));
		}
		catch (IOException exception)
		{
			throw new IllegalStateException();
		}
		
		// label for title screen image
		JLabel bombermanServerLabel = new JLabel(new ImageIcon( myPicture ));

		// button for navigating to the multiplayer screen
		JButton multiplayerButton = menuViewBuilder.buildButton("Multiplayer",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// calls main menu controller's method for navigating to multiplayer view
						mainMenuController.multiplayer();
					}
				});

		// button for navigating to credits screen
		JButton creditsButton = menuViewBuilder.buildButton("Credits",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// calls main menu controller's method for navigating to credits view
						mainMenuController.credits();
					}
				});

		// button for exiting the application
		JButton exitButton = menuViewBuilder.buildButton("Exit", new ICommand()
		{
			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// calls main menu controller's method for exiting the application
				mainMenuController.exit();
			}
		});

		JComponent[] components = { bombermanServerLabel, multiplayerButton,
				creditsButton, exitButton };

		// uses menu view builder to create main menu view with the created components
		return menuViewBuilder.buildMenu(components);
	}

	/**
	 * This is a helper method that creates a view for the multiplayer screen.
	 * 
	 * @param multiplayerController The controller for which to create the view.
	 * 
	 * @return The JPanel for the multiplayer view
	 */
	private JPanel createMultiplayerView(
			final IMultiplayerController multiplayerController)
	{
		// label for the title text
		JLabel multiplayerLabel = menuViewBuilder
				.buildTitleLabel("Multiplayer");

		// label for the server address field
		JLabel serverAddressLabel = menuViewBuilder
				.buildLabel("Server Address:");

		// text field for entering server address
		final JTextField textField = menuViewBuilder.buildTextField();

		// label for diplaying error messages when communicating with server
		// default shows no error
		final JLabel errorMessage = menuViewBuilder.buildLabel("Waiting to Join Game");

		// bind to errorMessage property to update error message label
		multiplayerController.bind("errorMessage", new ICommand()
		{

			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				errorMessage.setText(multiplayerController.errorMessage());
			}

		});

		// button for connecting to the host
		JButton joinButton = menuViewBuilder.buildButton("Join", new ICommand()
		{
			/**
			 * {@inheritDoc}
			 */
			public void execute()
			{
				// sets the server address to match the user's entry
				multiplayerController.setServerAddress(textField.getText());
				// calls multiplayer controller's join method to connect to server
				// and navigate to game lobby if successful
				multiplayerController.join();
			}
		});

		// button for returning to the main menu
		JButton mainMenuButton = menuViewBuilder.buildButton("Main Menu",
				new ICommand()
				{
					/**
					 * {@inheritDoc}
					 */
					public void execute()
					{
						// calls the multiplayer controller's method to navigate to the main menu
						multiplayerController.mainMenu();
					}
				});

		JComponent[] components = { multiplayerLabel, serverAddressLabel,
				textField, errorMessage, joinButton, mainMenuButton };

		// uses the menu view builder to construct the multiplayer view with the created components
		return menuViewBuilder.buildMenu(components);
	}
}
