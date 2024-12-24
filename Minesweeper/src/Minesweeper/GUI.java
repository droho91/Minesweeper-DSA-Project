package Minesweeper;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Date;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;

public class GUI extends JFrame {
	public boolean resetter = false;
    public boolean flagger = false;
	Date startDate = new Date();
    Date endDate;
    int spacing = 4;
    int neighs = 0;
    String vicMes = " Nothing yet!";
    public int mx = -100;
    public int my = -100;
    public int smileyX=605;
    public int smileyY=5;
    public int smileyCenterX = smileyX+35;
    public int smileyCenterY = smileyY+35;
    public int flaggerX = 445;
    public int flaggerY = 5;
    public int flaggerCenterX = flaggerX + 35;
    public int flaggerCenterY = flaggerY + 35;
    public int spacingX = 90;
    public int spacingY = 10;
    public int minusX = spacingX+160;
    public int minusY = spacingY;
    public int plusX = spacingX +240;
    public int plusY = spacingY;
    public int timeX=1130;
    public int timeY=5;
    public int vicMesX = 740;
    public int vicMesY= -50;
    public int sec = 0;
    public boolean happiness = true;
    public boolean victory = false;
    public boolean defeat = false;
    int[][] mines = new int[16][9];
    int[][] neighbours = new int[16][9];
    boolean[][] revealed = new boolean[16][9];
    boolean[][] flagged = new boolean[16][9];
    Random rand = new Random();
 	private Stack<GameState> undoStack = new Stack<>();
    private Stack<GameState> redoStack = new Stack<>();

    public GUI() {
        this.setTitle("Minesweeper");
        this.setSize(1286, 829);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(true);

        Board board = new Board();
        this.setContentPane(board);

        Move move = new Move();
        this.addMouseMotionListener(move);

        Click click = new Click();
        this.addMouseListener(click);
        
        for(int i=0;i<16;i++)
        	for(int j=0;j<9;j++) {
        		if(rand.nextInt(100)<20) {
        			mines[i][j]=1;
        		} else {
        			mines[i][j]=0;
        		}
        		revealed[i][j]=false;
        	}
        for(int i=0;i<16;i++)
        	for(int j=0;j<9;j++) {
        		neighs=0;
        		for(int m=0;m<16;m++)
                	for(int n=0;n<9;n++) {
                		if(!(m==i && n==j))
                		if(isN(i,j,m,n)==true) {
                			neighs++;
                		}
                	}
        		neighbours[i][j]=neighs;
        	}
    }

    public class Board extends JPanel {
        public void paintComponent(Graphics g) {
  
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 1280, 800);
            for (int i = 0; i < 16; i++)
              {  for (int j = 0; j < 9; j++) {
                    g.setColor(Color.GRAY);
                    if (revealed[i][j] == true) {
                    	g.setColor(Color.WHITE);
                    	if (mines[i][j]==1) {
                    		g.setColor(Color.red);
                    	}
                    }
                    if (mx >= spacing + i * 80 && mx < i * 80 + 80 - spacing && my >= spacing + j * 80 + 106
                            && my < spacing + j * 80 + 186 - 2 * spacing) {
                        g.setColor(Color.lightGray);
                    }
                    g.fillRect(spacing + i * 80, spacing + j * 80 + 80, 80 - 2 * spacing, 80 - 2 * spacing);
                    if (revealed[i][j] == true) {
                    	g.setColor(Color.BLACK);
                    	if(mines[i][j] == 0) {
                    		if(neighbours[i][j]==1) {
                    			g.setColor(Color.BLUE);
                    		} else if (neighbours[i][j] == 2) {
                    			g.setColor(Color.GREEN);
                    		} else if (neighbours[i][j] == 3) {
                    			g.setColor(Color.RED);
                    		} else if (neighbours[i][j] == 4) {
                    			g.setColor(new Color(0,0,128));
                    		} else if (neighbours[i][j] == 5) {
                    			g.setColor(new Color(178,34,34));
                    		} else if (neighbours[i][j] == 6) {
                    			g.setColor(new Color(72,209,204));
                    		} else if (neighbours[i][j] == 7) {
                    			g.setColor(Color.PINK);
                    		} else if (neighbours[i][j] == 4) {
                    			g.setColor(Color.DARK_GRAY);
                    		}
                    	g.setFont(new Font("Tahoma", Font.BOLD, 40));
                    	g.drawString(Integer.toString(neighbours[i][j]),i*80+27,j*80+80+55);
                    } else {
                    	g.fillRect(i*80+30, j*80+100, 20, 40);
                    	g.fillRect(i*80+20, j*80+110, 40, 20);
                    	g.fillRect(i*80+25, j*80+105, 30, 30);
                    	g.fillRect(i*80+38,j*80+95,4,50);
                    	g.fillRect(i*80+15, j*80+118, 50, 4);
                    }
                }
                    if (flagged[i][j]==true)
                    { g.setColor(Color.BLACK);
                        g.fillRect(i*80 +32+5,j*80+80 +15+5,5,40);
                        g.fillRect(i*80 +20+5,j*80+80 +50+5,30,10);
                        g.setColor(Color.RED);
                        g.fillRect(i*80+16+5,j*80+80+15+5,20,15);
                        g.setColor(Color.BLACK);
                        g.drawRect(i*80+16+5,j*80+80+15+5,20,15);
                        g.drawRect(i*80+17+5,j*80+80+16+5,18,13);
                        g.drawRect(i*80+18+5,j*80+80+17+5,16,11);
                    }
           }
        }
            
