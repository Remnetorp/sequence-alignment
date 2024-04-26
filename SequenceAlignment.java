import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;


public class SequenceAlignment{
    private Map<Integer, String> letters;
    private int[][] matrix;

    private void readFile(boolean output){
        Scanner scanner = new Scanner(System.in);
        letters = new HashMap<>();
        String[] firstLine = scanner.nextLine().split(" ");
        int index = 0;
        for(String s : firstLine){
            letters.put(index, s);
            index += 1;
        }
        matrix = new int[index][index];

        int dimensions = letters.size();
        for (int j = 0; j < dimensions; j++){
            for(int k = 0; k < dimensions; k++){
                int nextValue = scanner.nextInt();
                matrix[j][k] = nextValue;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        scanner.close();
    }

    public static void main(String[] args){
        SequenceAlignment test = new SequenceAlignment();
        test.readFile(true);

    }


}