/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

import org.jgap.InvalidConfigurationException;
import org.jgap.gp.CommandGene;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.impl.ProgramChromosome;

/**
 * Identifies that the ant may drop sand at the current position.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class MayDropSand
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Constructor for the may drop sand function
   * @param a_conf
   * @throws InvalidConfigurationException
   */
  public MayDropSand(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.BooleanClass);
  }

  /**
   * Executes the may drop sand function for a boolean argument
   */
  public boolean execute_boolean(ProgramChromosome a_chrom, int a_n,
                                 Object[] a_args) {
    AntMap map = getMap(a_chrom);
    return map.mayDropSand(map.getAnt().getXpos(), map.getAnt().getYpos());
  }

  /**
   * Returns the program listing name
   */
  public String toString() {
    return "SandBelongsHere";
  }
}
