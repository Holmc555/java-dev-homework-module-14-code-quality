package edit.game;

import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final int BOARD_SIZE = 9;
    private static final char EMPTY_BOX = ' ';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        char[] box = new char[BOARD_SIZE];
        initializeBoard(box);

        LOGGER.log(Level.INFO, "Enter box number to select. Enjoy!\n");

        boolean gameInProgress = true;
        while (gameInProgress) {
            printBoard(box);

            byte userMove = getUserMove(scan, box);
            box[userMove - 1] = PLAYER_X; // Player's move

            GameStatus status = checkGameStatus(box);
            if (status != GameStatus.IN_PROGRESS) {
                gameInProgress = false;
                printGameResult(status);
            } else {
                byte computerMove = getComputerMove(box);
                box[computerMove - 1] = PLAYER_O; // Computer's move

                status = checkGameStatus(box);
                if (status != GameStatus.IN_PROGRESS) {
                    gameInProgress = false;
                    printGameResult(status);
                }
            }
        }
        scan.close();
    }

    private static void initializeBoard(char[] box) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            box[i] = EMPTY_BOX;
        }
    }

    private static void printBoard(char[] box) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append("|").append(box[3 * i + j]);
            }
            sb.append("|\n");
        }
        LOGGER.log(Level.INFO, sb.toString());
    }

    private static byte getUserMove(Scanner scan, char[] box) {
        byte input;
        while (true) {
            input = scan.nextByte();
            if (input >= 1 && input <= BOARD_SIZE && box[input - 1] == EMPTY_BOX) {
                return input;
            } else {
                LOGGER.log(Level.WARNING, "Invalid input. Please, enter another box number.");
            }
        }
    }

    private static byte getComputerMove(char[] box) {
        byte rand;
        while (true) {
            rand = (byte) (Math.random() * BOARD_SIZE);
            if (box[rand] == EMPTY_BOX) {
                return (byte) (rand + 1);
            }
        }
    }

    private static GameStatus checkGameStatus(char[] box) {
        if (hasWinningLine(box)) {
            return (getCurrentPlayer(box) == PLAYER_X) ? GameStatus.WON : GameStatus.LOST;
        }
        if (isBoardFull(box)) {
            return GameStatus.DRAW;
        }
        return GameStatus.IN_PROGRESS;
    }

    private static boolean isBoardFull(char[] box) {
        for (char cell : box) {
            if (cell == EMPTY_BOX) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasWinningLine(char[] box) {
        // Перевірка рядків на виграшну комбінацію
        for (int i = 0; i < 3; i++) {
            if (box[i * 3] != EMPTY_BOX && box[i * 3] == box[i * 3 + 1] && box[i * 3] == box[i * 3 + 2]) {
                return true;
            }
        }
        // Перевірка колонок
        for (int i = 0; i < 3; i++) {
            if (box[i] != EMPTY_BOX && box[i] == box[i + 3] && box[i] == box[i + 6]) {
                return true;
            }
        }
        // Перевірка діагоналей
        if (box[0] != EMPTY_BOX && box[0] == box[4] && box[0] == box[8]) {
            return true;
        }
        if (box[2] != EMPTY_BOX && box[2] == box[4] && box[2] == box[6]) {
            return true;
        }
        // Якщо жодної виграшної лінії не знайдено, повертаємо false
        return false;
    }

    private static char getCurrentPlayer(char[] box) {
        int countX = 0;
        int countO = 0;
        for (char cell : box) {
            if (cell == PLAYER_X) {
                countX++;
            } else if (cell == PLAYER_O) {
                countO++;
            }
        }
        return (countX > countO) ? PLAYER_O : PLAYER_X;
    }

    private static void printGameResult(GameStatus status) {
        String resultMessage;
        switch (status) {
            case WON:
                resultMessage = "You won the game!";
                break;
            case LOST:
                resultMessage = "You lost the game!";
                break;
            case DRAW:
                resultMessage = "It's a draw!";
                break;
            default:
                resultMessage = "Game in progress.";
                break;
        }
        LOGGER.log(Level.INFO, resultMessage);
    }

    public enum GameStatus {
        IN_PROGRESS, WON, LOST, DRAW
    }
}
