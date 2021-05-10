package uoi.cs.searchengine.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandlerController implements ErrorController{

    @Override
    @RequestMapping("/error")
    @ResponseBody
    public String getErrorPath() {
        return "<h2 style=\"text-align: center;\">404. That’s an error. The requested URL was not found on this server. That’s all we know.</h2>";
    }
}
