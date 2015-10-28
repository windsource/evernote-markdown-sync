package de.windwolke.EvernoteMarkdownSync;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class EvernoteMarkdownSync {

	public static void main(String[] args) {
		boolean sandbox = false;
		String notebook;
		String token;
		String path;
		
		Options options = new Options();
		
		options.addOption( Option.builder("t")
				.desc( "Evernote token used to access account" )
                .hasArg()
                .argName("TOKEN")
                .required()
                .build() );
		
		options.addOption( Option.builder("n")
				.desc( "Evernote notebook to use" )
                .hasArg()
                .argName("NOTEBOOK")
                .required()
                .build() );

		options.addOption( Option.builder("p")
				.desc( "path to markdown documents inside git repository" )
                .hasArg()
                .argName("PATH")
                .required()
                .build() );

		options.addOption("s", false, "use Evernote sandbox");
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			
			token = cmd.getOptionValue("t");
			notebook = cmd.getOptionValue("n");
			path = cmd.getOptionValue("p");
					
			if ( cmd.hasOption("s") ) {
				sandbox = true;
			}
			
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "EvernoteMarkdownSync", options );
		}
		
		// Hier gehts weiter

	}

}
