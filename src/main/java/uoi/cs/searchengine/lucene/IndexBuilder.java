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
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IndexBuilder {

    private final IndexWriter iWriter;
    private final Directory directory;

    public IndexBuilder(String index_path) throws IOException {
        String path = new File(index_path).getAbsolutePath();
        this.directory = FSDirectory.open(new File(path).toPath());

        // Analyzer specifies options for text tokenization and normalization (e.g., stemming, stop words removal, case-folding)
        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String fieldName) {
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

    public void build(String path) throws IOException {

        // This is the field setting for url field (no tokenization, and stored).
        FieldType urlFieldType = new FieldType();
        urlFieldType.setOmitNorms(true);
        urlFieldType.setIndexOptions(IndexOptions.DOCS);
        urlFieldType.setStored(true);
        urlFieldType.setTokenized(false);
        urlFieldType.freeze();

        // This is the field setting for title text field (tokenized, searchable, store document vectors)
        FieldType titleFieldType  = new FieldType();
        titleFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        titleFieldType.setStoreTermVectors(true);
        titleFieldType.setStoreTermVectorPositions(true);
        titleFieldType.setTokenized(true);
        titleFieldType.setStored(true);
        titleFieldType.freeze();

        // This is the field setting for normal text field (tokenized, searchable, store document vectors)
        FieldType textFieldType  = new FieldType();
        textFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        textFieldType.setStoreTermVectors(true);
        textFieldType.setStoreTermVectorPositions(true);
        textFieldType.setTokenized(true);
        textFieldType.setStored(true);
        textFieldType.freeze();

        try (
                InputStream inputStream = Files.newInputStream(Paths.get(path));
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            while (reader.hasNext()) {
                // Create a Document object
                Document document = new Document();
                // Get document data from the json data file
                Article article = new Gson().fromJson(reader, Article.class);
                // Add each field to the document with the appropriate field type options
                document.add( new Field(ApplicationConstants.URL, article.getUrl(), urlFieldType));
                document.add( new Field(ApplicationConstants.TITLE, article.getTitle(), titleFieldType));
                document.add( new Field(ApplicationConstants.TEXT, article.getText(), textFieldType));
                // index it
                this.iWriter.addDocument(document);
            }
            reader.endArray();

            this.iWriter.close();
            this.directory.close();
        }
    }

    public static void main(String[] args) throws IOException {
        IndexBuilder iBuilder = new IndexBuilder(ApplicationConstants.INDEX_PATH);
        iBuilder.build(ApplicationConstants.CORPUS_PATH);
    }
}
