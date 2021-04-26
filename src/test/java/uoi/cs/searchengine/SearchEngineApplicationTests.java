package uoi.cs.searchengine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uoi.cs.searchengine.lucene.Builder;
import uoi.cs.searchengine.model.Article;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class SearchEngineApplicationTests {

    @Test
    void loadArticles() throws IOException {
        File file = new File("src/main/resources/data/data.json");
        String path = file.getAbsolutePath();
        ArrayList<Article> articles = new Builder().readJson(path);
        assert articles.size() == 500;
    }

}
