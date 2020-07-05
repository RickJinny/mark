package com.rickjinny.mark.controller.p22_apidesign.t01_apiresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义一个 @RestControllerAdvice 来完成自动包装响应体的工作：
 * 1、通过实现 ResponseBoyAdvice 接口的 beforeBodyWrite 方法，来处理成功请求的响应体转换。
 * 2、实现一个 @ExceptionHandler 来处理业务异常时，APIException 到 APIResponse 的转换。
 *
 * 此段代码只是Demo, 生产级应用还需要扩展很多细节。
 */
@RestControllerAdvice
@Slf4j
public class APIResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 自动处理 APIException，包装为 APIResponse
     */
    @ExceptionHandler(APIException.class)
    public APIResponse<Object> handleAPIException(HttpServletRequest request, APIException ex) {
        log.error("process url {} failed", request.getRequestURL().toString(), ex);
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setCode(ex.getErrorCode());
        apiResponse.setMessage(ex.getErrorMessage());
        return apiResponse;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public APIResponse<Object> handleException(NoHandlerFoundException nx) {
        log.error(nx.getMessage(), nx);
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setCode(4000);
        apiResponse.setMessage(nx.getMessage());
        return apiResponse;
    }

    /**
     * 仅当方法或类没有标记 @NoAPIResponse 才自动包装。
     */
    @Override
    public boolean supports(MethodParameter returnType, Class convertType) {
        return returnType.getParameterType() != APIResponse.class
                && AnnotationUtils.findAnnotation(returnType.getMethod(), NoAPIResponse.class) == null
                && AnnotationUtils.findAnnotation(returnType.getDeclaringClass(), NoAPIResponse.class) == null;
    }

    /**
     * 自动包装外层 APIResponse 响应
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        APIResponse<Object> apiResponse = new APIResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("OK");
        apiResponse.setCode(2000);
        apiResponse.setData(body);
        if (body instanceof String) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return objectMapper.writeValueAsString(apiResponse);
        } else {
            return apiResponse;
        }
    }
}
