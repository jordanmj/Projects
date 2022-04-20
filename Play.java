import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
public class Play {
	static String PLAYER = "Blue";
	static final double[][][] PIECE_COORDINATES = {{{95,305},{165,305}, {235, 305},{305, 305}},
												{{95,235},{165,235},{235,235},{305,235}},
												{{95,165},{165,165},{235,165},{305,165}},
												{{95,95},{165,95},{235,95},{305,95}}};
	
	static final double[] XCORS	 = {95,165,235,305};
	static final double[] YCORS	 = {95,165,235,305};
	static final double[] TEXT_COORDINATE = {200,27};
	static final double[][] STACK_COORDINATES = {{100,550},{200,550},{300,550}};
	static final double[] PIECE_SIZE = {30,20,10,3};
	static final double[][] OBSTICLE_COORDINATES = {{200,300}};
	//number of obsticles for each player
	static int num_TransportersBlue= 2;
	static int num_TransportersGreen= 2;
	static int num_BombsBlue = 2;
	static int num_BombsGreen = 2;
	static int num_ShiftersBlue = 2;
	static int num_ShiftersGreen = 2;
	static int num_PaintbrushesBlue = 2;
	static int num_PaintbrushesGreen = 2;
	
	static final char TRANSPORTER = 't';
	static final char PAINTBRUSH = 'p';		
	static final char BOMB = 'b';
	static final char SHIFTER = 'r';
	static final char[] COMMANDS = {'A','B','C','a','d','s','w','t','p','b','r'};
	
	static char[] commandSeq;
	//stacks for each player 
	static ArrayList<Integer> Bstack1 = new ArrayList<Integer>();
	static ArrayList<Integer> Bstack2 = new ArrayList<Integer>();
	static ArrayList<Integer> Bstack3 = new ArrayList<Integer>();
	static ArrayList<Integer> Gstack1 = new ArrayList<Integer>();
	static ArrayList<Integer> Gstack2 = new ArrayList<Integer>();
	static ArrayList<Integer> Gstack3 = new ArrayList<Integer>();
	
	static HashMap<Integer, String> BluePieces = new HashMap<Integer, String>();
	static HashMap<String, Integer> BluePiecesRev = new HashMap<String, Integer>();
	static HashMap<Integer, String> GreenPieces = new HashMap<Integer, String>();
	static HashMap<String, Integer> GreenPiecesRev = new HashMap<String, Integer>();
	//game state constants
	static boolean valid = true;
	static boolean draw;
	static boolean blueWin;
	static boolean greenWin;
	static boolean quit;
	
	static double XCOR;
	static double YCOR;
	
	static String[][][] Board =  {{{"  ", " ","c1"," ","c2"," ","c3"," ","c4"," "},
									{"r1", " ","__","|","__","|","__","|","__","|"},
							        {"r2", " ","__","|","__","|","__","|","__","|"},
							 	    {"r3", " ","__","|","__","|","__","|","__","|"},
									{"r4", " ","__","|","__","|","__","|","__","|"}},
									     //2nd layer
								   {{"  ", " ","c1"," ","c2"," ","c3"," ","c4"," "},
								    {"r1", " ","__","|","__","|","__","|","__","|"},
									{"r2", " ","__","|","__","|","__","|","__","|"},
									{"r3", " ","__","|","__","|","__","|","__","|"},
								    {"r4", " ","__","|","__","|","__","|","__","|"}},
										 //3rd layer
								   {{"  ", " ","c1"," ","c2"," ","c3"," ","c4"," "},
								    {"r1", " ","__","|","__","|","__","|","__","|"},
								    {"r2", " ","__","|","__","|","__","|","__","|"},
									{"r3", " ","__","|","__","|","__","|","__","|"},
									{"r4", " ","__","|","__","|","__","|","__","|"}},
									     //4th layer
								   {{"  ", " ","c1"," ","c2"," ","c3"," ","c4"," "},
								    {"r1", " ","__","|","__","|","__","|","__","|"},
								    {"r2", " ","__","|","__","|","__","|","__","|"},
									{"r3", " ","__","|","__","|","__","|","__","|"},
									{"r4", " ","__","|","__","|","__","|","__","|"}}};

	static int fRow;
	static int fCol;
	static int tRow;
	static int tCol;
	static int row;
	static int col;
	static int stackNum;
	static int numMoving;
	static int dir;
	
	//use transporter
	public static void place_Transporter() {
		//check if valid
		
		if(PLAYER.equals("Blue")) {
			if(Board[0][row][col*2].equals("__") || Board[0][row][col*2].equals("B1") || Board[0][row][col*2].equals("B2")||Board[0][row][col*2].equals("B3")||Board[0][row][col*2].equals("B4")) {
				num_TransportersBlue--;
				System.out.println("Invalid move");
				return;
			}
		}
		else if(PLAYER.equals("Green")) {
			if(Board[0][row][col*2].equals("__") || Board[0][row][col*2].equals("G1") || Board[0][row][col*2].equals("G2")||Board[0][row][col*2].equals("G3")||Board[0][row][col*2].equals("G4")) {
				num_TransportersGreen--;
				System.out.println("Invalid move");
				return;
			}
		}
		//determine closest empty position
		closest_Empty();
		
		
 	}
	public static void closest_Empty() {
		int count = 0;
		//Math.abs(row-r) + Math.abs(col*2-c*2)
		ArrayList<Integer> distances = new ArrayList<Integer>();
		for(int i = 0; i < 1; i++) {
			for(int r = 1; r < Board.length; r++) {
				for(int c = 1; c < Board[r].length; c*=2) {
					if(Board[i][r][c].equals("__")) {
						count++;	
						distances.add(Math.abs(row-r) + Math.abs(col*2-c*2));
					}
				}
			}
		}
		int flag = 0;
		int[] dists = new int[count];
		for(int dist: distances) {
			while(flag < count) {
				dists[flag] = dist;
				flag++;
				break;
			}
		}
		for(int dist:dists) {
			System.out.println(dist);
		}
	}
	//Math.abs(row-r)+Math.abs(col*2-c*2)
	//display game board
	
