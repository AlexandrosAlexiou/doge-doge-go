package uoi.cs.searchengine.lucene;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import uoi.cs.searchengine.model.Article;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Builder {

    public ArrayList<Article> readJson(String path) throws IOException {
        ArrayList<Article> articles = new ArrayList<>();
        try (
                InputStream inputStream = Files.newInputStream(Paths.get(path));
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            while (reader.hasNext()) {
                Article article = new Gson().fromJson(reader, Article.class);
                articles.add(article);
            }
            reader.endArray();
        }
        return articles;
    }
}
