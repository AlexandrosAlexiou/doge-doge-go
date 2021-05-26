package uoi.cs.searchengine.lucene;

public class Constants {
    public static final String CORPUS_PATH = "src/main/resources/data/corpus.json";
    public static final String INDEX_PATH = "src/main/resources/Index";
    public static final String ENGLISH_DICTIONARY = "src/main/resources/Dictionary/dict.txt";

    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String TEXT = "text";

    public static final String PRE_TAG = "<b><font color='black'><mark>";
    public static final String POST_TAG = "</font></mark></b>";

    public static final String TITLE_REGEX = "^title:[\\s*a-zA-Z]+$";
    public static final String WILDCARD_QUERY_REGEX = "^[a-zA-Z]+\\*$";
    public static final String FUZZY_QUERY_REGEX = "^[a-zA-Z]+\\~$";
}
