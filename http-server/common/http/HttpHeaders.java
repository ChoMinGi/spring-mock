package common.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class HttpHeaders {
    public static final String ACCEPT = "Accept";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_DELIMITER = ": ";

    private final Map<String, String> headers;

    public HttpHeaders(BufferedReader br) {
        this.headers = new HashMap<>();
        try {
            parseHeaders(br);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse headers", e);
        }
    }

    public HttpHeaders(String headers) {
        this(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(headers.getBytes()))));
    }

    public HttpHeaders(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    public Optional<String> accept() {
        return get(ACCEPT);
    }

    public Optional<String> authorization() {
        return get(AUTHORIZATION);
    }

    public Optional<String> contentType() {
        return get(CONTENT_TYPE);
    }

    public Optional<String> contentLength() {
        return get(CONTENT_LENGTH);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void put(String key, String value) {
        this.headers.putIfAbsent(key, value);
    }

    public Map<String, String> all() {
        return new HashMap<>(this.headers);
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.isBlank()) {
            putHeader(line);
        }
    }

    private void putHeader(String line) {
        String[] keyValue = line.split(HEADER_DELIMITER, 2);
        if (keyValue.length == 2) {
            headers.put(keyValue[0].trim(), keyValue[1].trim());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHeaders that = (HttpHeaders) o;
        return Objects.equals(headers, that.headers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers);
    }
}
