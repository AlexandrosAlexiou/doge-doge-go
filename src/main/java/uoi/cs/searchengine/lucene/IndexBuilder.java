package uoi.cs.searchengine.lucene;

import uoi.cs.searchengine.ApplicationConstants;
import uoi.cs.searchengine.model.Article;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class IndexBuilder {

    private final IndexWriter iWriter;
    private final Directory directory;

    public IndexBuilder(String index_path) throws IOException {
        String path = new File(index_path).getAbsolutePath();
        this.directory = FSDirectory.open(new File(path).toPath());

        // Analyzer specifies options for text tokenization and normalization (e.g., stemming, stop words removal, case-folding)
        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents( String fieldName ) {
                // tokenization (Lucene's StandardTokenizer is suitable for most text retrieval occasions)
                TokenStreamComponents ts = new TokenStreamComponents(new StandardTokenizer());
                // transforming all tokens into lowercased ones (recommended for the majority of the problems)
                ts = new TokenStreamComponents( ts.getSource(), new LowerCaseFilter(ts.getTokenStream()));
                // remove stop words (unnecessary to remove stop words unless you can't afford the extra disk space)
                ts = new TokenStreamComponents( ts.getSource(), new StopFilter(ts.getTokenStream(), EnglishAnalyzer.ENGLISH_STOP_WORDS_SET ));
                // apply stemming
                ts = new TokenStreamComponents( ts.getSource(), new KStemFilter(ts.getTokenStream()));
                return ts;
            }
        };

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // IndexWriterConfig.OpenMode.CREATE will override the original index in the folder
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        this.iWriter = new IndexWriter(directory, config);
    }

    public ArrayList<Article> build(String path) throws IOException {

        // This is the field setting for metadata field (no tokenization, and stored).
        FieldType url = new FieldType();
        url.setOmitNorms(true);
        url.setIndexOptions(IndexOptions.DOCS);
        url.setStored(true);
        url.setTokenized(false);
        url.freeze();

        // This is the field setting for title text field (tokenized, searchable, store document vectors)
        FieldType title = new FieldType();
        title.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        title.setStoreTermVectors(true);
        title.setStoreTermVectorPositions(true);
        title.setTokenized(true);
        title.setStored(true);
        title.freeze();

        // This is the field setting for normal text field (tokenized, searchable, store document vectors)
        FieldType text = new FieldType();
        text.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        text.setStoreTermVectors(true);
        text.setStoreTermVectorPositions(true);
        text.setTokenized(true);
        text.setStored(true);
        text.freeze();

        ArrayList<Article> articles = new ArrayList<>();
        try (
                InputStream inputStream = Files.newInputStream(Paths.get(path));
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            while (reader.hasNext()) {
                Article article = new Gson().fromJson(reader, Article.class);
                articles.add(article);
            }
            reader.endArray();
        }
        return articles;
    }

    public void close() throws IOException {
        this.iWriter.close();
        this.directory.close();
    }

    public static void main(String[] args) throws IOException {
        IndexBuilder builder = new IndexBuilder(ApplicationConstants.INDEX_PATH);
    }
}
