package model;

import java.util.Map.Entry;

public class Minimax {

	public static Move nextMove(char mark[][], int turn) {
		Map map = new Map();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				map.chessboard[i][j] = 0;
				if (mark[i][j] == 'B') {
					map.chessboard[i][j] = 1;
					map.countBlue++;
				}
				if (mark[i][j] == 'R') {
					map.chessboard[i][j] = -1;
					map.countRed++;
				}
			}
		}
		map.turn = turn;
		run(0, map);
		if (map.nextMap != -1)
			return map.linkMap.get(map.nextMap).pre_move;
		else {
			System.out.println("het nuoc di");
			return map.pre_move;
		}
	}


	public static void run(int k, Map m) {
		// kiểm tra nếu bàn cờ đã kết thúc thì dừng lại
		if (m.CheckFinish() != 0) {
			if (m.CheckFinish() == 1) {
				m.point = 8000 - 100 * k;
			} else {
				m.point = -8000 + 100 * k;
			}
			return;
		}

		// nếu đạt tới độ sâu thì dừng lại
		if (k == 6) {
			return;
		}
		
		// khởi tạo các bàn cờ có thể đi bỏ vào trong listmap
		m.nextFullMap();
		// nếu là lượt của máy thì tạo thêm bàn cờ đánh giá theo MAX
		if (m.turn == 1) {
			for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
				
				Map branchMap = set.getValue();
				run(k + 1, branchMap);
				if (m.MAX < branchMap.point) {
	                m.MAX = branchMap.point;
	            }
			}
			m.point = m.MAX;
		} else {
			// nếu ngược lại là lượt của người chơi thì sẽ đánh giá theo MIN
			for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
				
				Map branchMap = set.getValue();
				run(k + 1, branchMap);
				if (m.MIN > branchMap.point) {
					m.MIN = branchMap.point;
				}
				
			}
			m.point = m.MIN; // điểm của bàn cờ
		}

		// nếu là lần minimax đầu tiên và lượt của máy thì 
		if (k == 0) {
			if (m.turn == 1) {
				/*
				 * best là điểm tốt nhất 
				 * vt là index của bàn cờ co nuoc di tot nhat trong list map
				 * 
				 */
				int best = -8888;
				int vt = -1;
				 for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
					Map temp = set.getValue();
					if(m.an && m.countBlue == temp.countBlue)
						continue;

					if (temp.point > best) {
						best = temp.point;
						vt = set.getKey();
					}
				}
				m.nextMap = vt;
			} else {

				//nếu là lượt của người chơi
				/*
				 * best là điểm tốt nhất NHUNG TỐT NHẤT CHO NGƯỜI CHƠI
				 * vt là index của bàn cờ trong list map
				 */
				int best = 8888;
				int vt = -1;
				for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
					Map temp = set.getValue();
					if(m.an && m.countBlue == temp.countBlue)
						continue;

					if (temp.point < best) {
						best = temp.point;
						vt = set.getKey();
					}
				}
				m.nextMap = vt;
			}
		}
		return;
	}
}
