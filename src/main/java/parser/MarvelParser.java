package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MarvelParser {

	/** @param: filename The path to the "CSV" file that contains the <hero, book> pairs                                                                                                
        @param: charsInBooks The Map that stores parsed <book, Set-of-heros-in-book> pairs;
        	    usually an empty Map
        @param: chars The Set that stores parsed characters; usually an empty Set.
        @effects: adds parsed <book, Set-of-heros-in-book> pairs to Map charsInBooks;
        		  adds parsed characters to Set chars
        @throws: IOException if file cannot be read of file not a CSV file                                                                                     
	 */
    public static void readData(String filename, Map<String,Set<String>> charsInBooks, Set<String> chars) 
    		throws IOException {

    	@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;

        while ((line = reader.readLine()) != null) {
             int i = line.indexOf("\",\"");
             if ((i == -1) || (line.charAt(0)!='\"') || (line.charAt(line.length()-1)!='\"')) {
            	 throw new IOException("File "+filename+" not a CSV (\"HERO\",\"BOOK\") file.");
             }             
             String character = line.substring(1,i);
             String book = line.substring(i+3,line.length()-1);
             
             // Adds the character to the character set. If character already in, add has no effect.
             chars.add(character);

             // Adds the character to the set for book
             Set<String> s = charsInBooks.get(book);
             if (s == null) {
               s = new HashSet<String>();
               charsInBooks.put(book,s);
             }
             s.add(character);
        }
    }

}
