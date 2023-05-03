package boardgame.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.Optional;

/**
 * Class representing a TicTacToe state.
 */
@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class TicTacToeState {
    public static final int SIZE = 3;


    State[][] board;

    /**
     * The player who is coming next
     */
    State nextPlayer;

    public TicTacToeState() {
        this.board = new State[SIZE][SIZE];
        this.nextPlayer = State.BLUE;

        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(this.board[i], State.EMPTY);
        }
    }

    /**
     * Checks if the given move can be performed
     * @param row given row
     * @param col given column
     * @return if the move is permitted
     */
    public boolean canMove(final int row, final int col) {
        try {
            return board[row][col] == State.EMPTY;
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            Logger.error("The given index is not on the board: {}, {}", row, col);
            return false;
        }
    }

    public TicTacToeState doMove(final int row, final int col) {
        final var newBoard = new State[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            newBoard[i] = Arrays.copyOf(board[i], SIZE);
        }
        newBoard[row][col] = nextPlayer;
        Logger.info("Player {} is moving to: {}, {}", nextPlayer, row, col);

        return this.toBuilder()
                .board(newBoard)
                .nextPlayer(switch (nextPlayer) {
                    case RED -> State.BLUE;
                    case BLUE -> State.RED;
                    default -> throw new IllegalStateException();
                })
                .build();
    }

    public Optional<State> getWinner() {
        var hasEmpty = false;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == State.EMPTY) {
                    hasEmpty = true;
                    continue;
                }

                // horizontal
                try {
                    if (board[row][col] == board[row][col + 1] && board[row][col] == board[row][col + 2]) {
                        return Optional.of(board[row][col]);
                    }
                } catch (final ArrayIndexOutOfBoundsException ignored) {
                }

                // vertical
                try {
                    if (board[row][col] == board[row + 1][col] && board[row][col] == board[row + 2][col]) {
                        return Optional.of(board[row][col]);
                    }
                } catch (final ArrayIndexOutOfBoundsException ignored) {
                }

                // left diagonal
                try {
                    if (board[row][col] == board[row + 1][col + 1] && board[row][col] == board[row + 2][col + 2]) {
                        return Optional.of(board[row][col]);
                    }
                } catch (final ArrayIndexOutOfBoundsException ignored) {
                }

                // right diagonal
                try {
                    if (board[row][col] == board[row - 1][col + 1] && board[row][col] == board[row - 2][col + 2]) {
                        return Optional.of(board[row][col]);
                    }
                } catch (final ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        return hasEmpty ? Optional.empty() : Optional.of(State.EMPTY);
    }

    public enum State {
        RED, BLUE, EMPTY
    }
}
