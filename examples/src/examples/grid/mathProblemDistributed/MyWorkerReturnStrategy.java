/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.apache.log4j.Logger;
import org.jgap.distr.grid.gp.IWorkerReturnStrategyGP;
import org.jgap.distr.grid.gp.JGAPRequestGP;
import org.jgap.distr.grid.gp.JGAPResultGP;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.impl.GPGenotype;
import org.jgap.gp.impl.GPPopulation;

/**
 * Return the top 10 results to the client.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyWorkerReturnStrategy
    implements IWorkerReturnStrategyGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private static Logger log = Logger.getLogger(MyWorkerReturnStrategy.class);

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
  public JGAPResultGP assembleResult(JGAPRequestGP a_req, GPGenotype a_genotype)
      throws Exception {
    IGPProgram best;
    GPPopulation pop = a_genotype.getGPPopulation();
    if (pop == null) {
      log.fatal("Population was null!");
      best = null;
    }
    else {
      log.debug("Assembling result from population with size " + pop.size());
      best = pop.determineFittestProgram();
      if (best == null) {
        log.error("Could not determine the best program!");
      }
    }
    JGAPResultGP result = new JGAPResultGP(a_req.getSessionName(), a_req.getID(),
        a_req.getChunk(), best, 1);
    return result;
  }
}
