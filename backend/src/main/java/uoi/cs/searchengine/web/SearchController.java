package uoi.cs.searchengine.web;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uoi.cs.searchengine.lucene.Searcher;
import uoi.cs.searchengine.model.Article;

import java.io.IOException;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:3000/")
@Controller
@RequestMapping("/api/v1")
public class SearchController {

    @RequestMapping("/query/{term}")
    @ResponseBody
    public ArrayList<Article> search(@PathVariable String term) throws Exception {
        try{
            Searcher iSearcher = new Searcher();
             return iSearcher.searchAndHighlight(term);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
