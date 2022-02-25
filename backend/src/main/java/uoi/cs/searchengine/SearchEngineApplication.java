package uoi.cs.searchengine;

import uoi.cs.searchengine.lucene.Constants;
import uoi.cs.searchengine.lucene.IndexBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
public class SearchEngineApplication {

  private final Logger logger = LoggerFactory.getLogger(SearchEngineApplication.class);

  @EventListener(ApplicationReadyEvent.class)
  public void createLuceneIndexIfNotExists() {
    try {
      if (!DirectoryReader.indexExists(
          FSDirectory.open(Paths.get(Constants.INDEX_DIRECTORY_PATH)))) {
        logger.info(
            String.format(
                "Lucene Index was not present at directory: %s",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath()));
        logger.info(
            String.format(
                "Building Index at %s using %s",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath(),
                new File(Constants.CORPUS_PATH).getAbsolutePath()));

        new IndexBuilder(Constants.INDEX_DIRECTORY_PATH).build(Constants.CORPUS_PATH);
      } else {
        logger.info(
            String.format(
                "Lucene Index was present at directory: %s. Initiating normal startup.",
                new File(Constants.INDEX_DIRECTORY_PATH).getAbsolutePath()));
      }
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(SearchEngineApplication.class, args);
  }
}
