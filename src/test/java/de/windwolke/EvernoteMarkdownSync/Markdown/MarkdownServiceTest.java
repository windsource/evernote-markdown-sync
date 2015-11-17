package de.windwolke.EvernoteMarkdownSync.Markdown;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.pegdown.PegDownProcessor;
import org.xml.sax.SAXException;

public class MarkdownServiceTest {

	@Test
    public void testMarkdownToHtml() throws MarkdownException
    {
		MarkdownService ms = new MarkdownService();
		String md = "# Titel\nSome text!\n\n``` sql\necho \"hello world!\"```\n\n";
		String result = ms.markdownToHtml(md);
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

