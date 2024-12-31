package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import controller.GameController;
import model.Ai;
import model.Board;
import model.Move;

public class Ui extends JPanel {

	private static final long serialVersionUID = 1L;
	private final int SIZE = 100; // Kích thước của mỗi ô
	private final int PADDING = 90; // Khoảng cách từ viền đến lưới
	private int selectedRow = -1, selectedCol = -1; // Lưu tọa độ quân cờ đã chọn
	private Board b;
	private Color[][] board = new Color[5][5]; // Mảng lưu quân cờ
//	private Ai a = new Ai(getB());
	private JTable infoTable;
	private boolean useAlphaBeta = true;
	private JFrame frame = new JFrame("Game Menu");;
	GameController controller;

	public boolean isUseAlphaBeta() {
		return useAlphaBeta;
	}

	public Ui(GameController controller, Board board2) {
		this.controller = controller;
		this.b = board2;
		getB().displayBoard();
		this.board = convertBoardToColor(getB().getBoard());

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (getB().isRedTurn()) {
					int col = getRound((e.getX() - PADDING), SIZE);
					int row = getRound((e.getY() - PADDING), SIZE);

					if (getB().isInBounds(row, col)) {
						if (selectedRow != -1 && selectedCol != -1) {
							controller.playerMove(selectedRow, selectedCol, row, col);
							board = convertBoardToColor(getB().getBoard());
							selectedRow = -1;
							selectedCol = -1;
						} else if (board[row][col] != null) {
							System.out.println("Da chon quan co tai: Row = " + row + ", Col = " + col);
							selectedRow = row;
							selectedCol = col;
							updateBoard(b);
						}
					}
				}
			}

