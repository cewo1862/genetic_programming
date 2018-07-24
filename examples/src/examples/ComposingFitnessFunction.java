package examples;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class ComposingFitnessFunction
      extends FitnessFunction {
        /** String containing the CVS revision. Read out via reflection!*/
        private final static String CVS_REVISION = "$Revision: 1.18 $";

        private final int m_targetAmount;

        public static final int MAX_BOUND = 4000;

        public ComposingFitnessFunction(int a_targetAmount) {
        if (a_targetAmount < 1 || a_targetAmount >= MAX_BOUND) {
            throw new IllegalArgumentException(
                    "Change amount must be between 1 and " + MAX_BOUND + " cents.");
        }
        m_targetAmount = a_targetAmount;
        }

        /**
         * Determine the fitness of the given Chromosome instance. The higher the
         * return value, the more fit the instance. This method should always
         * return the same fitness value for two equivalent Chromosome instances.
         *
         * @param a_subject the Chromosome instance to evaluate
         *
         * @return positive double reflecting the fitness rating of the given
         * Chromosome
         * @since 2.0 (until 1.1: return type int)
         * @author Neil Rotstan, Klaus Meffert, John Serri
         */
    public double evaluate(IChromosome a_subject) {
        // Take care of the fitness evaluator. It could either be weighting higher
        // fitness values higher (e.g.DefaultFitnessEvaluator). Or it could weight
        // lower fitness values higher, because the fitness value is seen as a
        // defect rate (e.g. DeltaFitnessEvaluator)
        boolean defaultComparation = a_subject.getConfiguration().
                getFitnessEvaluator().isFitter(2, 1);

        // The fitness value measures both how close the value is to the
        // target amount supplied by the user and the total number of coins
        // represented by the solution. We do this in two steps: first,
        // we consider only the represented amount of change vs. the target
        // amount of change and return higher fitness values for amounts
        // closer to the target, and lower fitness values for amounts further
        // away from the target. Then we go to step 2, which returns a higher
        // fitness value for solutions representing fewer total coins, and
        // lower fitness values for solutions representing more total coins.
        // ------------------------------------------------------------------
        int changeAmount = amountOfChange(a_subject);
        int totalCoins = getTotalNumberOfCoins(a_subject);
        int changeDifference = Math.abs(m_targetAmount - changeAmount);
        double fitness;
        if (defaultComparation) {
            fitness = 0.0d;
        }
        else {
            fitness = MAX_BOUND/2;
        }
        // Step 1: Determine distance of amount represented by solution from
        // the target amount. If the change difference is greater than zero we
        // will divide one by the difference in change between the
        // solution amount and the target amount. That will give the desired
        // effect of returning higher values for amounts closer to the target
        // amount and lower values for amounts further away from the target
        // amount.
        // In the case where the change difference is zero it means that we have
        // the correct amount and we assign a higher fitness value.
        // ---------------------------------------------------------------------
        if (defaultComparation) {
            fitness += changeDifferenceBonus(MAX_BOUND/2, changeDifference);
        }
        else {
            fitness -= changeDifferenceBonus(MAX_BOUND/2, changeDifference);
        }
        // Step 2: We divide the fitness value by a penalty based on the number of
        // coins. The higher the number of coins the higher the penalty and the
        // smaller the fitness value.
        // And inversely the smaller number of coins in the solution the higher
        // the resulting fitness value.
        // -----------------------------------------------------------------------
        if (defaultComparation) {
            fitness -= computeCoinNumberPenalty(MAX_BOUND/2, totalCoins);
        }
        else {
            fitness += computeCoinNumberPenalty(MAX_BOUND/2, totalCoins);
        }
        // Make sure fitness value is always positive.
        // -------------------------------------------
        return Math.max(1.0d, fitness);
    }

}