            g.setColor(Color.BLACK);
            g.fillRect(spacingX,spacingY,300,60);

            g.setColor(Color.white);
            g.fillRect(minusX+5,minusY+10,40,40);
            g.fillRect(plusX+5,plusY+10,40,40);
        
            g.setFont(new Font("Tahoma",Font.PLAIN,35));
            g.drawString(" Spacing", spacingX+20, spacingY+45);
         
            g.setColor(Color.BLACK);
            g.fillRect(minusX+15, minusY+27,20 ,6 );
            g.fillRect(plusX+15, plusY+27,20 ,6 );
            g.fillRect(plusX+22, plusY+20,6 ,20 );
            
            g.setColor(Color.white);
            g.setFont(new Font("Tahoma",Font.PLAIN,30));
            if(spacing<10)
            {g.drawString("0"+Integer.toString(spacing),minusX+50,minusY+40);}
            else{
            g.drawString(Integer.toString(spacing),minusX+50,minusY+40);}

          

            g.setColor(Color.yellow);
            g.fillOval(smileyX, smileyY, 70, 70);
            g.setColor(Color.black);
            g.fillOval(smileyX+15,smileyY+20,10,10);
            g.fillOval(smileyX+45,smileyY+20,10,10);
            if (happiness == true) {
            	g.fillRect(smileyX+20, smileyY+50, 30, 5);
            	g.fillRect(smileyX+17, smileyY+45, 5, 5);
            	g.fillRect(smileyX+48, smileyY+45, 5, 5);
            } else {
            	g.fillRect(smileyX+20, smileyY+45, 30, 5);
            	g.fillRect(smileyX+17, smileyY+50, 5, 5);
            	g.fillRect(smileyX+48, smileyY+50, 5, 5);
            }
            g.setColor(Color.BLACK);
            g.fillRect(flaggerX +32,flaggerY +15,5,40);
            g.fillRect(flaggerX +20,flaggerY +50,30,10);
            g.setColor(Color.RED); 
            g.fillRect(flaggerX+16,flaggerY+15,20,15);
            g.setColor(Color.BLACK);
            g.drawRect(flaggerX+16,flaggerY+15,20,15);
            g.drawRect(flaggerX+17,flaggerY+16,18,13);
            g.drawRect(flaggerX+18,flaggerY+17,16,11);
            
            g.setColor(Color.YELLOW);
            g.fillRect(700, 5, 90, 50);
            g.setColor(Color.BLACK);
            g.drawString("Undo", 710, 50);
        	
            g.setColor(Color.YELLOW);
            g.fillRect(1000, 5, 90, 50);
            g.setColor(Color.BLACK);
            g.drawString("Redo", 1010, 50);

          	
         
            if (flagger == true)
            {g.setColor(Color.red);
                            }
            g.drawOval(flaggerX,flaggerY,70,70);
            g.drawOval(flaggerX+1,flaggerY+1,68,68);
            g.drawOval(flaggerX+2,flaggerY+2,66,66);
            g.setColor(Color.BLACK);
            g.fillRect(timeX,timeY,150,70);
            if (defeat == false && victory == false)
{            sec = (int)((new Date().getTime()-startDate.getTime())/1000);}
            if (sec>999)
            	sec=999;
            g.setColor(Color.WHITE);
            if (victory == true) {
            	g.setColor(Color.GREEN);
            } else if (defeat == true) {
            	g.setColor(Color.RED);
            }
            g.setFont(new Font("Tahoma", Font.PLAIN, 80));
            if(sec<10) {
            	g.drawString("00"+Integer.toString(sec), timeX, timeY+65);
            } else if (sec<100) {
            	g.drawString("0"+Integer.toString(sec), timeX, timeY+65);
            } else {
            	g.drawString(Integer.toString(sec), timeX, timeY+65);
            }

