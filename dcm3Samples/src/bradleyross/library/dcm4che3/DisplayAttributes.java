package bradleyross.library.dcm4che3;
// import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.dcm4che.data.Attributes;
import org.dcm4che.data.ElementDictionary;
import org.dcm4che.data.Sequence;
import org.dcm4che.data.VR;
import org.dcm4che.util.TagUtils;

import bradleyross.library.dcm4che3.TagFilter;
/**
 * Provides a means of displaying the contents of a Dicom object.abstract
 * @author Bradley Ross
 *
 */
public class DisplayAttributes {
	/**
	 * Controls amount of diagnostic printing.
	 * <p>A value of zero indicates the normal amount of 
	 *    output.  More positive values result in more
	 *    output, while more negative values result in less 
	 *    output.</p>
	 *    
	 */
	public int debugLevel = 0;
	public int getDebugLevel() {
		return debugLevel;
	}
	public void setDebugLevel(int value) {
		debugLevel = value;
	}
	public static int staticDebugLevel = 0;
	public static int getStaticDebugLevel() {
		return staticDebugLevel;
	}
	public static void setStaticDebugLevel(int value) {
		staticDebugLevel = value;
	}
	/**
	 * Tracks the level of nesting for nested Dicom objects.
	 */
	public int indentLevel = 0;
	public void display(Attributes dicomObject) {
		display(dicomObject, (TagFilter) null, System.out);
	}
	public void display(Attributes dicomObject, TagFilter selector) {
		display(dicomObject, selector, System.out);
	}
	public void display(Attributes dicomObject, TagFilter selector, PrintStream out) {
		OutputStreamWriter out1 = new OutputStreamWriter(out);
		PrintWriter print = new PrintWriter(out1);
		display(dicomObject, selector, print);
	}
	public void display(Attributes dicomObject, PrintWriter out) {

		display(dicomObject, (TagFilter) null, out);
	}
	/**
	 * Print the contents of a Dicom object.
	 * @param dicomObject object whose contents are to be displayed
	 * <p>This method will recursively call itself to handle
	 *    embedded objects.</p>
	 */
	public void display (Attributes dicomObject, TagFilter selector, PrintWriter out) {
		int[] tags = dicomObject.tags();
		// log("There are " + Integer.toString(tags.length) + " tags");
		for (int i = 0; i < tags.length; i++) {
			int tag = tags[i];
			if (selector != null && !selector.accept(tags[i])) { continue; }
			VR vr = dicomObject.getVR(tag);
			showValue(dicomObject, tag, out);
			if (vr == VR.SQ) {
				indentLevel++;
				Sequence seq = dicomObject.getSequence(tag);
				out.println("There are " + Integer.toString(seq.size()) + " items in the sequence");
				for (int i2 = 0; i2 < seq.size(); i2++) {
					out.println("Start of item " + Integer.toString(i2));
					indentLevel++;
					display(seq.get(i2), null, out);
					indentLevel--;
					out.println("End of item " + Integer.toString(i2));
				}
				indentLevel--;
			}
		}
	}
	/**
	 * Write information about a tag.
	 * @param dicomObject Dicom object containing the tag
	 * @param tag integer value of the tag to be displayed
	 */
	public void showValue(Attributes dicomObject, int tag, PrintWriter out) {
		String name;
		if (TagUtils.isPrivateGroup(tag)) {
			name = " - private group - ";
		} else {
		name = ElementDictionary.keywordOf(tag, null);
		}
		VR vr = dicomObject.getVR(tag);
		if (!dicomObject.containsValue(tag)) {
			out.println(indents(indentLevel) + TagUtils.toString(tag) + " " + name + " not found");
		} else if (vr == VR.LT || vr == VR.UI || vr == VR.ST ||
				vr == VR.LO || vr == VR.SH || vr == VR.UT || vr == VR.CS || vr == VR.PN) {
			String value = dicomObject.getString(tag);
			StringBuilder sb = new StringBuilder(indents(indentLevel));
			sb.append(TagUtils.toString(tag) + " " + name + " " + vr.toString() + " :: ");
			if (value.length() < 60) {
				sb.append(value);
			} else {
				sb.append(value.substring(0, 60));
			}
			out.println(sb.toString());
		} else if (vr == VR.DS || vr == VR.FD || vr == VR.FL) {
			StringBuilder sb = new StringBuilder(indents(indentLevel));
			sb.append(TagUtils.toString(tag) + " " + name);
			float[] values = dicomObject.getFloats(tag);
			if (values.length > 1) {
				sb.append(" - " + Integer.toString(values.length) + " values :: ");
			} else {
				sb.append(" :: ");
			}
			sb.append(Float.toString(values[0]));
			out.println(sb.toString());
		} else if (vr == VR.US || vr == VR.UL || vr == VR.SS || vr == VR.SL) {
			StringBuilder sb = new StringBuilder(indents(indentLevel));
			sb.append(TagUtils.toString(tag) + " " + name);
			int[] values = dicomObject.getInts(tag);
			if (values.length > 1) {
				sb.append(" - " + Integer.toString(values.length) + " values - ");
			} else {
				sb.append(" - ");
			}
			sb.append(Integer.toString(values[0]));
			out.println(sb.toString());
		} else if (vr == VR.TM) {
			StringBuilder sb = new StringBuilder(indents(indentLevel));
			sb.append(TagUtils.toString(tag) + " " + name + " :: ");
			float value = (float) dicomObject.getDate(tag).getTime() / 1000.0f;
			sb.append(Float.toString(value));
			out.println(sb.toString());
		} else if (vr == VR.DA || vr == VR.DT) {
			StringBuilder sb = new StringBuilder(indents(indentLevel));
			sb.append(TagUtils.toString(tag) + " " + name + " ");
			Date value = dicomObject.getDate(tag);
			sb.append(value.toString());
			out.println(sb.toString());
		} else {
			StringBuilder sb = new StringBuilder(indents(indentLevel));
			sb.append(TagUtils.toString(tag) + " " + name + " " + vr.toString() + " ");
			out.println(sb.toString());
		}
	}
	/**
	 * Generate the string for the start of the output lines.
	 * @param count level of indentation
	 * @return prefix for output line
	 */
	protected String indents(int count) {
		StringBuilder working = new StringBuilder();
		for (int i = 0; i < count ; i++) {
			working.append(">");
		}
		return working.toString();
	}
}
