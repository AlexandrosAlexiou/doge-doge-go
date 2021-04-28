package uoi.cs.searchengine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uoi.cs.searchengine.lucene.IndexBuilder;
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
        ArrayList<Article> articles = new IndexBuilder(ApplicationConstants.INDEX_PATH).build(ApplicationConstants.CORPUS_PATH);
        assert articles.size() == 500;
    }
}
