package multiplesOfThrees.sharedResourceUnsynchedThreads;

/*
This class examines what happens if one runnable class is shared by multiple threads without synchronization.
 */

import multiplesOfThrees.Calculation;
import multiplesOfThrees.Calculator;

public class SharedUnsynched {
    private Calculation calculation;
    private int numberOfThreads;

    public SharedUnsynched(int numberOfThreads, long min, long max) {
        calculation = new Calculation(min, max);
        this.numberOfThreads = numberOfThreads;
    }

    // Divides the max by the number of threads and inputs into thread.
    public void startCalculation() {
        System.out.println("Starting calculation of multiples between " + calculation.min + " and " + calculation.max + " without thread synchronization.");
        Long calculationTime;
        Long startTime = System.currentTimeMillis();
        Calculator calculator = new Calculator(calculation, false);
        Thread[] calculatorThreadArray = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            calculatorThreadArray[i] = new Thread(calculator);
            calculatorThreadArray[i].start();
        }

        /*
        Sleep while calculator runnable is used by a thread to ensure that final sum is printed after calculations are finished.
        Adds some overhead to total calculation time.
         */
        while (calculator.threadsFinishedCalculating < numberOfThreads) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        calculationTime = System.currentTimeMillis() - startTime;
        System.out.println("Total sum: " + calculation.sum);
        System.out.println("Calculation time: " + calculationTime + "\n");
    }
}
