package uoi.cs.searchengine.model;

public class Article {

    private final String url;
    private final String title;
    private final String text;

    public Article(String url, String title, String text) {
        this.url = url;
        this.title = title;
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Article{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
