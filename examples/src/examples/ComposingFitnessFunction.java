package examples;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class ComposingFitnessFunction
      extends FitnessFunction {
        /** String containing the CVS revision. Read out via reflection!*/
        private final static String CVS_REVISION = "$Revision: 1.18 $";

        private final int m_sequenceLength;

        public static final int MAX_BOUND = 4000;

        public ComposingFitnessFunction(int a_sequenceLength) {
            m_sequenceLength = a_sequenceLength;
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
        int[] notes = getNotes(a_subject, m_sequenceLength);
        double fitness = 10000.0d;
        fitness = checkNote(notes, fitness);
        
        return Math.max(1.0d, fitness);
    }

    public double checkNote(int[] notes, double fitness){
        double correctNotes = 0.0d;
        for(int i = 0; i <= notes.length; i++){
            if(notes[i] == i){
                correctNotes = correctNotes + 1.0d;
            }
        }

        double tmp = fitness / (double)m_sequenceLength;
        fitness = tmp * correctNotes;

        return fitness;
    }

    public static int[] getNotes(IChromosome a_subject, int sequenceLength){
        int[] notes = new int[sequenceLength];
        for(int i = 0; i <= sequenceLength; i++){
            int note = getNote(a_subject, i);
            notes[i] = note;
        }
        return notes;
    }

    public static int getNote(IChromosome a_subject, int genePosition){
        int tmp = (int) a_subject.getGene(genePosition).getAllele();
        return tmp;
    }
}
