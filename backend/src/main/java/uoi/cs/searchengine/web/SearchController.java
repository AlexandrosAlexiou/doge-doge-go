package uoi.cs.searchengine.web;

import org.springframework.beans.factory.annotation.Autowired;
import uoi.cs.searchengine.lucene.SpellCheckerWrapper;
import uoi.cs.searchengine.lucene.Searcher;
import uoi.cs.searchengine.model.Article;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000/")
public class SearchController {

  private final Searcher searcher;
  private final SpellCheckerWrapper spellCheckerWrapper;

  @Autowired
  public SearchController(Searcher searcher, SpellCheckerWrapper spellCheckerWrapper) {
    this.searcher = searcher;
    this.spellCheckerWrapper = spellCheckerWrapper;
  }

  @RequestMapping("/query")
  @ResponseBody
  public List<Article> search(@RequestParam String q) throws Exception {
    return searcher.search(q);
  }

  @RequestMapping("/suggestions")
  @ResponseBody
  public List<String> suggest(@RequestParam String t) throws IOException {
    return spellCheckerWrapper.suggest(t);
  }

  @PreDestroy
  public void destroy() throws IOException {
    searcher.close();
    spellCheckerWrapper.close();
  }
}
