package de.windwolke.EvernoteMarkdownSync.Markdown;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FilenameUtils;
import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.VerbatimSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class MarkdownService {
	final static Logger LOG = LoggerFactory.getLogger(MarkdownService.class);

	private PegDownProcessor processor;
	private Set<String> markdownFileExtensions = new HashSet<String>();
	private String styleSheet = "";
	private final String STYLE_SHEET_FILE = "/style.css";
	
	public MarkdownService () {
		markdownFileExtensions.add("md");
		markdownFileExtensions.add("txt");
		markdownFileExtensions.add("markdown");
		
		processor = new PegDownProcessor(Extensions.HARDWRAPS
				| Extensions.AUTOLINKS | Extensions.TABLES
				| Extensions.FENCED_CODE_BLOCKS | Extensions.STRIKETHROUGH
				| Extensions.ATXHEADERSPACE | Extensions.TASKLISTITEMS);
		
		// read style sheet
		URL url = MarkdownService.class.getResource(STYLE_SHEET_FILE);
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
			styleSheet = new String(encoded, "UTF-8");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converts a Markdown document to ENML (Evernote Markup Language)
	 * 
	 * @param path Path to the file containing the Markdown in UTF-8
	 * @return ENML coded document 
	 * @throws MarkdownException 
	 */
	public String markdownToEnml(Path path) throws MarkdownException {
		String enml;
		try {
			byte[] encoded;
			encoded = Files.readAllBytes(path);
			enml = markdownToEnml(new String(encoded, "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
	        throw new MarkdownException("Note is not UTF-8 encoded.");		
		}
		return enml;
	}

	/**
	 * Converts a Markdown document to ENML (Evernote Markup Language)
	 * 
	 * @param markdown String containing the Markdown to convert
	 * @return ENML coded document 
	 * @throws MarkdownException 
	 */
	public String markdownToEnml(String markdown) throws MarkdownException {
		String prefix = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
		              + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">\n"
		              + "<en-note>";
		String postfix = "</en-note>"; 
		
		return prefix + markdownToHtml(markdown) + postfix;
	}

	/**
	 * Convert Markdown into HTML (just the body content) with inline styles
	 * 
	 * @param markdown Markdown to convert into HTML
	 * @return String containing HTML (just the body content)
	 * @throws MarkdownException 
	 */
	String markdownToHtml(String markdown) throws MarkdownException {
		Map<String, VerbatimSerializer> verbatimSerializers = new HashMap<>();
		verbatimSerializers.put(MyCustomVerbatimSerializer.DEFAULT, MyCustomVerbatimSerializer.INSTANCE);

		String htmlBody = processor.markdownToHtml(markdown.toCharArray(), new LinkRenderer(), verbatimSerializers);
		LOG.debug("Body before styles: {}",htmlBody);

		String htmlWithStyleInline = "";
		try {
			htmlWithStyleInline = new HtmlStyleSheetInliner().inlineStyleSheet(htmlBody, styleSheet);
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
			throw new MarkdownException("Could not turn Markdown to HTML");
		}
		LOG.debug("Body after styles: {}",htmlWithStyleInline);
		
		return htmlWithStyleInline;
	}

	/**
	 * Returns true if the given filename contains one of the known Markdown
	 * filename extensions
	 * 
	 * @param filename
	 */
	public boolean isMarkdownFile(String filename) {
		String ext = FilenameUtils.getExtension(filename);
		return markdownFileExtensions.contains(ext);
	}
}
