package uoi.cs.searchengine.web;

import uoi.cs.searchengine.lucene.DogeDogeGoSpellChecker;
import uoi.cs.searchengine.lucene.Searcher;
import uoi.cs.searchengine.model.Article;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@Controller
@RequestMapping("/api/v1")
public class SearchController {

    @RequestMapping("/query")
    @ResponseBody
    public ArrayList<Article> search(@RequestParam String q) throws Exception {
        try {
            Searcher searcher = new Searcher();
            ArrayList<Article> results = searcher.searchAndHighlight(q);
            searcher.close();
            return results;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @RequestMapping("/suggestions")
    @ResponseBody
    public List<String> suggest(@RequestParam String t) {
        try {
            DogeDogeGoSpellChecker spellChecker = new DogeDogeGoSpellChecker();
            return spellChecker.suggest(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
