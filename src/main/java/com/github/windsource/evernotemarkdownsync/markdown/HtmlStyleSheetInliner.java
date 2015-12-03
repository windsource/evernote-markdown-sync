package com.github.windsource.evernotemarkdownsync.markdown;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

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
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws TransformerException 
	 */
	public String inlineStyleSheet(String htmlBody, String styleSheet) throws SAXException, IOException, TransformerException {
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
	    is = new ByteArrayInputStream( htmlDoc.getBytes( "UTF-8" ) );
		
        DocumentSource docSource = null;
		try {
			docSource = new StreamDocumentSource(is, null, "text/htnl");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

        //Parse the input document
        DOMSource parser = new DefaultDOMSource(docSource);
        Document doc = null;
		doc = parser.parse();

        //Create the CSS analyzer
        DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
        da.attributesToStyles(); 
        da.addStyleSheet(null, styleSheet, DOMAnalyzer.Origin.USER);
        da.stylesToDomInherited();

		// Extract body and rename to div
		Node body = doc.getElementsByTagName("body").item(0);
		body = doc.renameNode(body, body.getNamespaceURI(), "div");

		// Turn the node into a string
		// Note: When using CSSBox to do this the special characters (e.g. &lt;) 
		//       are not handled correctly 
		javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(body);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		transformer = tf.newTransformer();
		transformer.setOutputProperty("omit-xml-declaration", "yes");
		transformer.transform(domSource, result);

        return writer.toString();
	}

}
