package uoi.cs.searchengine.lucene;

import uoi.cs.searchengine.ApplicationConstants;
import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.service.SearchService;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Searcher implements SearchService {

    private final Analyzer analyzer;
    private final Directory dir;
    private final IndexReader iReader;
    private final IndexSearcher iSearcher;

    public Searcher() throws IOException {
        this.analyzer = new DogeDogeGoAnalyzer();
        this.dir = FSDirectory.open(new File(ApplicationConstants.INDEX_PATH).toPath());
        this.iReader = DirectoryReader.open(dir);
        this.iSearcher = new IndexSearcher(iReader);
    }

    public ArrayList<Article> search(String q) throws IOException, ParseException {
        Query query = createQuery(q, ApplicationConstants.TEXT);

        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        ArrayList<Article> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String article_url = doc.getFields().get(0).stringValue();
            String article_title = doc.getFields().get(1).stringValue();
            String article_text = doc.getFields().get(2).stringValue();
            results.add(new Article(article_url, article_title, article_text));
        }
        return results;
    }

    public ArrayList<Article> searchAndHighlight(String q) throws IOException, ParseException, InvalidTokenOffsetsException {
        Query query = createQuery(q, ApplicationConstants.TEXT);

        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(
                new SimpleHTMLFormatter(ApplicationConstants.PRE_TAG, ApplicationConstants.POST_TAG), scorer);
        highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);

        ArrayList<Article> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String text = doc.get(ApplicationConstants.TEXT);
            //TokenStream tokenStream = analyzer.tokenStream(ApplicationConstants.TEXT, new StringReader(text));
            String[] bestFrag = highlighter.getBestFragments(analyzer, ApplicationConstants.TEXT, text, 4);
            String highlighted = "...".concat(String.join("...", bestFrag).concat("..."));
            results.add(new Article(doc.get(ApplicationConstants.URL), doc.get(ApplicationConstants.TITLE), highlighted));
        }
        return results;
    }

    private Query createQuery(String query, String field) throws ParseException {
        QueryParser parser = new QueryParser(field, analyzer);
        return parser.parse(query);
    }

    public void close() throws IOException {
        iReader.close();
        dir.close();
    }

    public static void main(String[] args) throws Exception {
        Searcher test = new Searcher();
        ArrayList<Article> res = test.searchAndHighlight("Kyriakos Mitsotakis");
        System.out.println(res.get(0));
    }
}
