package uoi.cs.searchengine.service;

import uoi.cs.searchengine.model.Article;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.Query;
import java.io.IOException;
import java.util.List;

public interface SearchService {

  List<Article> search(String q)
      throws IOException, ParseException, InvalidTokenOffsetsException;

  List<Article> searchByText(Query query)
      throws IOException, ParseException, InvalidTokenOffsetsException;
}
