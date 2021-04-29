package uoi.cs.searchengine.lucene;

import org.apache.lucene.index.IndexableField;
import uoi.cs.searchengine.ApplicationConstants;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.service.ResultsService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Searcher implements ResultsService {

    Analyzer analyzer = new DogeDogeGoAnalyzer();

    public Searcher() { }

    public ArrayList<Article> search(String query) throws IOException, ParseException {
        String field = "text"; // the field you hope to search for
        QueryParser parser = new QueryParser(field, this.analyzer); // a query parser that transforms a text string into Lucene's query object

        Query lucene_query = parser.parse(query); // this is Lucene's query object

        // Okay, now let's open an index and search for documents
        Directory dir = FSDirectory.open(new File(ApplicationConstants.INDEX_PATH).toPath());
        IndexReader index = DirectoryReader.open(dir);

        // you need to create a Lucene searcher
        IndexSearcher iSearcher = new IndexSearcher(index);

        int top = 500; // Let's just retrieve the talk 500 results
        TopDocs docs = iSearcher.search(lucene_query, top); // retrieve the top 10 results; retrieved results are stored in TopDocs

        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0; i < docs.scoreDocs.length; i++) {
           // results.add(iSearcher.doc(docs.scoreDocs[i].doc).getFields().get(1).stringValue());
            String article_url = iSearcher.doc(docs.scoreDocs[i].doc).getFields().get(0).stringValue();
            String article_title = iSearcher.doc(docs.scoreDocs[i].doc).getFields().get(1).stringValue();
            String article_text = iSearcher.doc(docs.scoreDocs[i].doc).getFields().get(2).stringValue();
            results.add(new Article(article_url, article_title, article_text));
        }

        // remember to close the index and the directory
        index.close();
        dir.close();

        return results;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Searcher tet = new Searcher();
        System.out.println(tet.search("india"));
    }
}
