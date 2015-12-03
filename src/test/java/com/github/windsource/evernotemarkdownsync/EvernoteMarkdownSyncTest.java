package com.github.windsource.evernotemarkdownsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.windsource.evernotemarkdownsync.EvernoteMarkdownSync;

public class EvernoteMarkdownSyncTest {
	// A sandbox token just for test
	static final String token = "S=s1:U=91a09:E=15816c57126:C=150bf1444a0:P=1cd:A=en-devtoken:V=2:H=b3ecd2f9e715a8ef2d4b3fc6edf398fb";

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
