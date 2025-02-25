package common.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uri {
    private final String scheme;
    private final String host;
    private final int port;
    private final Path path;
    private final Query query;

    private Uri(String scheme, String host, int port, Path path, Query query) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
    }

    public static Uri of(String uri) {
        return UriParser.parse(uri);
    }

    public String scheme() {
        return scheme;
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public Path path() {
        return path;
    }

    public Query query() {
        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Uri uri = (Uri) o;
        return port == uri.port &&
                Objects.equals(scheme, uri.scheme) &&
                Objects.equals(host, uri.host) &&
                Objects.equals(path, uri.path) &&
                Objects.equals(query, uri.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheme, host, port, path, query);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(scheme).append("://").append(host);
        if (port != -1) sb.append(":").append(port);
        sb.append(path.toString());
        if (!query.isEmpty()) sb.append("?").append(query.toString());
        return sb.toString();
    }

    private static class UriParser {
        private static final Pattern URI_PATTERN = Pattern.compile(
            "^(?<scheme>[a-zA-Z][a-zA-Z\\d+.-]*)://(?<host>[^:/?#]+)(:(?<port>\\d+))?" +
            "(?<path>/[^?#]*)?(\\?(?<query>[^#]*))?$"
        );
        private static final String DEFAULT_PATH = "/";

        private UriParser() {}

        public static Uri parse(String uri) {
            Objects.requireNonNull(uri, "URI는 null이 될 수 없습니다.");
            Matcher matcher = URI_PATTERN.matcher(uri);

            if (!matcher.matches()) {
                throw new IllegalArgumentException("잘못된 URI 형식입니다: " + uri);
            }

            String scheme = matcher.group("scheme");
            String host = matcher.group("host");
            int port = matcher.group("port") != null ? Integer.parseInt(matcher.group("port")) : -1;
            String rawPath = matcher.group("path");
            Path path = Path.of(rawPath != null ? rawPath : DEFAULT_PATH);

            String rawQuery = matcher.group("query");
            Query query = parseQuery(rawQuery);

            return new Uri(scheme, host, port, path, query);
        }

        private static Query parseQuery(String rawQuery) {
            Query query = new Query();
            if (rawQuery == null || rawQuery.isBlank()) {
                return query;
            }

            String decodedQuery = URLDecoder.decode(rawQuery, StandardCharsets.UTF_8);
            String[] params = decodedQuery.split("&");

            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    query.put(keyValue[0], keyValue[1]);
                } else {
                    throw new IllegalArgumentException("잘못된 쿼리 파라미터 형식: " + param);
                }
            }
            return query;
        }
    }
}