			private int getRound(int i, int size) {
				if (i % size >= 50) {
					return (i + 100) / size;
				}
				return i / size;
			}
		});
		frame.add(this);
	}

	public void updateBoard(Board board) {
		this.board = convertBoardToColor(board.getBoard());
		repaint();
		updateInfoTable();
	}

	private Color[][] convertBoardToColor(char[][] aiBoard) {
		Color[][] colorBoard = new Color[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (aiBoard[i][j] == getB().getRED()) {
					colorBoard[i][j] = Color.RED;
				} else if (aiBoard[i][j] == getB().getBLUE()) {
					colorBoard[i][j] = Color.BLUE;
				} else {
					colorBoard[i][j] = null; // Nếu không có quân cờ, gán null
				}
			}
		}
		return colorBoard;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Vẽ các đường lưới
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				int x = PADDING + i * SIZE;
				int y = PADDING + j * SIZE;

				if (i < 4) {
					g2d.drawLine(x, y, x + SIZE, y);
				}
				if (j < 4) {
					g2d.drawLine(x, y, x, y + SIZE);
				}
				if (i < 4 && j < 4) {
					if ((i + j) % 2 == 0)
						g2d.drawLine(x, y, x + SIZE, y + SIZE);
					else
						g2d.drawLine(x + SIZE, y, x, y + SIZE);
				}
			}
		}

		// Vẽ các quân cờ từ mảng board
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (board[i][j] != null) {
					// Nếu là quân cờ được chọn, thay đổi màu
					if (i == selectedRow && j == selectedCol) {
						drawCircle(g2d, PADDING + j * SIZE, PADDING + i * SIZE, Color.YELLOW); // Vị trí quân cờ được
																								// chọn
					} else {
						drawCircle(g2d, PADDING + j * SIZE, PADDING + i * SIZE, board[i][j]); // Vẽ quân cờ
					}
				}
			}
		}
	}

	private void drawCircle(Graphics2D g2d, int x, int y, Color color) {
		g2d.setColor(color);
		g2d.fillOval(x - 10, y - 10, 20, 20); // Vẽ quân cờ dạng hình tròn
	}

	public JPanel createSidePanel() {
		JPanel sidePanel = new JPanel();
		sidePanel.setBackground(Color.GRAY);
		sidePanel.setPreferredSize(new Dimension(400, 0)); // Tăng chiều rộng của bảng bên

		// Tạo tiêu đề "Cờ Gánh"
		JLabel titleLabel = new JLabel("Cờ Gánh");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // Chữ to hơn
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều ngang
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa trong BoxLayout

		// Đặt tiêu đề vào một panel riêng
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.GRAY);
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.add(Box.createVerticalGlue()); // Đẩy tiêu đề xuống giữa
		titlePanel.add(titleLabel);
		titlePanel.add(Box.createVerticalGlue()); // Đẩy tiêu đề lên giữa

		// Vùng hiển thị thông báo (console)
		JTextArea consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		consoleArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Chữ lớn hơn
		consoleArea.setBackground(Color.WHITE);
		consoleArea.setLineWrap(true);
		consoleArea.setWrapStyleWord(true);
		JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
		consoleScrollPane.setPreferredSize(new Dimension(380, 500)); // Tăng chiều cao bảng thông báo

		// Cập nhật console mỗi khi có thông báo mới
		PrintStream printStream = new PrintStream(new OutputStream() {
			@Override
			public void write(int b) {
				consoleArea.append(String.valueOf((char) b));
				consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
			}
		}, true);

		try {
			System.setOut(new PrintStream(printStream, true, StandardCharsets.UTF_8.name()));
			System.setErr(new PrintStream(printStream, true, StandardCharsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// Bảng thông tin quân xanh và quân đỏ
		String[] columnNames = { "Loại quân", "Số lượng" };
		Object[][] data = { { "Quân xanh", countPieces(getB().getBLUE()) },
				{ "Quân đỏ", countPieces(getB().getRED()) } };

		infoTable = new JTable(data, columnNames); // Liên kết bảng vào thuộc tính infoTable
		infoTable.setEnabled(false);
		infoTable.setFont(new Font("Arial", Font.PLAIN, 16));
		infoTable.setRowHeight(30);
		JScrollPane tableScrollPane = new JScrollPane(infoTable);

		// Bảng thông tin phương pháp và độ sâu
		String[] methodColumnNames = { "", "" };
		Object[][] methodData = { { "Phương pháp:", "AlphaBeta" } };

		DefaultTableModel methodTableModel = new DefaultTableModel(methodData, methodColumnNames);
		JTable methodTable = new JTable(methodTableModel);
		methodTable.setEnabled(false);
		methodTable.setFont(new Font("Arial", Font.PLAIN, 16));
		methodTable.setRowHeight(30);
		JScrollPane methodScrollPane = new JScrollPane(methodTable);

		// Tạo panel chứa các nút
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2, 20, 20));
		buttonPanel.setBackground(Color.GRAY);

		// Tạo các nút
		JButton newGameButton = new JButton("New Game");
		JButton exitButton = new JButton("Exit");
		JButton alphaBetaButton = new JButton("AlphaBeta");
		JButton minimaxButton = new JButton("Minimax");

		// Tăng kích thước và font chữ các nút
		Font buttonFont = new Font("Arial", Font.BOLD, 18);
		Dimension buttonSize = new Dimension(150, 50);
		for (JButton button : new JButton[] { newGameButton, exitButton, alphaBetaButton, minimaxButton }) {
			button.setFont(buttonFont);
			button.setPreferredSize(buttonSize);
		}

		// Thêm sự kiện cho các nút
		newGameButton.addActionListener(e -> {
			getB().displayBoard();
			board = convertBoardToColor(getB().getBoard());
			repaint();
			System.out.println("Game moi da duoc tao!");
		});

		exitButton.addActionListener(e -> {
			System.exit(0);
		});
		alphaBetaButton.addActionListener(e -> {
			useAlphaBeta = true;
			methodTableModel.setValueAt("AlphaBeta", 0, 1); // Cập nhật phương pháp
			System.out.println("AI da chuyen sang thuat toan Alpha-Beta.");
		});

		minimaxButton.addActionListener(e -> {
			useAlphaBeta = false;
			methodTableModel.setValueAt("Minimax", 0, 1); // Cập nhật phương pháp
			System.out.println("AI da chuyen sang thuat toan Minimax.");
		});

		// Thêm nút vào buttonPanel
		buttonPanel.add(newGameButton);
		buttonPanel.add(exitButton);
		buttonPanel.add(alphaBetaButton);
		buttonPanel.add(minimaxButton);

		// Sử dụng BoxLayout để sắp xếp các thành phần theo chiều dọc
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		sidePanel.add(titlePanel); // Tiêu đề ở trên cùng và căn giữa
		sidePanel.add(Box.createVerticalStrut(10));
		sidePanel.add(consoleScrollPane); // Bảng thông báo
		sidePanel.add(Box.createVerticalStrut(10));
		sidePanel.add(tableScrollPane); // Bảng số lượng quân cờ
		sidePanel.add(Box.createVerticalStrut(10));
		sidePanel.add(methodScrollPane); // Bảng phương pháp và độ sâu
		sidePanel.add(Box.createVerticalStrut(20));
		sidePanel.add(buttonPanel); // Các nút ở cuối cùng

		return sidePanel;
	}

	private int countPieces(char pieceType) {
		int count = 0;
		char[][] aiBoard = getB().getBoard();
		for (int i = 0; i < aiBoard.length; i++) {
			for (int j = 0; j < aiBoard[i].length; j++) {
				if (aiBoard[i][j] == pieceType) {
					count++;
				}
			}
		}
		return count;
	}

	private void updateInfoTable() {
		Object[][] data = { { "Quân xanh", countPieces(getB().getBLUE()) },
				{ "Quân đỏ", countPieces(getB().getRED()) } };

		// Cập nhật bảng thông tin quân
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				infoTable.setValueAt(data[i][j], i, j); // Cập nhật số lượng quân
			}
		}
	}

	public void display() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.add(createSidePanel(), BorderLayout.EAST);
		frame.setVisible(true);
	}

	public Board getB() {
		return b;
	}

}
