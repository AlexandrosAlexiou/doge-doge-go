package uoi.cs.searchengine.model;

import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ToString
@AllArgsConstructor
public class Article {
  @Getter private final String url;
  @Getter private final String title;
  @Getter private final String text;
}
