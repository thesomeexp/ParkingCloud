package cn.edu.lingnan.exception;

import cn.edu.lingnan.entity.User;
import cn.edu.lingnan.util.MagicVariable;
import cn.edu.lingnan.util.Result;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

// 这类错误在别的微服务调用Feign时不会返回这个错误, 而是直接调用时才会返回(只会执行, 不会触发Result的返回), 所以还是应该用HystrixCommand
@RestController
@ControllerAdvice
public class GloabalExceptionHandler {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private void loggerUser(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null)
            logger.error(MagicVariable.LOGGER_USER + user.getId());
        logger.error(req.getRequestURI());
        Enumeration<String> headerNames=req.getHeaderNames();
        for(Enumeration<String> e = headerNames; e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=req.getHeader(thisName);
            logger.error("header的key:"+thisName+"--------------header的value:"+thisValue);
        }
        Enumeration<String> paraNames=req.getParameterNames();
        for(Enumeration<String> e=paraNames;e.hasMoreElements();){
            String thisName=e.nextElement().toString();
            String thisValue=req.getParameter(thisName);
            logger.error("param的key:"+thisName+"--------------param的value:"+thisValue);
        }
    }

    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) {
        loggerUser(req);
        logger.error(MagicVariable.UNKNOW_ERROR, e);
        return Result.fail(MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public Result nullPointerExceptionErrorHandler(HttpServletRequest req, NullPointerException e) {
        loggerUser(req);
        System.out.println("执行了该内容000");
        // 在控制台打印错误信息
        logger.error("空指针异常11", e);
        return Result.fail("空指针异常11");
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public Result nullPointerExceptionErrorHandler(HttpServletRequest req, ArithmeticException e) {
        loggerUser(req);
        // 在控制台打印错误信息
        logger.error("算数异常", e);
        return Result.fail("算数异常");
    }


    @ExceptionHandler(value = NumberFormatException.class)
    public Object numberFormatExceptionErrorHandler(HttpServletRequest req, NumberFormatException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_NUMBER_FORMAT_EXCEPTION, e);
        return Result.fail(MagicVariable.LOGGER_NUMBER_FORMAT_EXCEPTION + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Object numberFormatExceptionErrorHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MISSING_PARAMETER, e);
        return Result.fail(MagicVariable.LOGGER_MISSING_PARAMETER + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = NoSuchFieldException.class)
    public Object NoSuchFieldException(HttpServletRequest req, MissingServletRequestParameterException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MISSING_PARAMETER, e);
        return Result.fail(MagicVariable.LOGGER_MISSING_PARAMETER + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = IllegalAccessException.class)
    public Object IllegalAccessException(HttpServletRequest req, MissingServletRequestParameterException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MISSING_PARAMETER, e);
        return Result.fail(MagicVariable.LOGGER_MISSING_PARAMETER + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public Object DataIntegrityViolationException(HttpServletRequest req, DataIntegrityViolationException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_DATA_INTEGRITY_VIOLATION_EXCEPTION, e);
        return Result.fail(MagicVariable.LOGGER_DATA_INTEGRITY_VIOLATION_EXCEPTION + MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = MySQLIntegrityConstraintViolationException.class)
    public Object MySQLIntegrityConstraintViolationException(HttpServletRequest req, MySQLIntegrityConstraintViolationException e) {
        loggerUser(req);
        logger.error(MagicVariable.LOGGER_MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION, e);
        return Result.fail(MagicVariable.LOGGER_MYSQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION +
                MagicVariable.LOGGER_ERROR_MESSAGE);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public Object HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Result.fail(MagicVariable.REQUEST_METHOD_NOT_SUPPORTED);
    }

}