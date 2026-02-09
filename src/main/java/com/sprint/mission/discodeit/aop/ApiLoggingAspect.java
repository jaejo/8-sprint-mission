package com.sprint.mission.discodeit.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
// @Component
// 추후에 사용하도록 하겠습니다.
public class ApiLoggingAspect {

  @Pointcut("execution(* com.sprint.mission.discodeit..controller..*(..))")
  public void controllerPointcut() {
  }

  @Around("controllerPointcut()")
  public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    String method = request.getMethod();
    String uri = request.getRequestURI();
    String methodName = joinPoint.getSignature().getName();

    log.info(" [API Request] {} {} | Handler: {}", method, uri, methodName);

    Object result = joinPoint.proceed();

    log.info(" [API Response] {} {} | Result: {}", method, uri, result);

    return result;
  }
}
