package com.github.windsource.evernotemarkdownsync.git;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.github.windsource.evernotemarkdownsync.git.GitException;
import com.github.windsource.evernotemarkdownsync.git.GitObject;
import com.github.windsource.evernotemarkdownsync.git.GitService;

public class GitServiceTest {

	@Test
    public void testThisRepoNumberOfObjects() throws GitException
    {
		String path = ".";
		GitService service = new GitService(path);
		List<GitObject> objects = service.retrieveDocuments();
        assertTrue(objects.size() == 8);
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
				assertEquals(go.getLastChange(), new DateTime("2019-11-01T14:29:23.000+01:00"));
				found = true;
			}
		}
		assertTrue(found);
    }
}

