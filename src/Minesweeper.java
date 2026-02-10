import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {

  private class MineTile extends JButton {
    int rowNumber;
    int colNumber;

    public MineTile(int rowNumber, int colNumber) {
      this.rowNumber = rowNumber;
      this.colNumber = colNumber;
    }

    @Override
    public String toString() {
      return "Damn damn";
    }

  }
  
  int tileSize = 70;
  int numRows = 8;
  int numCols = numRows;
  int boardWidth = numCols*tileSize;
  int boardHeight = numRows*tileSize;
  
  JFrame frame = new JFrame("Minesweeper");
  JLabel textLabel = new JLabel();
  JPanel textPanel = new JPanel();
  JPanel boardPanel = new JPanel();

  MineTile[][] board = new MineTile[numRows][numCols];
  ArrayList<MineTile> mineList;

  Minesweeper() {

    frame.setSize(boardWidth, boardHeight);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    textLabel.setFont(new Font("Arial", Font.BOLD, 25));
    textLabel.setHorizontalAlignment(JLabel.CENTER);
    textLabel.setText("Minesweeper");
    textLabel.setOpaque(true);

    textPanel.setLayout(new BorderLayout());
    textPanel.add(textLabel);

    boardPanel.setLayout(new GridLayout(numRows, numCols)); // 8 x 8

    boardPanel.setBackground(Color.green);

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

            // right click
            // if (e.getButton() == MouseEvent.BUTTON3) {

            // }
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

    mineList.add(board[2][2]);
    mineList.add(board[4][7]);
    mineList.add(board[6][2]);
    mineList.add(board[5][7]);

    // mineList.get(0).setText("*");
  }

  void revealMines() {
    for (int i = 0; i < mineList.size(); i++) {
      MineTile tile = mineList.get(i);
      tile.setText("*");
      tile.setEnabled(false);
      tile.setBackground(Color.gray);
    }
  }

  // Core game logic
  void checkMine(int r, int c) {

    MineTile tile = board[r][c];
    tile.setEnabled(false); // disable the button as its clicked already by now

    int minesFound = 0;

    // top 3
    minesFound += countMine(r-1, c-1);  // top left
    minesFound += countMine(r-1, c);    // top
    minesFound += countMine(r-1, c+1);  // top right

    // left right neighbours
    minesFound += countMine(r, c-1);    // left
    minesFound += countMine(r, c+1);    // right

    // bottom three
    minesFound += countMine(r+1, c-1);  // bottom left
    minesFound += countMine(r+1, c);    // bottom
    minesFound += countMine(r+1, c+1);  // bottom right

    if (minesFound > 0) {
      tile.setText(Integer.toString(minesFound));
      return;
    } else {
      tile.setText("");
      checkMine(r + 1, c);
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
