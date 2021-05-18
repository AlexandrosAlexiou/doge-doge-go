package uoi.cs.searchengine.lucene;


import org.apache.lucene.analysis.Analyzer;
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

    private final Analyzer analyzer;
    private final Directory dir;
    private final IndexReader iReader;
    private final SpellChecker spellChecker;

    public DogeDogeGoSpellChecker() throws IOException {
        this.analyzer = new DogeDogeGoAnalyzer();
        this.dir = FSDirectory.open(new File(ApplicationConstants.INDEX_PATH).toPath());
        this.iReader = DirectoryReader.open(dir);
        this.spellChecker = new SpellChecker(dir);
    }

    public List<String> suggest(String input_word) throws IOException {
        //PlainTextDictionary txt_dict = new PlainTextDictionary(Paths.get(ApplicationConstants.ENGLISH_DICTIONARY));
        //spellChecker.indexDictionary(txt_dict, new IndexWriterConfig(new DogeDogeGoAnalyzer()), false);

        spellChecker.indexDictionary(
                new LuceneDictionary(iReader, ApplicationConstants.TEXT),
                new IndexWriterConfig(analyzer),
                true
        );

        // Searching and presenting the suggested words by selecting a string distance
        //checker.setStringDistance(new JaroWinklerDistance());
        //checker.setStringDistance(new LevenshteinDistance());
        //checker.setStringDistance(new LuceneLevenshteinDistance());
        spellChecker.setStringDistance(new NGramDistance());

        String[] suggestions = spellChecker.suggestSimilar(input_word, 10);

        iReader.close();
        dir.close();

        return Arrays.asList(suggestions);
    }


    public static void main(String[] args) throws Throwable {
        Scanner scan = new Scanner(System.in);
        DogeDogeGoSpellChecker spellChecker = new DogeDogeGoSpellChecker();
        System.out.print("\nType a word to spell check: ");
        String input_word = scan.next();
        System.out.println(spellChecker.suggest(input_word));
    }
}
