package com.github.windsource.evernotemarkdownsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.windsource.evernotemarkdownsync.EvernoteMarkdownSync;

public class EvernoteMarkdownSyncTest {
	// A sandbox token just for test
	static final String token = "S=s1:U=94d74:E=16ccd609f2b:C=16575af7248:P=1cd:A=en-devtoken:V=2:H=ddd7084a7be07d232718bde362e596fc";

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
