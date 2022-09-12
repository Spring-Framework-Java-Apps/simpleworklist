package org.woehlke.java.simpleworklist.domain;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static javax.servlet.RequestDispatcher.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@Slf4j
@Controller
@RequestMapping(path="/fehler")
public class ApplicationErrorController implements ErrorController {

    @ExceptionHandler
    @RequestMapping(path="", method={ GET, POST, PUT,  HEAD, PATCH, DELETE, OPTIONS, TRACE  })
    public String handleError(
        HttpServletRequest request
    ) {
        log.info("handleError");
        String errorMessage = (String) request.getAttribute(ERROR_MESSAGE);
        if(errorMessage!=null){
            log.warn("errorMessage :"+errorMessage);
        }
        Integer statusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE);
        if(statusCode != null){
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
            log.warn(httpStatus.value()+""+httpStatus.getReasonPhrase());
            switch (httpStatus){
                case NOT_FOUND:
                    log.warn("##################################################");
                    log.warn("           404 NOT_FOUND");
                    log.warn("##################################################");
                    return "error/error-404";
                case INTERNAL_SERVER_ERROR:
                    log.warn("##################################################");
                    log.warn("           500 INTERNAL_SERVER_ERROR");
                    log.warn("##################################################");
                    return "error/error-500";
                case UNAUTHORIZED:
                    log.warn("##################################################");
                    log.warn("           401 UNAUTHORIZED");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case METHOD_NOT_ALLOWED:
                    log.warn("##################################################");
                    log.warn("          405 METHOD_NOT_ALLOWED");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case FORBIDDEN:
                    log.warn("##################################################");
                    log.warn("          403 FORBIDDEN");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case REQUEST_TIMEOUT:
                    log.warn("##################################################");
                    log.warn("          408 REQUEST_TIMEOUT");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case CONFLICT:
                    log.warn("##################################################");
                    log.warn("          409 CONFLICT");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case PRECONDITION_FAILED:
                    log.warn("##################################################");
                    log.warn("          412 PRECONDITION_FAILED");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case URI_TOO_LONG:
                    log.warn("##################################################");
                    log.warn("          414 URI_TOO_LONG");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
                case UNSUPPORTED_MEDIA_TYPE:
                    log.warn("##################################################");
                    log.warn("          415 UNSUPPORTED_MEDIA_TYPE");
                    log.warn("##################################################");
                    return "redirect:/user/login?login_error=1";
            }
        }
        Throwable exception = (Throwable) request.getAttribute(ERROR_EXCEPTION);
        if(exception != null) {
            log.warn("##################################################");
            log.warn("Exception :" + exception.getMessage());
            for (StackTraceElement elem : exception.getStackTrace()) {
                log.warn("Stacktrace: " + elem.getFileName() + ":+" + elem.getLineNumber() + " " + elem.getClassName() + "." + elem.getMethodName());
            }
            log.warn("##################################################");
        }
        return "error/error";
    }

    public String getErrorPath() {
        return "/fehler";
    }
}
