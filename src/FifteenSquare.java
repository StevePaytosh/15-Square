/* Steve Paytosh
 * EECS 1510
 * 4/2/2014
 * 
 * Fifteen Square is a game where a user arranges a 4 by 4 grid of 15 squares numbered 1-15 in order. 1 space is blank and provides the 
 * challenge
 */

/* To Do List
 * Moar comments
 * Add stats for the game
 */
import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;
import java.util.Scanner;
import java.awt.event.*;
import java.awt.*;
import java.awt.Color;



public class FifteenSquare 
extends JFrame
{
	// data field
	int gameSize=4; // hold the size of the board, default is 4, as in a 4 by 4 board
	int buttonSize=100; // hold the size the buttons should be, may be changed in the setDifficulty method
	JButton[][] buttonArray= new JButton[gameSize][gameSize];
	
	// make variables used for a statistics files, p denotes that it is intended for this file
	long pGamesPlayed=0;  // hold the number of games played for statistics
	long pTimePlayed=0; // hold the amount of time played in seconds 
	long pMovesMade=0; // hold the number of moves made
	long pGamesWon=0;
	java.io.File stats=new java.io.File("stats.txt"); // create the text file for  holding statistics
	
	// variables for use in game
	int turnCount=0; // holds the number of turns made in a game
	int themeNumber=0; // hold a number associated with a theme, this is consistent with the index in setDisplay method
		// JFrames mostly used by one listener, but closed or sometimes used elsewhere
	JFrame mainWindow=new JFrame("		Fifteen Square Challenge");
	JLabel status= new JLabel("Moves: " + turnCount);  // creates a text label to display the number of moves in the game
	JFrame helpBox=new JFrame("Help");  // JFrame that tells user how to play the game
	JFrame winBox = new JFrame("		Congratulations!");
	
	JFrame loadError = new JFrame();  // creates a frame used to display errors to the user
	 JTextArea errorText=new JTextArea(); // creates a text area used by multiple error boxes
	

	JPanel p0= new JPanel();  // this is the main game panel
	
	// helper buttons
	JPanel p1=new JPanel(); // this panel will hold data for the game and helper buttons and is at the bottom of the screen
	JButton JBHelp= new JButton("How To");  // button that will bring up a text field on how to play
	JButton JBNewGame= new JButton("New Game");  // button that will restart the game and shuffle tiles
	JButton JBShuffle= new JButton("Shuffle"); // button will shuffle board without restarting game
	JButton JBSave= new JButton("Save Game"); // button saves game
	JButton JBLoadGame= new JButton("Load Game"); // loads a saved game
	JButton JBExit= new JButton("Exit"); // shows a high score list
	JButton JOK= new JButton("OK"); // generic button that says ok for various uses
	
	JLabel time= new JLabel("");  // designated label to display time of the game
	JComboBox themes = new JComboBox(new Object[] {"Choose a Theme","Easy Eyes", "Matrix", "Bamboo Garden", "Cosmic Bowling", "Randomized"});
	JComboBox difficulty= new JComboBox(new Object[] {"Select Difficutly","Easy", "Medium (default)", "Hard"});
	 
	// extra panels for decorative use
	 JPanel top= new JPanel(); // panel covers entire game screen and allows color to be changed around tiles
	
	 boolean isWinner=false; // This holds if the game is a winner
	 int clickedAt=-1;  // hold the table position of a clicked button
	 int zeroAt=-1; // hold the table position of the zero button
	 int xClicked=-1;    // hold the x and y coordinates of the clicked and zero piece
	 int yClicked=-1;
	 int xZero=-1;
	 int yZero=-1; 
	 
	 int timeInSeconds=0;  // variables hold the number of seconds, minutes and hours played in a game
	 int timeInMinuets=0;  // note that the word minute is misspelled, if you use it, makes sure you spell as it is here
	 int timeInHours=0;
	 int totalTime=0;
	 
	 private Timer timer= new Timer(1000, new timerListener());  // time event listener
	  
	 /*  Table Position
	  * Table position is the location on the board according to the following chart. variables clickedAt and zeroAt will use this number
	  * while xClicked and yZero hold the coordinates of the positions
	  * 
	  * 		------------------
	  * 		| 0 | 1 | 2 | 3  |
	  * 		| 4 | 5 | 6 | 7  |
	  * 		| 8 | 9 | 10| 11 |
	  * 		| 12| 13| 14| 15 |
	  *      	------------------
	  *      
	  *     Note that these positions don't change, even though the text inside them will. 
	  */
	
	// ends data field
	
	  
	  
	public FifteenSquare()  
	{
		// edit frame that hold the pieces
		// note the actual creation of many panels and buttons is in the data field 
		
		mainWindow.setSize(500,625);
	    mainWindow.setLayout(null);
		
	    // create the panel that holds the tiles
	  
		p0.setSize(400,400); // this will be just big enough to hold the squares
		p0.setLayout(null);
		p0.setLocation(50,50); // panel won't hug the upper left corner, it will be slightly centered
		
		// edit the panel the hold the helper buttons
		p1.setSize(500,175);
		p1.setLayout(null);
		p1.setLocation(0,475); // sets the panel at the bottom of the main game panel
		mainWindow.add(p0);
		mainWindow.add(p1);
	
		
	    // edit helper buttons
		
		JBHelp.setSize(100,50);	
		JBHelp.setLocation(0,0);
	
		JBNewGame.setSize(100,50);
		JBNewGame.setLocation(200,0);
			
		JBShuffle.setSize(100,50);
		JBShuffle.setLocation(200,50);
		
		JBSave.setSize(100,50);
		JBSave.setLocation(100,50);
		
		JBLoadGame.setSize(100,50);
		JBLoadGame.setLocation(100,0);
		
		JBExit.setSize(100,50);
		JBExit.setLocation(0,50);
		
		JOK.setSize(100,50);
		JOK.setLocation(50,50);
		loadError.add(errorText);
		errorText.add(JOK);
		
		// add helper buttons to the p1 panel
		p1.add(JBExit);  
		p1.add(JBLoadGame);
		p1.add(JBSave);
		p1.add(JBShuffle);
		p1.add(JBNewGame);
		p1.add(JBHelp);
		
		
		// sets an error box to display when there is an error
		 loadError.setSize(200,200);
		 errorText.setSize(200,200);
		 errorText.setEditable(false);
		 errorText.setLocation(0,0);
		 loadError.add(errorText);
		
		// edits combo boxes, not difficulty is not currently displayed due to issues in programming it's functionality
		themes.setSize(150,25);
		themes.setLocation(50,0);
		difficulty.setSize(150,25);
		difficulty.setLocation(250,0);
	
	
		top.setLayout(null);
		top.add(themes);
		top.add(difficulty);  // off until the functionality works, see setDifficulty method for more info
		
		
		// edits the large panel that covers the entire frame
		top.setSize(500,650);  
		top.setLocation(0,0);
		
		
		// registers the combo boxes with ItemListeners 
		
		themes.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e){
				setDisplay(themes.getSelectedIndex());
			}
		});
		
		difficulty.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e){
				setDifficulty(difficulty.getSelectedIndex());
			}
		});
	
		
		mainWindow.add(top);  // adds the large panel to the frame
		
	
		// creates listeners for the helper buttons
		numberListener listener1= new numberListener();   
		startListener listener2= new startListener(); 
		help helpListener= new help();
		saveGame saveListener= new saveGame();
		loadGame loadListener= new loadGame();
		shuffleGame shuffleListener= new shuffleGame();
		exitListener exitListener= new exitListener();
		OKListener okListener= new OKListener();
	
		//registers listeners with the helper buttons
		JBNewGame.addActionListener(listener2);   // register the listeners with their buttons
		JBShuffle.addActionListener(shuffleListener);
		JBHelp.addActionListener(helpListener);
		JBExit.addActionListener(exitListener);
		JBLoadGame.addActionListener(loadListener);
		JBSave.addActionListener(saveListener);
		JOK.addActionListener(okListener);
	
		// sets size and location of the status label, which holds the number of moves
		status.setSize(150,50);  
		status.setLocation(350,0);
		
		// sets size and location of the time label, which shows the amount of time played
		time.setText("Time: 0");
		time.setSize(150,50);
		time.setLocation(350,50);
		p1.add(time);
		p1.add(status);
	
		pGamesPlayed++;
		setArray();
		callStats(); // method calls ups statistics
		shuffle();  // method shuffles the text on the pieces
		blankSpace();  // turns the zero button invisible
		mainWindow.setVisible(true);  // sets the frame visible
			
	} // ends FifteenSquare constructor
	
	public static void main(String[] args)   
	{	
		FifteenSquare frame= new FifteenSquare();
		
	} // ends main method
	
