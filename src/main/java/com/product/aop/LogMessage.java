package com.product.aop;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage {
    private String className;
    private String methodName;
    private String methodArgs;
    private Long elapsedTimeInMillis;
    private Long elapsedTimeInMicros;
    private Object result;

    @SneakyThrows
    @Override
    public String toString() {
        return "{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodArgs='" + methodArgs + '\'' +
                ", elapsedTimeInMillis=" + elapsedTimeInMillis +
                ", elapsedTimeInMicros=" + elapsedTimeInMicros +
                ", result=" + new ObjectMapper().writeValueAsString(result) +
                '}';
    }
}
