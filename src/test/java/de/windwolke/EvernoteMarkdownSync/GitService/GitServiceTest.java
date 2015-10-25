package de.windwolke.EvernoteMarkdownSync.GitService;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.windwolke.EvernoteMarkdownSync.GitService.GitDocument;
import de.windwolke.EvernoteMarkdownSync.GitService.GitService;

public class GitServiceTest {

	@Test
    public void testThisRepo()
    {
		String path = ".";
		GitService service = new GitService(path);
		List<GitDocument> docs = service.retrieveDocuments();
		
        assertTrue(docs.size() == 1);
    }

}