// ============================================================================================================================
// ============================================================================================================================
//			LISTENERS  				LISTENERS				LISTENERS				LISTENERS
// ============================================================================================================================
// numberListener - startListener - loadGame- saveGame - shuffleGame- help - OKListener- timerListener - exitListener
// ============================================================================================================================
	
	

	class numberListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)   // this reacts to any numbered button being pushed, regardless of text
		{
		
			
			timer.start();  // starts the timer, which doesn't automatically run
			JButton holdButton=(JButton)e.getSource();   // this helps identify which button was pushed
			zeroAt=findZero();  // finds where the blank spot is on the board
		
			if (!isWinner) // blocks pieces from moving is the game is already won
			{
				for (int i=0; i<gameSize; i++)
				{							// loop performs a linear search to determine which button was clicked
					for (int j=0; j<gameSize; j++)
					{
						if (buttonArray[j][i].getText()==holdButton.getText() )
						{
							xClicked=j;
							yClicked=i;
							clickedAt=findNum(j,i);  // assigns the number of the clicked button to the variable for use
							
							if (xClicked==xZero || yClicked==yZero) // checks if the clicked tile is valid
							{
							moveTiles(); // determines how to move, if at all
							turnCount++;  // increments the number of moves made
							}
							
						} // ends if
					} // ends j loop
				} //ends i loop
				
			} // ends !isWinner if
			
			status.setText("Moves:"+ turnCount); // updates the display for how many moves have been played
			
			// checks if a winning move was made		
			if(isSolved()) // runs if the puzzle is complete
			{
				//winner protocol
				timer.stop();
				isWinner=true;
				// display a new frame that tells the player they've won and offer options
				
				totalTime= (timeInHours*3600) + (timeInMinuets*60)+ timeInSeconds; // determines the length of the game in seconds
				JButton OKW= new JButton("Start a new game");  // create a button that will start a new game 
				JButton exitW= new JButton("Close 15-Square"); // creates a button that will close the game
				
				JTextArea winText=new JTextArea(" \n"             // creates text for the win box
						+ "	Congratulations! You Won The Game"
						+ "\n"
						+ "\n"
						+ "	Time of Play: " + totalTime 
						+ "\n" 
						+ "	Move: " + turnCount
						+ "\n"
						+ "\n"
						+ "	Would You Like to Play Again?");   // ends the text area 
				
				
				// settings for the win box
				winBox.setLayout(null);  
				winBox.setSize(350,250);
				winBox.setDefaultCloseOperation(winBox.EXIT_ON_CLOSE);
				OKW.setSize(150,50);
				exitW.setSize(150,50);
				winText.setSize(350,250);
				winText.setLocation(0,0);
				OKW.setLocation(25,150);
				exitW.setLocation(180,150);
				
				// create/register listeners for the included buttons in the winbox
				startListener newGame= new startListener();
				OKW.addActionListener(newGame);
				exitListener exitGame= new exitListener();
				exitW.addActionListener(exitGame);
			
				// add components to the win box
				winBox.add(OKW);
				winBox.add(exitW);
				winBox.add(winText);	
				winBox.setVisible(true);
				
			}  // ends winning puzzle protocol
		
			repaint();
			
		} // ends method	
		
	} // ends numberListener class
	
	
	class startListener implements ActionListener  // class triggers a new game by shuffling pieces and  restarting move counter
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			timer.stop();  // stops the timer
			
			// clear any open frames
			winBox.dispose();  	
			helpBox.dispose();
			loadError.dispose();
			
			// reset the board and variables
			shuffle();   
			turnCount=0;
			timeInSeconds=0;
			timeInMinuets=0;
			timeInHours=0;
			pGamesPlayed++;
			isWinner=false;
			status.setText("Moves:"+turnCount);
			time.setText("Time: - - -");
			repaint();
			
		} // ends actionPerformed
	} // ends startListener
	
		
	class loadGame implements ActionListener 
	{
		/* loadGame takes a saved game and prints it to the board.
		 loadGame does not distinguish between save files for games of different difficulties
		 so the game must already be set the appropriate difficulty. If it is not, a box will appear
		 telling the user to set it to that difficulty. This action is performed in the called method loadGame, not the class
		 */
		
		@Override
		public void actionPerformed(ActionEvent e)  
		{
			timer.stop();  
			try  // run the loadGame method and catch any exceptions
			{   
				
				loadGame();    // load the game, this is where exceptions will occur
				blankSpace();  // sets the 0 button invisible 
				timer.start(); // restarts the timer
			}
			catch(EOFException ex)  // exception runs if the file reaches it's end
			{
				{
					
					 errorText.setText("\n "
					 		+ "\n"
					 		+ " (1) Error Occured, Game Could Not Be Loaded!");
					 loadError.setVisible(true);
				}
			}
			 catch (IOException ex)
			 {
				 loadError.setSize(300,200);
				 errorText.setText("\n "
				 		+ "\n"
				 		+ " Error Occured, There Is No Game Saved!");
				 loadError.setVisible(true);
				 
			 }
			catch(ClassNotFoundException ex)
			{
				 loadError.setSize(300,200);
				 errorText.setText("\n "
				 		+ "\n"
				 		+ " (2) Error Occured, Game Could Not Be Loaded!");
				 loadError.setVisible(true);
			}		
			
		} // ends actionPerfoemed Method	
	} // ends loadGame class
	
	class saveGame implements ActionListener  
	{
		@Override
		
		// saveGame class is a listener that runs the saveGame method when the Save Game button is clicked
		public void actionPerformed(ActionEvent e)
		{
			
			timer.stop();
			
			try
			{
				saveGame();  // run the saveGame method, this is where exceptions may occur
				
				// if saveGame runs successfully, display a box t let the player know the game has been saved
				
				
				  errorText.setText("\n "
				 		+ "\n"
				 		+ "  Game Saved Sucessfully!");
				  errorText.add(JOK);
				  
				 loadError.setVisible(true);
			}
			catch(IOException ex)  //if exceptions occur, a box occurs telling the user it was unable to save the game
			{						// each error is numbered so the user can report it if it occurs.
			
				 
				  errorText.setText("\n "
				 		+ "\n"
				 		+ " (1) Error Occured, Game Failed To Save!");
				 loadError.setVisible(true);
			}
			catch(ClassNotFoundException ex)
			{
				 
				 errorText.setText("\n "
				 		+ "\n"
				 		+ " (2) Error Occured, Game Failed To Save!");
				
				 loadError.setVisible(true);
			}
		
		} // ends actionPerformed method
	} // ends saveGame class
	
	class shuffleGame implements ActionListener
	{
		@Override
		
		// shuffleGame class acts as a listener that shuffles the game without restarting it
		// this listener should not be confused with startListener which starts a new game
		
		public void actionPerformed(ActionEvent e) 
		{
			timer.start();
			shuffle();   // shuffles the tiles
			turnCount++; // increments the number of moves made
			status.setText("Moves:"+turnCount); // updates the display for the number of turns
			
		} // ends actionPerformed
	} // ends shuffleGame

	
	class help implements ActionListener
	{
		// help class acts as a listener for a button that says How To
		// button will tell the user how to play the game
		@Override
		public void actionPerformed(ActionEvent e)
		{
			timer.stop(); // stops the timer while the help box is up
			
			// sets the size/ layout of the box that pops up
			helpBox.setSize(700,300); 
			helpBox.setLayout(null);
			
			// text area details how to play the game
			JTextArea helpText= new JTextArea("Hello, Welcome to 15 Square."
					+ "\n"
					+ "\n"
					+ "*The objective of this game is to arrange all the tiles in order from 1-15"
					+ "\n"
					+ "with the empty space in the bottom right."
					+ "\n"
					+ "\n"
					+ "*There is no limit on time or the number of moves."
					+ "\n"
					+ "\n"
					+ "Buttons:"
					+ "\n"
					+ "Shuffle: Shuffles the tiles on the board, but costs move"
					+ "\n"
					+ "New Game: Restarts the game with new tiles, move count, and timer"
					+ "\n"
					+ "Save Game: Saves your current game"
					+ "\n"
					+ "Load Game: Loads the most recent save"
					+ "\n"
					+ "Exit: Exits The Game Without Saving");
			
			// text area displays stats for the game
			JTextArea statText= new JTextArea("\n"
					+ "\n"
					+ "\n"
					+ "Stats"
					+ "\n"
					+ "\n"
					+ "Games Played: " + pGamesPlayed
					+ "\n"
					+ "Games Won: " + pGamesWon
					+ "\n"
					+ "Time Played: " +pTimePlayed
					+ "\n"
					+ "Moves Made: " + pMovesMade);
			
		
			helpText.setEditable(false);   // makes it so the user cannot write in the text area
			
			//JLabel helpText= new JLabel("Hello, welcome to 15 square. \n  The point of the game is to arrange the squares" +
			//"from left to right and top to bottom, with 1 at the top left corner and the empty space in the bottom right.");
			helpText.setSize(450,300);
			helpText.setLocation(0,0);
			statText.setSize(250,300);
			statText.setLocation(450,0);
			
			// edits the buttons on the helpBox
			    // create JOK button here if the data field version has issues
			JOK.setSize(100,50);
			JOK.setLocation(225,200);
			
			// registers the buttons on the listeners
			OKListener helpOK = new OKListener();
			JOK.addActionListener(helpOK);
			
			// add the buttons to the helpBox frame
			helpBox.add(JOK);
			helpBox.add(helpText);
			helpBox.add(statText);
			helpBox.setVisible(true);
			
		} // ends actionPerformed
	} // ends help
	
	class OKListener implements ActionListener
	{
		// listener for buttons that say ok, and will close a frame
		@Override
		public void actionPerformed(ActionEvent e)  // this listener is used to process ok buttons in the helper boxes
		{
			timer.start(); // restarts the timer
			helpBox.dispose();
			loadError.dispose();
		} // ends actionPerformed
	} // ends OKlistener
	class timerListener implements ActionListener
	{
		// class acts as a listener for a timer that tells the user how long they've been playing
		@Override
		public void actionPerformed(ActionEvent e)
		{
			
			if (turnCount != 0)
			{
				timeInSeconds++;  // increments every time the listener is triggered, which is every seconds
			
				if (timeInSeconds>=60)    // increments minute count each time the seconds count hits 60
				{
					timeInMinuets++;
					timeInSeconds=0;
				}
				else if (timeInMinuets>=60)  // increments the hour count every time the minute count hits 60
				{
					timeInMinuets=0;
					timeInHours++;
				}
			
				// calculates the total time, this variable is used in load and save methods
				totalTime=(timeInHours*3600) + (timeInMinuets*60)+ timeInSeconds;  
			
			
				// displays time based on duration of play
			
				if(timeInSeconds<60 && timeInMinuets==0 && timeInHours==0)  
				{
					time.setText("Time: " + timeInSeconds + " s");
				}
				else if (timeInMinuets>0 && timeInHours==0)
				{
					time.setText("Time: " + timeInMinuets+ " min  "+ timeInSeconds+ " s");
				}
				else
				{
					time.setText("Time: " + timeInHours+ " hrs  "+ timeInMinuets+ " min  "+ timeInSeconds + " s");
				}
				repaint();
			} // end turnCount if
			else
			{
				// if turnCount is 0, do nothing
			}
		} // ends actionPerformed
	} // ends timerListener

	class exitListener implements ActionListener
	{
		@Override
		
		// class acts as a listener for helper helper boxes to close the game
		public void actionPerformed(ActionEvent e)
		{
			// disposes any open frames including the game
			saveStats();
			winBox.dispose(); 
			helpBox.dispose();
			mainWindow.dispose();
			loadError.dispose();
		} // ends actionPerformed
	} // ends exitListener
	


	
	// ends listener classes
	
