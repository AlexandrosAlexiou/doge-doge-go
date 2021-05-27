package uoi.cs.searchengine.service;

import org.apache.lucene.search.Query;
import uoi.cs.searchengine.model.Article;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.ArrayList;

public interface SearchService {

    ArrayList<Article> search(String q) throws IOException, ParseException, InvalidTokenOffsetsException;

    ArrayList<Article> searchByText(Query query) throws IOException, ParseException, InvalidTokenOffsetsException;

}
