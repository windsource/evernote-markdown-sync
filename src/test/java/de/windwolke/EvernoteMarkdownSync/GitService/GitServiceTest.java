package de.windwolke.EvernoteMarkdownSync.GitService;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import de.windwolke.EvernoteMarkdownSync.Git.GitException;
import de.windwolke.EvernoteMarkdownSync.Git.GitObject;
import de.windwolke.EvernoteMarkdownSync.Git.GitService;

public class GitServiceTest {

	@Test
    public void testThisRepoNumberOfObjects() throws GitException
    {
		String path = ".";
		GitService service = new GitService(path);
		List<GitObject> objects = service.retrieveDocuments();
        assertTrue(objects.size() == 7);
    }

	@Test
    public void testThisRepoDateTime() throws GitException
    {
		String path = ".";
		GitService service = new GitService(path);
		List<GitObject> objects = service.retrieveDocuments();
		boolean found = false;
		for (GitObject go : objects) {
			if (go.getFileName().equals("gradlew.bat")) {
				assertEquals(go.getLastChange(), new DateTime("2015-10-25T17:11:25+01:00"));
				found = true;
			}
		}
		assertTrue(found);
    }
}
