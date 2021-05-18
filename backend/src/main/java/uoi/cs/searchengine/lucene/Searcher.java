package uoi.cs.searchengine.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;
import uoi.cs.searchengine.ApplicationConstants;
import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.service.SearchService;

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

    private final Analyzer analyzer = new DogeDogeGoAnalyzer();
    private final Directory dir = FSDirectory.open(new File(ApplicationConstants.INDEX_PATH).toPath());

    public Searcher() throws IOException { }

    public ArrayList<Article> search(String q) throws IOException, ParseException {

        IndexReader iReader = DirectoryReader.open(dir);
        IndexSearcher iSearcher = new IndexSearcher(iReader);
        QueryParser parser = new QueryParser(ApplicationConstants.TEXT, this.analyzer);
        Query query = parser.parse(q);

        TopDocs docs = iSearcher.search(query, iReader.maxDoc());

        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0; i < docs.scoreDocs.length; i++) {
            Document doc = iSearcher.doc(docs.scoreDocs[i].doc);
            String article_url = doc.getFields().get(0).stringValue();
            String article_title = doc.getFields().get(1).stringValue();
            String article_text = doc.getFields().get(2).stringValue();
            results.add(new Article(article_url, article_title, article_text));
        }

        iReader.close();
        dir.close();
        return results;
    }

    public ArrayList<Article> searchAndHighlight(String q) throws IOException, ParseException, InvalidTokenOffsetsException {

        IndexReader iReader = DirectoryReader.open(dir);
        IndexSearcher iSearcher = new IndexSearcher(iReader);
        QueryParser parser = new QueryParser(ApplicationConstants.TEXT, analyzer);
        Query query = parser.parse(q);

        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(ApplicationConstants.PRE_TAG, ApplicationConstants.POST_TAG);
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        ArrayList<Article> results = new ArrayList<>();
        highlighter.setMaxDocCharsToAnalyze(Integer.MAX_VALUE);
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String text = doc.get(ApplicationConstants.TEXT);
            //TokenStream tokenStream = analyzer.tokenStream(ApplicationConstants.TEXT, new StringReader(text));
            String[] bestFrag = highlighter.getBestFragments(analyzer, ApplicationConstants.TEXT, text, 4);
            String highlighted = "...".concat(String.join("...", bestFrag).concat("..."));
            results.add(new Article(doc.get(ApplicationConstants.URL), doc.get(ApplicationConstants.TITLE), highlighted));
        }
        iReader.close();
        dir.close();
        return results;
    }

    public static void main(String[] args) throws Exception {
        Searcher tet = new Searcher();
        ArrayList<Article> res = tet.searchAndHighlight("Kyriakos Mitsotakis");
        System.out.println(res.get(0));
    }
}
