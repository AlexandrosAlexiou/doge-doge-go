package uoi.cs.searchengine.lucene;

import org.apache.lucene.index.Term;
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
import java.util.regex.Pattern;

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
        if (isTitleQuery(q)) {
            return searchByTitle(createQuery(q, Constants.TITLE));
        }
        if (isWildcardQuery(q)) {
            return searchWithWildcard(new WildcardQuery(new Term(Constants.TEXT, q)));
        }
        if (isFuzzyQuery(q)) {
            return searchWithWildcard(new FuzzyQuery(new Term(Constants.TEXT, q)));
        }
        // default
        ArrayList<Article> results = searchByText(createQuery(q, Constants.TEXT));
        if (results.size() == 0) {
            SpellCheckerWrapper spellCheckerWrapper = new SpellCheckerWrapper();
            List<String> suggestions = spellCheckerWrapper.suggest(q);
            results = searchByText(createQuery(suggestions.get(0), Constants.TEXT));
        }
        return  results;
    }

    public ArrayList<Article> searchByText(Query query) throws IOException, InvalidTokenOffsetsException {
        HighlighterWrapper highlighterWrapper = new HighlighterWrapper(query);
        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        ArrayList<Article> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String text = doc.get(Constants.TEXT);
            String[] bestFrag = highlighterWrapper.getHighlighter().getBestFragments(analyzer, Constants.TEXT, text, 4);
            String highlighted = "...".concat(String.join("...", bestFrag).concat("..."));
            results.add(new Article(doc.get(Constants.URL), doc.get(Constants.TITLE), highlighted));
        }

        return results;
    }

    public ArrayList<Article> searchByTitle(Query query) throws IOException, InvalidTokenOffsetsException {

        HighlighterWrapper highlighterWrapper = new HighlighterWrapper(query);
        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        ArrayList<Article> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String title = doc.get(Constants.TITLE);
            String bestFrag = highlighterWrapper.getHighlighter().getBestFragment(
                    analyzer, Constants.TITLE, title);
            results.add(new Article(doc.get(Constants.URL), bestFrag, doc.get(Constants.TEXT)));
        }
        return results;
    }

    public ArrayList<Article> searchWithWildcard(Query query) throws IOException, InvalidTokenOffsetsException {
        HighlighterWrapper highlighterWrapper = new HighlighterWrapper(query);
        TopDocs hits = iSearcher.search(query, iReader.maxDoc());

        ArrayList<Article> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = iSearcher.doc(scoreDoc.doc);
            String text = doc.get(Constants.TEXT);
            String[] bestFrag = highlighterWrapper.getHighlighter().getBestFragments(analyzer, Constants.TEXT, text, 4);
            String highlighted = "...".concat(String.join("...", bestFrag).concat("..."));
            results.add(new Article(doc.get(Constants.URL), doc.get(Constants.TITLE), highlighted));
        }
        return results;
    }

    public boolean isTitleQuery(String q) {
        return Pattern.compile(Constants.TITLE_REGEX, Pattern.CASE_INSENSITIVE).matcher(q).matches();
    }

    public boolean isWildcardQuery(String q) {
        return Pattern.compile(Constants.WILDCARD_QUERY_REGEX, Pattern.CASE_INSENSITIVE).matcher(q).matches();
    }

    public boolean isFuzzyQuery(String q) {
        return Pattern.compile(Constants.FUZZY_QUERY_REGEX, Pattern.CASE_INSENSITIVE).matcher(q).matches();
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
