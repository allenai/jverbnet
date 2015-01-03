/*%{compilationUnitHeader}*/

package edu.mit.jverbnet.util;

/** 
 * An object with an ID.
 *
 * @author Mark A. Finlayson
 * @version 1.0.0
 * @since JVerbnet 1.0.0
 */
public interface IHasID {
	
	/**
	 * Returns the id of the type, as defined in the xsd file. Will never be
	 * <code>null</code> or empty.
	 * 
	 * @return the id of the type
	 * @since JVerbnet 1.0.0
	 */
	public String getID();


}
