package de.windwolke.EvernoteMarkdownSync.Markdown;

/**
 * Created in case any problems occur when turning Markdown to ENML
 */
public class MarkdownException extends Exception {
	public MarkdownException(String message) {
		super(message);
	}
}
