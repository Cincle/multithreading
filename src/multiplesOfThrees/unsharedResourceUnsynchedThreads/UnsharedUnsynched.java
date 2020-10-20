package multiplesOfThrees.unsharedResourceUnsynchedThreads;

/*
This class examines what happens if the runnable class and its resources are split up over threads.
Seems to be fast and safe, and doesn't need to be synchronized.
 */

import multiplesOfThrees.Calculation;
import multiplesOfThrees.Calculator;

public class UnsharedUnsynched {
    private Calculation calculation;
    private int numberOfThreads;

    public UnsharedUnsynched(int numberOfThreads, long min, long max) {
        calculation = new Calculation(min, max);
        this.numberOfThreads = numberOfThreads;
    }

    // Divides the max by the number of threads and inputs into thread.
    public void startCalculation() {
        System.out.println("Starting calculation of multiples between " + calculation.min + " and " + calculation.max + " with separated resource classes.");
        Long startTime = System.currentTimeMillis();
        Long calculationTime;
        Calculator[] calculatorArray = new Calculator[numberOfThreads];
        Thread[] calculatorThreadArray = new Thread[numberOfThreads];


        // Get remainder to make sure the entire max long is used.
        int remainder = (int) (calculation.max - calculation.min) % numberOfThreads;
        long rangeToCalculate = (calculation.max - calculation.min) / numberOfThreads;
        long minRange = calculation.min;
        long maxRange = rangeToCalculate + remainder; // Add remainder to first range to be calculated to ensure entire complete range is used

        for (int i = 0; i < numberOfThreads; i++) {
            calculatorArray[i] = new Calculator(new Calculation(minRange, maxRange));
            calculatorThreadArray[i] = new Thread(calculatorArray[i]);
            calculatorThreadArray[i].start();

            minRange = maxRange;
            maxRange += rangeToCalculate;


        }

        for(int i = 0; i < numberOfThreads; i++) {
            try {
                calculatorThreadArray[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            calculation.sum += calculatorArray[i].getCalculation().sum;
            System.out.println(calculation.sum);
        }

        calculationTime = System.currentTimeMillis() - startTime;
        System.out.println("Total sum: " + calculation.sum);
        System.out.println("Calculation time: " + calculationTime + "\n");
    }
}
