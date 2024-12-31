package game;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.GameController;
import model.Board;
import view.Ui;

public class Main {
	
	public static void main(String[] args) {
	    Board board = new Board();
	    GameController controller = new GameController(board, null);
	    Ui ui = new Ui(controller,board);
	    controller.setGameView(ui);	    
	}
	
//	public static void main(String[] args) {
//		JFrame frame = new JFrame("Cờ Gánh");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(1000, 600);
//		frame.setLocationRelativeTo(null);
//		Ui ui = new Ui();
//		frame.add(ui);
//		frame.add(ui.createSidePanel(), BorderLayout.EAST);
//		frame.setVisible(true);
//	}
}
