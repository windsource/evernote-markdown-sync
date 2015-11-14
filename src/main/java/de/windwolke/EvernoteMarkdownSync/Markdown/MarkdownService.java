package de.windwolke.EvernoteMarkdownSync.Markdown;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.VerbatimSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkdownService {
	final static Logger LOG = LoggerFactory.getLogger(MarkdownService.class);

	private PegDownProcessor processor;
	private Set<String> markdownFileExtensions = new HashSet<String>();
	
	public MarkdownService () {
		markdownFileExtensions.add("md");
		markdownFileExtensions.add("txt");
		markdownFileExtensions.add("markdown");
		
		processor = new PegDownProcessor(Extensions.HARDWRAPS
				| Extensions.AUTOLINKS | Extensions.TABLES
				| Extensions.FENCED_CODE_BLOCKS | Extensions.STRIKETHROUGH
				| Extensions.ATXHEADERSPACE | Extensions.TASKLISTITEMS);
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
		
		return prefix + markdownToHtml(markdown) + postfix;
	}

	/**
	 * Convert markdown into HTML (just the body content)
	 * 
	 * @param markdown Markdown to convert into HTML
	 * @return
	 */
	String markdownToHtml(String markdown) {
		String html;
		Map<String, VerbatimSerializer> verbatimSerializers = new HashMap<>();
		verbatimSerializers.put(MyCustomVerbatimSerializer.DEFAULT, MyCustomVerbatimSerializer.INSTANCE);

		html = processor.markdownToHtml(markdown.toCharArray(), new LinkRenderer(), verbatimSerializers);
		
		return html;
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
