package com.dominest.dominestbackend.global.exception.handler;

import com.dominest.dominestbackend.global.exception.dto.ErrorResponseDto;
import com.dominest.dominestbackend.global.exception.exceptions.AppServiceException;
import com.dominest.dominestbackend.global.exception.exceptions.BusinessException;
import com.dominest.dominestbackend.global.util.LoggingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @ModelAttribute 으로 binding error 발생시 BindException 발생한다.
     * @RequestBody @Valid 바인딩 오류(HttpMessageConverter binding) 시 발생하는 MethodArgumentNotValidException 도 BindException 을 확장한다.
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto<Map<String, String>>> handleBindException(BindException e, HttpServletRequest request) {
        printLog(e, request);

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        StringBuilder sb = new StringBuilder();
        Map<String, String> errorInfoMap = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String errorMsg = sb
                    .append(fieldError.getDefaultMessage())
                    .append(". 요청받은 값: ")
                    .append(fieldError.getRejectedValue())
                    .toString();

            errorInfoMap.put(fieldError.getField(), errorMsg);

            sb.setLength(0);
        }

        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, errorInfoMap);
    }

    /** @RequestParam 파라미터 누락*/
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto<String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        printLog(e, request);
        String message = "파라미터 '" + e.getParameterName() + "'이 누락되었습니다.";
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, message);
    }

    // BusinessException 을 상속한 다른 Custom Exception 에도 적용된다.
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponseDto<String>> handleBusinessException(IllegalArgumentException e, HttpServletRequest request){
        printLog(e, request);
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // BusinessException 을 상속한 다른 Custom Exception 에도 적용된다.
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ErrorResponseDto<String>> handleBusinessException(BusinessException e, HttpServletRequest request){
        printLog(e, request);
        return createErrorResponse(e.getStatusCode(), e.getHttpStatus(), e.getMessage());
    }

    // 비즈니스 로직이 아닌 애플리케이션 서비스 로직상 예외
    @ExceptionHandler({AppServiceException.class})
    public ResponseEntity<ErrorResponseDto<String>> handleAppServiceException(AppServiceException e, HttpServletRequest request){
        printLog(e, request);
        return createErrorResponse(e.getStatusCode(), e.getHttpStatus(), e.getMessage());
    }

    // 예상하지 못한 예외 발생 시, 예외 로그 전체를 서버에 남기고, 로그 자체를 모두 클라이언트에 전송한다.
    // TODO 실제 서비스 시  전체 로그 클라이언트에 전송하지 않는다.
    //  즉 CreateErrorResponseDto 에서 stackTrace 를 빼고 getMessage 정도만 보낸다.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto<String>> handleException(Exception e, HttpServletRequest request){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("예외처리 범위 외의 오류 발생. " + httpStatus.toString()); // enum.name() enum.toString() 차이 기억하자. name은 단순 문자열변환, toString은 오버라이딩된 메서드 호출
        printLog(e, request);
        e.printStackTrace(); // 콘솔에 StackTrace 남기기
        String stackTrace = LoggingUtil.stackTraceToString(e);// 클라 반환용 StackTrace 생성

        return createErrorResponse(httpStatus.value(), httpStatus, e.getMessage() +", " + stackTrace);
    }

    private <T> ResponseEntity<ErrorResponseDto<T>> createErrorResponse(int statusCode, HttpStatus httpStatus, T errorMessage) {
        ErrorResponseDto<T> errDto = new ErrorResponseDto<>(statusCode, httpStatus, errorMessage);
        return ResponseEntity.status(httpStatus).body(errDto);
    }

    private void printLog(Exception e, HttpServletRequest request) {
        log.error(e.getClass().getSimpleName() + ": 발생, 에러 메시지: "+ e.getMessage() + ",요청 METHOD " + request.getMethod() + ", 요청 url + " + request.getRequestURI());
    }


}

















