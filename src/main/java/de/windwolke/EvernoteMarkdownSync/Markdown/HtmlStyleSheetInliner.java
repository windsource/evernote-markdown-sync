package de.windwolke.EvernoteMarkdownSync.Markdown;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.css.NormalOutput;
import org.fit.cssbox.css.Output;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;
import org.xml.sax.SAXException;
import org.w3c.dom.*;


public class HtmlStyleSheetInliner {
	
	/**
	 * Takes an HTML body and a CSS style sheet makes the style inline
	 * 
	 * @param htmlBody The text inside the body to inline with a style sheet
	 * @param styleSheet The style sheet content to inline
	 * @return Body text of the HTML
	 */
	public String inlineStyleSheet(String htmlBody, String styleSheet) {
		final String htmlPrefix = "<!DOCTYPE html>"+
		                          "<html>"+
		 					      "<head>"+
		                          "<meta charset=\"UTF-8\""+
		                          "</head>"+
 		                          "<body>";
 		final String htmlPostfix = "</body></html>"; 
		
 		String htmlDoc = new StringBuilder().
 				append(htmlPrefix).
 				append(htmlBody).
 				append(htmlPostfix).
 				toString();
 		
 		// Create a stream from the HTML
		InputStream is = null;
		try {
			is = new ByteArrayInputStream( htmlDoc.getBytes( "UTF-8" ) );
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
        DocumentSource docSource = null;
		try {
			docSource = new StreamDocumentSource(is, null, "text/htnl");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        //Parse the input document
        DOMSource parser = new DefaultDOMSource(docSource);
        Document doc = null;
		try {
			doc = parser.parse();
		} catch (SAXException | IOException e1) {
			e1.printStackTrace();
		}

        //Create the CSS analyzer
        DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
        da.attributesToStyles(); 
        da.addStyleSheet(null, styleSheet, DOMAnalyzer.Origin.USER);
        da.stylesToDomInherited();

		// Extract body and rename to div
		Node body = doc.getElementsByTagName("body").item(0);
		body = doc.renameNode(body, body.getNamespaceURI(), "div");

		//Save the output
		ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream(); 
        PrintStream printStream = new PrintStream(byteArrayStream);		
		
        Output out = new NormalOutput(body);
        out.dumpTo(printStream);
        printStream.close();

        try {
			docSource.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        String result = "";
        try {
			result = byteArrayStream.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

        return result;
	}

}
