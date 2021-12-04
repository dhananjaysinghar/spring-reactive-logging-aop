package com.product.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(0)
@Aspect
@Configuration
@Slf4j
public class LogAspect {


    @Around(value = "com.product.aop.LogPointcuts.combinedPointcut()")
    public Object calculateMethodTimeAdvice(ProceedingJoinPoint joinPoint) {
        return logMethodTiming(joinPoint);
    }

    @SneakyThrows
    private Object logMethodTiming(ProceedingJoinPoint joinPoint) {
        final Logger classLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        if (!classLogger.isDebugEnabled()) {
            return joinPoint.proceed();
        }

        String className = ((MethodSignature) joinPoint.getSignature()).getMethod()
                .getDeclaringClass()
                .getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        long startTimer = System.nanoTime();
        Object result = joinPoint.proceed();
        long elapsedTime = System.nanoTime() - startTimer;
        try {
            classLogger.debug("LogAspect : {}", LogMessage.builder()
                    .className(className)
                    .methodName(methodName)
                    .methodArgs(Stream.of(joinPoint.getArgs()).collect(Collectors.toList()).toString())
                    .elapsedTimeInMillis(TimeUnit.NANOSECONDS.toMillis(elapsedTime))
                    .elapsedTimeInMicros(TimeUnit.NANOSECONDS.toMicros(elapsedTime))
                    .result(result)
                    .build());
        } catch (Exception ex) {
            log.error("Unexpected error occurred", ex);
        }
        return result;
    }


    @Around(value = "com.product.aop.LogPointcuts.combinedPointcut() " +
            "&& com.product.aop.LogPointcuts.pointcutMonoAndFlux()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            LogMessage logMessage = LogMessage.builder()
                    .className(joinPoint.getTarget().getClass().getTypeName())
                    .methodName(((MethodSignature) joinPoint.getSignature()).getMethod().getName())
                    .build();
            if (LoggerFactory.getLogger(joinPoint.getTarget().getClass()).isDebugEnabled()) {
                if (joinPoint.proceed() instanceof Mono) {
                    return ((Mono) joinPoint.proceed()).log(logMessage.toString(), Level.FINE, false, SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_COMPLETE);
                }
                if (joinPoint.proceed() instanceof Flux) {
                    return ((Flux) joinPoint.proceed()).log(logMessage.toString(), Level.FINE, false, SignalType.ON_NEXT, SignalType.ON_SUBSCRIBE, SignalType.ON_COMPLETE);
                }
            }
        } catch (Exception ex) {
            log.error("Unexpected error occurred", ex);
        }
        return joinPoint.proceed();
    }
}
