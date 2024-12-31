package Pieces;

import Main.Gameboard;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


    public class Queen extends ChessPieces {
        public Queen(int color, int col, int row) {
            super(color, col, row);
            type = Type.Queen;
            if (color == Gameboard.WHITE) {
                try {
                    image = ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_qlt60.png"));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else {
                try {
                    image = ImageIO.read(new File("C:\\Users\\mathe\\OneDrive\\Desktop\\Chess_qdt60.png"));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

        public boolean canMove(int targetCol, int targetRow) {
            if (isWithinBoard(targetCol, targetRow) && !isSameSquare(targetCol, targetRow)) {
                if (targetCol == preCol || targetRow == preRow) {
                    if (isValidSquare(targetCol, targetRow) && !pieceIsOnDiagonalLine(targetCol, targetRow)) {
                        return true;
                    }
                }

                if (Math.abs(targetCol - preCol) == Math.abs(targetRow - preRow)) {
                    return isValidSquare(targetCol, targetRow) && !pieceIsOnDiagonalLine(targetCol, targetRow);
                }


                return false;
            }
            return false;
        }
    }



