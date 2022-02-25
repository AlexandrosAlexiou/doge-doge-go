package uoi.cs.searchengine.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  @Override
  @RequestMapping("/error")
  @ResponseBody
  public String getErrorPath() {
    return "<h2 style=\"text-align: center;\">404. That’s an error. "
        + "The requested URL was not found on this server. That’s all we know.</h2>";
  }

  @RequestMapping("/search")
  @ResponseBody
  public String searchPath() {
    return "<script>document.location.href=\"/\";</script>>";
  }
}