            if (victory==true){g.setColor(Color.GREEN);
                vicMes = "You win!";}

            else if  (defeat==true){g.setColor(Color.RED);
                vicMes = "You lose!";}

        if (victory == true || defeat == true)
            {vicMesY= -50 + (int)(new Date().getTime() - endDate.getTime())/10;}
           if(vicMesY>67){vicMesY=67;}
            g.setFont(new Font ("Tahoma",Font.PLAIN,70));
            g.drawString(vicMes,vicMesX,vicMesY);

            
        }
    }

    public class Move implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mx = e.getX();
            my = e.getY();
        }

    }

    public class Click implements MouseListener {
    	
   

        @Override
        public void mouseClicked(MouseEvent e) {
            mx = e.getX();
            my = e.getY();

            saveState(); // Save the current state before making any changes

            if (mx >= minusX + 20 && mx <= minusX + 60 && my >= minusY + 20 && my <= minusY + 50) {
                spacing--;
                if (spacing < 1) {
                    spacing = 1;
                }
            } else if (mx >= plusX + 20 && mx <= plusX + 60 && my >= plusY + 20 && my <= plusY + 50) {
                spacing++;
                if (spacing > 15) {
                    spacing = 15;
                }
            }

            if (inRedoButton()) {
                redo();
                System.out.println("Redo works");
            } else if (inUndoButton()) {
                undo();
                System.out.println("Undo works");
            }

            if (inBoxX() != -1 && inBoxY() != -1) {
                System.out.println("The mouse is in the [" + inBoxX() + "], Number of mine neighs:" + neighbours[inBoxX()][inBoxY()]);
                if (flagger == true && revealed[inBoxX()][inBoxY()] == false) {
                    if (flagged[inBoxX()][inBoxY()] == false) {
                        flagged[inBoxX()][inBoxY()] = true;
                    } else {
                        flagged[inBoxX()][inBoxY()] = false;
                    }
                } else {
                    if (flagged[inBoxX()][inBoxY()] == false) {
                        revealed[inBoxX()][inBoxY()] = true;
                    }
                }
            } else {
                System.out.println("The pointer is not inside of any box!");
            }

            if (inSmiley()) {
                resetAll();
            }

            if (inFlagger()) {
                flagger = !flagger;
            }
        }


        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }
    
    private void saveState() {
        // Only save the state if the game is in progress (not during reset or victory/defeat)
        if (!resetter && !victory && !defeat) {
            undoStack.push(new GameState(mines, neighbours, revealed, flagged, spacing, happiness, resetter, victory, defeat));
            // Clear redo stack whenever a new state is saved
            redoStack.clear();
        }
    }

    private void loadState(GameState state) {
        this.mines = state.mines;
        this.neighbours = state.neighbours;
        this.revealed = state.revealed;
        this.flagged = state.flagged;
        this.spacing = state.spacing;
        this.happiness=state.happiness;
        this.resetter=state.resetter;
        this.victory=state.victory;
        this.defeat=state.defeat;
        
    }

    
    public void undo() {
        if (!undoStack.isEmpty()) {
            // Save current state to redo stack before undoing
            GameState currentState = new GameState(mines, neighbours, revealed, flagged, spacing, happiness, resetter, victory, defeat);
            redoStack.push(currentState);

            // Load the previous state from the undo stack
            GameState previousState = undoStack.pop();
            loadState(previousState);
            repaint();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            // Save current state to undo stack before redoing
            GameState currentState = new GameState(mines, neighbours, revealed, flagged, spacing, happiness, resetter, victory, defeat);
            undoStack.push(currentState);

            // Load the next state from the redo stack
            GameState nextState = redoStack.pop();
            loadState(nextState);
            
            repaint();
        }
    }

    
    private boolean inUndoButton() {
        int undoX = 700; 
        int undoY = 5;  
        int undoWidth = 90; 
        int undoHeight = 50; 

        return mx >= undoX && mx <= undoX + undoWidth && my >= undoY && my <= undoY + undoHeight;
    }

    private boolean inHelp() {
        int helpX = 700; 
        int helpY = 5;  
        int helpWidth = 90; 
        int helpHeight = 50; 

        return mx >= helpX && mx <= helpX + helpWidth && my >= helpY && my <= helpY + helpHeight;
    } 
    private boolean inRedoButton() {
        int redoX = 1000; 
        int redoY = 5;    
        int redoWidth = 90;  
        int redoHeight = 50;
        
        return mx >= redoX && mx <= redoX + redoWidth && my >= redoY && my <= redoY + redoHeight;
    }
    
    
    public void checkVictoryStatus() {
        if(defeat == false){
    	for(int i=0;i<16;i++)
    		{for(int j=0;j<9;j++) {
    			if(revealed[i][j] == true && mines[i][j]==1) {
    				defeat = true;
    				happiness = false;
                    endDate = new Date();
    			}
    		}
        }
        }
    	if(totalBoxesRevealed()>=144-totalMines()&&victory == false) {
    		victory = true;
            endDate = new Date();
    	}
    }
    
    public int totalMines() {
    	int total =0;
    	for (int i=0;i<16;i++)
    		for(int j=0;j<9;j++) {
    			if(mines[i][j]==1) 
    				total++;
    		}
    	return total;
    }
    
    public int totalBoxesRevealed() {
    	int total =0;
    	for (int i=0;i<16;i++)
    		for(int j=0;j<9;j++) {
    			if(revealed[i][j]==true) 
    				total++;
    		}
    	return total;
    }
    
    public void resetAll() {
        resetter = true;
        flagger = false;
        startDate = new Date();
        vicMesY = -50;
        vicMes = "Nothing yet!";
        happiness = true;
        victory = false;
        defeat = false;

        // Clear undo and redo stacks on reset
        undoStack.clear();
        redoStack.clear();

        // Reset the board
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                if (rand.nextInt(100) < 20) {
                    mines[i][j] = 1;
                } else {
                    mines[i][j] = 0;
                }
                revealed[i][j] = false;
                flagged[i][j] = false;
            }
        }

        // Recalculate neighbors
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                neighs = 0;
                for (int m = 0; m < 16; m++) {
                    for (int n = 0; n < 9; n++) {
                        if (!(m == i && n == j) && isN(i, j, m, n)) {
                            neighs++;
                        }
                    }
                }
                neighbours[i][j] = neighs;
            }
        }

        resetter = false;
    }

    public boolean inSmiley() {
    	int dif = (int) Math.sqrt(Math.abs(mx-smileyCenterX)*Math.abs(mx-smileyCenterX)+Math.abs(my-smileyCenterY)*Math.abs(my-smileyCenterY));
    	if (dif<35)
    		return true;
    	return false;
    }

    public boolean inFlagger() {
    	int dif = (int) Math.sqrt(Math.abs(mx-flaggerCenterX)*Math.abs(mx-flaggerCenterX)+Math.abs(my-flaggerCenterY)*Math.abs(my-flaggerCenterY));
    	if (dif<35)
    		return true;
    	return false;
    }

    public int inBoxX() {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 9; j++) {
            	if (mx >= spacing + i * 80 && mx < i * 80 + 80 - spacing && my >= spacing + j * 80 + 106
                        && my < spacing + j * 80 + 186 - 2 * spacing) {
                    return i;
                }
            }
        return -1;
    }

    public int inBoxY() {
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 9; j++) {
            	if (mx >= spacing + i * 80 && mx < i * 80 + 80 - spacing && my >= spacing + j * 80 + 106
                        && my < spacing + j * 80 + 186 - 2 * spacing) {
                    return j;
                }
            }
        return -1;
    }
    
    public boolean isN(int mX, int mY, int cX, int cY) {
    	if(mX-cX <2 && mX-cX>-2 && mY-cY<2 && mY-cY >-2 && mines[cX][cY]==1) {
    		return true;
    	}
    	return false;
    }
    
}

class GameState {
    int[][] mines;
    int[][] neighbours;
    boolean[][] revealed;
    boolean[][] flagged;
    int spacing;
    boolean happiness; // Represents the current state (e.g., in progress, game over)
    boolean resetter;
    boolean victory;
    boolean defeat;

    public GameState(int[][] mines, int[][] neighbours, boolean[][] revealed, 
    				boolean[][] flagged, int spacing, boolean happiness, 
    				boolean resetter, boolean victory, boolean defeat) {
        this.mines = clone2DArray(mines);
        this.neighbours = clone2DArray(neighbours);
        this.revealed = clone2DArray(revealed);
        this.flagged = clone2DArray(flagged);
        this.spacing = spacing;
        this.happiness = happiness;
        this.resetter = resetter;
        this.victory = victory;
        this.defeat = defeat;
    }

    private int[][] clone2DArray(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i].clone();
        }
        return copy;
    }

    private boolean[][] clone2DArray(boolean[][] array) {
        boolean[][] copy = new boolean[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = array[i].clone();
        }
        return copy;
    }
}

