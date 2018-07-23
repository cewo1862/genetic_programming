package examples;

import examples.audiotechnik.MapOffset;
import org.jgap.*;
import org.jgap.audit.EvolutionMonitor;
import org.jgap.data.DataTreeBuilder;
import org.jgap.data.IDataCreators;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.xml.XMLDocumentBuilder;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class ComposingMusic {

    private final static String CVS_REVISION = "$Revision: 1.27 $";
    private static final int MAX_ALLOWED_EVOLUTIONS = 50;
    public static EvolutionMonitor m_monitor;

    public static void makeChangeForAmount(int a_targetChangeAmount,
                                           boolean a_doMonitor)
            throws Exception {
        Configuration conf = new DefaultConfiguration();
        conf.setPreservFittestIndividual(true);
        conf.setKeepPopulationSizeConstant(false);
        // Set the fitness function we want to use, which is our
        // MinimizingMakeChangeFitnessFunction. We construct it with
        // the target amount of change passed in to this method.
        // ---------------------------------------------------------
        FitnessFunction myFunc =
                new MinimizingMakeChangeFitnessFunction(a_targetChangeAmount);
        conf.setFitnessFunction(myFunc);
        if (a_doMonitor) {
            m_monitor = new EvolutionMonitor();
            conf.setMonitor(m_monitor);
        }
        Gene[] sampleGenes = new Gene[1];
        //upperBound auf größe der map, map.size()
        double[] tmp = new double[5];
        MapOffset offset = new MapOffset();
        Map offsetmap = offset.getOffset(tmp);

        sampleGenes[0] = new IntegerGene(conf, 0, offsetmap.size()); // Key Value out of Map
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(20);

        // Create random initial population of Chromosomes.
        // Here we try to read in a previous run via XMLManager.readFile(..)
        // for demonstration purpose only!
        // -----------------------------------------------------------------
        Genotype population;
        try {
            Document doc = XMLManager.readFile(new File("JGAPExample32.xml"));
            population = XMLManager.getGenotypeFromDocument(conf, doc);
        }
        catch (UnsupportedRepresentationException uex) {
            // JGAP codebase might have changed between two consecutive runs.
            // --------------------------------------------------------------
            population = Genotype.randomInitialGenotype(conf);
        }
        catch (FileNotFoundException fex) {
            population = Genotype.randomInitialGenotype(conf);
        }
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
            if(m_monitor != null) {
                population.evolve(m_monitor);
            }
            else {
                population.evolve();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total evolution time: " + ( endTime - startTime)
                + " ms");
        // Save progress to file. A new run of this example will then be able to
        // resume where it stopped before! --> this is completely optional.
        // ---------------------------------------------------------------------

        // Represent Genotype as tree with elements Chromomes and Genes.
        // -------------------------------------------------------------
        DataTreeBuilder builder = DataTreeBuilder.getInstance();
        IDataCreators doc2 = builder.representGenotypeAsDocument(population);
        // create XML document from generated tree
        XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
        Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
        XMLManager.writeFile(xmlDoc, new File("JGAPExample26.xml"));
        // Display the best solution we found.
        // -----------------------------------
        IChromosome bestSolutionSoFar = population.getFittestChromosome();
        double v1 = bestSolutionSoFar.getFitnessValue();
        System.out.println("The best solution has a fitness value of " +
                bestSolutionSoFar.getFitnessValue());
        bestSolutionSoFar.setFitnessValueDirectly(-1);
        System.out.println("It contains the following: ");
        System.out.println("\t" +
                MinimizingMakeChangeFitnessFunction.
                        getNumberOfCoinsAtGene(
                                bestSolutionSoFar, 0) + " quarters.");
    }

    public static void main(String[] args)
            throws Exception {
        if (args.length < 1) {
            System.out.println("Syntax: MinimizingMakeChange <amount>");
        }
        else {
            int amount = 0;
            try {
                amount = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.out.println(
                        "The <amount> argument must be a valid integer value");
                System.exit(1);
            }
            if (amount < 1 ||
                    amount >= MinimizingMakeChangeFitnessFunction.MAX_BOUND) {
                System.out.println("The <amount> argument must be between 1 and "
                        +
                        (MinimizingMakeChangeFitnessFunction.MAX_BOUND - 1)
                        + ".");
            }
            else {
                boolean doMonitor = false;
                if (args.length > 1) {
                    String monitoring = args[1];
                    if(monitoring != null && monitoring.equals("MONITOR")) {
                        doMonitor = true;
                    }
                }
                makeChangeForAmount(amount, doMonitor);
            }
        }
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
