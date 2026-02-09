package com.sprint.mission.discodeit.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
// @Component
// 추후에 사용하도록 하겠습니다.
public class ServiceLoggingAspect {

  @Pointcut("execution(* com.sprint.mission.discodeit..service..*(..))")
  public void servicePointcut() {
  }

  @Around("servicePointcut()")
  public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();

    log.debug(" [Service Start] {}.{} | Args: {}", className, methodName, args);

    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long endTime = System.currentTimeMillis();

    log.debug(" [Service End] {}.{} | Time: {}ms", className, methodName, endTime - startTime);

    return result;
  }
}
