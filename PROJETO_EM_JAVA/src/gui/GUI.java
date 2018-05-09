package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Stack;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import logic.Game; 

public class GUI {

	private static JTable tabel = null; 
	
	private static JFrame boardFrame = null;
	
	private static JPanel boardPanel = null;
	
	private static Game game = null;
	
	private static String[][] board = null;
	
	private static void generatePlays(){
		
		Stack<Integer[]> plays = game.startAIvsAI();
		
		runBoardPlays(plays);
	}
	
	 private static DefaultTableCellRenderer getRenderer() {
	        return new DefaultTableCellRenderer(){
	            @Override
	            public Component getTableCellRendererComponent(JTable table,
	                    Object value, boolean isSelected, boolean hasFocus,
	                    int row, int column) {
	                Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
	                if("X".equals(value)){
	                    tableCellRendererComponent.setBackground(Color.WHITE);
	                    tableCellRendererComponent.setForeground(Color.WHITE);
	                } else  if("0".equals(value)){
	                    tableCellRendererComponent.setBackground(Color.BLACK);
	                    tableCellRendererComponent.setForeground(Color.BLACK);
	                } else {
	                    tableCellRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
	                }
	                return tableCellRendererComponent;
	            }
	        };
	    }

	 
	private static void runBoardPlays(Stack<Integer[]> plays){
		
		
		int i = 0;
		for(Integer[] ie: plays) {

			i++;
			
			String playPiece = "" + (((i%2)==0)?(char)Game.blackpiece:(char)Game.whitepiece);
			
        	board[ie[0]][ie[1]]= playPiece; 	
        	board[ie[2]][ie[3]] = playPiece;

        	
        	
			boardPanel.getComponent(0).repaint();
			boardPanel.getComponent(1).repaint();
			boardPanel.repaint();
			boardFrame.repaint();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
		boardPanel.repaint();
		boardFrame.repaint();
		
	}
	
	private static void showMenu() {
        //Create and set up the window.
        JFrame menuFrame = new JFrame("MenuFrame");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.pack();
        menuFrame.setSize(540, 540);
 
        //Add the ubiquitous "Hello World" label.
        JButton aivsaiButton = new JButton("AI vs AI");
        JButton playervsaiButton = new JButton("Player vs AI");
        JButton playervsplayerButton = new JButton("Player vs Player");
        
        aivsaiButton.setMinimumSize(new Dimension(250, 100));
        playervsaiButton.setMinimumSize(new Dimension(250, 100));
        playervsplayerButton.setMinimumSize(new Dimension(250, 100));
        
        aivsaiButton.setPreferredSize(new Dimension(250, 100));
        playervsaiButton.setPreferredSize(new Dimension(250, 100));
        playervsplayerButton.setPreferredSize(new Dimension(250, 100));
        
        JPanel buttonPanel = new JPanel();
        
        buttonPanel.add(playervsplayerButton );
        buttonPanel.add(playervsaiButton );
        buttonPanel.add(aivsaiButton );

        playervsplayerButton.setSize(new Dimension(540, 540));
        
        buttonPanel.setLayout(new GridLayout(3,1));
        

        boardFrame = new JFrame("BoardFrame");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.pack();
        boardFrame.setSize(540, 600);
        
        boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));   
        boardPanel.setBackground(new Color(245,222,179));
        
        
        String[] columns = new String[Game.boardsize];
        
        for(int i = 0; i < Game.boardsize; i++){
        	columns[i] = i + "";
        }
        
        
        tabel = new  JTable(board,columns);
        tabel.setBackground(new Color(222,184,135));
        tabel.setRowHeight(40);
        
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(TableColumn cl :Collections.list(tabel.getColumnModel().getColumns())){
        	cl.setPreferredWidth(25);
        	cl.setCellRenderer(getRenderer());
        }
        tabel.updateUI();
        tabel.repaint();
        
        JButton backToMenu = new JButton("Back to Menu");
        backToMenu.setSize(new Dimension(250, 100));


        boardPanel.add(tabel);
        boardPanel.add(Box.createRigidArea(new Dimension(0,25)));
        boardPanel.add(backToMenu);
        
        

        
        menuFrame.getContentPane().add(buttonPanel);
        boardFrame.getContentPane().add(boardPanel);
 
        //Display the window.
        menuFrame.setVisible(true);
        boardFrame.setVisible(false);
        
        
        backToMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				menuFrame.setVisible(true);
				boardFrame.setVisible(false);
			}
        	
        });
        
        aivsaiButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				Thread thread = new Thread(new Runnable() {
		            public void run() {
		            	generatePlays();
		            }
		        });
				thread.start();
			}
        	
        });
    }
	
	private static void cleanBoard(){

		Byte[][] boardOri = game.getBoard();
		
		for(int i = 0; i < Game.boardsize; i++){
			board[i] = new String[Game.boardsize];
			for(int j = 0; j < Game.boardsize; j++){
				char ch = (char)((byte)boardOri[i][j]);
				board[i][j] = "" + ch;
			}
		}
	}
 
    public static void main(String[] args) {
		
    	game = new Game();

		board = new String[Game.boardsize][Game.boardsize]; 
		
		cleanBoard();
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	showMenu();
            }
        });
        
    }
}
