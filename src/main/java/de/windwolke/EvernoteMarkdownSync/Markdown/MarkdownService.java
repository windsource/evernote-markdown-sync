package de.windwolke.EvernoteMarkdownSync.Markdown;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.pegdown.PegDownProcessor;

public class MarkdownService {

	private PegDownProcessor processor = new PegDownProcessor();
	private Set<String> markdownFileExtensions = new HashSet<String>();
	
	public MarkdownService () {
		markdownFileExtensions.add("md");
		markdownFileExtensions.add("txt");
		markdownFileExtensions.add("markdown");		
	}

	/**
	 * Converts a markdown document to ENML (Evernote Markup Language)
	 * 
	 * @param path Path to the file containing the markdown in UTF-8
	 * @return ENML coded document 
	 * @throws IOException 
	 */
	public String markdownToEnml(Path path) throws IOException {
		byte[] encoded = Files.readAllBytes(path);
		return markdownToEnml(new String(encoded, "UTF-8"));
	}

	/**
	 * Converts a markdown document to ENML (Evernote Markup Language)
	 * 
	 * @param markdown String containing the markdown to convert
	 * @return ENML coded document 
	 */
	public String markdownToEnml(String markdown) {
		String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
		              + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n"
		              + "<en-note>";
		String postfix = "</en-note>"; 
		
		return prefix + processor.markdownToHtml(markdown) + postfix;
	}

	/**
	 * Returns true if the given filename contains one of the known markdown
	 * filename extensions
	 * 
	 * @param filename
	 */
	public boolean isMarkdownFile(String filename) {
		String ext = FilenameUtils.getExtension(filename);
		return markdownFileExtensions.contains(ext);
	}
}
