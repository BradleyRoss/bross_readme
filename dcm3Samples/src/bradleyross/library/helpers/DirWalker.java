package bradleyross.library.helpers;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Generates an Iterator that will contain all of the
 * files belonging to a directory file structure.
 * @author Bradley Ross
 *
 */
public class DirWalker implements Iterator<File> {
	/**
	 * Controls amount of diagnostic listing.
	 * 
	 * <p>The default value is zero.  Higher values result in more information.</p>
	 */
	public int debugLevel = 0;
	/**
	 * Getter for {@link #debugLevel} property.
	 * @return value of property
	 */
	public int getDebugLevel() {
		return debugLevel;
	}
	/**
	 * Setter for {@link #debugLevel} property.
	 * @param value value for property
	 */
	public void setDebugLevel(int value) {
		debugLevel = value;
	}
	/**
	 * Root directory  of directory structure.
	 */
	protected File root = null;
	/**
	 * FileFilter to be applied against files before
	 * returning through iterator.
	 */
	protected FileFilter filter = new DummyFilter();
	/**
	 * Set to true when no more items to be returned.
	 */
	protected boolean done = false;
	/**
	 * Maximum number of files to be returned.
	 */
	protected int maxCount = 2000;
	/**
	 * Getter for {@link #maxCount} property.
	 * @return value of property
	 */
	public int getMaxCount() {
		return maxCount;
	}
	/**
	 * Setter for {@link #maxCount} property.
	 * @param value value for property
	 */
	public void setMaxCount(int value) {
		maxCount = value;
	}
	/**
	 * Dummy file filter that accepts all files and directories.
	 * @author Bradley Ross
	 */
	protected class DummyFilter implements FileFilter{
		public boolean accept(File value) { return true; }
	}
	/**
	 * FileFilter that only accepts files.
	 * @author Bradley Ross
	 *
	 */
	protected class FilesOnly implements FileFilter {
		public boolean accept(File value) {
			if (value.isFile() && filter.accept(value)) {
				return true;
			} else {
				return false;
			}
		}
	}
	/**
	 * FileFilter that only accepts directories.
	 * @author Bradley Ross
	 *
	 */
	protected class DirectoriesOnly implements FileFilter {
		public boolean accept(File value) {
			if (value.isDirectory()) {
				return true; 
			} else {
				return false;
			}
		}
	}
	/**
	 * Instance of files only file filter.
	 */
	protected FileFilter filesOnly = new FilesOnly();
	/**
	 * Instance of directories only file filter.
	 */
	protected FileFilter directoriesOnly = new DirectoriesOnly();
	/**
	 * Instance of iterator for going through directories.
	 */
	protected DirIterator directories = null;
	/**
	 * Instance of iterator for going through files in directory.
	 */
	protected LocalIterator files = null;
	/**
	 * Indicates whether a directory contains files.
	 * @param value directory
	 * @return true if it contains files
	 */
	protected boolean hasFiles(File value) {
		File[] test = value.listFiles(filesOnly);
		if (test == null) {
			return false;
		} else if (test.length == 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Indicates if a directory contains subdirectories.
	 * @param value directory
	 * @return true if it contains subdirectories
	 */
	protected boolean hasDirectories(File value) {
		File[] test = value.listFiles(directoriesOnly);
		if (test == null) {
			return false;
		} else if (test.length == 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Constructor that results in list of all files that are
	 * 	children of root directory.
	 * @param rootValue root directory
	 * @throws IOException
	 */
	public  DirWalker (File rootValue) throws IOException {
		if (!rootValue.isDirectory()) {
			throw new IOException("Root " + rootValue.getCanonicalPath() + 
					" is not a directory");
		}
		root = rootValue;
		filter = new DummyFilter();
		if (!hasFiles(root) && !hasDirectories(root)) {
			done = true;
			return;
		}
		directories = new DirIterator(rootValue);
		if (!directories.hasNext()) {
			done = true;
			return;
		}
		files = new LocalIterator(directories.next());
	}
	/**
	 * Constructor that results in a list that contains all files that
	 * 	  are descendants of the root directory and satisfy the conditions
	 *    contained in the file filter.
	 * @param rootValue root directory
	 * @param filterValue file filter
	 * @throws IOException
	 */
	public  DirWalker (File rootValue, FileFilter filterValue) throws IOException {
		if (!rootValue.isDirectory()) {
			throw new IOException("Root " + rootValue.getCanonicalPath() + 
					" is not a directory");
		}
		root = rootValue;
		filter = filterValue;
		if (!hasFiles(root) && !hasDirectories(root)) {
			done = true;
			return;
		}
		directories = new DirIterator(rootValue);
		if (!directories.hasNext()) {
			done = true;
			return;
		}
		files = new LocalIterator(directories.next());
	}
	/**
	 * Iterator returning the files in a directory.
	 * @author Bradley Ross
	 *
	 */
	protected class LocalIterator implements Iterator<File>  {
		protected File dir = null;
		protected File[] files = null;
		protected int count;
		protected int position;
		public LocalIterator(File dirValue) {
			dir = dirValue;
			files = dir.listFiles(filesOnly);
			count = files.length;
			position = -1;
		}
		public File next() {
			position = position + 1;
			return files[position];
		}
		public boolean hasNext() {
			if (position < count - 1) {
				return true;
			} else {
				return false;
			}
		}
		public void remove() {
			throw new UnsupportedOperationException("remove not valid for this class");
		}
	}
	/**
	 * This iterates through the directories.
	 * 
	 * <p>The current location in the tree is given by the structures
	 *    {@link #levels} and {@link #depth}.
	 *    
	 * @author Bradley Ross
	 *
	 */
	protected class DirIterator implements Iterator<File> {
		protected File root = null;
		/**
		 * Depth of the search tree.
		 * <p>A value of zero means that there are no levels.  A value
		 *    of n means that there are n levels and 
		 *    the highest level is n-1.</p>
		 */
		protected int depth = 0;
		/**
		 * Array of Level objects representing the levels in the tree
		 * search.
		 * 
		 * <p>The current positions in the levels at the start of executing
		 *    next represent the next item to be retrieved.</p>
		 */
		protected ArrayList<Level> levels = new ArrayList<Level>();
		/**
		 * Set to true after the last directory has been retrieved using
		 * the next method.
		 */
		protected boolean done = false;
		/**
		 * Set to false after first directory has been retrieved using the
		 * next method.
		 */
		protected boolean initial = true;
		protected Level currentLevel = null;
		protected File currentDir = null;
		protected File nextDir = null;
		protected int limit = maxCount;
		/** 
		 * Number of directories that have been retrieved using next.
		 */
		protected int count = 0;
		/**
		 * Constructor using depth first search.
		 * @param value root directory
		 */
		public DirIterator(File value) throws IOException {
			if (!value.isDirectory()) {
				done = true;
				throw new IOException("Root " + value.getCanonicalPath() +
						" is not a directory");
			}
			root = value;
		}
		/**
		 * Returns the next item in the list.
		 * @return next item
		 */
		public File next() throws NoSuchElementException {
			if (!initial) {
				if (debugLevel > 0) {
					System.out.println("*****");
					System.out.println("Starting next, iteration " + Integer.toString(count) + "  Depth = " + Integer.toString(depth));
					System.out.print("Current positions:  "); 
					for (int i = 0; i < depth; i++) {
						System.out.print(Integer.toString(levels.get(i).getCurrentPosition()) + " ");
					}
					System.out.println();
				}
			}
			if (done) {
				throw new NoSuchElementException();
			}
			count++;
			if (limit > 0 && count > limit) {
				done = true;
				throw new NoSuchElementException();
			}
			if (initial) {
				if (debugLevel > 0) {
					System.out.println("Initial pass");
				}
				initial = false;
				if (!hasDirectories(root)) {
					done = true;
					return root;
				} else {
					Level newLevel = new Level(root);
					newLevel.next();
					levels.add(newLevel);
					depth = 1;
					return root;
				}
			}
			currentLevel = levels.get(depth - 1);
			if (hasDirectories(currentLevel.current())) {
				if (debugLevel > 0) {
					System.out.println("Last entry had subdirectory");
				}
				Level newLevel = new Level(currentLevel.current());
				if (newLevel.hasNext()) {
					File returnItem = newLevel.next();
					levels.add(newLevel);
					depth = depth + 1;
					return returnItem;
				} else {
					throw new NoSuchElementException("Shouldn't occur");
				}

			} else if (currentLevel.hasNext()) {
				if (debugLevel > 0) {
					System.out.println("Go to next at current level");
				}
				File returnItem = currentLevel.next();
				return returnItem;
			} else {
				while (true) {
					if (debugLevel > 0) {
						System.out.println("Move up tree");
					}
					levels.remove(depth - 1);
					depth = depth - 1;
					if (depth == 0) {
						done = true;
						throw new NoSuchElementException();
					}
					currentLevel = levels.get(depth - 1);
					if (currentLevel.hasNext()) { break; }
				}
				File returnItem = currentLevel.next();
				return returnItem;
			}
		}
		/**
		 * Determines if there are more items in the list.
		 * @return true if there are more items
		 */
		public boolean hasNext() {
			if (!root.isDirectory()) { return false; }
			if (initial && (hasFiles(root) || hasDirectories(root))) {
				return true;
			}
			if (done) { 
				return false;
			}
			if (hasDirectories(levels.get(depth - 1).current())) {
				return true;
			}
			for (int i = 0; i < depth; i++) {
				if (levels.get(i).hasNext()) { return true; }
			}
			return false;  // temporary
		}
		/**
		 * This method is an optional method that is not implemented for this class.
		 */
		public void remove() {
			throw new UnsupportedOperationException("remove not valid for this class");
		}
	}
	/**
	 * This represents a level at a single depth in the tree search.
	 * @author Bradley Ross
	 *
	 */
	public class Level implements Iterator<File> {
		protected File[] dirs = null;
		/**
		 * Index of last element fetched using next().
		 */
		protected int position = 0;
		/**
		 * Number of directories to be processed.
		 */
		protected int length = 0;
		protected boolean done = false;
		public Level(File value) { 
			dirs = value.listFiles(directoriesOnly);
			if (dirs == null) { 
				length = 0;
				position = -1;
				done = true;
			}
			else if (dirs.length == 0) { 
				length = 0;
				position = -1;
				done = true; }
			length = dirs.length;
			position = -1;
		}
		public boolean hasNext() {
			if (done) {
				return false;
			}
			if (length - 1 < position + 1) {

				return false;
			}
			return true;
		}
		public File current()  {
			if (done) {
				throw new NoSuchElementException();
			}
			return dirs[position];
		}
		/**
		 * Returns position of last item retrieved.
		 * @return position of last item retrieved
		 */
		public int getCurrentPosition() {
			return position;
		}
		public File next() {
			if (done) {
				throw new NoSuchElementException();
			}
			if (length - 1 < position + 1) {
				done = true;
				throw new NoSuchElementException();
			}
			position = position + 1;
			return dirs[position];
		}
		public void remove() {
			throw new UnsupportedOperationException("remove not valid for this class");
		}
	}
	/**
	 * Indicates whether there are more files in the list.
	 * @return true if there are more items
	 */
	public boolean hasNext() {
		if (done) {
			return false;
		} else {
			while (!files.hasNext() && directories.hasNext()) {
				files = new LocalIterator(directories.next());
			}
			if (files.hasNext()) { return true; }
			if (directories.hasNext()) { return true; }
			return false;
		}
	}
	/**
	 * Returns next file in list.
	 * @return next file
	 */
	public File next() {
		if (done) {
			throw new NoSuchElementException();
		}
		while (!files.hasNext() && directories.hasNext()) {
			files = new LocalIterator(directories.next());
		}
		if (!files.hasNext() && !directories.hasNext()) {
			done = true;
			throw new NoSuchElementException();
		}
		return files.next();
	}
	/**
	 * This is an optional method that is not implemented for this class.
	 * 
	 */
	public void remove() {
		throw new UnsupportedOperationException("remove not valid for this class");
	}
	/**
	 * Test program for Level class.
	 * @param value root directory
	 */
	public void test1(File value) throws IOException {
		Level test = new Level(value);
		while (test.hasNext()) {
			File item = test.next();
			System.out.println(item.getAbsolutePath() + " " + 
					Integer.toString(test.getCurrentPosition()));
		}
	}
	/**
	 * Test iterator for DirWalker class.
	 * @param value root of directory structure
	 * @return iterator
	 * @throws IOException
	 */
	public Iterator<File> test2(File value) throws IOException {
		return new DirIterator(value);
	}
	/**
	 * Test driver.
	 * @param args - not used
	 */
	public static void main(String[] args) {
		try {
			File root = new File("/");
			DirWalker instance = new DirWalker(root);
			instance.test1(new File("/Users/bradleyross/AmtrakDesktop"));
			System.out.println("Starting second test");
			Iterator<File> second = instance.test2(new File("/Users/bradleyross/AmtrakDesktop"));
			while (second.hasNext()) {
				File item = second.next();
				System.out.println(item.getAbsolutePath());
			}
		} catch (Exception e)  {
			System.out.println(e.getClass().getName() + " " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Program complete");
	}
}
