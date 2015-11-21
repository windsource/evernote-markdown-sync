package de.windwolke.EvernoteMarkdownSync;

import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;

import de.windwolke.EvernoteMarkdownSync.Evernote.MyEvernoteService;
import de.windwolke.EvernoteMarkdownSync.Git.GitObject;
import de.windwolke.EvernoteMarkdownSync.Git.GitService;
import de.windwolke.EvernoteMarkdownSync.Markdown.MarkdownService;

public class EvernoteMarkdownSync {
	final static Logger LOG = LoggerFactory.getLogger(EvernoteMarkdownSync.class);

	private boolean sandbox = false;
	private String token = "";
	private String path = "";

	public static void main(String[] args) throws Exception {
		EvernoteMarkdownSync ems = new EvernoteMarkdownSync(args);
		ems.sync();
	}
	
	public EvernoteMarkdownSync(String[] args) {
		parseArgs(args);
	}
	
	private void parseArgs(String[] args) {
		Options options = new Options();
		
		options.addOption( Option.builder("t")
				.desc( "Evernote token used to access account" )
                .hasArg()
                .argName("TOKEN")
                .required()
                .build() );
		
		options.addOption( Option.builder("p")
				.desc( "path to markdown documents inside git repository" )
                .hasArg()
                .argName("PATH")
                .required()
                .build() );

		options.addOption("s", false, "use Evernote sandbox");
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			
			token = cmd.getOptionValue("t");
			path = cmd.getOptionValue("p");
					
			if ( cmd.hasOption("s") ) {
				sandbox = true;
			}
			
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "java -jar evernote-markdown-sync-<version>.jar -p <PATH> -t <TOKEN>", options );
			
			System.exit(1);
		}
	}
	
	public void sync() throws Exception {		
		GitService gs = new GitService(path);
		MarkdownService ms = new MarkdownService();
		MyEvernoteService es = new MyEvernoteService(token, sandbox);
		
		List<GitObject> gitObjects = gs.retrieveDocuments();
		for (GitObject go: gitObjects) {
			if ( ms.isMarkdownFile(go.getFileName())) {	
				String source = fileNameToSource(go.getFileName());
				
				NoteList nl = es.findNoteBySource(source);
	
				if (nl.getTotalNotes() == 0) {
					// Note does not exit yet in Evernote
					// Create a new one in Evernote
					String title = FilenameUtils.getBaseName(go.getFileName());
					String content = ms.markdownToEnml(Paths.get(path,go.getFileName()));
					
					es.createNote(title, content, go.getLastChange().getMillis(), source);
					LOG.info("New '{}'", go.getFileName());
				}
				else if (nl.getTotalNotes() == 1) {
					// Note already exists in Evernote
					Note note = nl.getNotes().get(0);
					
					// Check if the note needs to be updated
					if ( go.getLastChange().getMillis() > note.getUpdated() ) {
						String content = ms.markdownToEnml(Paths.get(path,go.getFileName()));
						note.setContent(content);
						note.setUpdated(go.getLastChange().getMillis());
						es.updateNote(note);
						LOG.info("Modified '{}'", go.getFileName());						
					}
					else {
						LOG.info("Unmodified '{}'", go.getFileName());						
					}
				}
				else {
					LOG.error("Note '{}' found multiple times!", go.getFileName());											
				}
			}
		}
	}

	private String fileNameToSource(String fileName) {
		final String baseName = "EvernoteMarkdownSync.";
		return fileName + baseName;
	}

}
