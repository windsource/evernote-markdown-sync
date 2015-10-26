package de.windwolke.EvernoteMarkdownSync.GitService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invokes git to retrieve information for a given repository
 *
 */
public class GitService {
	private File pathToDocuments;
	final Logger log = LoggerFactory.getLogger(GitService.class);

	/**
	 * @param pathToDocs path to the folder inside a git repository 
	 *        where the documents are stored that should be analyzed
	 */
	public GitService(String pathToDocs) {
		pathToDocuments = new File(pathToDocs);		
	}
	
	
	/**
	 * @return list of all objects in the path this service is responsible for 
	 * @throws GitException
	 */
	public List<GitObject> retrieveDocuments() throws GitException {
		List<GitObject> result = new ArrayList<GitObject>();

		// first retrieve the list of the objects
		Process p;
		try {
			String cmd[] = {"git", "ls-tree", "--name-only", "HEAD", "."}; 
			p = Runtime.getRuntime().exec(cmd, null, pathToDocuments);
			p.waitFor();
			if ( p.exitValue() != 0 ) {
				throw new GitException();
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;			
			while ((line = reader.readLine())!= null) {
				DateTime dt = getDateTimeForObject(line);
				result.add(new GitObject(line, dt));
				log.debug("Found file {} with date {}",line, ISODateTimeFormat.dateTime().print(dt));
			}
			reader.close();
		}
		catch (IOException|InterruptedException ex) {
			ex.printStackTrace();
			throw new GitException();
		}
		
		return result;
	}
	
	private DateTime getDateTimeForObject(String name) throws IOException, InterruptedException, GitException {
		DateTime result = null;
		
		Process p;
		String cmd[] = {"git", "log", "-1", "--format=%ad", "--date=iso8601-strict", name}; 
		p = Runtime.getRuntime().exec(cmd, null, pathToDocuments);
		p.waitFor();
		if ( p.exitValue() != 0 ) {
			throw new GitException();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line = reader.readLine();
		if (line != null) {
			result = new DateTime(line);
		}
		reader.close();
		
		return result;
	}

}
