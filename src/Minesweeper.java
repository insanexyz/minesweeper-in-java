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
    frame.setVisible(true);

  }
}