	public static void display_Boxes() {

		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(400, 600, 400, 600);
		StdDraw.setPenRadius(0.005);
	    StdDraw.setPenColor(StdDraw.CYAN);
	    
	    StdDraw.line(0,420,400,420);
	    //display_Boxes();
	    StdDraw.setPenColor(StdDraw.ORANGE);
	    StdDraw.setPenRadius(0.004);
	    StdDraw.rectangle(200, 550, 150, 50);
	    
		for(int i = 0; i < PIECE_COORDINATES.length; i++) {
			for(int j = 0; j < 4; j++) {
				StdDraw.setPenColor(StdDraw.BLACK);
				double x = PIECE_COORDINATES[i][j][0];
				double y = PIECE_COORDINATES[i][j][1];
				
				StdDraw.square(x,y,30);
				
			}
		}
		
	}
	//show appropriate pieces for player
	public static void display_UIStacksBlue() {
		int val = Bstack1.size();
		switch(val) {
		case 4:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(100, 550, 30);
			StdDraw.text(100, 590, "A");
			break;
		case 3:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(100, 550, 20);
			StdDraw.text(100, 590, "A");
			break;
		case 2: 
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(100, 550, 10);
			StdDraw.text(100, 590, "A");
			break;
		case 1:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(100, 550, 3);
			StdDraw.text(100, 590, "A");
			break;
		case 0:
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledCircle(100, 550, 0);
			break;
		}
		val = Bstack2.size();
		switch(val) {
		case 4:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(200, 550, 30);
			StdDraw.text(200, 590, "B");
			break;
		case 3:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(200, 550, 20);
			StdDraw.text(200, 590, "B");
			break;
		case 2: 
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(200, 550, 10);
			StdDraw.text(200, 590, "B");
			break;
		case 1:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(200, 550, 3);
			StdDraw.text(200, 590, "B");
			break;
		case 0:
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledCircle(200, 550, 0);
			break;
		}
		val = Bstack3.size();
		switch(val) {
		case 4:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(300, 550, 30);
			StdDraw.text(300, 590, "C");
			break;
		case 3:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(300, 550, 20);
			StdDraw.text(300, 590, "C");
			break;
		case 2: 
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(300, 550, 10);
			StdDraw.text(300, 590, "C");
			break;
		case 1:
			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
			StdDraw.filledCircle(300, 550, 3);
			StdDraw.text(300, 590, "C");
			break;
		case 0:
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledCircle(300, 550, 0);
			break;
		}
		
	}
	public static void display_UIStacksGreen() {
		int val = Gstack1.size();
		switch(val) {
		case 4:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(100, 550, 30);
			StdDraw.text(100, 590, "A");
			break;
		case 3:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(100, 550, 20);
			StdDraw.text(100, 590, "A");
			break;
		case 2: 
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(100, 550, 10);
			StdDraw.text(100, 590, "A");
			break;
		case 1:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(100, 550, 3);
			StdDraw.text(100, 590, "A");
			break;
		case 0:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(100, 550, 0);
			break;
		}
		val = Gstack2.size();
		switch(val) {
		case 4:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(200, 550, 30);
			StdDraw.text(200, 590, "B");
			break;
		case 3:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(200, 550, 20);
			StdDraw.text(200, 590, "B");
			break;
		case 2: 
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(200, 550, 10);
			StdDraw.text(200, 590, "B");
			break;
		case 1:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(200, 550, 3);
			StdDraw.text(200, 590, "B");
			break;
		case 0:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(200, 550, 0);
			break;
		}
		val = Gstack3.size();
		switch(val) {
		case 4:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(300, 550, 30);
			StdDraw.text(300, 590, "C");
			break;
		case 3:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(300, 550, 20);
			StdDraw.text(300, 590, "C");
			break;
		case 2: 
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(300, 550, 10);
			StdDraw.text(300, 590, "C");
			break;
		case 1:
			StdDraw.setPenColor(StdDraw.GREEN);
			StdDraw.filledCircle(300, 550, 3);
			StdDraw.text(300, 590, "C");
			break;
		case 0:
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledCircle(300, 550, 0);
			break;
		}
	}
	public static void display_Text(String gameState) {
		
		StdDraw.setPenColor(StdDraw.BOOK_RED);
		StdDraw.text(200, 27, gameState);
		
	}
	public static void display_Images() {
		
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius(0.007);
		if(PLAYER == "Blue") {
			if(num_TransportersBlue > 0) {
				StdDraw.picture(95, 430, "transporter.png", 50, 50);
				StdDraw.text(95, 380, "X"+num_TransportersBlue, 20.0);
				StdDraw.text(95, 470, "t");
			}
			if(num_BombsBlue > 0) {
				StdDraw.picture(165, 430, "bomb.png", 50, 50);	
				StdDraw.text(165, 380, "X"+num_BombsBlue, 20.0);
				StdDraw.text(165, 470, "b");
			}
		    if(num_ShiftersBlue > 0) {
		    	StdDraw.picture(235, 430, "shifter.png", 50, 50);
		    	StdDraw.text(235, 380, "X"+num_ShiftersBlue, 20.0);
		    	StdDraw.text(235, 470, "r");
		    }
		    if(num_PaintbrushesBlue > 0) {
		    	StdDraw.picture(305, 430, "paintbrush.png", 50, 50);
		    	StdDraw.text(305, 380, "X"+num_PaintbrushesBlue, 20.0);
		    	StdDraw.text(305, 470, "p");
		    }
		}
		else if(PLAYER == "Green") {
			if(num_TransportersGreen > 0) {
				StdDraw.picture(95, 430, "transporter.png", 50, 50);
				StdDraw.text(95, 380, "X"+num_TransportersGreen, 20.0);
				StdDraw.text(95, 470, "t");
			}
			if(num_BombsGreen > 0) {
				StdDraw.picture(165, 430, "bomb.png", 50, 50);
				StdDraw.text(165, 380, "X"+num_BombsGreen, 20.0);
				StdDraw.text(165, 470, "b");
			}
		    if(num_ShiftersGreen > 0) {
		    	StdDraw.picture(235, 430, "shifter.png", 50, 50);
		    	StdDraw.text(235, 380, "X"+num_ShiftersGreen, 20.0);
		    	StdDraw.text(235, 470, "r");
		    }
		    if(num_PaintbrushesGreen > 0) {
		    	StdDraw.picture(305, 430, "paintbrush.png", 50, 50);
		    	StdDraw.text(305, 380, "X"+num_PaintbrushesGreen, 20.0);
		    	StdDraw.text(305, 470, "p");
		    }
		}
	}
	//determine position from which the piece is being moved form and to
	public static void determine_FromPos() {
		StdDraw.setPenColor(StdDraw.RED);
		
		while(!StdDraw.hasNextKeyTyped()){}//wait
			
		int posX= 0;
		int posY= 0;
		
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);	
		
