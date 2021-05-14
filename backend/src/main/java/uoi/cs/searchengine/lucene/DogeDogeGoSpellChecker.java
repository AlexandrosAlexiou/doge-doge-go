package uoi.cs.searchengine.lucene;


import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import uoi.cs.searchengine.ApplicationConstants;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DogeDogeGoSpellChecker {


    public List<String> suggest(String input_word) throws IOException {
        // Creating the index
        Directory directory = FSDirectory.open(new File(ApplicationConstants.INDEX_PATH).toPath());
        //PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get(ApplicationConstants.ENGLISH_DICTIONARY));
        SpellChecker checker = new SpellChecker(directory);
        IndexReader reader = DirectoryReader.open(directory);
        //checker.indexDictionary(txt_dict, new IndexWriterConfig(new DogeDogeGoAnalyzer()), false);
        checker.indexDictionary(
                new LuceneDictionary(reader, ApplicationConstants.TEXT),
                new IndexWriterConfig(new DogeDogeGoAnalyzer()),
                true
        );
        directory.close();

        // Searching and presenting the suggested words by selecting a string distance
        //checker.setStringDistance(new JaroWinklerDistance());
        //checker.setStringDistance(new LevenshteinDistance());
        //checker.setStringDistance(new LuceneLevenshteinDistance());
        checker.setStringDistance(new NGramDistance());

        String[] suggestions = checker.suggestSimilar(input_word, 10);

        // System.out.println("By '" + input_word + "' did you mean:");
        return Arrays.asList(suggestions);
    }


    public static void main(String[] args) throws IOException, Throwable {
        Scanner scan = new Scanner(System.in);
        DogeDogeGoSpellChecker spellChecker = new DogeDogeGoSpellChecker();
        System.out.print("\nType a word to spell check: ");
        String input_word = scan.next();
        System.out.println(spellChecker.suggest(input_word));
    }
}
