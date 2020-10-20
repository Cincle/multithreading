package multiplesOfThrees;

import multiplesOfThrees.sharedResourceSynchedThreads.SharedSynched;
import multiplesOfThrees.sharedResourceUnsynchedThreads.SharedUnsynched;
import multiplesOfThrees.unsharedResourceUnsynchedThreads.UnsharedUnsynched;

public class MultiplesOfThrees {
    private int iterations;
    private long min ;
    private long max;
    private long totalRunTime;
    private long controlSum;

    public MultiplesOfThrees(long min, long max, int iterations) {
        this.min = min;
        this.max = max;
        this.iterations = iterations;

        SharedSynched sharedSynched = new SharedSynched(2, min, max);
        sharedSynched.startCalculation();

        SharedUnsynched sharedUnsynched = new SharedUnsynched(2, min, max);
        sharedUnsynched.startCalculation();

        UnsharedUnsynched unsharedUnsynched = new UnsharedUnsynched(2, min, max);
        unsharedUnsynched.startCalculation();
    }
}
