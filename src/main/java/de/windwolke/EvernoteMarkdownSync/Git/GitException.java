package de.windwolke.EvernoteMarkdownSync.Git;

/**
 * Created in case any problems occur when invoking git
 */
public class GitException extends Exception {
	public GitException(String message) {
		super(message);
	}
}
