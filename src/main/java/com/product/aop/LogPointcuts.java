package com.product.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogPointcuts {

    @Pointcut("execution(* com.product..*(..))")
    public void withinApplicationPackage() {
    }

    @Pointcut("!execution(* get*()) && !execution(void set*(*)) && !execution(* is*())")
    public void beanNoGetterSetter() {

    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void pointcutControllers() {
    }

    @Pointcut("within(@org.springframework.stereotype.Component *)")
    public void pointcutComponents() {
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void pointcutServices() {
    }


    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void pointcutRepository() {
    }

    @Pointcut("pointcutControllers() || pointcutComponents() || pointcutServices()")
    public void pointcutSpringBeans() {
    }

    @Pointcut("withinApplicationPackage() && (pointcutSpringBeans() || pointcutRepository() && beanNoGetterSetter())")
    public void combinedPointcut() {
    }

    @Pointcut("execution(reactor.core.publisher.Flux *(..))")
    public void pointcutFlux() {
    }

    @Pointcut("execution(reactor.core.publisher.Mono *(..))")
    public void pointcutMono() {
    }

    @Pointcut("pointcutMono() || pointcutFlux()")
    public void pointcutMonoAndFlux() {
    }

}
