import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {

  private class MineTile extends JButton {
    int rowNumber;
    int colNumber;
    boolean isFlag;

    public MineTile(int rowNumber, int colNumber) {
      this.rowNumber = rowNumber;
      this.colNumber = colNumber;
      this.isFlag = false;
    }

    @Override
    public String toString() {
      return "Damn damn";
    }
  }

  int tileSize = 70;
  int numRows = 8;
  int numCols = numRows;
  int boardWidth = numCols * tileSize;
  int boardHeight = numRows * tileSize;

  JFrame frame = new JFrame("Minesweeper");
  JLabel textLabel = new JLabel();
  JPanel textPanel = new JPanel();
  JPanel boardPanel = new JPanel();

  MineTile[][] board = new MineTile[numRows][numCols];
  ArrayList<MineTile> mineList;
  int mineCount = 10;
  Random random = new Random();

  int tilesClicked = 0;
  boolean gameOver = false;
  boolean gameWon = false;

  Minesweeper() {

    frame.setSize(boardWidth, boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    textLabel.setFont(new Font("Arial", Font.BOLD, 25));
    textLabel.setHorizontalAlignment(JLabel.CENTER);
    textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
    textLabel.setOpaque(true);

    textPanel.setLayout(new BorderLayout());
    textPanel.add(textLabel);

    boardPanel.setLayout(new GridLayout(numRows, numCols)); // 8 x 8

    boardPanel.setBackground(Color.black);

    frame.add(textPanel, BorderLayout.NORTH);
    frame.add(boardPanel);

    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        MineTile tile = new MineTile(r, c);
        board[r][c] = tile;

        tile.setFocusable(false);
        tile.setMargin(new Insets(0, 0, 0, 0));
        tile.setFont(new Font("JetBrainsMono NF", Font.BOLD, 40));
        // tile.setText("💣");

        tile.addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            MineTile tile = (MineTile) e.getSource(); // this tile basically is now same as those in board arrayList

            // left click
            if (e.getButton() == MouseEvent.BUTTON1) {

              if (gameOver || gameWon) return; // tiles wont work if game over

              if (tile.getText().isEmpty()) {
                if (mineList.contains(tile)) {
                  revealMines(); // game over
                } else {
                  checkMine(tile.rowNumber, tile.colNumber);
                }
              }
              // System.out.println(tile);
              // System.out.println(board[0][0]);
            }

            if (e.getButton() == MouseEvent.BUTTON2) {
              System.out.println(tile.getText());
            }

            // right click
            if (e.getButton() == MouseEvent.BUTTON3) {
              if (tile.getText() == "" && tile.isEnabled()) {
                tile.setText("|>");
                tile.isFlag = true;
              } else if (tile.getText() == "|>"){
                tile.setText("");
                tile.isFlag = false;
              }
            }
          }
        });

        boardPanel.add(tile);
      }
    }

    setMines();

    frame.setVisible(true);

  }

  void setMines() {
    mineList = new ArrayList<MineTile>();

    // Set static mines (for testing)
    // mineList.add(board[1][1]);
    // mineList.add(board[2][3]);
    // mineList.add(board[4][3]);
    // mineList.add(board[6][5]);

    int mineLeft = mineCount;

    while (mineLeft > 0) {
      int r = random.nextInt(numRows); // 0 to 7 (doesnt include 8)
      int c = random.nextInt(numRows); // 0 to 7 (doesnt include 8)
      MineTile tile = board[r][c];
      if (!mineList.contains(tile)) {
        mineList.add(tile);
        mineLeft--;
      }
    }

  }

  void revealMines() {
    for (int i = 0; i < mineList.size(); i++) {
      MineTile tile = mineList.get(i);

      tile.setText("*");
      tile.setEnabled(false);
      tile.setBackground(Color.gray);
    }
    gameOver = true;

    if (!gameWon) {
      textLabel.setText("Game Over !!");
    }
  }

  // Core game logic
  void checkMine(int r, int c) {

    // first base case if out of bounds
    if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
      return;
    }

    MineTile tile = board[r][c];

    // second base case if mine already clicked and checked
    if (!tile.isEnabled()) {
      return;
    }

    if (tile.isFlag) {
      return;
    }

    tile.setEnabled(false); // disable the button as its clicked already by now
    tilesClicked += 1;
    System.out.println(tilesClicked);

    int minesFound = 0;

    // top 3
    minesFound += countMine(r - 1, c - 1); // top left
    minesFound += countMine(r - 1, c); // top
    minesFound += countMine(r - 1, c + 1); // top right

    // left right neighbours
    minesFound += countMine(r, c - 1); // left
    minesFound += countMine(r, c + 1); // right

    // bottom three
    minesFound += countMine(r + 1, c - 1); // bottom left
    minesFound += countMine(r + 1, c); // bottom
    minesFound += countMine(r + 1, c + 1); // bottom right

    if (minesFound > 0) {
      tile.setText(Integer.toString(minesFound));
    } else {
      tile.setText("");
      checkMine(r - 1, c - 1); // top left
      checkMine(r - 1, c); // top
      checkMine(r - 1, c + 1); // top right

      // left right neighbours
      checkMine(r, c - 1); // left
      checkMine(r, c + 1); // right

      // bottom three
      checkMine(r + 1, c - 1); // bottom left
      checkMine(r + 1, c); // bottom
      checkMine(r + 1, c + 1); // bottom right
    }


    // Check for win
    if (tilesClicked == numRows*numCols - mineList.size()) {
      gameWon = true;
      textLabel.setText("You win!!");
      revealMines();
    }
  }

  int countMine(int r, int c) {

    if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
      return 0;
    }

    if (mineList.contains(board[r][c])) {
      return 1;
    }

    return 0;
  }
}