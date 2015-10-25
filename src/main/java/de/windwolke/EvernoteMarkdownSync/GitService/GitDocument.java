package de.windwolke.EvernoteMarkdownSync.GitService;

import org.joda.time.DateTime;

public class GitDocument {
	private String fileName;
	private DateTime lastChange;
	
	public GitDocument(String fileName, DateTime lastChange) {
		super();
		this.fileName = fileName;
		this.lastChange = lastChange;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public DateTime getLastChange() {
		return lastChange;
	}
	public void setLastChange(DateTime lastChange) {
		this.lastChange = lastChange;
	}
	
}
