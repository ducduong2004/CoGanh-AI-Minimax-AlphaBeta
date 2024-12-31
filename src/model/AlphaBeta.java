package model;

import java.util.Map.Entry;

public class AlphaBeta {

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

        // Khởi chạy Alpha-Beta Pruning
        run(0, map, Integer.MIN_VALUE, Integer.MAX_VALUE);

        if (map.nextMap != -1)
            return map.linkMap.get(map.nextMap).pre_move;
        else {
            System.out.println("het nuoc di");
            return map.pre_move;
        }
    }

    public static void run(int depth, Map m, int alpha, int beta) {
        // Kiểm tra nếu trò chơi đã kết thúc
        if (m.CheckFinish() != 0) {
            if (m.CheckFinish() == 1) {
                m.point = 8000 - 100 * depth; // AI thắng
            } else {
                m.point = -8000 + 100 * depth; // Người chơi thắng
            }
            return;
        }

        // Nếu đạt đến độ sâu tối đa, dừng lại
        if (depth == 3) {
            return;
        }

        // Sinh tất cả các trạng thái tiếp theo
        m.nextFullMap();

        // Nếu là lượt của AI (MAX)
        if (m.turn == 1) {
            for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
                Map branchMap = set.getValue();
                run(depth + 1, branchMap, alpha, beta);
                m.MAX = Math.max(m.MAX, branchMap.point);
                alpha = Math.max(alpha, branchMap.point);
                if (beta <= alpha) {
                    break; // Cắt tỉa
                }
            }
            m.point = m.MAX;
        } else {
            // Nếu là lượt của người chơi (MIN)
            for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
                Map branchMap = set.getValue();
                run(depth + 1, branchMap, alpha, beta);
                m.MIN = Math.min(m.MIN, branchMap.point);
                beta = Math.min(beta, branchMap.point);
                if (beta <= alpha) {
                    break; // Cắt tỉa
                }
            }
            m.point = m.MIN;
        }

        // Chọn nước đi tốt nhất ở độ sâu đầu tiên (depth == 0)
        if (depth == 0) {
            if (m.turn == 1) {
                int best = Integer.MIN_VALUE;
                int vt = -1;
                for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
                    Map temp = set.getValue();
                    if (temp.point > best) {
                        best = temp.point;
                        vt = set.getKey();
                    }
                }
                m.nextMap = vt;
            } else {
                int best = Integer.MAX_VALUE;
                int vt = -1;
                for (Entry<Integer, Map> set : m.linkMap.entrySet()) {
                    Map temp = set.getValue();
                    if (temp.point < best) {
                        best = temp.point;
                        vt = set.getKey();
                    }
                }
                m.nextMap = vt;
            }
        }
    }
}
