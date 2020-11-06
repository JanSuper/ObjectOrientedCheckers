package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Board.Board;
import Gamecontroller.Gamecontroller;

public class BoardPanel{
	
	public Board board = Gamecontroller.board;
	Color black = new Color (0, 0, 0);
	Color white = new Color (255, 255, 255);
	Color green = new Color (0, 255, 0);
	Color blue = new Color (0, 0, 255);
	JFrame frame;
	JPanel panel;
	
	public BoardPanel () {
		frame = new JFrame("Checkers");
		panel = new JPanel();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,800);
		panel.setSize(800,800);
		
		panel.setLayout(new GridLayout(8,8));
		
		for(int i = 0; i <= 7; i++) {
			for(int j = 0; j <= 7; j++) {
			JButton holdButton = new JButton();
				holdButton.setSize(100, 100);
//				holdButton.setOpaque(false);
//				holdButton.setContentAreaFilled(false);
//				holdButton.setBorderPainted(false);
				frame.add(holdButton);
			}
		}
		
		frame.add(panel);
		frame.setVisible(true);
	}
	
}
