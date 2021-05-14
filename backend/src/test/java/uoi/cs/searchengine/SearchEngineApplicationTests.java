package uoi.cs.searchengine;

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

@SpringBootTest
class SearchEngineApplicationTests {

    @Autowired
    private SearchController searchController;

    @Autowired
    private ErrorHandlerController errorHandlerController;

    @Test
    public void contextLoads() {
        assertThat(searchController).isNotNull();
        assertThat(errorHandlerController).isNotNull();
    }

    @Test
    void loadArticles() throws IOException {
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
}
