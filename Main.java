import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {

        String filename0 = "insertLogs.txt";
        BufferedWriter writerInsert = new BufferedWriter(new FileWriter(filename0));
        String filename1 = "removeLogs.txt";
        BufferedWriter writerRemove = new BufferedWriter(new FileWriter(filename1));
        String filename2 = "searchLogs.txt";
        BufferedWriter writerSearch = new BufferedWriter(new FileWriter(filename2));

        final int INSERT = 10000;
        final int SEARCH = 100;
        final int REMOVE = 1000;

        int[] timeToInsert = new int[INSERT];
        int[] timeToSearch = new int[SEARCH];
        int[] timeToRemove = new int[REMOVE];

        int[] countOfIterationToInsert = new int[INSERT];
        int[] countOfIterationToSearch = new int[SEARCH];
        int[] countOfIterationToRemove = new int[REMOVE];

        int[] numbers = new int[INSERT];

        Random random = new Random();
        TwoThreeTree tree = new TwoThreeTree();

        for (int i = 0; i < INSERT; i++) {
            numbers[i] = random.nextInt();
            long startTime = System.nanoTime();
            tree.insert(numbers[i]);
            long endTime = System.nanoTime();
            timeToInsert[i] = (int) ((endTime - startTime));
            countOfIterationToInsert[i] = tree.getCountOfIterations();
            writerInsert.write(timeToInsert[i] + " " + countOfIterationToInsert[i]);
            writerInsert.newLine();
        }

        for (int i = 0; i < SEARCH; i++) {
            int randomMarkInArray = random.nextInt(0, numbers.length);
            long startTime = System.nanoTime();
            tree.search(numbers[randomMarkInArray]);
            long endTime = System.nanoTime();
            timeToSearch[i] = (int) ((endTime - startTime));
            countOfIterationToSearch[i] = tree.getCountOfIterations();
            writerSearch.write(timeToSearch[i] + " " + countOfIterationToSearch[i]);
            writerSearch.newLine();
        }

        for (int i = 0; i < REMOVE; i++) {
            int randomMarkInArray = random.nextInt(0, numbers.length);
            long startTime = System.nanoTime();
            tree.remove(numbers[randomMarkInArray]);
            long endTime = System.nanoTime();
            timeToRemove[i] = (int) ((endTime - startTime));
            countOfIterationToRemove[i] = tree.getCountOfIterations();
            writerRemove.write(timeToRemove[i] + " " + countOfIterationToRemove[i]);
            writerRemove.newLine();
        }

        System.out.println("Run time information (in nanoseconds):");
        writerInsert.write(printInfo("Time of Insert", timeToInsert));
        writerSearch.write(printInfo("Time of Search", timeToSearch));
        writerRemove.write(printInfo("Time of Remove", timeToRemove));

        System.out.println("\n" + "Information about the number of iterations in the work:");
        writerInsert.write(printInfo("Iteration of Insert", countOfIterationToInsert));
        writerSearch.write(printInfo("Iteration of Search", countOfIterationToSearch));
        writerRemove.write(printInfo("Iteration of Remove", countOfIterationToRemove));

        writerInsert.close();
        writerSearch.close();
        writerRemove.close();

    }

    public static String printInfo(String infoText, int[] array) {
        System.out.println("| " + infoText + ": max -> " +
                Arrays.stream(array).max().getAsInt() + "; min -> " +
                Arrays.stream(array).min().getAsInt() + "; average -> " +
                Arrays.stream(array).average().getAsDouble());
        return ("| " + infoText + ": max -> " +
                Arrays.stream(array).max().getAsInt() + "; min -> " +
                Arrays.stream(array).min().getAsInt() + "; average -> " +
                Arrays.stream(array).average().getAsDouble());
    }
}