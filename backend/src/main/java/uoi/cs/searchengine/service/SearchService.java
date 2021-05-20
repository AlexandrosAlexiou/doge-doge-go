package uoi.cs.searchengine.service;

import uoi.cs.searchengine.model.Article;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.ArrayList;

public interface SearchService {

    ArrayList<Article> search(String keyword) throws IOException, ParseException;

    ArrayList<Article> searchAndHighlight(String keyword) throws IOException, ParseException, InvalidTokenOffsetsException;

}
