package model;

import java.util.ArrayList;
import java.util.List;

public class Ai {
	private Board b;

	public Ai(Board b) {
		super();
		this.b = b;
	}

	public int evaluateBoard(char player) {
		char[][] board = b.getBoard();
		int score = 0;
		char opponent = (player == b.getRED()) ? b.getBLUE() : b.getRED();
		boolean hasPlayerPieces = false;
		boolean hasOpponentPieces = false;

		for (int i = 0; i < b.getSIZE(); i++) {
			for (int j = 0; j < b.getSIZE(); j++) {
				if (board[i][j] == player) {
					score++;
					hasPlayerPieces = true;
				} else if (board[i][j] == opponent) {
					score--;
					hasOpponentPieces = true;
				}
			}
		}
		
		if (!hasPlayerPieces) {
			return Integer.MIN_VALUE; // Đối thủ thắng
		}
		
		if (!hasOpponentPieces) {
			return Integer.MAX_VALUE; // Người chơi thắng
		}

		// Kiểm tra nếu không còn nước đi hợp lệ
		if (generateAllMoves(player).isEmpty()) {
			return Integer.MIN_VALUE; // Đối thủ thắng
		}
		if (generateAllMoves(opponent).isEmpty()) {
			return Integer.MAX_VALUE; // Người chơi thắng
		}

		return score; // Trả về điểm nếu không có điều kiện thắng/thua
	}

	private List<Move> generateAllMoves(char player) {
		List<Move> moves = new ArrayList<>();
		char[][] board = b.getBoard();
		for (int i = 0; i < b.getSIZE(); i++) {
			for (int j = 0; j < b.getSIZE(); j++) {
				if (board[i][j] == player) {
					for (int x = -1; x <= 1; x++) {
						for (int y = -1; y <= 1; y++) {
							int newRow = i + x;
							int newCol = j + y;
							if (b.isValidMove(i, j, newRow, newCol)) {
								moves.add(new Move(i, j, newRow, newCol));
							}
						}
					}
				}
			}
		}
		return moves;
	}

	public Move findBestMove(char player, boolean useAlphaBeta) {
		char opponent = (player == b.getRED()) ? b.getBLUE() : b.getRED();
		List<Move> possibleMoves = generateAllMoves(player);
		Move bestMove = null;
		int bestValue = Integer.MIN_VALUE;

		for (Move move : possibleMoves) {
			// Thử nước đi
			b.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());

			// Tính giá trị nước đi dựa trên thuật toán được chọn
			int moveValue;
			if (useAlphaBeta) {
				moveValue = alphabeta(false, opponent, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
			} else {
				moveValue = minimax(false, opponent, 3);
			}

			// Hoàn tác nước đi
			b.movePiece(move.getToRow(), move.getToCol(), move.getFromRow(), move.getFromCol());

			// Cập nhật nước đi tốt nhất
			if (moveValue > bestValue) {
				bestValue = moveValue;
				bestMove = move;
			}
		}

		return bestMove;
	}

	public int alphabeta(boolean isMaximizing, char player, int depth, int alpha, int beta) {
		if (depth == 0) {
			return evaluateBoard(player);
		}
		char opponent = (player == b.getRED()) ? b.getBLUE() : b.getRED();
		List<Move> possibleMoves = generateAllMoves(player);
		if (isMaximizing) {
			int maxEval = Integer.MIN_VALUE;
			for (Move move : possibleMoves) {
				b.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
				int eval = alphabeta(false, opponent, depth - 1, alpha, beta);
				b.movePiece(move.getToRow(), move.getToCol(), move.getFromRow(), move.getFromCol());
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if (beta <= alpha) {
					break;
				}
			}
			return maxEval;
		} else {
			int minEval = Integer.MAX_VALUE;
			for (Move move : possibleMoves) {
				b.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
				int eval = alphabeta(true, opponent, depth - 1, alpha, beta);
				b.movePiece(move.getToRow(), move.getToCol(), move.getFromRow(), move.getFromCol());
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alpha) {
					break;
				}
			}
			return minEval;
		}
	}

	public int minimax(boolean isMaximizing, char player, int depth) {
		if (depth == 0) {
			return evaluateBoard(player);
		}
		char opponent = (player == b.getRED()) ? b.getBLUE() : b.getRED();
		List<Move> possibleMoves = generateAllMoves(player);
		if (isMaximizing) {
			int maxEval = Integer.MIN_VALUE;
			for (Move move : possibleMoves) {
				b.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
				int eval = minimax(false, opponent, depth - 1);
				b.movePiece(move.getToRow(), move.getToCol(), move.getFromRow(), move.getFromCol());
				if (eval > maxEval) {
					maxEval = Math.max(maxEval, eval);
				}
			}
			return maxEval;
		} else {
			int minEval = Integer.MAX_VALUE;
			for (Move move : possibleMoves) {
				b.movePiece(move.getFromRow(), move.getFromCol(), move.getToRow(), move.getToCol());
				int eval = minimax(true, opponent, depth - 1);
				b.movePiece(move.getToRow(), move.getToCol(), move.getFromRow(), move.getFromCol());
				if (eval < minEval) {
					minEval = Math.min(minEval, eval);
				}
			}
			return minEval;
		}
	}
}
