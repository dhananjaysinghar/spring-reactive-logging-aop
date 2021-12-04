package com.product.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService {

    public Mono<String> getProductName() {
        log.info("Getting Product info");
        return Mono.just("Welcome Gift");
    }
}