		while(!StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
			while(!StdDraw.hasNextKeyTyped()){}//wait
			char dir = StdDraw.nextKeyTyped();
			if(dir == 'a') {	
				display_Boxes();
				
				if(posX!=0 ) {				
					posX--;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
			}	
			else if(dir == 'd') {
				display_Boxes();
				
				if(posX != 3) {
					posX ++;					
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
			}
			else if(dir == 'w') {
				display_Boxes();
				
				if(posY!=0) {
					posY--;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);				
			}
			else if(dir == 's') {
				display_Boxes();
				
				if(posY!=3) {
					posY++;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);
			}
			display_Images();
			
			if(PLAYER.equals("Blue")) {
				
				display_UIStacksBlue();
				show_UIPiece();
				fRow = posY+1;
				fCol = posX+1;
				
				
				
			}
			else {
				
				display_UIStacksGreen();
				show_UIPiece();
				fRow = posY+1;
				fCol = posX+1;
				
				
			}
			
				
		}
	}
	public static void determine_ToPos() {
		StdDraw.setPenColor(StdDraw.RED);
		
		while(!StdDraw.hasNextKeyTyped()){}//wait
			
		int posX= 0;
		int posY= 0;
		
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);	
		
		while(!StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
			while(!StdDraw.hasNextKeyTyped()){}//wait
			char dir = StdDraw.nextKeyTyped();
			if(dir == 'a') {	
				display_Boxes();
				
				if(posX!=0 ) {				
					posX--;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
			}	
			else if(dir == 'd') {
				display_Boxes();
				
				if(posX != 3) {
					posX ++;					
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
			}
			else if(dir == 'w') {
				display_Boxes();
				
				if(posY!=0) {
					posY--;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);				
			}
			else if(dir == 's') {
				display_Boxes();
				
				if(posY!=3) {
					posY++;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);
			}
			display_Images();
			
			if(PLAYER.equals("Blue")) {
				
				display_UIStacksBlue();
				show_UIPiece();
				tRow = posY+1;
				tCol = posX+1;
				
				
				
			}
			else {
				
				display_UIStacksGreen();
				show_UIPiece();
				tRow = posY+1;
				tCol = posX+1;
				
				
			}
			
				
		}
	}
	public static void focus_Box() {
		
		StdDraw.setPenColor(StdDraw.RED);
	
		//while(!StdDraw.hasNextKeyTyped()){}//wait
			
		int posX= 0;
		int posY= 0;
		
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);	
		
		while(!StdDraw.isKeyPressed(KeyEvent.VK_ENTER)) {
			while(!StdDraw.hasNextKeyTyped()){}//wait
			char dir = StdDraw.nextKeyTyped();
			if(dir == 'a') {	
				display_Boxes();
				
				if(posX!=0 ) {				
					posX--;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
				
			}	
			else if(dir == 'd') {
				display_Boxes();
				
				if(posX != 3) {
					posX ++;					
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
				
			}
			else if(dir == 'w') {
				display_Boxes();
				
				if(posY!=0) {
					posY--;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);		
				
			}
			else if(dir == 's') {
				display_Boxes();
				
				if(posY!=3) {
					posY++;
				}
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.square(PIECE_COORDINATES[posY][posX][0], PIECE_COORDINATES[posY][posX][1], 35);
				
			}
			display_Images();
			
						
			if(PLAYER.equals("Blue")) {
				display_UIStacksBlue();
				show_UIPiece();
				row = posY+1;
				col = posX+1;
				//show_UIPiece(stackNum);
//				place_BPiece(row, col);
//				update_Bstacks();
				
			}
			else {
				display_UIStacksGreen();
				show_UIPiece();
				row = posY+1;
				col = posX+1;
				//show_UIPiece(stackNum);
//				place_GPiece(row, col);
//				update_Gstacks();
			}
		
		}	
	}
	
	
	public static void place_BPieceUI() {
		
	}
	public static void determine_Action() {
		while(!StdDraw.hasNextKeyTyped()) {
			//wait for key input
		}
		
		char i = StdDraw.nextKeyTyped();
		
				if(PLAYER.equals("Blue")) {
					switch(i) {
					case'A':
						stackNum = 1;
						focus_Box();
						
						place_BPiece(row, col);	
						
						update_Bstacks();
						check_Outcome(row, col);
					break;
					
					case'B':
						stackNum = 2;
						focus_Box();
						
						place_BPiece(row, col);
						
						update_Bstacks();
						check_Outcome(row, col);
					break;
					
					case'C':
						stackNum = 3;
						focus_Box();
						
						place_BPiece(row, col);
						
						update_Bstacks();
						check_Outcome(row, col);
					break;		
					}
					if(i == 'a' || i == 'd' || i == 'w' || i == 's') {
						determine_FromPos();	
						determine_ToPos();		
						move_Piece();
						
						check_Outcome(tRow, tCol);
						check_Outcome(fRow, fCol);
					}
					else if(i == 'q') {
						terminate();
					}
					
				}
				else if(PLAYER.equals("Green")) {
					switch(i) {
					case'A':
						stackNum = 1;
						focus_Box();
						
						place_GPiece(row, col);
						
						update_Gstacks();
						check_Outcome(row, col);
					break;
					
					case'B':
						stackNum = 2;
						focus_Box();
						
						place_GPiece(row, col);
						
						update_Gstacks();
						check_Outcome(row, col);
					break;
					
					case'C':
						stackNum = 3;
						focus_Box();
						
						place_GPiece(row, col);
						
						update_Gstacks();
						check_Outcome(row, col);
					break;	
					
					}
					if(i == 'a' || i == 'd' || i == 'w' || i == 's') {
						stackNum = 4;
						determine_FromPos();
						determine_ToPos();
						move_Piece();
						
						check_Outcome(tRow, tCol);
						check_Outcome(fRow, fCol);
					}
					else if(i == 'q') {
						terminate();
					}
				}
	}
	
	//terminate game
	public static void terminate() {
		Stopwatch sw = new Stopwatch();
		double time = sw.elapsedTime();
		
		while(time == 5.0) {
			System.out.println("Quit, draw!");
			display_Text("Quit, draw!");
		}
		System.out.println("Quit, draw!");
		quit = true;
		System.exit(0);
	}
	public static void show_UIPiece() {
		
		for(int i = 3; i >= 0; i--) {
			for(int r = 1; r < Board[i].length; r++) {
				for(int c = 2; c < Board[i][r].length; c+=2) {
					String val = Board[i][r][c];
					switch(val) {
					case "B1":
						StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 30);
						break;
					case "B2":
						StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 20);
						break;
					case "B3":
						StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 10);
						break;
					case "B4":
						StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 4);
						break;
					case "G1":
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 30);
						break;
					case "G2":
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 20);
						break;
					case "G3":
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 10);
						break;
					case "G4":
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.filledCircle(PIECE_COORDINATES[r-1][c/2-1][0], PIECE_COORDINATES[r-1][c/2-1][1], 4);
						break;	
					
					}
				}
			}
		}
	}
 	//Display Board
	public static void print_Board() {
		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < Board[i].length; j++) {
				for(int y = 0; y < Board[i][j].length; y++) {
					System.out.print(Board[i][j][y]);
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	//update blue stacks
	public static void update_Bstacks() {
		switch(stackNum) {
		case 1:if(Bstack1.size()!=0) Bstack1.remove(0);
		break;
		case 2:if(Bstack2.size()!=0) Bstack2.remove(0);
		break;
		case 3:if(Bstack2.size()!=0) Bstack3.remove(0);
		break;
		
		}
	}
	//update green stack
	public static void update_Gstacks() {
		switch(stackNum) {
		case 1:if(Gstack1.size()!=0) Gstack1.remove(0);
		break;
		case 2:if(Gstack2.size()!=0) Gstack2.remove(0);
		break;
		case 3:if(Gstack3.size()!=0) Gstack3.remove(0);
		break;
		
		}
	}
	
	//place blue piece
	public static void place_BPiece(int row, int col) {
		int key;
		boolean isempty1 = (Board[0][row][col*2] == "__");
		boolean isempty2 = (Board[1][row][col*2] == "__");
		boolean isempty3 = (Board[2][row][col*2] == "__");
		boolean isempty4 = (Board[3][row][col*2] == "__");
		//maps pieces on board to their literal sizes
		int literal = 0;
		String val = Board[0][row][col*2];
		switch(val) {
		case "G1": literal = 4;
		break;
		case "G2": literal = 3;
		break;
		case "G3": literal = 2;
		break;
		case "G4": literal = 1;
		break;
		case "B1": literal = 4;
		break;
		case "B2": literal = 3;
		break;
		case "B3": literal = 2;
		break;
		case "B4": literal = 1;
		break;
		case "__": literal = 0;
		}
		//determine weather green player could possibly win(3 pieces in row, col or diagonal)
		boolean gwin = potentialGWin();
		
    	if(stackNum == 1) {
    		if(Bstack1.size() == 0) {
				System.out.println("Invalid move");
				
				return;
			}
    		else key = Bstack1.get(0);
			if(isempty1) {
				Board[0][row][col*2] = BluePieces.get(key);
				print_Board();
				
				return;
			}
			else if(isempty1 == false && gwin == false) {
				System.out.println("Invalid move");
				return;
			}
			else if(literal < key && gwin == true && isempty1 == false) {
				//push pieces down to another layer when placing another piece ontop
				if(isempty2) {
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				else if(isempty3) {
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				else if(isempty4) {
					Board[3][row][col*2] = Board[2][row][col*2];
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				print_Board();
				return;
				
			}
			else if(literal > key && gwin == true && isempty1 == false) {
				System.out.println("Invalid move");
				return;
			}
			
    	}		
		else if(stackNum == 2) {
			if(Bstack2.size() == 0) {
				System.out.println("Invalid move");
				return;
			}
			else key = Bstack2.get(0);
			if(isempty1) {
				Board[0][row][col*2] = BluePieces.get(key);
				print_Board();
			}
			else if(isempty1 == false && gwin == false) {
				System.out.println("Invalid move");
				return;
			}
			else if(literal < key && gwin == true && isempty1 == false) {
				if(isempty2) {
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				else if(isempty3) {
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				else if(isempty4) {
					Board[3][row][col*2] = Board[2][row][col*2];
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				print_Board();
				return;
			}
			else if(literal > key && gwin == true && isempty1 == false) {
				System.out.println("Invalid move");
				return;
			}
		}
		else if(stackNum == 3) {
			if(Bstack3.size() == 0) {
				System.out.println("Invalid move");
				return;
			}
			else key = Bstack3.get(0);
			if(isempty1) {
				Board[0][row][col*2] = BluePieces.get(key);
				print_Board();
				return;
			}
			else if(isempty1 == false && gwin == false) {
				System.out.println("Invalid move");
				return;
			}
			else if(literal < key && gwin == true && isempty1 == false) {
				if(isempty2) {
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				else if(isempty3) {
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				else if(isempty4) {
					Board[3][row][col*2] = Board[2][row][col*2];
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = BluePieces.get(key);
				}
				print_Board();
				return;
			}
			else if(literal > key && gwin == true && isempty1 == false) {
				System.out.println("Invalid move");
				return;
			}
		}	
	}
	
	//place green piece
	public static void place_GPiece(int row, int col) {
		int key;
		boolean isempty1 = (Board[0][row][col*2] == "__");
		boolean isempty2 = (Board[1][row][col*2] == "__");
		boolean isempty3 = (Board[2][row][col*2] == "__");
		boolean isempty4 = (Board[3][row][col*2] == "__");
		int literal = 0;
		String val = Board[0][row][col*2];
		switch(val) {
		case "G1": literal = 4;
		break;
		case "G2": literal = 3;
		break;
		case "G3": literal = 2;
		break;
		case "G4": literal = 1;
		break;
		case "B1": literal = 4;
		break;
		case "B2": literal = 3;
		break;
		case "B3": literal = 2;
		break;
		case "B4": literal = 1;
		break;
		case "__": literal = 0;
		}
		boolean bwin = potentialBWin();
		
		if(stackNum == 1) {
			if(Gstack1.size() == 0) {
				System.out.println("Invalid move");
				return;
			}
			else key = Gstack1.get(0);
			if(isempty1) {
				Board[0][row][col*2] = GreenPieces.get(key);
				print_Board();
			}
			else if(isempty1 == false && bwin == false) {
				System.out.println("Invalid move");
				return;
			}
			else if(literal < key && bwin == true && isempty1 == false) {
				if(isempty2) {
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				else if(isempty3) {
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				else if(isempty4) {
					Board[3][row][col*2] = Board[2][row][col*2];
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				print_Board();	
				return;
			}
			else if(literal > key && bwin == true && isempty1 == false) {
				System.out.println("Invalid move");
				return;
			}
		}
		else if(stackNum == 2) {
			if(Gstack2.size() == 0) {
				System.out.println("Invalid move");
				return;
			}
			else key = Gstack2.get(0);
			if(isempty1) {
				Board[0][row][col*2] = GreenPieces.get(key);
				print_Board();
				return;
			}
			else if(isempty1 == false && bwin == false) {
				System.out.println("Invalid move");
				return;
			}
			else if(literal < key && bwin == true && isempty1 == false) {
				if(isempty2) {
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				else if(isempty3) {
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				else if(isempty4) {
					Board[3][row][col*2] = Board[2][row][col*2];
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				print_Board();	
				return;
			}
			else if(literal > key && bwin == true && isempty1 == false) {
				System.out.println("Invalid move");
				return;
			}
		}
		else if(stackNum == 3) {
			if(Gstack3.size() == 0) {
				System.out.println("Invalid move");
				return;
			}
			else key = Gstack3.get(0);
			if(isempty1) {
				Board[0][row][col*2] = GreenPieces.get(key);
				print_Board();
				return;
			}
			else if(isempty1 == false && bwin == false) {
				System.out.println("Invalid move");
				return;
			}
			else if(literal < key && bwin == true && isempty1 == false) {
				if(isempty2) {
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				else if(isempty3) {
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				else if(isempty4) {
					Board[3][row][col*2] = Board[2][row][col*2];
					Board[2][row][col*2] = Board[1][row][col*2];
					Board[1][row][col*2] = Board[0][row][col*2];
					Board[0][row][col*2] = GreenPieces.get(key);
				}
				print_Board();	
				return;
			}
			else if(literal > key && bwin == true && isempty1 == false) {
				System.out.println("Invalid move");
				return;
			}
		}
		
		
	}
	//Move pieces on board
	public static void move_Piece() {
//		boolean isempty1 = (Board[0][tRow][tCol*2] == "__");
//		boolean isempty2 = (Board[1][tRow][tCol*2] == "__");
//		boolean isempty3 = (Board[2][tRow][tCol*2] == "__");
//		boolean isempty4 = (Board[3][tRow][tCol*2] == "__");
		String[] bluePieces = new String[]{"B1","B2","B3","B4"};
		String[] greenPieces = new String[]{"G1","G2","G3","G4"};
		//piece that is being moved onto piece being covered
		String movingPiece = Board[0][fRow][fCol*2];
		String coverPiece = Board[0][tRow][tCol*2];
		//literal/numeric value of pieces
		int cPliteral = 0;
		switch(coverPiece) {
		case "G1": cPliteral = 4;
		break;
		case "G2": cPliteral = 3;
		break;
		case "G3": cPliteral = 2;
		break;
		case "G4": cPliteral = 1;
		break;
		case "B1": cPliteral = 4;
		break;
		case "B2": cPliteral = 3;
		break;
		case "B3": cPliteral = 2;
		break;
		case "B4": cPliteral = 1;
		break;
		case "__": cPliteral = 0;
		break;
		}
		//boolean bwin = potentialBWin();
		//boolean gwin = potentialGWin();
		if(PLAYER == "Blue" && Arrays.asList(bluePieces).contains(movingPiece)) {
			//int mPliteral = BluePiecesRev.get(movingPiece);
			if(BluePiecesRev.get(movingPiece) > cPliteral && !movingPiece.equals("__")) {
				Board[3][tRow][tCol*2] = Board[2][tRow][tCol*2];
				Board[2][tRow][tCol*2] = Board[1][tRow][tCol*2];
				Board[1][tRow][tCol*2] = Board[0][tRow][tCol*2];
				Board[0][tRow][tCol*2] = Board[0][fRow][fCol*2];
				Board[0][fRow][fCol*2] = Board[1][fRow][fCol*2];
				Board[1][fRow][fCol*2] = Board[2][fRow][fCol*2];
				Board[2][fRow][fCol*2] = Board[3][fRow][fCol*2];
				Board[3][fRow][fCol*2] = "__";
				print_Board();
			}else System.out.println("Invalid move");
			
		}
		else if(PLAYER == "Green" && Arrays.asList(greenPieces).contains(movingPiece)) {
			//int mPliteral = GreenPiecesRev.get(movingPiece);
			if(GreenPiecesRev.get(movingPiece) > cPliteral && !movingPiece.equals("__")) {
				Board[3][tRow][tCol*2] = Board[2][tRow][tCol*2];
				Board[2][tRow][tCol*2] = Board[1][tRow][tCol*2];
				Board[1][tRow][tCol*2] = Board[0][tRow][tCol*2];
				Board[0][tRow][tCol*2] = Board[0][fRow][fCol*2];
				Board[0][fRow][fCol*2] = Board[1][fRow][fCol*2];
				Board[1][fRow][fCol*2] = Board[2][fRow][fCol*2];
				Board[2][fRow][fCol*2] = Board[3][fRow][fCol*2];
				Board[3][fRow][fCol*2] = "__";
				print_Board();
			}else System.out.println("Invalid move");
			
		}
		else System.out.println("Invalid move");
	}
	//use obsticles
	
	//count number of opponents pieces in stack
	public static int count_GreenStack(String[][][] Board, int row, int col) {
		ArrayList<String> pieces = new ArrayList<>();
		pieces.add("G1");pieces.add("G2");
		pieces.add("G3");pieces.add("G4");
		int count = 0;
		for(int i = 0; i < Board.length; i++) {
			for(int r = row; r == row; r++) {
				for(int c = col*2; c == col*2; c++) {
					if(pieces.contains(Board[i][r][c]))count++;
					else break;
				}
			}
		}
		return count;
	}
	public static int count_BlueStack(String[][][] Board, int row, int col) {
		ArrayList<String> pieces = new ArrayList<>();
		pieces.add("B1");pieces.add("B2");
		pieces.add("B3");pieces.add("B4");
		int count = 0;
		for(int i = 0; i < Board.length; i++) {
			for(int r = row; r == row; r++) {
				for(int c = col*2; c == col*2; c++) {
					if(pieces.contains(Board[i][r][c]))count++;
					else break;
				}
			}
		}
		return count;
	}
	
	
	// check for a potential win, ie minor diagonal, cols, rows and major diagonals
	public static boolean potentialGWin() {
		int countRow = 0;
		int countCol = 0;
		int countDiag = 0;
		int countAntiD = 0;
		int countLTD = 0; //left top diagonal
		int countLBD = 0; //left bottom diagonal
		int countRTD = 0; //right top diagonal
		int countRBD = 0; //right bottom diagonal
		int pos;
		String[] greenPieces = new String[] {"G1","G2","G3","G4"};
		//check row
		for(int i = 0; i < Board[0][row].length; i++) {
			if(Arrays.asList(greenPieces).contains(Board[0][row][i])) {
				countRow++;
			}
			else continue;
		}
		
		
		//check col
		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < Board[i].length; j++) {
				if(Arrays.asList(greenPieces).contains(Board[i][j][col*2])){
					countCol++;
				}
				else continue;
			}
		}
		//check diagonal
		pos = 2;
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][pos])) {
				countDiag++;
				pos+=2;
			}else pos+=2;
		}
		//check antidiagonal
		pos = 8;
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][pos])) {
				countAntiD++;
				pos-=2;
			}else pos-=2;
		}
		//check 3 piece left top diagonal 
		pos = 6;
		for(int r = 1; r < Board[0].length-1; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][pos])) {
				countLTD++;
				pos-=2;
			}else pos-=2;
		}
		//check 3 piece left bottom diagonal
		pos = 2;
		for(int r = 2; r < Board[0].length; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][pos])) {
				countLBD++;
				pos+=2;
			}else pos+=2;
		}
		//check 3 piece right top diagonal
		pos = 4;
		for(int r = 1; r < Board[0].length-1; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][pos])) {
				countRTD++;
				pos+=2;
			}else pos+=2;
		}
		//check 3 piece right bottom diagonal
		pos = 8;
		for(int r = 2; r < Board[0].length; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][pos])) {
				countRBD++;
				pos-=2;
			}else pos-=2;
		}
		//return a true value for any 4, 3 piece diagonal exceptions
		boolean threePDiagonal = false;
		if(countLTD == 3 || countLBD == 3 || countRTD == 3 || countRBD == 3) threePDiagonal = true;
		if(countRow == 3 || countCol == 3 || countDiag == 3 || countAntiD == 3 || threePDiagonal) return true;
		else return false;

		
	}
	// check for a potential win, ie minor diagonal, cols, rows and major diagonals
	public static boolean potentialBWin() {
		int countRow = 0;
		int countCol = 0;
		int countDiag = 0;
		int countAntiD = 0;
		int countLTD = 0; //left top diagonal
		int countLBD = 0; //left bottom diagonal
		int countRTD = 0; //right top diagonal
		int countRBD = 0; //right bottom diagonal
		int pos;
		String[] bluePieces = new String[]{"B1","B2","B3","B4"};
		
		//check row
		for(int i = 0; i < Board[0][row].length; i++) {
			if(Arrays.asList(bluePieces).contains(Board[0][row][i])) {
				countRow++;
			}
			else continue;
		}
		
		//check col
		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < Board[i].length; j++) {
				if(Arrays.asList(bluePieces).contains(Board[i][j][col*2])){
					countCol++;
				}
				else continue;
			}
		}
		//check diagonal
		pos = 2;
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][pos])) {
				countDiag++;
				pos+=2;
			}else pos+=2;
		}
		//check antidiagonal
		pos = 8;
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][pos])) {
				countAntiD++;
				pos-=2;
			}else pos-=2;
		}
		//check 3 piece left top diagonal 
		pos = 6;
		for(int r = 1; r < Board[0].length-1; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][pos])) {
				countLTD++;
				pos-=2;
			}else pos-=2;
		}
		//check 3 piece left bottom diagonal
		pos = 2;
		for(int r = 2; r < Board[0].length; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][pos])) {
				countLBD++;
				pos+=2;
			}else pos+=2;
		}
		//check 3 piece right top diagonal
		pos = 4;
		for(int r = 1; r < Board[0].length-1; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][pos])) {
				countRTD++;
				pos+=2;
			}else pos+=2;
		}
		//check 3 piece right bottom diagonal
		pos = 8;
		for(int r = 2; r < Board[0].length; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][pos])) {
				countRBD++;
				pos-=2;
			}else pos-=2;
		}
		//return a true value for any 4, 3 piece diagonal exceptions
		boolean threePDiagonal = false;
		//checks for 3 piece diagonal instance
		if(countLTD == 3 || countLBD == 3 || countRTD == 3 || countRBD == 3) threePDiagonal = true;
		if(countCol == 3 || countRow == 3 || countDiag == 3 || countAntiD == 3 || threePDiagonal) return true;
		else return false;
		
		
	}
	//check for draw, win or no winner
	
	public static void check_Outcome(int R, int C) {
		int countRow = 0; int countRowG = 0;
		int countCol = 0; int countColG = 0;
		int countDiag = 0; int countDiagG = 0;
		int countAntiD = 0; int countAntiDG = 0;
		int posDiag = 2; int posDiagG = 2;
		int posAntiD = 8; int posAntiDG = 8;
		String[] bluePieces = new String[]{"B1","B2","B3","B4"};
		String[] greenPieces = new String[]{"G1","G2","G3","G4"};
		//blue pieces
		//check row
		for(int i = 0; i < Board[0][R].length; i++) {
			if(Arrays.asList(bluePieces).contains(Board[0][R][i])) {
				countRow++;
			}
			else continue;
		}
		//check col
		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < Board[i].length; j++) {
				if(Arrays.asList(bluePieces).contains(Board[i][j][C*2])){
					countCol++;
				}
				else continue;
			}
		}
		//check diagonal
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][posDiag])) {
				countDiag++;
				posDiag +=2;
			}else posDiag+=2;
		}
		//check antidiagonal
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(bluePieces).contains(Board[0][r][posAntiD])) {
				countAntiD++;
				posAntiD -=2;
			}else posAntiD-=2;
		}
		//check green pieces
		//check row
		for(int i = 0; i < Board[0][R].length; i++) {
			if(Arrays.asList(greenPieces).contains(Board[0][R][i])) {
				countRowG++;
			}
			else continue;
		}
		//check col
		for(int i = 0; i < 1; i++) {
			for(int j = 0; j < Board[i].length; j++) {
				if(Arrays.asList(greenPieces).contains(Board[i][j][C*2])){
					countColG++;
				}
				else continue;
			}
		}
		//check diagonal
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][posDiagG])) {
				countDiagG++;
				posDiagG+=2;
			}else posDiagG+=2;
		}
		//check antidiagonal
		for(int r = 1; r < Board[0].length; r++) {
			if(Arrays.asList(greenPieces).contains(Board[0][r][posAntiDG])) {
				countAntiDG++;
				posAntiDG -=2;
			}else posAntiDG-=2;
		}
		//check for any draw game plays
		
		ArrayList<Integer> ItemsGreen = new ArrayList<Integer>();
		ArrayList<Integer> ItemsBlue = new ArrayList<Integer>();
		//ArrayList<Integer> Wins = new ArrayList<Integer>();
		ItemsBlue.add(countRow); ItemsGreen.add(countRowG);
		ItemsBlue.add(countCol); ItemsGreen.add(countColG);
		ItemsBlue.add(countDiag); ItemsGreen.add(countDiagG);
		ItemsBlue.add(countAntiD); ItemsGreen.add(countAntiDG);
		if(ItemsGreen.contains(4) && ItemsBlue.contains(4)) {
			controller = false;
			draw = true;
			System.out.println("Draw!");
			return;
		}
		else if(ItemsGreen.contains(4) && !ItemsBlue.contains(4)) {
			controller = false;
			greenWin = true;
			return;
		}
		else if(!ItemsGreen.contains(4) && ItemsBlue.contains(4)) {
			controller = false;
			blueWin = true;
			return;
		}
			
		else return;
		
	}
	//check for win
	public static boolean win;
	
	
	//display stacks present on board
	public static void display_BoardStacks() {
		System.out.println("Statistics for each stack present on the board");
		int row = 1;
		int pos = 2;
		int layer = 0;
				
		for(int r = row; r < Board[layer].length; r++) {
			for(int p = pos; p < Board[layer][r].length; p+=2) {
				if(Board[0][r][p] != "__") {
					for(int l = layer; l < Board.length; l++) {
						if(Board[l][r][p] != "__") System.out.print(Board[l][r][p] +" ");
						else break;
					}
					layer = 0;
					System.out.println();
				}
			}
		}
	}
	
	public static void display_BlueStacks() {
		System.out.println("Statistics external stacks blue player");
		System.out.print("Stack 1: ");
		for(int i: Bstack1) {
			switch(i) {
			case 4: System.out.print("B1 ");
			break;
			case 3: System.out.print("B2 ");
			break;
			case 2: System.out.print("B3 ");
			break;
			case 1: System.out.print("B4 ");
			break;
			}
		}System.out.println();
		System.out.print("Stack 2: ");
		for(int i: Bstack2) {
			switch(i) {
			case 4: System.out.print("B1 ");
			break;
			case 3: System.out.print("B2 ");
			break;
			case 2: System.out.print("B3 ");
			break;
			case 1: System.out.print("B4 ");
			break;
			}
		}System.out.println();
		System.out.print("Stack 3: ");
		for(int i: Bstack3) {
			switch(i) {
			case 4: System.out.print("B1 ");
			break;
			case 3: System.out.print("B2 ");
			break;
			case 2: System.out.print("B3 ");
			break;
			case 1: System.out.print("B4 ");
			break;
			}
		}
		
	}
	public static void display_GreenStacks() {
		System.out.println();
		System.out.println("Statistics external stacks green player");
		System.out.print("Stack 1: ");
		for(int i: Gstack1) {
			switch(i) {
			case 4: System.out.print("G1 ");
			break;
			case 3: System.out.print("G2 ");
			break;
			case 2: System.out.print("G3 ");
			break;
			case 1: System.out.print("G4 ");
			break;
			}
		}System.out.println();
		System.out.print("Stack 2: ");
		for(int i: Gstack2) {
			switch(i) {
			case 4: System.out.print("G1 ");
			break;
			case 3: System.out.print("G2 ");
			break;
			case 2: System.out.print("G3 ");
			break;
			case 1: System.out.print("G4 ");
			break;
			}
		}System.out.println();
		System.out.print("Stack 3: ");
		for(int i: Gstack3) {
			switch(i) {
			case 4: System.out.print("G1 ");
			break;
			case 3: System.out.print("G2 ");
			break;
			case 2: System.out.print("G3 ");
			break;
			case 1: System.out.print("G4 ");
			break;
			}
		}
	}
	//terminate functions 
	public static void greenWin() {
		if(blueWin == false && greenWin == true) {
			//display green win for 3 seconds then terminate
			display_Text("Green Wins!");
			System.out.println("Green Wins!");
			if(PLAYER.equals("Green")) {
				display_UIStacksGreen();
			}
			else display_UIStacksBlue();
			Stopwatch sw = new Stopwatch();
			while(sw.elapsedTime() < 2) {
				
			}
			System.exit(0);
		}
	}
	public static void blueWin() {
		if(blueWin == true && greenWin == false) {
			//display green win for 3 seconds then terminate
			display_Text("Blue Wins!");
			System.out.println("Blue Wins!");
			if(PLAYER.equals("Green")) {
				display_UIStacksGreen();
			}
			else display_UIStacksBlue();
			Stopwatch sw = new Stopwatch();
			while(sw.elapsedTime() < 2) {
				
			}
			System.exit(0);
		}
	}
	public static void finalDraw() {
		if(blueWin == true && greenWin == true) {
			//display green win for 3 seconds then terminate
			display_Text("Draw!");
			System.out.println("Draw!");
			if(PLAYER.equals("Green")) {
				display_UIStacksGreen();
			}
			else display_UIStacksBlue();
			Stopwatch sw = new Stopwatch();
			while(sw.elapsedTime() < 2) {
				
			}
			System.exit(0);
		}
	}
	public static void quitDraw() {
		if(blueWin == true && greenWin == true) {
			//display green win for 3 seconds then terminate
			display_Text("Quit, draw!");
			System.out.println("Quit, draw!");
			if(PLAYER.equals("Green")) {
				display_UIStacksGreen();
			}
			else display_UIStacksBlue();
			Stopwatch sw = new Stopwatch();
			while(sw.elapsedTime() < 2) {
				
			}
			System.exit(0);
		}
	}
	public static boolean controller = true;
    public static void main(String[] args) {
    	//add pieces to stacks
    	for(int i = 4; i >= 1; i--) {
    		Bstack1.add(i);
    	}
    	
    	for(int i = 4; i >= 1; i--) {
    		Bstack2.add(i);
    	}
    	
    	for(int i = 4; i >= 1; i--) {
    		Bstack3.add(i);
    	}
    	
    	for(int i = 4; i >= 1; i--) {
    		Gstack1.add(i);
    	}
    	
    	for(int i = 4; i >= 1; i--) {
    		Gstack2.add(i);
    	}
    	
    	for(int i = 4; i >= 1; i--) {
    		Gstack3.add(i);
    	}
    	//Map piece size to key and value
    	//HashMap<Integer, String> BluePieces = new HashMap<Integer, String>();
		BluePieces.put(1, "B4");
		BluePieces.put(2, "B3");
		BluePieces.put(3, "B2");
		BluePieces.put(4, "B1");
		//HashMap<String, Integer> BluePiecesRev = new HashMap<String, Integer>();
		BluePiecesRev.put("B4", 1);
		BluePiecesRev.put("B3", 2);
		BluePiecesRev.put("B2", 3);
		BluePiecesRev.put("B1", 4);
		//HashMap<Integer, String> GreenPieces = new HashMap<Integer, String>();
		GreenPieces.put(1, "G4");
		GreenPieces.put(2, "G3");
		GreenPieces.put(3, "G2");
		GreenPieces.put(4, "G1");
		//HashMap<String, Integer> GreenPiecesRev = new HashMap<String, Integer>();
		GreenPiecesRev.put("G4", 1);
		GreenPiecesRev.put("G3", 2);
		GreenPiecesRev.put("G2", 3);
		GreenPiecesRev.put("G1", 4);
		
        if (args.length == 1) {
            /** Text Mode */
            In in = new In(args[0]);  
            print_Board();  
            //String player =  "Blue";
           
            while (in.hasNextLine() && controller) {
            	
            	//check players turn then update their stack and place their piece
            	if (PLAYER == "Blue") {
            		int test = in.readInt();
            		if(test != 4 && test != -1 && test != 5) {
            			stackNum = test;
	            		row = in.readInt();
	            		col = in.readInt();     		
	            		place_BPiece(row, col);
	            		update_Bstacks();
	            		check_Outcome(row, col);
	            		//checkWin(Board, row, col, player);
	            		if(blueWin == true && greenWin == false) {
	            			System.out.println("Blue Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == false) {
	            			System.out.println("Green Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == true) {
	            			System.out.println("Draw!");
	            			controller = false;
	            			break;
	            		}
	            		
	            		
            		}
            		else if(test == 4){           			
            			fRow = in.readInt();
            			fCol = in.readInt();
            			tRow = in.readInt();
            			tCol = in.readInt();
            			move_Piece();       		
            			check_Outcome(fRow,fCol);
            			check_Outcome(tRow,tCol);
            			if(blueWin == true && greenWin == false) {
	            			System.out.println("Blue Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == false) {
	            			System.out.println("Green Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == true) {
	            			System.out.println("Draw!");
	            			controller = false;
	            			break;
	            		}
	            	}
            		else if(test == 5) {
            			String obs = in.readString();
            			row = in.readInt();
        				col = in.readInt();
        				
            			switch(obs) {
            			case "T":
            				numMoving = in.readInt();
            				place_Transporter();
            			break;
            			case "B":	
            				//place_Bomb();
            			break;
            			case "S":
            				dir = in.readInt();
            				//place_Shifter(row, col, dir, Board);
            			break;
            			case "P":
            				//place_Paintbrush();
            			break;
            			}
            		}
            		else {
            			System.out.println("Quit");
            			quit = true;
            			controller = false;
            		}
            		//if the quit command isnt entered, change players
            		if(test != -1) {
            			//print_Board(Board);  
            			PLAYER = "Green";
            		}
            		
            	}
            	else if(PLAYER == "Green") {
            		int test = in.readInt();
            		if(test != 4 && test != -1 && test != 5) {
            			stackNum = test;
	            		row = in.readInt();
	            		col = in.readInt();
	            		
	            		place_GPiece(row, col);
	            		update_Gstacks();
	            		check_Outcome(row, col);
	            		if(blueWin == true && greenWin == false) {
	            			System.out.println("Blue Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == false) {
	            			System.out.println("Green Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == true) {
	            			System.out.println("Draw!");
	            			controller = false;
	            			break;
	            		}	
            		}
            		else if(test == 4){
            			fRow = in.readInt();
            			fCol = in.readInt();
            			tRow = in.readInt();
            			tCol = in.readInt();
            			move_Piece();           			
            			check_Outcome(tRow, tCol);
            			check_Outcome(fRow, fCol);
            			if(blueWin == true && greenWin == false) {
	            			System.out.println("Blue Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == false) {
	            			System.out.println("Green Wins!");
	            			controller = false;
	            			break;
	            		}
	            		else if(greenWin == true && blueWin == true) {
	            			System.out.println("Draw!");
	            			controller = false;
	            			break;
	            		}
            		}
            		else if(test == 5) {
            			String obs = in.readString();
            			row = in.readInt();
        				col = in.readInt();
        				
            			switch(obs) {
            			case "T":
            				numMoving = in.readInt();
            				place_Transporter();
            			break;
            			case "B":	
            				//place_Bomb();
            			break;
            			case "S":
            				dir = in.readInt();
            				//place_Shifter(row, col, dir, Board);
            			break;
            			case "P":
            				//place_Paintbrush();
            			break;
            			}
            			
            		}
            		else {
            			System.out.println("Quit");
            			quit = true;
            			controller = false;
            		}
            		if(test != -1) {
            			//print_Board(Board);	
            			PLAYER = "Blue";
            		}          		
            	}
            }
     
            if(draw == false && greenWin == false && blueWin == false && quit != true)System.out.println("No winner, input file ended");
            display_BoardStacks();
            display_BlueStacks();
        	display_GreenStacks();
        	System.out.println();
        	
        	
        } 
       else if (args.length == 0) {
    	   //design board
    	   StdDraw.setPenColor(StdDraw.GRAY);
    	   StdDraw.setCanvasSize(400,600);
    	   StdDraw.setXscale(0,400);
	       StdDraw.setYscale(0,600);
	       StdDraw.setPenRadius(0.005);
	       StdDraw.setPenColor(StdDraw.CYAN);
	       StdDraw.line(0,420,400,420);
	       //display_Boxes();
	       StdDraw.setPenColor(StdDraw.ORANGE);
	       StdDraw.setPenRadius(0.004);
	       StdDraw.rectangle(200, 550, 150, 50);
	       display_Boxes();
	       display_Images();
	       //test
	       print_Board();
	       PLAYER = "Blue";
	       //StdDraw.enableDoubleBuffering();
	       while(controller) {
	    	   if(PLAYER.equals("Blue")) {
	    		   
	    		   display_UIStacksBlue();
	    		   determine_Action();
	    		   display_Boxes();
	    	       display_Images();
	    	       show_UIPiece();
	    	       
	    		   //break statement 
	    		   PLAYER = "Green";
	    	   }
	    	   else if(PLAYER.equals("Green")) {
	    		   
	    		   display_UIStacksGreen();
	    		   determine_Action();
	    		   display_Boxes();
	    	       display_Images();
	    	       show_UIPiece();
	    	       
	    		  
	    		   //break statement 
	    		   PLAYER = "Blue";
	    	   }
	    	  
	       }
	       //check outcome of game
	       greenWin();
	       blueWin();
	       finalDraw();
	       quitDraw();
	       show_UIPiece();
        } 
        else {
           System.out.println("Incorrect number of command line arguments provided");
           System.exit(0);
        }
    }
}
