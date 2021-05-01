package uoi.cs.searchengine.service;

import org.apache.lucene.queryparser.classic.ParseException;
import uoi.cs.searchengine.model.Article;

import java.io.IOException;
import java.util.ArrayList;

public interface ResultsService {

    ArrayList<Article> search(String keyword) throws IOException, ParseException;

}
