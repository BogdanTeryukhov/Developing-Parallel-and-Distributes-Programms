package Lab1;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.*;


public class MatrixMultiplier extends Thread{

    @Getter
    @Setter
    static class CalcThread extends Thread{
        private int startRow, endRow;
        private double[][] A, B, result;

        public CalcThread(double[][] a, double[][] b, double[][] result, int startRow, int endRow) {
            this.A = a;
            this.B = b;
            this.result = result;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            //System.out.println("Считаю со строки " + startRow + " до строки " + endRow + " включительно");
            for (int row = startRow; row <= endRow ; row++) {
                for (int col = 0; col < result[row].length; col++) {
                    result[row][col] = calculate(row, col);
                }
            }
        }
        private double calculate(int row, int column){
            double count = 0;
            for (int k = 0; k < B.length; k++) {
                count += A[row][k] * B[k][column];
            }
            return count;
        }
    }


    public static double[][] matrixMaker() {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        int rows = scanner.nextInt();
        int columns = scanner.nextInt();

        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double elem = rand.nextDouble(10);
                matrix[i][j] = elem;
            }
        }
        return matrix;
    }


    public static double[][] simpleMatrixMultiplicationRows(double[][] A, double[][] B){
        long startTime = System.nanoTime();
        double[][] result = new double[A.length][B[0].length];

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                double count = 0;
                for (int k = 0; k < B.length; k++) {
                    count += A[i][k] * B[k][j];
                }
                result[i][j] = count;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Время выполнения обычного алгоритма по строкам: " + ((endTime - startTime)/1000000));
        return result;
    }

    public static double[][] simpleMatrixMultiplicationColumns(double[][] A, double[][] B){
        long startTime = System.nanoTime();
        double[][] result = new double[A.length][B[0].length];

        for (int j = 0; j < B[0].length; j++){
            for (int i = 0; i < A.length; i++) {
                double count = 0;
                for (int k = 0; k < B.length; k++) {
                    count += A[i][k] * B[k][j];
                }
                result[i][j] = count;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Время выполнения обычного алгоритма по столбцам: " + ((endTime - startTime)/1000000));
        return result;
    }


    public static double[][] parallelMatrixMultiplication(double[][] A, double[][] B, int threadsCount)
            throws InterruptedException {
        long startTime = System.nanoTime();
        double[][] result = new double[A.length][B[0].length];

        if (threadsCount > A.length){
            threadsCount = A.length;
        }

        int count = A.length / threadsCount;
        int additional = A.length % threadsCount; //если не делится на threadsCount, то добавим к первому потоку

        //создаем и запускаем потоки
        Thread[] threads = new Thread[threadsCount];

        int start = 0;
        for (int i = 0; i < threadsCount; i++) {
            int cnt = ((i == 0) ? count + additional : count);
            threads[i] = new CalcThread(A, B, result, start, start + cnt - 1);
            start += cnt;
            threads[i].start();
        }
        //ждем завершения
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        long endTime = System.nanoTime();
        System.out.println("Время выполнения параллелльного алгоритма: " + ((endTime - startTime)/1000000));
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        double[][] A = matrixMaker();
        double[][] B = matrixMaker();

        double[][] res3 = parallelMatrixMultiplication(A, B, 500);
        double[][] res1 = simpleMatrixMultiplicationColumns(A, B);
        double[][] res2 = simpleMatrixMultiplicationRows(A,B);


        if (Arrays.deepEquals(res1, res2) && Arrays.deepEquals(res1, res3)){
            System.out.println("TRUE");
        }

    }
}