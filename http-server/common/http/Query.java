package common.http;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Query {
    private static final String QUERY_DELIMITER = "&";

    private final Map<String, List<String>> queries;

    public Query() {
        this.queries = new HashMap<>();
    }

    public Query(Map<String, List<String>> queries) {
        this.queries = new HashMap<>(queries);
    }


    public Optional<List<String>> get(String key) {
        return Optional.ofNullable(queries.get(key));
    }

    public void put(String key, String value) {
        queries.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public Map<String, List<String>> getQueries() {
        return queries;
    }


    @Override
    public String toString() {
        return queries.entrySet().stream()
                .flatMap(e -> e.getValue().stream()
                        .map(value -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                                      URLEncoder.encode(value, StandardCharsets.UTF_8)))
                .collect(Collectors.joining(QUERY_DELIMITER));
    }

    public boolean isEmpty() {
        return queries.isEmpty();
    }

    public static Query fromString(String query) {
        Query queries = new Query();
        String[] parts = query.split(QUERY_DELIMITER);
        for (String part : parts) {
            String[] keyValue = part.split("=", 2);
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                queries.put(key, value);
            }
        }
        return queries;
    }

}
