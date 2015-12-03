package com.github.windsource.evernotemarkdownsync.evernote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteAttributes;
import com.evernote.thrift.TException;

public class MyEvernoteService {
	final static Logger LOG = LoggerFactory.getLogger(MyEvernoteService.class);

	private UserStoreClient userStore;
	private NoteStoreClient noteStore;
	
	@SuppressWarnings("unused")
	private String token;
	
	/**
	 * Creates a service used to access Evernote
	 * 
	 * @param token authentication token
	 * @param sandbox true for snadox, false for prouction enviroment
	 * @throws Exception
	 */
	public MyEvernoteService(String token, boolean sandbox) throws Exception {
		this.token = token;
		// Set up the UserStore client and check that we can speak to the server
		EvernoteService service = EvernoteService.PRODUCTION;
		if ( sandbox ) {
			service = EvernoteService.SANDBOX;
		}
		EvernoteAuth evernoteAuth = new EvernoteAuth(service, token);
		ClientFactory factory = new ClientFactory(evernoteAuth);
		userStore = factory.createUserStoreClient();

		boolean versionOk = userStore.checkVersion("EvernoteMarkdownSync",
				com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
				com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR);
		if (!versionOk) {
			LOG.error("Incompatible Evernote client protocol version");
			System.exit(1);
		}

		// Set up the NoteStore client
		noteStore = factory.createNoteStoreClient();
	}
	
	/**
	 * Creates a new note
	 * 
	 * @param title The note's title
	 * @param content The note's content as EHTML
	 * @param date If not null used for created and updated date 
	 * @param source Source attribute to be added to the note 
	 * @throws Exception
	 */
	public Note createNote(String title, String content, Long date, String source) throws Exception {
		LOG.debug("Creating new note: {}", title);
		
		Note note = new Note();
		note.setTitle(title);
		note.setContent(content);
		if ( date != null) {
			note.setCreated(date);	
			note.setUpdated(date);
		}
		if (source != null && source.length()>0) {
			NoteAttributes attributes = new NoteAttributes();
			attributes.setSource(source);
			note.setAttributes(attributes);
		}

		LOG.debug(content);
		return noteStore.createNote(note);
	}

	/**
	 * Search for note using the note's source attribute
	 * 
	 * @param source The source attribute to search for 
	 * @return
	 * @throws Exception
	 */
	public NoteList findNoteBySource(String source) throws Exception {
		NoteFilter filter = new NoteFilter();
		String query = "source:\"" + source + "\"";
		filter.setWords(query);

		NoteList notes = noteStore.findNotes(filter, 0, 10);
		
		return notes;
	}
	
	/**
	 * Updates a note
	 * 
	 * @param note The note to update
	 * @return
	 * @throws EDAMUserException
	 * @throws EDAMSystemException
	 * @throws EDAMNotFoundException
	 * @throws TException
	 */
	public Note updateNote(Note note) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
		LOG.debug("Updating note: {}", note.getTitle());
		return noteStore.updateNote(note);
	}
}
