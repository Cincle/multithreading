package multiplesOfThrees;

public class Calculator implements Runnable {
    private boolean isSynched = true;
    private Calculation calculation;
    public int threadsFinishedCalculating = 0;
    private long valueToCheck;

    public Calculator(Calculation calculation) {
        this.calculation = calculation;
        this.valueToCheck = calculation.min;
    }

    public Calculator(Calculation calculation, boolean isSynched) {
        this(calculation);
        this.isSynched = isSynched;
    }

    private void calculate() {
        if (isSynched) {
            synchronized (this) {
                //Not possible to use generic "i" variable; local variable doesn't get shared by threads; hence "valueToCheck" class variable.
                while (valueToCheck < calculation.max) {
                    if (valueToCheck % 3 == 0 || valueToCheck % 5 == 0) {
                        calculation.sum += valueToCheck;
                    }
                    valueToCheck++;
                }
            }
        } else {
            while (valueToCheck < calculation.max) {
                if (valueToCheck % 3 == 0 || valueToCheck % 5 == 0) {
                    calculation.sum += valueToCheck;
                }
                valueToCheck++;
            }
        }

        System.out.println("Sum for thread " + Thread.currentThread().toString() + ": " + calculation.sum);
        threadsFinishedCalculating += 1;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    @Override
    public void run() {
        calculate();
    }
}
