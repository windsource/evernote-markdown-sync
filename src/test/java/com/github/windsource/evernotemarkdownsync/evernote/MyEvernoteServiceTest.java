package com.github.windsource.evernotemarkdownsync.evernote;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.github.windsource.evernotemarkdownsync.evernote.MyEvernoteService;

public class MyEvernoteServiceTest {
	// A sandbox token just for test
	static final String token = "S=s1:U=91a09:E=15816c57126:C=150bf1444a0:P=1cd:A=en-devtoken:V=2:H=b3ecd2f9e715a8ef2d4b3fc6edf398fb";

	final static String noteContentPath = "/noteContent.enml";
	static String noteContent = "";
			
	@BeforeClass
	public static void oneTimeSetUp() throws IOException, URISyntaxException {
		URL url = MyEvernoteServiceTest.class.getResource(noteContentPath);
		byte[] encoded = Files.readAllBytes(Paths.get(url.toURI()));
		noteContent = new String(encoded, "UTF-8");
	}
	
	@Test
	public void testMyEvernoteService() throws Exception {
		new MyEvernoteService(token, true);
	}

	@Test
	public void testCreateNote() throws Exception {
		MyEvernoteService service = new MyEvernoteService(token, true);
		service.createNote("My new note", noteContent, null, "");
	}
	
	@Test
	public void testCreateNoteWithCreationDate() throws Exception {
		MyEvernoteService service = new MyEvernoteService(token, true);
		DateTime date = new DateTime("2015-10-25T17:11:25+01:00");
		Note note = service.createNote("My new note", noteContent, date.getMillis(), "");
		assertEquals(note.getCreated(), date.getMillis());
		assertEquals(note.getUpdated(), date.getMillis());
	}
	
	@Test
	public void testCreateNoteWithSource() throws Exception {
		MyEvernoteService service = new MyEvernoteService(token, true);
		String source = "My great app.abc";
		Note note = service.createNote("My new note", noteContent, null, source);
		assertEquals(note.getAttributes().getSource(), source);
	}
	
	@Test
	public void testFindNoteBySource() throws Exception {
		String source = UUID.randomUUID().toString();
		
		// create a note
		MyEvernoteService service = new MyEvernoteService(token, true);
		Note note = service.createNote("My new note", noteContent, null, source);
		
		// now search for it
		NoteList noteList = service.findNoteBySource(source);
		assertEquals(noteList.getNotesSize(), 1);
		assertEquals(noteList.getNotes().get(0).getGuid(), note.getGuid());
	}
}
