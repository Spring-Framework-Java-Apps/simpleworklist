package org.woehlke.simpleworklist.common.error;


import lombok.extern.java.Log;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static javax.servlet.RequestDispatcher.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@Log
@Controller
@RequestMapping(path="/fehler")
public class ApplicationErrorController implements ErrorController {

    @ExceptionHandler
    @RequestMapping(path="/", method={ GET, POST, PUT,  HEAD, PATCH, DELETE, OPTIONS, TRACE  })
    public String handleError(
        HttpServletRequest request
    ) {
        log.info("handleError");
        String errorMessage = (String) request.getAttribute(ERROR_MESSAGE);
        if(errorMessage!=null){
            log.warning("errorMessage :"+errorMessage);
        }
        Integer statusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE);
        if(statusCode != null){
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
            log.warning(httpStatus.value()+""+httpStatus.getReasonPhrase());
            switch (httpStatus){
                case NOT_FOUND:
                    log.warning("##################################################");
                    log.warning("           404 NOT_FOUND");
                    log.warning("##################################################");
                    return "error/error-404";
                case INTERNAL_SERVER_ERROR:
                    log.warning("##################################################");
                    log.warning("           500 INTERNAL_SERVER_ERROR");
                    log.warning("##################################################");
                    return "error/error-500";
                case UNAUTHORIZED:
                    log.warning("##################################################");
                    log.warning("           401 UNAUTHORIZED");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case METHOD_NOT_ALLOWED:
                    log.warning("##################################################");
                    log.warning("          405 METHOD_NOT_ALLOWED");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case FORBIDDEN:
                    log.warning("##################################################");
                    log.warning("          403 FORBIDDEN");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case REQUEST_TIMEOUT:
                    log.warning("##################################################");
                    log.warning("          408 REQUEST_TIMEOUT");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case CONFLICT:
                    log.warning("##################################################");
                    log.warning("          409 CONFLICT");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case PRECONDITION_FAILED:
                    log.warning("##################################################");
                    log.warning("          412 PRECONDITION_FAILED");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case URI_TOO_LONG:
                    log.warning("##################################################");
                    log.warning("          414 URI_TOO_LONG");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
                case UNSUPPORTED_MEDIA_TYPE:
                    log.warning("##################################################");
                    log.warning("          415 UNSUPPORTED_MEDIA_TYPE");
                    log.warning("##################################################");
                    return "redirect:/login?login_error=1";
            }
        }
        Throwable exception = (Throwable) request.getAttribute(ERROR_EXCEPTION);
        if(exception != null) {
            log.warning("##################################################");
            log.warning("Exception :" + exception.getMessage());
            for (StackTraceElement elem : exception.getStackTrace()) {
                log.warning("Stacktrace: " + elem.getFileName() + ":+" + elem.getLineNumber() + " " + elem.getClassName() + "." + elem.getMethodName());
            }
            log.warning("##################################################");
        }
        return "error/error";
    }

    public String getErrorPath() {
        return "/fehler";
    }
}
