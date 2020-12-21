
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// @author Dominic Campese

public class SudokuApp {


    private int[][] gameBoard;
    private Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        new SudokuApp();
    }
    
    // treat as main
    public SudokuApp() {
        initializeGame();
        printGameBoard();
        System.out.println();
        while (!hasGameBeenSolved()) {
            int[] cellLocation = getNextCell();
            if (cellLocation != null) {
                gameBoard[cellLocation[0]][cellLocation[1]] = cellLocation[2];
                printGameBoard();
               System.out.println("******************");
            } else {
                System.out.println("Difficult Level Sudoku Game. \n"
                        + "You need to implement more rules to solve it...");
                break;
            }
        }

   }

    private void initializeGame() {
        Scanner numbersScanner;
        int numRows = 9;
        int numCols = 9;
        gameBoard = new int[numRows][numCols];
        try {
            File numbers = new File("SudokuNumbers.txt");
            numbersScanner = new Scanner(numbers);
            // Place numbers from the file into the global 2D gameBoard array
            for (int row = 0; row < gameBoard.length; row++) { // Total number of rows
                for (int col = 0; col < gameBoard[row].length; col++) {  // Number of columns
                    gameBoard[row][col] = numbersScanner.nextInt();
                }
            }

        } catch (FileNotFoundException ex) {
        }
    }
    
    private void printGameBoard() {
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                System.out.print(gameBoard[row][col]);  
                // To avoid extra spaces
                if (col != gameBoard[row].length-1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
    
    private boolean hasGameBeenSolved() {
        // Loop over all the cells, if you encounter a 0, return false immediately
        for (int row = 0; row < gameBoard.length; row++) {
            for (int col = 0; col < gameBoard[row].length; col++) {
                if (gameBoard[row][col] == 0) {
                    return false;
                }
            }
        }
        // No zeros
        return true;
    }
    
    private int[] getNextCell() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gameBoard[row][col] == 0) {
                    Set<Integer> firstRuleElimination = getFirstRuleElimination(row);
                    Set<Integer> secondRuleElimination = getSecondRuleElimination(col);
                    Set<Integer> thirdRuleElimination = getThirdRuleElimination(row, col);
                    Set<Integer> survivors = getSurvivors(firstRuleElimination,
                            secondRuleElimination, thirdRuleElimination);
                    if (survivors.size() == 1) {
                        List<Integer> list = new ArrayList<>(survivors);
                        int uniqueValue = list.get(0);
                        return new int[]{row, col, uniqueValue};
                    }
                }
            }
        }
        return null;
    }
    
    private Set<Integer> getFirstRuleElimination(int row) {
        // Loops over all the columns of the given row (method input) and adds all
        // the numbers found in these cells into a Set object. Returns this set object.
        Set<Integer> numbersInRow = new HashSet<>();
        for (int col = 0; col < gameBoard[row].length; col++) {
            if (gameBoard[row][col] != 0) {
               numbersInRow.add(gameBoard[row][col]);
            }
        }
        return numbersInRow;
    }
    
    private Set<Integer> getSecondRuleElimination(int col) {
        // Loops over all the rows of the given column (method input) and adds all the numbers
        // found in these cells into a Set object. Returns this set object.
        Set<Integer> numbersInCol = new HashSet<>();
        for (int row = 0; row < gameBoard.length; row++) {
            if (gameBoard[row][col] != 0) {
                numbersInCol.add(gameBoard[row][col]);
            }
        }
        
        return numbersInCol;
    }
    
    
    private Set<Integer> getThirdRuleElimination(int row, int col) {
        int[] rowRangeForGroup = getRowRangeForGroup(row);
        int[] colRangeForGroup = getColRangeForGroup(col);
        Set<Integer> thirdRuleElimination = new HashSet<>();
        for (int i = rowRangeForGroup[0]; i <= rowRangeForGroup[1]; i++) {
            for (int j = colRangeForGroup[0]; j <= colRangeForGroup[1]; j++) {
                if (gameBoard[row][col] != 0) {
                thirdRuleElimination.add(gameBoard[i][j]);
                }
            }
        }
        return thirdRuleElimination;
    }
    
    private int[] getRowRangeForGroup(int row) {
        int min;
        int max;
        // Check if the is the first or the last (so it doesn't give incorrect ranges)
        if (row == 0) {
            min = 0;
            max = 2;
        } else if (row == 8) {
            min = 6;
            max= 8;
        } else {
            min = row - 1;
            max = row + 1;
        }
        return new int[] {min, max};
    }
    
    private int[] getColRangeForGroup(int col) {
        int min;
        int max;
        if (col == 0) {
            min = 0;
            max = 2;
        } else if (col == 8) {
            min = 6;
            max = 8;
        } else {
            min = col - 1;
            max = col + 1;
        }
        return new int[] {min, max};
    }
    
    private Set<Integer> getSurvivors(Set<Integer> firstRuleElimination,
                                      Set<Integer> secondRuleElimination,
                                      Set<Integer> thirdRuleElimination) {
        // Temp contains all the values the cell can't be
        Set<Integer> forbiddenValues = new HashSet<>();
        // Add different rule sets into the forbiddenValues set (values the cell cannot be)
        forbiddenValues.addAll(firstRuleElimination);
        forbiddenValues.addAll(secondRuleElimination);
        forbiddenValues.addAll(thirdRuleElimination);
        // Will contain all the values the cell could be
        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 1; i < 10; i++) {
            possibleValues.add(i);
        } 
        // Subtract the forbidden values from the possible values to get the survivors
        Set<Integer> survivors = new HashSet<>();
        possibleValues.removeAll(forbiddenValues);
         // Return the survivors
        return possibleValues;
    }
}
