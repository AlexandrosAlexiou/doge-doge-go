package uoi.cs.searchengine.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class CustomAnalyzer extends Analyzer {

  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    // tokenization (Lucene's StandardTokenizer is suitable for most text retrieval occasions)
    TokenStreamComponents ts = new TokenStreamComponents(new StandardTokenizer());
    // transforming all tokens into lowercased ones (recommended for the majority of the problems)
    ts = new TokenStreamComponents(ts.getSource(), new LowerCaseFilter(ts.getTokenStream()));
    // remove stop words (unnecessary to remove stop words unless you can't afford the extra disk
    // space)
    ts =
        new TokenStreamComponents(
            ts.getSource(),
            new StopFilter(ts.getTokenStream(), EnglishAnalyzer.ENGLISH_STOP_WORDS_SET));
    // apply stemming
    ts = new TokenStreamComponents(ts.getSource(), new KStemFilter(ts.getTokenStream()));
    return ts;
  }
}
