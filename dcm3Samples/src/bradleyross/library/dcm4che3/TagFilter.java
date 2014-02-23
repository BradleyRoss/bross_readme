package bradleyross.library.dcm4che3;
/**
 * Filters tags by tag value.
 * @author Bradley Ross
 *
 */
public interface TagFilter {
	/**
	 * Determines whether tag should be processed.
	 * @param tag for element being considered
	 * @return true if tag should be processed, false
	 *         if tag should be skipped
	 */
	public boolean accept(int tag);
}
