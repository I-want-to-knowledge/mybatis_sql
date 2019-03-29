package com.example.demo.controller;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.controller.ErrorCode.*;

/**
 * TODO
 *
 * @author YanZhen
 * @since 2019-03-29 17:13
 */
@Controller
public class ErrorController extends AbstractErrorController {

  @Resource
  private ServerProperties properties;

  private ErrorAttributes errorAttributes;

  public ErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
    super(errorAttributes, errorViewResolvers);
    this.errorAttributes = errorAttributes;
  }

  @Override
  public String getErrorPath() {
    return properties.getError().getPath();
  }

  @RequestMapping(produces = "text/plain")
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {

    response.setStatus(HttpServletResponse.SC_OK);
    Throwable error = errorAttributes.getError(new DispatcherServletWebRequest(request));
    Map<String, Object> errorAttributes = getErrorAttributes(request,
            isIncludeStackTrace(request, MediaType.TEXT_HTML_VALUE));
    HashMap<String, Object> errorMap = new HashMap<>(errorAttributes);
    errorMap.put("result", processErrorDetail(error, errorAttributes));
    return new ModelAndView("error/show", errorMap);
  }

  @ResponseBody
  @RequestMapping
  public Result error(HttpServletRequest request, HttpServletResponse response) {
    response.setStatus(HttpServletResponse.SC_OK);
    Throwable error = errorAttributes.getError(new DispatcherServletWebRequest(request));
    Map<String, Object> errorAttributes = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL_VALUE));

    return processErrorDetail(error, errorAttributes);
  }

  /**
   * 异常解析
   *
   * @param error           异常信息
   * @param errorAttributes 异常属性
   * @return 响应信息
   */
  private Result processErrorDetail(Throwable error, Map<String, Object> errorAttributes) {
    Integer status = (Integer) errorAttributes.get("status");
    if (status == 403) {
      return translateResult(HTTP_ACCESS_DENIED);
    }
    if (status == 404) {
      return translateResult(HTTP_HANDLER_NOT_FOUND);
    }
    if (error instanceof Exception) {
      System.err.println("捕捉异常");
    }
    return translateResult(SYSTEM_ERROR);
  }

  private Result translateResult(ErrorCode code) {
    return translateResult(code, null);
  }

  private Result translateResult(ErrorCode code, Object data) {
    return Result.failure(code.getCode(), code.getMessage());
  }

  /*private Result translateResult() {

    return null;
  }*/

  /**
   * 确定是否包含详细信息
   *
   * @param request       请求信息
   * @param textHtmlValue text/html
   * @return 是否包含详细信息
   */
  private boolean isIncludeStackTrace(HttpServletRequest request, String textHtmlValue) {
    ErrorProperties.IncludeStacktrace includeStacktrace = properties.getError().getIncludeStacktrace();
    if (includeStacktrace == ErrorProperties.IncludeStacktrace.ALWAYS) {
      return true;
    }
    if (includeStacktrace == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
      return getTraceParameter(request);
    }
    return false;
  }
}
