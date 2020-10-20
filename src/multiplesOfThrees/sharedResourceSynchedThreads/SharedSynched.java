package multiplesOfThrees.sharedResourceSynchedThreads;

/*
This class examines what happens if one runnable class is shared by multiple threads with synchronization.
This has no added value over running multithreaded; the i-value in the while loop of the calculator runnable gets locked by the first
started thread.
 */

import multiplesOfThrees.Calculation;
import multiplesOfThrees.Calculator;

public class SharedSynched {
    private Calculation calculation;
    private int numberOfThreads;

    public SharedSynched(int numberOfThreads, long min, long max) {
        calculation = new Calculation(min, max);
        this.numberOfThreads = numberOfThreads;
    }

    public void startCalculation() {
        System.out.println("Starting calculation of multiples between " + calculation.min + " and " + calculation.max + " with thread synchronization.");
        Long calculationTime;
        Long startTime = System.currentTimeMillis();
        Calculator calculator = new Calculator(calculation);
        Thread[] calculatorThreadArray = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            calculatorThreadArray[i] = new Thread(calculator);
            System.out.println("starting thread " + i);

            calculatorThreadArray[i] = new Thread(calculator);
            calculatorThreadArray[i].start();
            }

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                calculatorThreadArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        calculationTime = System.currentTimeMillis() - startTime;
        System.out.println("Total sum: " + calculation.sum);
        System.out.println("Calculation time: " + calculationTime + "\n");
    }
}
