package examples;

import examples.audiotechnik.JSON_Reader;
import examples.audiotechnik.MapOffset;
import org.jgap.*;
import org.jgap.audit.EvolutionMonitor;
import org.jgap.data.DataTreeBuilder;
import org.jgap.data.IDataCreators;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MapGene;
import org.jgap.xml.XMLDocumentBuilder;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import static examples.audiotechnik.JSON_Writer.writeJSON;

public class ComposingMusic {

    private final static String CVS_REVISION = "$Revision: 1.27 $";
    private static final int MAX_ALLOWED_EVOLUTIONS = 100;
    public static EvolutionMonitor m_monitor;

    public static int[] composeMusic(int[] sequenceValues, int a_sequenceLength)
            throws Exception{
        // Start with a DefaultConfiguration, which comes setup with the
        // most common settings.
        // -------------------------------------------------------------
        Configuration conf = new DefaultConfiguration();
        // Care that the fittest individual of the current population is
        // always taken to the next generation.
        // Consider: With that, the pop. size may exceed its original
        // size by one sometimes!
        // -------------------------------------------------------------
        conf.setPreservFittestIndividual(true);
        conf.setKeepPopulationSizeConstant(false);
        // Set the fitness function we want to use, which is our
        // MinimizingMakeChangeFitnessFunction. We construct it with
        // the target amount of change passed in to this method.
        // ---------------------------------------------------------
    FitnessFunction myFunc =
            new ComposingFitnessFunction(a_sequenceLength);
    conf.setFitnessFunction(myFunc);
        // Now we need to tell the Configuration object how we want our
        // Chromosomes to be setup. We do that by actually creating a
        // sample Chromosome and then setting it on the Configuration
        // object. As mentioned earlier, we want our Chromosomes to each
        // have four genes, one for each of the coin types. We want the
        // values (alleles) of those genes to be integers, which represent
        // how many coins of that type we have. We therefore use the
        // IntegerGene class to represent each of the genes. That class
        // also lets us specify a lower and upper bound, which we set
        // to sensible values for each coin type.
        // --------------------------------------------------------------
        Gene[] sampleGenes = new Gene[a_sequenceLength];

            for(int i = 0; i<= a_sequenceLength-1; i++){
                sampleGenes[i] = new IntegerGene(conf, 1, a_sequenceLength); //gene in bound of possible notes
        }

        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);
        // Finally, we need to tell the Configuration object how many
        // Chromosomes we want in our population. The more Chromosomes,
        // the larger number of potential solutions (which is good for
        // finding the answer), but the longer it will take to evolve
        // the population (which could be seen as bad).
        // ------------------------------------------------------------
        conf.setPopulationSize(20);

        // Create random initial population of Chromosomes.
        // ------------------------------------------------
        Genotype population = Genotype.randomInitialGenotype(conf);
        // Now we initialize the population randomly, anyway (as an example only)!
        // If you want to load previous results from file, remove the next line!
        // -----------------------------------------------------------------------
        population = Genotype.randomInitialGenotype(conf);
        // Evolve the population. Since we don't know what the best answer
        // is going to be, we just evolve the max number of times.
        // ---------------------------------------------------------------
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
            if (!uniqueChromosomes(population.getPopulation())) {
                throw new RuntimeException("Invalid state in generation "+i);
            }
            population.evolve();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total evolution time: " + ( endTime - startTime)
                + " ms");
        // Display the best solution we found.
        // -----------------------------------

        int[] resultValues = new int[a_sequenceLength];
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        for(int i = 0; i < a_sequenceLength; i++){
            System.out.println("The best solution for position: " + i + " is the offset value: " +  ComposingFitnessFunction.getNote(bestSolutionSoFar,i));
            resultValues[i] = ComposingFitnessFunction.getNote(bestSolutionSoFar,i);
        }

        return resultValues;
    }

    public static void main(String[] args)
            throws Exception {

        //read filename from command line and create the filepath
        String filepath = "./resources/" + args[0];
        //read JSON-File found in given path and create an array of offset values
        double[] offset_array = JSON_Reader.readJSON(filepath);

        //map the offset values in the array to integer values
        MapOffset offset = new MapOffset();
        Map offsetmap = offset.getOffset(offset_array);

        int sequenceLength = offsetmap.size();

        int[] sequenceValues = new int[sequenceLength];
        for(int i = 0; i<= sequenceLength-1;i++){
            sequenceValues[i] = i;
        }


        int[] resultValues = composeMusic(sequenceValues,sequenceLength);

        //TODO Ergebnissequenz auf Offsetwerte mappen
        writeJSON(filepath,resultValues);
        //TODO mit Offsetwerten Noten in JSON-Format ausgeben und in File speichern

    }

    public static boolean uniqueChromosomes(Population a_pop) {
        // Check that all chromosomes are unique
        for(int i=0;i<a_pop.size()-1;i++) {
            IChromosome c = a_pop.getChromosome(i);
            for(int j=i+1;j<a_pop.size();j++) {
                IChromosome c2 =a_pop.getChromosome(j);
                if (c == c2) {
                    return false;
                }
            }
        }
        return true;
    }
}
