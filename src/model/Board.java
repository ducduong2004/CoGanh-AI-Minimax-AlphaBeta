package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
	private final int SIZE = 5;
	private final char EMPTY = '.';
	private final char RED = 'R';
	private final char BLUE = 'B';
	public boolean isAIturn = false;
	public char[][] board = new char[SIZE][SIZE];



	public Board() {

	}


	private boolean isRedTurn = true;

	public int getSIZE() {
		return SIZE;
	}

	public char getEMPTY() {
		return EMPTY;
	}

	public char getRED() {
		return RED;
	}

	public char getBLUE() {
		return BLUE;
	}

	public char[][] getBoard() {
		return board;
	}

	public boolean isRedTurn() {
		return isRedTurn;
	}

	public void setRedTurn(boolean isRedTurn) {
		this.isRedTurn = isRedTurn;
	}

	public void displayBoard() {
		int n = SIZE;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				board[0][i] = RED;
				board[SIZE - 1][i] = BLUE;
				if (i != 0 && i != n - 1 && j != 0 && j != n - 1) {
					board[i][j] = EMPTY;
				} else {
					board[1][n - 1] = RED;
					board[1][0] = RED;
					board[2][4] = RED;
					board[i][j] = BLUE;
				}
			}
		}
	}

	public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
		if (toRow < 0 || toRow >= SIZE || toCol < 0 || toCol >= SIZE)
			return false;
		if (board[fromRow][fromCol] == EMPTY || board[toRow][toCol] != EMPTY)
			return false;
		if (isRedTurn && board[fromRow][fromCol] != RED)
			return false;
		if (!isRedTurn && board[fromRow][fromCol] != BLUE)
			return false;

		// duong cheo
		if ((fromRow + fromCol) % 2 != 0 && (((toRow - 1 == fromRow) && (toCol - 1 == fromCol)
				|| (toRow + 1 == fromRow) && (toCol + 1 == fromCol))
				|| ((toRow + 1 == fromRow) && (toCol - 1 == fromCol)
						|| (toRow - 1 == fromRow) && (toCol + 1 == fromCol))))
			return false;

		int dRow = Math.abs(fromRow - toRow);
		int dCol = Math.abs(fromCol - toCol);

		return dRow <= 1 && dCol <= 1 && (dRow + dCol > 0);
	}

	public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
		board[toRow][toCol] = board[fromRow][fromCol];
		board[fromRow][fromCol] = EMPTY;
	}

	public void checkGanh(int row, int col) {
		char player = board[row][col];
		char opponent = (player == RED) ? BLUE : RED;

		checkAndCapture(row - 1, col, row + 1, col, player, opponent);
		checkAndCapture(row, col - 1, row, col + 1, player, opponent);
		checkAndCapture(row - 1, col - 1, row + 1, col + 1, player, opponent);
		checkAndCapture(row - 1, col + 1, row + 1, col - 1, player, opponent);
	}

	public void checkAndCapture(int row1, int col1, int row2, int col2, char player, char opponent) {
		if (isInBounds(row1, col1) && isInBounds(row2, col2)) {
			if (board[row1][col1] == opponent && board[row2][col2] == opponent) {
				board[row1][col1] = player;
				board[row2][col2] = player;
				System.out.println("Ganh thanh cong! quan doi thu da bi ganh");
			}
		}

	}

	public boolean isInBounds(int row, int col) {
		return row >= 0 && row <= SIZE - 1 && col >= 0 && col <= SIZE - 1;
	}
	// kiểm tra nước vây
	public void checkVay(int row, int col) {
		char player = board[row][col];
		boolean[][] visited;
		Set<int[]> group;
		
		visited = new boolean[SIZE][SIZE];
		group = new HashSet<>();

		int[] dx, dy;
		// Duyệt qua các ô xung quanh
		dx = new int[] {-1, 1, 0, 0}; // Dịch chuyển hàng
		dy = new int[] {0, 0, -1, 1}; // Dịch chuyển cột

		//lặp qua 4 hướng xung quanh
		for (int i = 0; i < dx.length; i++) {
			int currentRow = row + dx[i];
			int currentCol = col + dy[i];


			// nếu cùng màu thì bỏ qua
			if(!isInBounds(currentRow, currentCol) || board[currentRow][currentCol] == player || board[currentRow][currentCol] == '.') {
				continue;
			}

			char current = board[currentRow][currentCol];
			boolean surround = isSurrounded(currentRow, currentCol, current, visited, group);

			if (surround) {
				System.out.println("VAY THANH CONG: " + group.size() + " quân cờ");
				for (int[] pos : group) {
					board[pos[0]][pos[1]] = player; // Chuyển đổi quân cờ
				}
			}
		}
	}

	private boolean isSurrounded(int row, int col, char current, boolean[][] visited, Set<int[]> group) {
		boolean isSurrounded = true;
		// Duyệt qua các ô xung quanh 
		int[] dx,dy;
		if(isCrossed(row,col)) {
			dx = new int[] {-1, 1, 0, 0, -1, -1, +1, +1}; // Dịch chuyển hàng
			dy = new int[] {0, 0, -1, 1, -1, -1, +1, +1}; // Dịch chuyển cột

		} else {
			dx = new int[] {-1, 1, 0, 0}; // Dịch chuyển hàng
			dy = new int[] {0, 0, -1, 1}; // Dịch chuyển cột

		}


		// Sử dụng DFS để tìm nhóm liên thông
		visited[row][col] = true;
		group.add(new int[]{row, col});
		for (int i = 0; i < dx.length; i++) {
			int newRow = row + dx[i];
			int newCol = col + dy[i];

			// Kiểm tra nếu ô nằm ngoài biên
			if (newRow < 0 || newRow >= SIZE || newCol < 0 || newCol >= SIZE) {
				continue; // Không bị vây nếu có ô ngoài biên
			}

			// Nếu gặp ô trống, không bị vây
			if (board[newRow][newCol] == '.') {
				return false;
			}

			// Nếu gặp ô chưa được kiểm tra và cùng màu, tiếp tục tìm kiếm
			if (!visited[newRow][newCol] && board[newRow][newCol] == current) {
				isSurrounded &= isSurrounded(newRow, newCol, current, visited, group);
			}


		}
		return isSurrounded;
	}


	//kiểm tra ô hiện tại có cùng màu không
	private boolean checkSameColor(int i, int j, char current) {
		if(!isInBounds(i, j)) return false;
		if(board[i][j] == '.') return false;
		if(board[i][j] == current) return false;
		return true;
	}

	// kiểm tra ô hiện tại có nằm ở vị trí đi đường chéo được hay không
	private boolean isCrossed(int x, int y) {
		if(x % 2 == y % 2) return true;
		return false;
	}

	// kiểm tra ô hiện tại có chẹt quân xung quanh nào không
	public void checkChet(int row, int col) {
		char player = board[row][col];
		char opponent = (player == RED) ? BLUE : RED;

		captureForChet(row, col + 1, row, col + 2, player, opponent);
		captureForChet(row, col - 1, row, col - 2, player, opponent);
		captureForChet(row - 1, col, row - 2, col, player, opponent);
		captureForChet(row + 1, col, row + 2, col, player, opponent);
		if (isCrossed(row, col)) {
			captureForChet(row + 1, col + 1, row + 2, col + 2, player, opponent);
			captureForChet(row - 1, col - 1, row - 2, col - 2, player, opponent);
			captureForChet(row - 1, col + 1, row - 2, col + 2, player, opponent);
			captureForChet(row + 1, col - 1, row + 2, col - 2, player, opponent);
		}
	}

	// kiểm tra và đổi quân bị chẹt thành màu khác
	public void captureForChet(int row1, int col1, int row2, int col2, char player, char opponent) {
		if (isInBounds(row1, col1) && isInBounds(row2, col2)) {
			if (board[row1][col1] == opponent && board[row2][col2] == player && board[row1][col1] != '.'
					&& board[row2][col2] != '.') {
				board[row1][col1] = player;
				System.out.println("Chet thanh cong! quan doi thu da bi chet");
			}
		}

	}


	public List<Move> checkDirections(int row, int col) {
		List<Move> allMoves = new ArrayList<>();

		// Duyệt qua các hướng xung quanh (8 hướng)
		for (int dRow = -1; dRow <= 1; dRow++) {
			for (int dCol = -1; dCol <= 1; dCol++) {
				if (dRow == 0 && dCol == 0)
					continue; // Bỏ qua ô hiện tại

				int newRow = row + dRow;
				int newCol = col + dCol;

				// Kiểm tra nếu ô này nằm trong phạm vi bàn cờ
				if (isInBounds(newRow, newCol)) {
					// Giả lập rằng bàn cờ chỉ có một quân cờ tại vị trí (row, col)
					// Và không có quân cờ nào khác xung quanh, kiểm tra lối đi hợp lệ
					if ((row + col) % 2 != 0 && (((newRow - 1 == row) && (newCol - 1 == col))
							|| (newRow + 1 == row) && (newCol + 1 == col)
							|| ((newRow + 1 == row) && (newCol - 1 == col))
							|| (newRow - 1 == row) && (newCol + 1 == col))) {
						continue; // Nếu thỏa mãn điều kiện đường chéo thì bỏ qua, không thêm lối đi này
					}
				}

				// Thêm lối đi hợp lệ vào danh sách dưới dạng đối tượng Move
				allMoves.add(new Move(row, col, newRow, newCol));

			}
		}

		return allMoves; // Trả về danh sách các lối đi hợp lệ dưới dạng đối tượng Move
	}

}
