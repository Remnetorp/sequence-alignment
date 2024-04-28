import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuilder;

public class SequenceAlignment {
    private int insertCost = -4;
    private Map<Character, Integer> letters;
    private Map<Integer, int[][]> matrixes;
    private Map<Integer, String[]> queries;
    private int[][] costMatrix, alignmentMatrix;

    /**
     * Method readFile() takes in-files in the format where The first line contains
     * a number of space-separated characters, these
     * characters will be used in the strings. Then follows k lines with k
     * space-separated integers where the jth integer on the ith row
     * is the cost of aligning character i and character j . Then follows one line
     * with an integer, the number of queries that will be solved
     * by the algorithm. Then follows Q lines, each describing one query. Each of
     * these lines contain two space-separated strings,
     * the strings that should be aligned with maximal gain.
     * 
     * @param output a boolean value determined if there will be extra information
     *               printed in terminal.
     */
    private void readFile(boolean output) {
        Scanner scanner = new Scanner(System.in);
        letters = new HashMap<>();
        queries = new HashMap<>();
        matrixes = new HashMap<>();

        String[] firstLine = scanner.nextLine().split(" ");
        int index = 0;
        for (String s : firstLine) {
            letters.put(s.charAt(0), index);
            index += 1;
        }
        costMatrix = new int[index][index];
        int dimensions = letters.size();

        for (int j = 0; j < dimensions; j++) {
            for (int k = 0; k < dimensions; k++) {
                int nextValue = scanner.nextInt();
                costMatrix[j][k] = nextValue;
            }
        }

        if (output) {
            System.out.print("  ");
            letters.forEach((key, value) -> System.out.print(key + " "));
            System.out.println("");
            for (int i = 0; i < costMatrix.length; i++) {
                for (int j = 0; j < costMatrix[i].length; j++) {
                    System.out.print(costMatrix[i][j] + " ");
                }
                System.out.println();
            }
        }

        int nQueries = scanner.nextInt();
        scanner.nextLine();

        // Creates an allignment matrix for each query containing gap costs and other
        // indexes empty.
        for (int q = 1; q <= nQueries; q++) {

            String[] query = scanner.nextLine().split(" ");
            int rows = query[1].length() + 1;
            int cols = query[0].length() + 1;
            alignmentMatrix = new int[rows][cols];

            for (int r = 0; r < rows; r++) {
                alignmentMatrix[r][0] = r * insertCost;
            }
            for (int c = 0; c < cols; c++) {
                alignmentMatrix[0][c] = c * insertCost;
            }

            matrixes.put(q, alignmentMatrix);
            queries.put(q, query);
        }

        scanner.close();
    }

    /**
     * Queries method sole reason is to go through the map containing the queries
     * and
     * send them into the method performing the Needleman-Wunsch algorithm. It
     * prints
     * the resulting Strings.
     *
     */
    public void queries() {
        for (int q : queries.keySet()) {
            String s1 = queries.get(q)[0];
            String s2 = queries.get(q)[1];
            int[][] emptyMatrix = matrixes.get(q);

            char[][] result = alignment(s1, s2, emptyMatrix);
            String[] strings = buildStrings(s1, s2, result);
            System.out.println(strings[0] + " " + strings[1]);
        }
    }

    /**
     * The alignment method performs the Needleman-Wunsch algorithm, iterating
     * through the matrix with s1 on the x-axis and s2 on the y-axis.
     * It starts in (1,1) and checks three different costs, the cost of alligning
     * the two letters in that index, the cost of inserting a gap in s1, and
     * the cost of inserting a gap in s2. After that is puts in a letter
     * representing a the direction in a traceback matrix depending on which
     * direction had the least cost.
     *
     * @param s1     The first string of the query.
     * @param s2     The second string of the query.
     * @param matrix The empty allignment matrix, only contains the gap costs.
     * 
     * @return a traceback matrix representing the least cost way.
     */
    public char[][] alignment(String s1, String s2, int[][] matrix) {
        char[][] traceBack = new char[s2.length()][s1.length()];

        for (int row = 1; row < s2.length() + 1; row++) {
            int indexChar1 = letters.get(s2.charAt(row - 1));

            for (int col = 1; col < s1.length() + 1; col++) {
                int indexChar2 = letters.get(s1.charAt(col - 1));

                int cost = costMatrix[indexChar1][indexChar2];
                int diagonal = matrix[row - 1][col - 1] + cost;
                int fromLeft = matrix[row][col - 1] + insertCost;
                int fromAbove = matrix[row - 1][col] + insertCost;

                int bestValue = Math.max(Math.max(diagonal, fromLeft), fromAbove);
                matrix[row][col] = bestValue;

                char direction;
                if (bestValue == diagonal) {
                    direction = 'D';
                } else if (bestValue == fromLeft) {
                    direction = 'L';
                } else {
                    direction = 'A';
                }
                traceBack[row - 1][col - 1] = direction;

            }
        }
        return traceBack;
    }

    /**
     * The buildStrings method starts at the element in the bottom right corner of
     * the traceback matrix.
     * There it takes the element which represent which direction to go, depending
     * where it came from it either
     * insert the current characters on that col/row, or insert a '*' in one of the
     * two strings.
     *
     * @param s1        The first string of the query.
     * @param s2        The second string of the query.
     * @param traceBack The traceback matrix containing elements representing the
     *                  direction.
     * 
     * @return a String array containing the two resulting strings.
     * 
     */
    public String[] buildStrings(String s1, String s2, char[][] traceBack) {
        int indexRow = s2.length() - 1;
        int indexCol = s1.length() - 1;
        StringBuilder resultString1 = new StringBuilder();
        StringBuilder resultString2 = new StringBuilder();
        while ((indexRow + indexCol) >= 0) {
            char direction = traceBack[indexRow][indexCol];
            switch (direction) {
                case 'D':
                    resultString1.append(s1.charAt(indexCol));
                    resultString2.append(s2.charAt(indexRow));
                    indexRow -= 1;
                    indexCol -= 1;
                    break;
                case 'L':
                    resultString1.append(s1.charAt(indexCol));
                    resultString2.append('*');
                    indexCol -= 1;
                    break;
                case 'A':
                    resultString1.append('*');
                    resultString2.append(s2.charAt(indexRow));
                    indexRow -= 1;
                    break;
            }
        }

        // checks if there is a gap at the begining of the strings.
        if (indexRow == 0) {
            resultString1.append('*');
            resultString2.append(s2.charAt(indexRow));
        } else if (indexCol == 0) {
            resultString1.append(s1.charAt(indexCol));
            resultString2.append('*');
        }

        String[] result = new String[2];
        result[0] = resultString1.reverse().toString();
        result[1] = resultString2.reverse().toString();
        return result;
    }

    public static void main(String[] args) {
        boolean output = false;
        for (String arg : args) {
            if (arg.equals("output")) {
                output = true;
            }
        }
        SequenceAlignment test = new SequenceAlignment();
        long time0 = System.currentTimeMillis();
        test.readFile(output);
        long time1 = System.currentTimeMillis();
        test.queries();
        long time2 = System.currentTimeMillis();

        long readTime = (time1 - time0);
        long algorithmTime = (time2 - time1);

        if(output){
            System.out.println("Time spent for reading input: " + readTime);
            System.out.println("Time spent for algorithm method: " + algorithmTime);
        }

    }

}