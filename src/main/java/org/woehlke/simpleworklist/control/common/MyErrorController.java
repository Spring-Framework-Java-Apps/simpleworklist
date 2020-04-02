package org.woehlke.simpleworklist.control.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/fehler")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(statusCode!=null){
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
            switch (httpStatus){
                case NOT_FOUND:
                    return "error/error-404";
                case INTERNAL_SERVER_ERROR:
                    return "error/error-500";
            }
        }
        String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        log.warn("errorMessage :"+errorMessage);
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        log.warn("errorMessage :"+exception.getMessage());
        for(StackTraceElement elem:exception.getStackTrace()){
            log.warn(elem.getFileName()+":+"+elem.getLineNumber()+" "+elem.getClassName()+"."+elem.getMethodName());
        }
        return "error/error";
    }

    @Override
    public String getErrorPath() {
        return "/fehler";
    }
}
