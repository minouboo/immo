package studi.immo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorImmoController implements ErrorController {

    @GetMapping (value ="/error")
    public String handleError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null){
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()){
                return "ErreurLogementSuppression";
            }
            else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()){
                return "Erreur";
            }
        }
        return "Erreur";
    }


}
