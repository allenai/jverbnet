/* Insert License Here */

package edu.mit.jverbnet.data;

/**
 * The wordnet type is identical to the wordnet sense key, which is a unique
 * string that identifies a Wordnet word. The string representation is:
 * 
 * <pre>
 * lemma%ss_type:lex_filenum:lex_id
 * </pre>
 * 
 * In verbnet, ss_type is always 2 (meaning it is a verb)
 * 
 * @author Mark A. Finlayson
 * @version 1.2.0
 * @since JVerbnet 1.0.0
 */
public interface IWordnetKey extends Comparable<IWordnetKey> {

	/**
	 * The lemma (root form) of the word indicated by wordnet type. The returned
	 * lemma will not be <code>null</code>, empty, or all whitespace.
	 * 
	 * @return the lemma of the type
	 * @since JVerbnet 1.0.0
	 */
	public String getLemma();

	/**
	 * Returns the synset number for the type, which will usually be 2 for all verbnet
	 * types.
	 * 
	 * In general, the synset type is a one digit decimal integer representing
	 * the synset type for the sense.
	 * 
	 * <pre>
	 * 1=NOUN
	 * 2=VERB
	 * 3=ADJECTIVE
	 * 4=ADVERB
	 * 5=ADJECTIVE SATELLITE
	 * </pre>
	 * 
	 * @return the synset type, 2
	 * @since JVerbnet 1.0.0
	 */
	public int getSynsetType();

	/**
	 * Returns the lexical file number associated with this wordnet type.
	 * 
	 * @return the lexical file number associated with this wordnet type
	 * @since JVerbnet 1.0.0
	 */
	public int getLexicalFileNumber();

	/**
	 * Returns the lexical id for this sense key, which is a non-negative
	 * integer.
	 * 
	 * @return the non-negative lexical id for this sense key
	 * @since JVerbnet 1.0.0
	 */
	public int getLexicalID();

}
