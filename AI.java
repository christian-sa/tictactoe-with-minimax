package tictactoe;

import static tictactoe.Mark.*;
import java.util.Random;

/**
 * AI for Tic Tac Toe.
 * Multiple difficulty settings:
 * {@link #easyMove(Field, Mark, boolean isMedium)}.
 *
 * {@link #hardMove(TicTacToe, Mark, boolean isMedium)}.
 */
abstract class AI {
    static Random random = new Random();
    /**
     * Make a random move and print updated field.
     * @param field current field.
     * @param markAI the AIs mark.
     * @param isMedium mediumMove method can execute easyMove depending on circumstances.
     *                 This tells the method to print if medium or easy move was made.
     */
    private static void easyMove(Field field, Mark markAI, boolean isMedium) {
        // X always starts the game! If AI is O, skip turn.
        if (markAI == O && field.numOfMarks() == 0) { return; }
        System.out.println(isMedium ? "Making move level \"medium EASY\"" : "Making move level \"easy\"");
        boolean makingMove = true;
        while (makingMove) {
            int row = random.nextInt(3);
            int column = random.nextInt(3);
            if (field.available(row, column)) {
                field.setMark(row, column, markAI);
                makingMove = false;
            }
        }
        field.print();
    }

    /**
     * Make a medium difficulty move and print updated field.
     * Either Easy or Hard move depending on on circumstances:
     * Hard if 1 move away from losing or winning, else Easy (random move).
     * @param ticTacToe current instance of the game.
     * @param markAI the AIs mark.
     */
    private static void mediumMove(TicTacToe ticTacToe, Mark markAI) {
        Field field = ticTacToe.field;
        int bestScore = Integer.MIN_VALUE;
        int worstScore = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field.available(i, j)) {
                    field.setMark(i, j, markAI);
                    int score = miniMax(ticTacToe, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false, markAI);
                    field.setMark(i, j, BLANK);
                    bestScore = Math.max(bestScore, score);
                    worstScore = Math.min(worstScore, score);
                    if (bestScore == 10 || worstScore == -20) {
                        hardMove(ticTacToe, markAI, true);
                        return;
                    }
                }
            }
        }
        easyMove(ticTacToe.field, markAI, true);
    }


    /**
     * Make an optimal move and print updated field.
     * @param ticTacToe current instance of the game.
     * @param markAI the AIs mark.
     * @param isMedium mediumMove can execute hardMove depending on circumstances.
     *                 This tells the method to print if medium or hard move was made.
     */
    private static void hardMove(TicTacToe ticTacToe, Mark markAI, boolean isMedium) {
        Field field = ticTacToe.field;
        // X always starts the game! If AI is O, skip turn.
        if (markAI == O && field.numOfMarks() == 0) { return; }
        System.out.println(isMedium ? "Making move level \"medium HARD\"" : "Making move level \"hard\"");
        int bestScore = Integer.MIN_VALUE;
        int[] move = {-1, -1};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field.available(i, j)) {
                    field.setMark(i, j, markAI);
                    int score = miniMax(ticTacToe, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false, markAI);
                    field.setMark(i, j, BLANK);
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        field.setMark(move[0], move[1], markAI);
        field.print();
    }

    /**
     * This algorithm is used to determine the optimal move under the given conditions.
     * @param ticTacToe current instance of the game.
     * @param depth how deep the branch should be
     * @param alpha value used for alpha-beta pruning
     * @param beta value used for alpha-beta pruning
     * @param isMax true if want to maximize score.
     * @param markAI AIs mark.
     * @return score.
     */
    private static int miniMax(TicTacToe ticTacToe, int depth, int alpha, int beta, boolean isMax, Mark markAI) {
        // Previous Bug was here. Finally solved after a couple hours wasted lol.
        // I wasn't accounting for the score to change depending on the AIs mark.
        // Different mark of course has a different goal.

        // Terminal condition: Ends method execution if end-of-game state is reached in during recursion.
        // Logic: If the AI marks miniMax move (achieved by the recursion later) ultimately results in a win,
        // the moves score is valued as 10. If its a loss its -10 and the score is 0 in case of a draw
        // (-20 if lose next turn for clearer distinction from regular lose, used for medium difficulty).
        // We then also subtract the depth (if win) or subtract from the depth (if loose).

        // This will achieve two things:
        // It will have the effect that a faster win has a higher score and a slower (delayed) lose will have a higher score then losing fast.
        // For example if a lose can be delayed for 2 turns by blocking a row (considering opponent plays perfect and AI knows
        // loose is guaranteed because of that anyway), this score will be higher than the score for not blocking and
        // letting the opponent win faster.
        if (!ticTacToe.getGameState().equals("Game not finished")) {
            if (markAI == X) {
                return ticTacToe.getGameState().equals("X wins") ? 10 - depth :
                        ticTacToe.getGameState().equals("O wins") && depth == 1 ? -20 :
                         ticTacToe.getGameState().equals("O wins") ? depth - 10 : 0;

            } else {
                return ticTacToe.getGameState().equals("O wins") ? 10 - depth :
                        ticTacToe.getGameState().equals("X wins") && depth == 1 ? -20 :
                        ticTacToe.getGameState().equals("X wins") ? depth - 10 : 0;

            }
        }

        Field field = ticTacToe.field;
        Mark mark;
        depth += 1;

        // If maximizing..
        if (isMax) {
            // Set a "worst case" score (unattainably LOW)
            int maxScore = Integer.MIN_VALUE;
            // Switch marks depending on the AIs mark.
            // We have to use the AIs mark here.
            mark = markAI == X ? X : O;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field.available(i, j)) {
                        field.setMark(i, j, mark);
                        // Set new maxScore if miniMax results in a higher score
                        // We want to get the highest possible score -> maximizing.
                        int score = miniMax(ticTacToe, depth, alpha, beta, false, markAI);
                        maxScore = Math.max(score, maxScore);
                        alpha = Math.max(alpha, score);
                        field.setMark(i, j, BLANK);
                        if (alpha >= beta) {
                            return maxScore;
                        }
                    }
                }
            }
            return maxScore;
        // If minimizing..
        } else {
            // Set a "worst case" score (unattainably HIGH)
            int minScore = Integer.MAX_VALUE;
            // Switch marks depending the AIs mark.
            // In this case we need to use the opponents mark.
            mark = markAI == X ? O : X;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (field.available(i, j)) {
                        field.setMark(i, j, mark);
                        // Set new minScore if miniMax results in a lower score.
                        // We want to get the lowest possible score -> minimizing.
                        int score = miniMax(ticTacToe, depth, alpha, beta, true, markAI);
                        minScore = Math.min(score, minScore);
                        beta = Math.min(beta, score);
                        field.setMark(i, j, BLANK);
                        if (beta <= alpha) {
                            return minScore;
                        }
                    }
                }
            }
            return minScore;
        }
    }

     static void doCorrectMove(TicTacToe ticTacToe, Mark markAI, String difficulty) {
        switch (difficulty) {
            case "easy":
                easyMove(ticTacToe.field, markAI, false);
                break;
            case "medium":
                mediumMove(ticTacToe, markAI);
                break;
            case "hard":
                hardMove(ticTacToe, markAI, false);
                break;
            default:
                System.out.println("Unsupported AI difficulty.");
        }
    }
}
