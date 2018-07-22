/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.evolutionDistributed;

import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.distr.grid.IWorkerReturnStrategy;
import org.jgap.distr.grid.JGAPRequest;
import org.jgap.distr.grid.JGAPResult;

import java.util.List;

/**
 * Return the top 10 results to the client.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyWorkerReturnStrategy
    implements IWorkerReturnStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Determines the top 10 chromosomes and returns them.
   *
   * @param a_req JGAPRequest
   * @param a_genotype Genotype
   * @return JGAPResult
   * @throws Exception in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResult assembleResult(JGAPRequest a_req, Genotype a_genotype)
      throws Exception {
    List top = a_genotype.getPopulation().determineFittestChromosomes(10);
    Population pop = new Population(a_req.getConfiguration());
    for (int i = 0; i < top.size(); i++) {
      pop.addChromosome( (IChromosome) top.get(i));
    }
    JGAPResult result = new JGAPResult(a_req.getSessionName(), a_req.getRID(),
                                       pop, 1);
    return result;
  }
}
