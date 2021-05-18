package uoi.cs.searchengine;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import uoi.cs.searchengine.lucene.DogeDogeGoSpellChecker;
import uoi.cs.searchengine.lucene.Searcher;
import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.web.ErrorHandlerController;
import uoi.cs.searchengine.web.SearchController;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SearchEngineApplicationTests {

    @Autowired
    private SearchController searchController;

    @Autowired
    private ErrorHandlerController errorHandlerController;

    @Test
    public void contextLoadsTest() {
        assertThat(searchController).isNotNull();
        assertThat(errorHandlerController).isNotNull();
    }

    @Test
    void loadArticlesTest() throws IOException {
        ArrayList<Article> articles = new ArrayList<>();
        try (
                InputStream inputStream = Files.newInputStream(Paths.get(ApplicationConstants.CORPUS_PATH));
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            while (reader.hasNext()) {
                articles.add(new Gson().fromJson(reader, Article.class));
            }
            reader.endArray();
        }
        assert articles.size() == 500;
    }

    @Test
    public void searchTest() throws IOException, InvalidTokenOffsetsException, ParseException {
        Searcher searcher = new Searcher();
        ArrayList<Article> results = searcher.searchAndHighlight("Greece");
        assertThat(results.get(0).getTitle()).isEqualTo("COVID-19_pandemic_in_Greece");

        results = searcher.searchAndHighlight("India");
        assertThat(results.get(0).getTitle()).isEqualTo("COVID-19_pandemic_in_India");
        searcher.close();
    }

    @Test
    public void suggestionsTest() throws IOException{
        DogeDogeGoSpellChecker spellChecker = new DogeDogeGoSpellChecker();
        List<String> suggestions = spellChecker.suggest("chldrne");
        assertThat(suggestions).contains("children");
    }
}
