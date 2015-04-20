package nlp_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class sets up and executes a thread pool to sentence
 * tokenizer a zip file containing text files. This code assumes
 * all text files are within the root directory of the zip file
 * and it processes all files.
 * 
 * @author Dave Turner
 *
 */
public class ZipProcessor {

	/*
	 * File of named entities to match. 
	 */
	protected String NER_FILE = "data/NER.txt";
	
	/*
	 * The name our our input zip file
	 */
	protected String zipFileName;
	
	/*
	 * A directory to output files too.
	 */
	protected String outputDir;
	
	/*
	 * The number of worker threads
	 */
	protected int numThreads;
	
	public ZipProcessor(String zipFileName, String outputDir, int numThreads)
	{
		this.zipFileName = zipFileName;
		this.outputDir = outputDir;
		this.numThreads = numThreads;
	}

	/**
	 * Execute the processing of the zip file
	 */
	public void process()
	{
		try
		{
			ZipFile zip = new ZipFile(zipFileName);		
			Enumeration<? extends ZipEntry> entries = zip.entries();
			
			// Create the output directory if needed.
			File outDir = new File(outputDir);
			outDir.mkdir();
			
			// Create our thread pool executor
			ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		        
			while(entries.hasMoreElements()){
				 ZipEntry entry = entries.nextElement();
				 
				 if(entry.isDirectory())
					 continue;
				 
				 // Ignore those weird MACOS copy files
				 if(entry.getName().startsWith("__MACOSX"))
					 continue;
			 
				 // Get the stream
				 InputStream stream = zip.getInputStream(entry);
				 
				 // Turn the stream into a string
				 String inputText = getStringFromInputStream(stream);
	
				 SentenceTokenizer tokenizer = new PunktFirstPassSentenceTokenizer();
						 	
				 // Create a named entity matcher
				 SimpleNamedEntityMatcher smatch = new SimpleNamedEntityMatcher(NER_FILE);
	 
				 File outFile = new File(outDir, entry.getName() + ".xml");
				 outFile.getParentFile().mkdirs();
				 
				 // Run our thread
				 SentenceTokenizerThread worker = 
						 new SentenceTokenizerThread(inputText, outFile.getPath(), tokenizer);
				 
				 worker.setNamedEntityMatcher(smatch);

			     executor.execute((Runnable)worker);
			}
		
			// Shutdown the executor and block till all threads
			// terminate.
			executor.shutdown();
	        while (!executor.isTerminated()) {}
	        System.out.println("Complete.");
			
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
		
	// Little function to convert and input stream to a string.
	// Source:
	// http://www.mkyong.com/java/how-to-convert-inputstream-to-string-in-java/
	private static String getStringFromInputStream(InputStream is) {
 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	
}
