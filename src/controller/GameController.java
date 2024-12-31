package controller;

import model.Ai;
import model.AlphaBeta;
import model.Board;
import model.Minimax;
import model.Move;
import view.Ui;

public class GameController {
	private Board board;
	private Ui view;
	private Ai ai;

	public GameController(Board board, Ui view) {
		this.board = board;
		this.view = view;
		this.ai = new Ai(board);
	}

	public void setGameView(Ui view) {
		this.view = view;
		view.display();
	}

	public void startGame() {
		board.displayBoard(); // Khởi tạo bàn cờ
		view.updateBoard(board); // Cập nhật giao diện
	}

	public void playerMove(int fromRow, int fromCol, int toRow, int toCol) {
		System.out.println(toRow);
		System.out.println(toCol);
		if (board.isValidMove(fromRow, fromCol, toRow, toCol)) {
			board.movePiece(fromRow, fromCol, toRow, toCol);
			board.checkGanh(toRow, toCol);
			board.checkChet(toRow, toCol);
			board.checkVay(toRow, toCol);
			view.updateBoard(board); // Cập nhật giao diện
			board.setRedTurn(false);
			aiMove(); // AI thực hiện nước đi
		} else {
			System.out.println("Nuoc di khong hop le");
		}
	}

	private void aiMove() {
		Move bestMove;
		long timeTaken;
		long memoryUsed;
		if (view.isUseAlphaBeta()) {
			// Đo thời gian khi sử dụng AlphaBeta
			long startTime = System.nanoTime(); 
			bestMove = AlphaBeta.nextMove(board.board, 1); 
			long endTime = System.nanoTime(); 
			timeTaken = (endTime - startTime) / 1000000; // Tính thời gian (ms)

			// Đo bộ nhớ khi sử dụng AlphaBeta
			long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
			AlphaBeta.nextMove(board.board, 1); 
			long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
			memoryUsed = (endMemory - startMemory) / 1024; // Tính bộ nhớ sử dụng (KB)

		} else {
			// Đo thời gian khi sử dụng Minimax
			long startTime = System.nanoTime(); 
			bestMove = Minimax.nextMove(board.board, 1); 
			long endTime = System.nanoTime(); 
			timeTaken = (endTime - startTime) / 1000000; // Tính thời gian (ms)

			// Đo bộ nhớ khi sử dụng Minimax
			long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
			Minimax.nextMove(board.board, 1); 
			long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); 
			memoryUsed = (endMemory - startMemory) / 1024; // Tính bộ nhớ sử dụng (KB)
		}

		// In ra kết quả thời gian và bộ nhớ
		System.out.println("Time taken for AI move: " + timeTaken + " ms");
		System.out.println("Memory used for AI move: " + memoryUsed + " KB");

		if (bestMove != null) {
			board.movePiece(bestMove.getFromRow(), bestMove.getFromCol(), bestMove.getToRow(), bestMove.getToCol());
			// Kiểm tra gánh và vây
			board.checkGanh(bestMove.getToRow(), bestMove.getToCol());
			board.checkChet(bestMove.getToRow(), bestMove.getToCol());
			board.checkVay(bestMove.getToRow(), bestMove.getToCol());
			view.updateBoard(board); // Cập nhật giao diện
			board.setRedTurn(true);
		}

	}
}