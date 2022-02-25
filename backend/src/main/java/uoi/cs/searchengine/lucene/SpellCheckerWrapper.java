package uoi.cs.searchengine.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.NGramDistance;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class SpellCheckerWrapper {

  private final Analyzer analyzer;
  private final Directory dir;
  private final IndexReader iReader;
  private final SpellChecker spellChecker;

  public SpellCheckerWrapper() throws IOException {
    this.analyzer = new CustomAnalyzer();
    this.dir = FSDirectory.open(new File(Constants.INDEX_DIRECTORY_PATH).toPath());
    this.iReader = DirectoryReader.open(dir);
    this.spellChecker = new SpellChecker(dir);
  }

  public List<String> suggest(String input_word) throws IOException {
    // PlainTextDictionary txt_dict = new
    // PlainTextDictionary(Paths.get(ApplicationConstants.ENGLISH_DICTIONARY));
    // spellChecker.indexDictionary(txt_dict, new IndexWriterConfig(new DogeDogeGoAnalyzer()),
    // false);

    spellChecker.indexDictionary(
        new LuceneDictionary(iReader, Constants.TEXT), new IndexWriterConfig(analyzer), true);

    // Searching and presenting the suggested words by selecting a string distance
    // spellChecker.setStringDistance(new JaroWinklerDistance());
    // spellChecker.setStringDistance(new LevenshteinDistance());
    // spellChecker.setStringDistance(new LuceneLevenshteinDistance());
    spellChecker.setStringDistance(new NGramDistance());

    String[] suggestions = spellChecker.suggestSimilar(input_word, 10);

    return Arrays.asList(suggestions);
  }

  public void close() throws IOException {
    iReader.close();
    dir.close();
  }

  public static void main(String[] args) throws Throwable {
    Scanner scan = new Scanner(System.in);
    SpellCheckerWrapper spellChecker = new SpellCheckerWrapper();
    System.out.print("\nType a word to spell check: ");
    String input_word = scan.next();
    System.out.println(spellChecker.suggest(input_word));
  }
}
