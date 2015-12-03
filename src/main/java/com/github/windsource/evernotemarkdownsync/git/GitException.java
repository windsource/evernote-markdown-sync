package com.github.windsource.evernotemarkdownsync.git;

/**
 * Created in case any problems occur when invoking git
 */
public class GitException extends Exception {
	public GitException(String message) {
		super(message);
	}
}
