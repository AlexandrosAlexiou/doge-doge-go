package uoi.cs.searchengine.lucene;

import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.service.SearchService;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

@Component
public class Searcher implements SearchService {

  private final Analyzer analyzer;
  private final Directory dir;
  private final IndexReader iReader;
  private final IndexSearcher iSearcher;
  private final Logger logger = LoggerFactory.getLogger(Searcher.class);

  public Searcher() throws IOException {
    createLuceneIndexIfNotExists();
    this.analyzer = new CustomAnalyzer();
    this.dir = FSDirectory.open(new File(Constants.INDEX_DIRECTORY_PATH).toPath());
    this.iReader = DirectoryReader.open(dir);
    this.iSearcher = new IndexSearcher(iReader);
  }

  private void createLuceneIndexIfNotExists() {
    try {
      File luceneIndexDirectory = new File(Constants.INDEX_DIRECTORY_PATH);
      if (!luceneIndexDirectory.exists() && luceneIndexDirectory.mkdir()) {
        logger.info(
            String.format(
                "Lucene Index Directory was not present and was created at %s",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath()));
        new IndexBuilder(Constants.INDEX_DIRECTORY_PATH).build(Constants.CORPUS_PATH);
        logger.info(
            String.format(
                "Lucene Index was built at %s using %s",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath(),
                new File(Constants.CORPUS_PATH).getAbsolutePath()));
        return;
      }
      if (!DirectoryReader.indexExists(
          FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY_PATH)))) {
        logger.info(
            String.format(
                "Lucene Index was not present at directory: %s",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath()));
        new IndexBuilder(Constants.INDEX_DIRECTORY_PATH).build(Constants.CORPUS_PATH);
        logger.info(
            String.format(
                "Lucene Index was built at %s using %s for the corpus",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath(),
                new File(Constants.CORPUS_PATH).getAbsolutePath()));
      } else {
        logger.info(
            String.format(
                "Lucene Index is present at directory: %s",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath()));
      }
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public List<Article> search(String q)
      throws InvalidTokenOffsetsException, IOException, ParseException {
    return searchByText(createQuery(q));
  }

  public List<Article> searchByText(Query query) throws IOException, InvalidTokenOffsetsException {
    logger.info(String.format("Searching by text with query: %s", query.toString()));
    HighlighterWrapper highlighterWrapper = new HighlighterWrapper(query);
    TopDocs hits = iSearcher.search(query, iReader.maxDoc());

    List<Article> results = new ArrayList<>();
    for (ScoreDoc scoreDoc : hits.scoreDocs) {
      Document doc = iSearcher.doc(scoreDoc.doc);
      String title = doc.get(Constants.TITLE);
      String bestFrag =
          highlighterWrapper.getHighlighter().getBestFragment(analyzer, Constants.TITLE, title);
      if (bestFrag == null) bestFrag = doc.get(Constants.TITLE);
      String text = doc.get(Constants.TEXT);
      String[] bestFrags =
          highlighterWrapper.getHighlighter().getBestFragments(analyzer, Constants.TEXT, text, 4);
      String highlighted = "...".concat(String.join("...", bestFrags).concat("..."));
      results.add(new Article(doc.get(Constants.URL), bestFrag, highlighted));
    }
    return results;
  }

  private Query createQuery(String query) throws ParseException {
    QueryParser parser = new QueryParser(Constants.TEXT, analyzer);
    return parser.parse(query);
  }

  public void close() throws IOException {
    iReader.close();
    dir.close();
    logger.info("Closed Lucene index reader");
  }

  public static void main(String[] args) throws Exception {
    Searcher test = new Searcher();
    List<Article> res = test.search("misinfor*");
    System.out.println(res);
  }
}
