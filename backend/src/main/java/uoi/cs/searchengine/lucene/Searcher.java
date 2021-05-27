package uoi.cs.searchengine.lucene;

import org.apache.lucene.search.*;
import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.service.SearchService;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Searcher implements SearchService {

    private final Analyzer analyzer;
    private final Directory dir;
    private final IndexReader iReader;
    private final IndexSearcher iSearcher;

    public Searcher() throws IOException {
        this.analyzer = new CustomAnalyzer();
        this.dir = FSDirectory.open(new File(Constants.INDEX_PATH).toPath());
        this.iReader = DirectoryReader.open(dir);
        this.iSearcher = new IndexSearcher(iReader);
    }

    public ArrayList<Article> search(String q) throws InvalidTokenOffsetsException, IOException, ParseException {
        return searchByText(createQuery(q, Constants.TEXT));
    }

    public ArrayList<Article> searchByText(Query query) throws IOException, InvalidTokenOffsetsException {
        HighlighterWrapper highlighterWrapper = new HighlighterWrapper(query);
        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        ArrayList<Article> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String title = doc.get(Constants.TITLE);
            String bestFrag = highlighterWrapper.getHighlighter().getBestFragment(analyzer, Constants.TITLE, title);
            if (bestFrag == null) {
                bestFrag = doc.get(Constants.TITLE);
            }
            String text = doc.get(Constants.TEXT);
            String[] bestFrags = highlighterWrapper.getHighlighter().getBestFragments(analyzer, Constants.TEXT, text, 4);
            String highlighted = "...".concat(String.join("...", bestFrags).concat("..."));
            results.add(new Article(doc.get(Constants.URL), bestFrag, highlighted));
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
        ArrayList<Article> res = test.search("misinfor*");
        System.out.println(res);
    }
}
