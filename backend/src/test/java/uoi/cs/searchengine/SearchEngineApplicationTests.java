package uoi.cs.searchengine;

import uoi.cs.searchengine.lucene.Constants;
import uoi.cs.searchengine.lucene.SpellCheckerWrapper;
import uoi.cs.searchengine.lucene.Searcher;
import uoi.cs.searchengine.model.Article;
import uoi.cs.searchengine.web.ErrorController;
import uoi.cs.searchengine.web.SearchController;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SearchEngineApplicationTests {

  @Autowired private SearchController searchController;
  @Autowired private ErrorController errorHandlerController;

  @Test
  public void contextLoadsTest() {
    assertThat(searchController).isNotNull();
    assertThat(errorHandlerController).isNotNull();
  }

  @Test
  void loadArticlesTest() throws IOException {
    ArrayList<Article> articles = new ArrayList<>();
    try (InputStream inputStream = Files.newInputStream(Paths.get(Constants.CORPUS_PATH));
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream)); ) {
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
    List<Article> results = searcher.search("Greece");
    assertThat(results.get(0).getTitle()).contains("Greece");
    assertThat(results.get(0).getText()).contains("Greece");

    results = searcher.search("India");
    assertThat(results.get(0).getTitle()).contains("India");
    assertThat(results.get(0).getText()).contains("India");
    searcher.close();
  }

  @Test
  public void suggestionsTest() throws IOException {
    SpellCheckerWrapper spellChecker = new SpellCheckerWrapper();
    List<String> suggestions = spellChecker.suggest("chldrne");
    assertThat(suggestions).contains("children");
  }
}
