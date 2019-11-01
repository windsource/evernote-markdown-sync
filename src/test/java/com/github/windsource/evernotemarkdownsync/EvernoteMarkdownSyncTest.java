package com.github.windsource.evernotemarkdownsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.windsource.evernotemarkdownsync.EvernoteMarkdownSync;

public class EvernoteMarkdownSyncTest {
	// A sandbox token just for test
	static final String token = "S=s1:U=958f1:E=1757e169412:C=16e266566d0:P=1cd:A=en-devtoken:V=2:H=396128d9e470bd02311b195c8b494e81";

	@Test
	public void testSync() throws Exception {
		List<String> args = new ArrayList<String>();
		args.add("-t");
		args.add(token);
		args.add("-p");
		File file = new File("src/test/resources/markdown");
		args.add(file.getAbsolutePath());
		args.add("-s");
		
		EvernoteMarkdownSync ems = new EvernoteMarkdownSync(args.toArray(new String[args.size()]));
		ems.sync();
	}
}
