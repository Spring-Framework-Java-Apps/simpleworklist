package org.woehlke.java.simpleworklist.domain;


import lombok.extern.java.Log;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;


import static jakarta.servlet.RequestDispatcher.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@Log
@Controller
@RequestMapping(path="/fehler")
public class ApplicationErrorController implements ErrorController {

    @ExceptionHandler
    @RequestMapping(path="", method={ GET, POST, PUT,  HEAD, PATCH, DELETE, OPTIONS, TRACE })
    public String handleError(
        HttpServletRequest request
    ) {
        String errorMessage = (String) request.getAttribute(ERROR_MESSAGE);
        if(errorMessage!=null){
            errorMessage = errorMessage.strip();
            if(!errorMessage.isEmpty()) {
              log.info("handleError - errorMessage: " + errorMessage);
            }
        }
        Integer statusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE);
        if(statusCode != null){
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
            log.info("handleError - HTTP Code: " +httpStatus.value()+" "+httpStatus.getReasonPhrase());
            String redirectLoginPageWithError = "redirect:/user/login?login_error=1";
            switch (httpStatus){
                case NOT_FOUND:
                    log.info("##################################################");
                    log.info("#          404 NOT_FOUND                         #");
                    log.info("##################################################");
                    return "error/error-404";
                case INTERNAL_SERVER_ERROR:
                    log.info("##################################################");
                    log.info("#          500 INTERNAL_SERVER_ERROR             #");
                    log.info("##################################################");
                    return "error/error-500";
                case UNAUTHORIZED:
                    log.info("##################################################");
                    log.info("#          401 UNAUTHORIZED                      #");
                    log.info("##################################################");
                    return redirectLoginPageWithError;
                case METHOD_NOT_ALLOWED:
                    log.info("##################################################");
                    log.info("#         405 METHOD_NOT_ALLOWED                 #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
                case FORBIDDEN:
                    log.info("##################################################");
                    log.info("#         403 FORBIDDEN                          #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
                case REQUEST_TIMEOUT:
                    log.info("##################################################");
                    log.info("#         408 REQUEST_TIMEOUT                    #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
                case CONFLICT:
                    log.info("##################################################");
                    log.info("#         409 CONFLICT                           #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
                case PRECONDITION_FAILED:
                    log.info("##################################################");
                    log.info("#         412 PRECONDITION_FAILED                #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
                case URI_TOO_LONG:
                    log.info("##################################################");
                    log.info("#         414 URI_TOO_LONG                       #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
                case UNSUPPORTED_MEDIA_TYPE:
                    log.info("##################################################");
                    log.info("#         415 UNSUPPORTED_MEDIA_TYPE             #");
                    log.info("##################################################");
                  return redirectLoginPageWithError;
            }
        }
        Throwable exception = (Throwable) request.getAttribute(ERROR_EXCEPTION);
        if(exception != null) {
            log.info("##################################################");
            log.info("Exception :" + exception.getMessage());
            for (StackTraceElement elem : exception.getStackTrace()) {
                log.info("Stacktrace: " + elem.getFileName() + ":+" + elem.getLineNumber() + " " + elem.getClassName() + "." + elem.getMethodName());
            }
            log.info("##################################################");
        }
        return "error/error";
    }

    public String getErrorPath() {
        return "/fehler";
    }
}