// ================================================================================================================================
// ================================================================================================================================
//						METHODS				METHODS				METHODS
//=================================================================================================================================
// shuffle - blankSpace - setButtonsOff - row - col- isSolved- findZero - findNum - moveTiles- loadGame
//  - saveGame- setDisplay - setDifficulty - setTheme - randomShade - setArray- callStats- saveStats
//=================================================================================================================================
	
	 
	 public void shuffle()
	 {
		 
		 /*
		  * The shuffle method has the responsibility of shuffling the tiles on the board. 
		  * The shuffle can not be done a strictly random manner by swapping the text on the tiles
		  * Instead, the method must simulate numerous clicks so the the pieces have moved in way that
		  * is consistent with the winning configuration. Half of all configurations are not winnable, so
		  * each configuration must set from the winning position. 
		  * 
		  * When the method runs, it clears the current iteration of the board and sets it to the default configuration
		  */
		 
		 
		 // improved loop for random shuffle eliminates non-moving clicks by setting the row or col of the simulated click (idea courtesy of Andrew)
		
		 zeroAt=findZero();
		 xZero=row(zeroAt);  // find the x and y value of the current 
		 yZero=col(zeroAt);
		 int holdNum=-1;
		 
		 for(int i=0; i<500; i++)
		 {
			 
			 holdNum=(int)(Math.random()*11);  // produce a random number from 0-10
			 
			 if(holdNum>=0 && holdNum <5)   // set either the row or column based on the value of holdNum
			 {
				 xClicked=xZero;		 
			 }
			 else
				 yClicked=yZero;
			 
			 // then determine the other value of the simulated click
			 while(true)
			 {
				 
				 holdNum=(int)(Math.random()*6); // produces a new random number, the most it can be is 5
			
				 if (holdNum<gameSize && holdNum >=0)  // if this criteria isn't met, produce a new number
				 {
					 if (xClicked==xZero && holdNum!=yZero) // conditions check to make sure that the simulated click is in the same row or column as the zero
					 {	 							// and is not the same button as button as the zero. Once this is met, the while loop breaks and the 
						 yClicked=holdNum;
						 break;
					 }											
					 else if(yClicked==yZero && holdNum!=xZero) 
					 {
						 xClicked=holdNum;
						 break;
					 }
					 else
					 {
						 continue;
					 }
					 					 
				 } // ends outermost if
				 else
					 continue;
			 } // ends while loop
			 
			 moveTiles(); // executes the simulated click based on the parameters determined
			 zeroAt=findZero();
			 xZero=row(zeroAt);  // find the x and y value of the current 
			 yZero=col(zeroAt);
			
			 
		 } // ends for loop
		 
		 
		 
	 }// end shuffle method
	
	 private void blankSpace() // turns off the zero Button and turns all others on
	 {
		 for (int i=0; i<gameSize; i++)
			{
				for (int j=0; j<gameSize; j++)
				{
					if (buttonArray[j][i].getText().equals("0")) // checks if a button has text of 0
						buttonArray[j][i].setVisible(false); // if it does, turn the button off
					else 
						buttonArray[j][i].setVisible(true); // else ensure that it is visible
						
				} // ends j loop
			} // ends i loop
		}// ends blankSpace	
	 
	 public void setButtonsOff()  // method turns off any buttons in the grid
	 {
			for(int i=0; i<gameSize;i++)  // loop turns off all current buttons
			{
				for(int j=0; j<gameSize;j++)
				{
					buttonArray[j][i].setVisible(false);
							
				}
			}
	 }
	 
	 
	 private int row(int number) // this method will take a table posistion 0-15 and return it's x coordinate
	 {
		if(gameSize==3) // find the value if a game is on easy
		{
			if(number==0 || number ==3 || number==6 )        return 0;
			 else if (number==1 || number==4 || number==7 )  return 1;
			 else if (number ==2 || number==5 || number==8 ) return 2;
			 else                                            return 0;
		 
		}
		else if(gameSize==4) // finds the value if the game is on medium
		{
			if(number==0 || number ==4 || number==8 || number==12)          return 0;
			 else if (number==1 || number==5 || number==9 || number==13)    return 1;
			 else if (number ==2 || number==6 || number==10 || number ==14) return 2;
			 else if (number == 3 || number ==7 || number==11 || number==15)return 3;
			 else 															return 0;
		}
		else if(gameSize==6) // finds the value if the game is on hard
		{
			if		  (number==0 || number ==6 || number==12|| number==18 || number==24 || number==30)    return 0;
			 else if (number==1 || number==7 || number==13 || number==19 || number==25 || number==31)	  return 1;
			 else if (number==2 || number==8 || number==14 || number ==20 || number==26 || number==32)    return 2;
			 else if (number==3 || number==9 || number==15 || number==21 || number==27 || number==33)     return 3;
			 else if(number==4 || number==10 || number==16 || number==22 || number==28 || number==34)     return 4;
			 else if(number==5 || number==11 || number==17 || number==23 || number==29 || number==35)     return 5;
			 else                                                                                         return 0;
		}
		else
			return 0;
	 } // ends row method
	 
	 private int col(int number) // this method will take a table position and return it's y coordinate with respect to the board
	 {
		 
		 if (gameSize==3)  // finds the value if the game is on easy
		 {
			 if(number==0 || number ==1 || number==2 )       return 0;
			 else if (number==3 || number==4 || number==5 )  return 1;
			 else if (number ==6 || number==7 || number==8 ) return 2;
			 else                                            return 0;
			 
		 }
		 else if(gameSize==4) // finds the value if the game is on medium
		 {
		 if(number==0 || number ==1 || number==2 || number==3)		       return 0;
		 else if (number==4 || number==5 || number==6 || number==7)        return 1;
		 else if (number ==8 || number==9 || number==10 || number ==11)    return 2;
		 else if (number == 12 || number ==13|| number==14 || number==15)  return 3;
		 else                                                              return 0;
		 }
		 else if (gameSize==6) // finds the value is the game in on medium
		 {
			 if		  (number==0 || number ==1 || number==2|| number==3 || number==4 || number==5)       return 0;
			 else if (number==6 || number==7 || number==8 || number==9 || number==10 || number==11)      return 1;
			 else if (number==12 || number==13 || number==14 || number ==15 || number==16 || number==17) return 2;
			 else if (number==18 || number==19 || number==20 || number==21 || number==22 || number==23)  return 3;
			 else if(number==24 || number==25 || number==26 || number==27 || number==28 || number==29)   return 4;
			 else if(number==30 || number==31 || number==32 || number==33 || number==34 || number==35)   return 5; 
			 else
				 return 0;
		 }
		 else 
			 return 0;
	 }
	 private boolean isSolved()  // method checks if the current configuration has solved the puzzle, returns true if it is
	 {
		 if (turnCount<=0) // return false immediately if there have been no moves made in the game
			 return false;
		 
		 int text=1;  // decalres an int to be used as a holder value that will be incremented after each i loop
		Integer holdText= new Integer(text); // Integer hold the value of text as a String
			
		 for (int i=0; i<gameSize; i++)
		 {
			 for (int j=0; j<gameSize; j++)
			 {
					 
				 // checks that each button matches the approriate text, use holdText to increment this value accordingly
			   if (buttonArray[j][i].getText().equals(holdText.toString()) && !( i == gameSize-1 && j == gameSize-1) )
				 {
					holdText++; 
					System.out.println("isSolved");
				 }
				 else if(i== gameSize-1 && j==gameSize-1) // this check will only run for the bottom right tile on the board
				 {
					 if (buttonArray[gameSize-1][gameSize-1].getText().equals("0") ) // if the bottom right tile is the zero button, then continue loop
					 {
						 
					 } // ends if
					 else
						 return false; // else if the bottom right is not zero, return false
				 } // ends else if
				 else
					 return false; 
			
			   
			 } //ends j loop
		 } // ends i loop
		 
		  
		
		 pGamesWon++;  // increments the count of games won
		 saveStats();  // saves statistics on the game 
		 return true;
		 
	 } // ends isSolved
	 
	 private int findZero() // finds which index holds the zero button and returns the table position of zero
	 {
		 
		 for (int i=0; i<gameSize; i++)  // i and j loop determine where the zero is located
			{
				for (int j=0; j<gameSize; j++)
				{
					if(buttonArray[j][i].getText().equals("0"))
					{
						xZero=j;  // the x and y value of the zero button are the j and i indexes of the array
						yZero=i;
						break;
					} // ends if
						
				} // ends j loop
			} // ends i loop
		 
		 	// create determine the appropriate number to return based on x and y
		 	zeroAt=findNum(xZero,yZero); // returns the table position based on the x and y of the zero button
		 	return zeroAt;
	 } // ends findZero
	 
	 private int findNum(int x, int y)
	 {
		 if (gameSize==3) // find the value for a game on easy
		 {										// to save space and make it more readable, if and else ifs are placed on a single line
			 if(x==0 && y==0)       return 0;
			 else if (x==0 && y==1) return 3;
			 else if (x==0 && y==2) return 6;
			 else if (x==1 && y==0) return 1;
			 else if (x==1 && y==1) return 4;
			 else if (x==1 && y==2) return 7;
			 else if (x==2 && y==0) return 2;
			 else if (x==2 && y==1) return 5;
			 else				    return 8;
			 
		 }
		 else if (gameSize==4) // finds the value for a game on easy
		 {
		 if(x==0 && y==0)       return 0;
		 else if (x==0 && y==1) return 4;
		 else if (x==0 && y==2) return 8;
		 else if (x==0 && y==3) return 12;
		 else if (x==1 && y==0) return 1;
		 else if (x==1 && y==1) return 5;
		 else if (x==1 && y==2) return 9;
		 else if (x==1 && y==3) return 13;
		 else if (x==2 && y==0) return 2;
		 else if (x==2 && y==1) return 6;
		 else if (x==2 && y==2) return 10;
		 else if (x==2 && y==3) return 14;
		 else if (x==3 && y==0) return 3;
		 else if (x==3 && y==1) return 7;
		 else if (x==3 && y==2) return 11;
		 else 					return 15;
		 
		 }
		 else if( gameSize==6) // checks for a game on hard
		 {
			 if(x==0 && y==0)       return 0;
			 else if (x==0 && y==1) return 6;
			 else if (x==0 && y==2) return 12;
			 else if (x==0 && y==3) return 18;
			 else if (x==0 && y==4) return 24;
			 else if (x==0 && y==5) return 30;
			 else if (x==1 && y==0) return 1;
			 else if (x==1 && y==1) return 7;
			 else if (x==1 && y==2) return 13;
			 else if (x==1 && y==3) return 19;
			 else if (x==1 && y==4) return 25;
			 else if (x==1 && y==5) return 31;
			 else if (x==2 && y==0) return 2;
			 else if (x==2 && y==1) return 8;
			 else if (x==2 && y==2) return 14;
			 else if (x==2 && y==3) return 20;
			 else if (x==2 && y==4) return 26;
			 else if (x==2 && y==5) return 32;
			 else if (x==3 && y==0) return 3;
			 else if (x==3 && y==1) return 9;
			 else if (x==3 && y==2) return 15;
			 else if (x==3 && y==3) return 21;
			 else if (x==3 && y==4) return 27;
			 else if (x==3 && y==5) return 33;
			 else if (x==4 && y==0) return 4;
			 else if (x==4 && y==1) return 10;
			 else if (x==4 && y==2) return 16;
			 else if (x==4 && y==3) return 22;
			 else if (x==4 && y==4) return 28;
			 else if (x==4 && y==5) return 34;
			 else if (x==5 && y==0) return 5;
			 else if (x==5 && y==1) return 11;
			 else if (x==5 && y==2) return 17;
			 else if (x==5 && y==3) return 23;
			 else if (x==5 && y==4) return 29;
			 else                   return 35;
			 
		 }
		 else
			 return 0;
	 } // ends findNum
	 
	 private void moveTiles() // method determines how to move pieces and is not responsible for incrementing turnCount 
	 {
		 /*
		  *  method is responsible for moving tiles, to do this, the zero tile is swapped with a tile closer to the clicked button
		  *  and then again with any other button closer to the clicked button until it is in the spot originally 
		  *  occupied by the clicked button. So each time a tile is slid in the game, this method begins with the zero button
		  *   
		  *  it is possible to move more than one tile at a time, the this process is ideal to cover that possibility.
		  *  
		  *  it is worth noting that the method is called by both the number listener and the shuffle method. So this 
		  *  method executes both real and simulated clicks for shuffling, as such it cannot be responsible for
		  *  incrementing the turnCount.
		  */
		
		 JButton hold = new JButton("F");  // a button used to temporarily hold a button that will be swapped
		 
		 if(xClicked==xZero && yClicked>yZero) // buttons are in the same column and the clicked is below the zero
		 {
				 
			 for(int i=yZero; i<yClicked; i++)
			 {
				 if(i+1 < gameSize) // prevents an out of bounds exception
				 {
					 hold.setText(buttonArray[xZero][i].getText());
					 buttonArray[xZero][i].setText(buttonArray[xZero][i+1].getText());
					 buttonArray[xZero][i+1].setText(hold.getText());
				 }
			 } // ends for loop
		
		 } // ends outer if
		 else if(xClicked==xZero && yClicked<yZero) // buttons are in the same column and the clicked is above zero
		 {
			 
			 for (int i=yZero; i>yClicked;i--)
			 {
				 if (i-1>=0)
				 {
					 hold.setText(buttonArray[xZero][i].getText()); // hold the zero button
					 buttonArray[xZero][i].setText(buttonArray[xZero][i-1].getText()); // replaces zero with the button above it
					 buttonArray[xZero][i-1].setText(hold.getText());  // replaces the button above with zero
				 }
			
			 } // ends for loop
			
		 } // ends else if
		 else if (yClicked==yZero && xClicked>xZero) // buttons are in the same row and clicked is right of the zero
		 {
			 
			 for (int i=xZero; i<xClicked;i++)
			 {
				 if (i+1<gameSize)
				 {
					 hold.setText(buttonArray[i][yZero].getText());
					 buttonArray[i][yZero].setText(buttonArray[i+1][yZero].getText());
					 buttonArray[i+1][yZero].setText(hold.getText()); 
				 }
			
			 } // ends for loop
			 
			 
		 } // ends else if
		 else if (yClicked==yZero && xClicked<xZero) // button are in the same row and clicked is left of the zero
		 {
			
			 for (int i=xZero; i>xClicked;i--)
			 {
				 if (i-1>=0)
				 {
					 hold.setText(buttonArray[i][yZero].getText());
					 buttonArray[i][yZero].setText(buttonArray[i-1][yZero].getText());
					 buttonArray[i-1][yZero].setText(hold.getText()); 
				 }
			
			 } // ends for loop
			
		 } // ends else if
		 else  
		 {
			// if none of the above conditions hit, the move is invalid and does not move any tiles
		 } // ends else 
		 
		 blankSpace();
		
	 } // end moveTiles
	 
	
	 private void loadGame() throws IOException, ClassNotFoundException, EOFException, NullPointerException
	 {
		 	/*
		 	 * loadGame calls up the save.dat file to recall a previously saved game
		 	 * 
		 	 * the gameSize is read first and is used to make sure that the correct difficulty is set
		 	 * then the text on each button is loaded, followed by the turnCount, and totalTime variables
		 	 */
		
		ObjectInputStream input= new ObjectInputStream(new FileInputStream("save.dat"));
		
		
		
		setButtonsOff(); // turn off the current array
		
		
		if (input.readInt() != gameSize)  // checks if the current difficulty is the same as on the saved file
		{									// if not, a box pops up telling the user to change the difficulty
			
			
			errorText.setText("\n "
			 		+ "\n"
			 		+ " Error Occured, Game Could Not Be Loaded!"
			 		+ "\n"
			 		+ " Make Sure The Game Is Set To Correct Difficulty");
			
			loadError.setSize(300,200); 
			errorText.setSize(300,200);
			 JOK.setLocation(50,75);
			 errorText.add(JOK);
			 JOK.setVisible(true);
			 loadError.setVisible(true);
		}
		else  // if the correct difficulty is set, the proper text is read in
		{
		for( int i=0; i<gameSize; i++)
		{
			for (int j=0; j<gameSize; j++)
			{
				// read the text to the button array
			buttonArray[i][j].setText(input.readUTF());
			}
		}
		
		totalTime= input.readInt(); // reads in the totalTime
		turnCount=input.readInt(); //                                                                                                                                                                                                                                                                                                                                                 .
		
		
		// calculate the time intervals based on totalTime
		timeInHours=totalTime/3600;
		timeInMinuets=(totalTime-(timeInHours*3600))/60;
		timeInSeconds= totalTime-(timeInHours*3600)-(timeInMinuets*60);
		
		// display a box that tell the user the game loaded successfully
		errorText.setText("  Game Loaded Sucessfully");
		loadError.setVisible(true);
		JOK.setVisible(true);
		input.close();
		repaint();
		} // ends else 
					
	 }// end loadGame
	 
	 private  void saveGame() throws IOException, ClassNotFoundException
	 {
		 	
		 ObjectOutputStream output= new ObjectOutputStream(new FileOutputStream("save.dat"));
		 //ObjectOutputStream output= new ObjectOutputStream(saveData);
		 
	
		 output.writeInt(gameSize);  // writes the game size to the file
		 						// this is used to ensure the right difficulty is set when loading a game
		 
		for( int i=0; i<gameSize; i++)
		{
			for (int j=0; j<gameSize; j++)
			{
			output.writeUTF(buttonArray[i][j].getText());  // write the text on each button to the appropriate location
			}
		}
			
		output.writeInt(totalTime);  // write the total time and number of turns used
		output.writeInt(turnCount);
		
		saveStats(); // if somebody saves the game, they are likely to exit, so save statistics
		output.close(); //closes the output stream
	 
	 } // ends saveGame
	 
	 private void setDisplay(int index)
	 {
		 // method receives input from the display combo box and changes the display accordingly
		 
		 if (index==1) // easy eyes display
		 {
			 //produces a display that is easy on the eyes, meant to replicate the default color
			 Color BABY_BLUE= new Color(164,251,247);
			 setTheme(BABY_BLUE, Color.BLACK, Color.WHITE, Color.BLACK);
			 p0.setBackground(Color.LIGHT_GRAY);
			 themeNumber=1; // holds this number if the board is resized
			 
		 }
		 else if (index==2) // matrix
		 {
			 // black and green theme to mimic sci-fi movies
			 setTheme(Color.GRAY, Color.WHITE, Color.BLACK, Color.GREEN);
			 themeNumber=2; // holds this number if the board is resized
			 	
		 }
		 else if (index==3) // bamboo garden
		 {
			 // oriental theme with a green jade color, and gold on red combinations
			 Color IMPERIAL_RED= new Color(140,10,10);
			 Color IMPERIAL_GOLD= new Color(238,223,26);
			 Color JADE = new Color(27,133,7);
			 setTheme(JADE, Color.BLACK, IMPERIAL_RED, IMPERIAL_GOLD);
			 p0.setBackground(Color.GRAY);
			 themeNumber=3; // holds this number if the board is resized
		 }
		 else if (index==4) // cosmic bowling
		 {
			 //  theme meant to mimic the colors of black light bowling, dark with bright neon colors
			 Color DARK_BLUE= new Color(6,20,142);
			 Color PURPLE= new Color(243,12,250);
			 setTheme(Color.BLACK, PURPLE, DARK_BLUE,PURPLE);
			 themeNumber=4; // holds this number if the board is resized
			
		 }
		 else if (index==5)  // randomized theme, sure to produce some ugly results 
		 {
			 // produce 4 new custom colors, shades are randomize by randomShade
			Color a= new Color(randomShade(), randomShade(), randomShade());  // produces a new color
			Color b= new Color(randomShade(), randomShade(), randomShade());
			Color c= new Color(randomShade(), randomShade(), randomShade());
			Color d= new Color(randomShade(), randomShade(), randomShade());
			
			setTheme(a,b,c,d); // call the method that automatically sets a theme based on input colors
			 themeNumber=5; // holds this number if the board is resized
			 
		 }
	 }
	 
	 private void setDifficulty(int index)
	 {
		 // method receives an input from the difficulty combo box and changes the number of tiles in the game
		// which makes the game easier or harder to play
		 if (index==1) // easy 3x3 grid
		 {
			 timer.stop();
			 setButtonsOff(); // turns off all the old buttons
			 gameSize=3; // hold the new size of the game
			 buttonSize=133; // hold the size of each button
			 pGamesPlayed++;
			 
			 if (turnCount != 0)  // increments the number of games played, only if the player made a move in the current game
				 pGamesPlayed++; 
			 
			 turnCount=0; 
			 timeInSeconds=0;
			 timeInMinuets=0;
			 timeInHours=0;	 
			 
			
			 p0.setSize(400,400);
			 setArray();  // sets a new array based on the new dimensions
			 shuffle(); //shuffle the array
			 setDisplay(themeNumber);
			 blankSpace();
			 
		 }
		 else if (index==2) // medium 4x4 grid, default
		 {
			 timer.stop();
			 setButtonsOff(); // turns off all the old buttons
			 gameSize=4; // hold the new size of the game
			 buttonSize=100; // hold the size of each button
			 pGamesPlayed++;
			 
			 if (turnCount != 0)  // increments the number of games played, only if the player made a move in the current game
				 pGamesPlayed++; 
			 
			 turnCount=0;
			 timeInSeconds=0;
			 timeInMinuets=0;
			 timeInHours=0;
			 
			 p0.setSize(400,400);
			 setArray();  // sets a new array based on the new dimensions
			 shuffle(); //shuffle the array
			 setDisplay(themeNumber);
			 blankSpace();
			 
		 }
		 else if (index==3) // hard 6x6 grid
		 {
			 timer.stop();
			 setButtonsOff(); // turns off all the old buttons
			 gameSize=6; // hold the new size of the game
			 buttonSize=67; // hold the size of each button
			 
			 if (turnCount != 0)  // increments the number of games played, only if the player made a move in the current game
				 pGamesPlayed++; 
			 
			 turnCount=0;
			 timeInSeconds=0;
			 timeInMinuets=0;
			 timeInHours=0;
			 pGamesPlayed++;
			 p0.setSize(402,402);
			 setArray();  // sets a new array based on the new dimensions
			 shuffle(); //shuffle the array
			 setDisplay(themeNumber);
			 blankSpace();
			 
		 } // ends else if
		
			
			 
	} // ends method
	 
	 private void setTheme(Color BG, Color text, Color buttonBG, Color buttonText)
	 {
		 // method sets a common theme based on common color, i.e if all buttons have the same background color and text
		 
		 
		 p1.setForeground(text);
		 p1.setBackground(BG);
		 top.setBackground(BG);
		 top.setForeground(text);
		 time.setForeground(text);
		 status.setForeground(text);
		 p0.setBackground(buttonText); // set this last to override the top JPanel
		 
		 JBHelp.setBackground(buttonBG);
		 JBHelp.setForeground(buttonText);
		 JBSave.setBackground(buttonBG);
		 JBSave.setForeground(buttonText);
		 JBShuffle.setBackground(buttonBG);
		 JBShuffle.setForeground(buttonText);
	     JBLoadGame.setBackground(buttonBG);
	     JBLoadGame.setForeground(buttonText);
	     JBNewGame.setBackground(buttonBG);
	     JBNewGame.setForeground(buttonText);
	     JBExit.setBackground(buttonBG);
		 JBExit.setForeground(buttonText);
		 
		 for (int i=0; i<gameSize; i++)
		 {
		
			 
			 for (int j=0; j<gameSize; j++)
			 {
				 buttonArray[j][i].setBackground(buttonBG);
				 buttonArray[j][i].setForeground(buttonText);
			 }
		 }// ends i loop
	 }
	 private int randomShade()
	 {
		 		// method creates a random shade to help produce a new random color
		 		// output should be a number between 0 and 255
		 int i=0; // hold the hundreds place, 0, 1 or 2
		 int j=0; // hold the tens place 0-5
		 int k=0; // hold the ones place 0-5
		 
		 while (true)  // this loop produces a random number 0-2
		 {
			  i= (int)(Math.random()*10);
			  if (i>=0 && i<= 3)
				  i=0;
			  else if(i>3 && i<=6)
				  i=1;
			  else
				  i=2;
			  
			  break;
		 }
		 
		 while (true) // this loop produces 2 random numbers 0-5 for use in j and k
		 {
			 j=(int)(Math.random()*10);
			 k=(int)(Math.random()*10);
			 
			 if (j<=5 && k<=5)
				 break;
		 }
		 
		 return (i*100+j*10+k); // adds the random numbers together to produce a number 0-255
	 }// ends randomShade
	 
	 private void setArray()
	 {
		 /*
		  * setArray creates a brand new JButton array and sets it's it to the winning posistion
		  * this method is used to create a new game, the array must be shuffled to play, however this process is called elsewhere
		  * so this method is only concerned with making the new array.
		  */
		// Assigns each button to the array
			int text=1;  // used as a way to add text to buttons
			JButton[][] holdArray= new JButton[gameSize][gameSize];
			
			
			// loops assigns the button in the table to text, registers the buttons and sets locations
			
			for (int i=0; i<gameSize; i++)
			{	
				for (int j=0; j<gameSize;j++)
				{
					Integer number= new Integer(text); // variable holds the text variable and converts it to a string to display on buttons
					holdArray[j][i]=new JButton();
					holdArray[j][i].setText(number.toString());
					holdArray[j][i].setSize(buttonSize,buttonSize);  // sets the size of all button to 100 x 100
					
					int x=j*buttonSize;    // hold the x and y location of each button
					int y=i*buttonSize;
					
					holdArray[j][i].setLocation(x,y);
					p0.add(holdArray[j][i]);  // adds the button to p0
				
					holdArray[j][i].addActionListener(new numberListener()); // registers the button with a number listener
					text++; // increments the text int for the next button
					
				} // ends j loop
			} // ends i loop
			holdArray[gameSize-1][gameSize-1].setText("0"); // sets the last JButton as the zero button (as required by the shuffle method)
			buttonArray=holdArray;
			zeroAt=findZero();
		
			blankSpace();
			
	 }// ends setArray
	 

	 
	
	 private void callStats() 
	 {
		 // method calls up statistics which are running sum from previous games
		 // method is only called from the constructor
		 try
		 {
		 FileInputStream statFile= new FileInputStream("stats.dat");
		 ObjectInputStream input= new ObjectInputStream(statFile);
		 	// read the values from the file
		 	pGamesPlayed=input.readLong();
		 	pTimePlayed=input.readLong();
		 	pMovesMade=input.readLong();
		 	pGamesWon=input.readLong();
		 	
		 	input.close();
		 }
		 catch (IOException ex)
		 {
			 
		 }
	 }
	 
	 private void saveStats()
	 {
		 // method saves stats to the save.dat file
		 // method is called whenver the player exits the game or the game is won
		 try
		 {
			 FileOutputStream statFile= new FileOutputStream("stats.dat");
			 ObjectOutputStream output= new ObjectOutputStream(statFile);
			 
			
			 	pTimePlayed+=(long)(timeInMinuets*60+timeInHours*3600+timeInSeconds);
			 	pMovesMade+= (long)turnCount;
			 	
			 	
			 	output.writeLong(pGamesPlayed);
			 	output.writeLong(pTimePlayed);
			 	output.writeLong(pMovesMade);
			 	output.writeLong(pGamesWon);
			 	output.close();
			
		 }
		 catch(IOException ex)
		 {
			 // if there is an exception, do nothing
		 }
	 }
	 
} // ends 15 Square Class 

