package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Map {
	public int[][] chessboard = new int[5][5];
	public int MIN = 9999;
	public int MAX = -9999;
	public int countRed;
	public int countBlue;
	public int point;
	public int nextMap = -1;
	public Map parent;
	public boolean an;
	public Move pre_move;
	public static int[][] diem = { 
			{ 50, 20, 40, 20, 50 }, 
			{ 20, 40, 30, 40, 20 },
			{ 40, 30, 100, 30, 40 }, 
			{ 20, 40, 30, 40, 20 }, 
			{ 50, 20, 40, 20, 50 } };
	
	
	public HashMap<Integer,Map> linkMap = new HashMap<>();
	public int turn;

	public int keyGenerator(Map map) {
		return map.hashCode();
	}
	
	public Map() {
		
	}

	/*
	 * import giá các giá trị quân cờ vào bàn cờ
	 * 
	 * Ai = 1
	 * player = -1
	 * ô trống = 0
	 * 
	 * */ 
	
	public void printMap() {
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[i].length; j++) {
                System.out.print(chessboard[i][j] + " "); // Print each element followed by a space
            }
            System.out.println(); // Move to the next line after each row
        }
    }
		public void generateMap() {
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5; j++) {
				this.chessboard[i][j] = 0;
			}
		}
		for (int j = 1; j <= 5; j++) {
			int i = 1;
			this.chessboard[i][j] = -1;
		}
		for (int j = 1; j <= 5; j++) {
			int i = 5;
			this.chessboard[i][j] = 1;
		}
		this.chessboard[2][1] = -1;
		this.chessboard[2][5] = -1;
		this.chessboard[3][5] = -1;
		this.chessboard[3][1] = 1;
		this.chessboard[4][1] = 1;
		this.chessboard[4][5] = 1;
		this.countBlue = 8;
		this.countRed = 8;
		this.turn = 1;
	}

	public void copyMap(Map map) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				this.chessboard[i][j] = map.chessboard[i][j];
			}
		}
		this.countBlue = map.countBlue;
		this.countRed = map.countRed;
		this.turn = map.turn;
	}	



	public void nextFullMap() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (this.chessboard[i][j] == this.turn) {
					this.nextMap(i, j);
				}
			}
		}
	}

	public void nextMap(int i, int j) {
		if (i != 0 && this.chessboard[i - 1][j] == 0) {
			Map m = new Map();
			m.copyMap(this);
			m.chessboard[i][j] = 0;
			m.chessboard[i - 1][j] = m.turn;
			m.pre_move = new Move(i, j, i - 1, j);
			this.checkMap(i - 1, j, m);
			m.turn = -m.turn;
			m.point = m.heuristic_Map();
			m.parent = this;
			int key = keyGenerator(m);
			this.linkMap.put(key,m);
		}
		if (i != 4 && this.chessboard[i + 1][j] == 0) {
			Map m = new Map();
			m.copyMap(this);
			m.chessboard[i][j] = 0;
			m.chessboard[i + 1][j] = m.turn;
			m.pre_move = new Move(i, j, i + 1, j);
			this.checkMap(i + 1, j, m);
			m.turn = -m.turn;
			m.point = m.heuristic_Map();
			m.parent = this;
			int key = keyGenerator(m);
			this.linkMap.put(key,m);		}
		if (j != 0 && this.chessboard[i][j - 1] == 0) {
			Map m = new Map();
			m.copyMap(this);
			m.chessboard[i][j] = 0;
			m.chessboard[i][j - 1] = m.turn;
			m.pre_move = new Move(i, j, i, j - 1);
			this.checkMap(i, j - 1, m);
			m.turn = -m.turn;
			m.point = m.heuristic_Map();
			m.parent = this;
			int key = keyGenerator(m);
			this.linkMap.put(key,m);
		}
		if (j != 4 && this.chessboard[i][j + 1] == 0) {
			Map m = new Map();
			m.copyMap(this);
			m.chessboard[i][j] = 0;
			m.chessboard[i][j + 1] = m.turn;
			m.pre_move = new Move(i, j, i, j + 1);
			this.checkMap(i, j + 1, m);
			m.turn = -m.turn;
			m.point = m.heuristic_Map();
			m.parent = this;
			int key = keyGenerator(m);
			this.linkMap.put(key,m);
		}
		if ((i + j) % 2 == 0 && i != 0) {
			if (j != 0 && this.chessboard[i - 1][j - 1] == 0) {
				Map m = new Map();
				m.copyMap(this);
				m.chessboard[i][j] = 0;
				m.chessboard[i - 1][j - 1] = m.turn;
				m.pre_move = new Move(i, j, i - 1, j - 1);
				this.checkMap(i - 1, j - 1, m);
				m.turn = -m.turn;
				m.point = m.heuristic_Map();
				m.parent = this;
				int key = keyGenerator(m);
				this.linkMap.put(key,m);
			}
			if (j != 4 && this.chessboard[i - 1][j + 1] == 0) {
				Map m = new Map();
				m.copyMap(this);
				m.chessboard[i][j] = 0;
				m.chessboard[i - 1][j + 1] = m.turn;
				m.pre_move = new Move(i, j, i - 1, j + 1);
				this.checkMap(i - 1, j + 1, m);

				m.turn = -m.turn;
				m.point = m.heuristic_Map();
				m.parent = this;
				int key = keyGenerator(m);
				this.linkMap.put(key,m);
			}
		}
		if ((i + j) % 2 == 0 && i != 4) {
			if (j != 0 && this.chessboard[i + 1][j - 1] == 0) {
				Map m = new Map();
				m.copyMap(this);
				m.chessboard[i][j] = 0;
				m.chessboard[i + 1][j - 1] = m.turn;
				m.pre_move = new Move(i, j, i + 1, j - 1);
				this.checkMap(i + 1, j - 1, m);

				m.turn = -m.turn;
				m.point = m.heuristic_Map();
				m.parent = this;
				int key = keyGenerator(m);
				this.linkMap.put(key,m);
			}
			if (j != 4 && this.chessboard[i + 1][j + 1] == 0) {
				Map m = new Map();
				m.copyMap(this);
				m.chessboard[i][j] = 0;
				m.chessboard[i + 1][j + 1] = m.turn;
				m.pre_move = new Move(i, j, i + 1, j + 1);
				this.checkMap(i + 1, j + 1, m);

				m.turn = -m.turn;
				m.point = m.heuristic_Map();
				m.parent = this;
				int key = keyGenerator(m);
				this.linkMap.put(key,m);
			}
			
			
			
		}
		
	}
	
	public static boolean valid(int i, int j) {
		if(i < 0 || i > 4 || j < 0 || j > 4)
			return false;
		else
			return true;
	}

	public static boolean isCrossed(int i, int j) {
		if(i % 2 == j % 2) return true;
		return false;
	}

	public void checkMap(int i, int j, Map map) {
		int k = -map.turn;
		if (j != 0 && j != 4) {
			if (map.chessboard[i][j - 1] == k && map.chessboard[i][j + 1] == k) {
				map.chessboard[i][j - 1] = map.turn;
				map.chessboard[i][j + 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 2;
					map.countRed -= 2;
				} else {
					map.countBlue -= 2;
					map.countRed += 2;
				}
			}
		}
		if (i != 0 && i != 4) {
			if (map.chessboard[i - 1][j] == k && map.chessboard[i + 1][j] == k) {
				map.chessboard[i - 1][j] = map.turn;
				map.chessboard[i + 1][j] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 2;
					map.countRed -= 2;
				} else {
					map.countBlue -= 2;
					map.countRed += 2;
				}
			}
		}
		if ((i + j) % 2 == 0 && i != 0 && i != 4 && j != 0 && j != 4) {
			if (map.chessboard[i - 1][j - 1] == k && map.chessboard[i + 1][j + 1] == k) {
				map.chessboard[i - 1][j - 1] = map.turn;
				map.chessboard[i + 1][j + 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 2;
					map.countRed -= 2;
				} else {
					map.countBlue -= 2;
					map.countRed += 2;
				}
			}
			if (map.chessboard[i - 1][j + 1] == k && map.chessboard[i + 1][j - 1] == k) {
				map.chessboard[i - 1][j + 1] = map.turn;
				map.chessboard[i + 1][j - 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 2;
					map.countRed -= 2;
				} else {
					map.countBlue -= 2;
					map.countRed += 2;
				}
			}
		}
		if(valid(i, j + 1) && valid(i, j + 2) && map.chessboard[i][j + 1] == -map.turn && map.chessboard[i][j + 2] == map.turn) {
			map.chessboard[i][j + 1] = map.turn;
			if (map.turn == 1) {
				map.countBlue += 1;
				map.countRed -= 1;
			} else {
				map.countBlue -= 1;
				map.countRed += 1;
			}
		}

		if(valid(i, j - 1) && valid(i, j - 2) && map.chessboard[i][j - 1] == -map.turn && map.chessboard[i][j - 2] == map.turn) {
			map.chessboard[i][j - 1] = map.turn;
			if (map.turn == 1) {
				map.countBlue += 1;
				map.countRed -= 1;
			} else {
				map.countBlue -= 1;
				map.countRed += 1;
			}
		}

		if(valid(i - 1, j) && valid(i - 2, j) && map.chessboard[i - 1][j] == -map.turn && map.chessboard[i - 2][j] == map.turn) {
			map.chessboard[i - 1][j] = map.turn;
			if (map.turn == 1) {
				map.countBlue += 1;
				map.countRed -= 1;
			} else {
				map.countBlue -= 1;
				map.countRed += 1;
			}
		}

		if(valid(i + 1, j) && valid(i + 2, j) && map.chessboard[i + 1][j] == -map.turn && map.chessboard[i + 2][j] == map.turn) {
			map.chessboard[i + 1][j] = map.turn;
			if (map.turn == 1) {
				map.countBlue += 1;
				map.countRed -= 1;
			} else {
				map.countBlue -= 1;
				map.countRed += 1;
			}
		}

		if(isCrossed(i, j)) {
			if(valid(i + 1, j + 1) && valid(i + 2, j + 2) && map.chessboard[i + 1][j + 1] == -map.turn && map.chessboard[i + 2][j + 2] == map.turn) {
				map.chessboard[i + 1][j + 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 1;
					map.countRed -= 1;
				} else {
					map.countBlue -= 1;
					map.countRed += 1;
				}
			}

			if(valid(i - 1, j - 1) && valid(i - 2, j - 2) && map.chessboard[i - 1][j - 1] == -map.turn && map.chessboard[i - 2][j - 2] == map.turn) {
				map.chessboard[i - 1][j - 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 1;
					map.countRed -= 1;
				} else {
					map.countBlue -= 1;
					map.countRed += 1;
				}
			}

			if(valid(i - 1, j + 1) && valid(i - 2, j + 2) && map.chessboard[i - 1][j + 1] == -map.turn && map.chessboard[i - 2][j + 2] == map.turn) {
				map.chessboard[i - 1][j + 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 1;
					map.countRed -= 1;
				} else {
					map.countBlue -= 1;
					map.countRed += 1;
				}
			}

			if(valid(i + 1, j - 1) && valid(i + 2, j - 2) && map.chessboard[i + 1][j - 1] == -map.turn && map.chessboard[i + 2][j - 2] == map.turn) {
				map.chessboard[i + 1][j - 1] = map.turn;
				if (map.turn == 1) {
					map.countBlue += 1;
					map.countRed -= 1;
				} else {
					map.countBlue -= 1;
					map.countRed += 1;
				}
			}
		}

		//map.chessboard = checkVay(i, j);

		int blue = 0;
		int red = 0;
		for(int a = 0; a < 5; a++) {
			for(int b = 0; b < 5; b++) {
				if(map.chessboard[a][b] == 1 ) blue++;
				else if (map.chessboard[a][b] == -1) red++;
			}
		}
		map.countBlue = blue;
		map.countRed = red;

	}


	public int heuristic_Map() {
		int S = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (this.chessboard[i][j] == 1) {
					S += diem[i][j] + 50 * (countBlue - countRed);
				}
				if (this.chessboard[i][j] == -1) {
					S -= diem[i][j] + 50 * (-countBlue + countRed);
				}
			}
		}
		S += this.linkMap.size() * 5 * this.turn;
		return S;
	}



	public int CheckFinish() {
		if (this.countRed == 0) {
			return 1;
		}
		if (this.countBlue == 0) {
			return -1;
		}
		return 0;
	}


	// kiểm tra nước vây
	public int[][] checkVay(int row, int col) {
		int player = turn;
		boolean[][] visited;
		Set<int[]> group;
		
		visited = new boolean[5][5];
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
			if(!isInBounds(currentRow, currentCol) || chessboard[currentRow][currentCol] == player || chessboard[currentRow][currentCol] == '.') {
				continue;
			}

			int current = chessboard[currentRow][currentCol];
			boolean surround = isSurrounded(currentRow, currentCol, current, visited, group);

			if (surround) {
				System.out.println("VAY THANH CONG: " + group.size() + " quân cờ");
				for (int[] pos : group) {
					chessboard[pos[0]][pos[1]] = player; // Chuyển đổi quân cờ
				}
			}
		}
		
		return chessboard;
	}

	
	
	public boolean isInBounds(int row, int col) {
		return row >= 0 && row <= 5 - 1 && col >= 0 && col <= 5 - 1;
	}
	
	private boolean isSurrounded(int row, int col, int current, boolean[][] visited, Set<int[]> group) {
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
			if (newRow < 0 || newRow >= 5 || newCol < 0 || newCol >= 5) {
				continue; // Không bị vây nếu có ô ngoài biên
			}

			// Nếu gặp ô trống, không bị vây
			if (chessboard[newRow][newCol] == 0) {
				return false;
			}

			// Nếu gặp ô chưa được kiểm tra và cùng màu, tiếp tục tìm kiếm
			if (!visited[newRow][newCol] && chessboard[newRow][newCol] == current) {
				isSurrounded &= isSurrounded(newRow, newCol, current, visited, group);
			}


		}
		return isSurrounded;
	}

}
