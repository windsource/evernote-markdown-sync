package de.windwolke.EvernoteMarkdownSync.Markdown;

import static org.junit.Assert.*;

import org.junit.Test;
import org.pegdown.PegDownProcessor;

public class MarkdownServiceTest {

	@Test
    public void testPegDown() 
    {
		PegDownProcessor p = new PegDownProcessor();
		String md = "# Titel\nSome text!\n\n```bash\necho \"hello world!\"```";
		String result = p.markdownToHtml(md);
		System.out.println(result);
    }
	
	@Test
    public void testIsMarkdownFile() {
		MarkdownService ms = new MarkdownService();
		assertTrue(ms.isMarkdownFile("test.md"));
		assertTrue(ms.isMarkdownFile("my great file.markdown"));
		assertTrue(ms.isMarkdownFile("my great file 2.0.txt"));				
		assertFalse(ms.isMarkdownFile("my great file 2.0.txt.gz"));				
		assertFalse(ms.isMarkdownFile("justafile"));				
		assertFalse(ms.isMarkdownFile("image.jpg"));				
	}
}

