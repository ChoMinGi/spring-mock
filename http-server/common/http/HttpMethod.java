package common.http;

import java.util.Arrays;
import java.util.Optional;

import common.exception.MethodNotAllowedException;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    CONNECT("CONNECT"),
    HEAD("HEAD");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod of(String method) {
        Optional<HttpMethod> optionalMethod = Arrays.stream(HttpMethod.values())
                .filter(e -> e.method.equals(method))
                .findAny();

        if (optionalMethod.isEmpty()) {
            throw new MethodNotAllowedException();
        }
        return optionalMethod.get();
    }

    public boolean isRequestBodyAcceptable() {
        return this != GET && this != HEAD && this != DELETE;
    }
}

