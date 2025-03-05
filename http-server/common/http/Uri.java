package common.http;

import java.util.Objects;

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

    public static Uri of(String scheme, String host, int port, Path path, Query query) {
        return new Uri(scheme, host, port, path, query);
    }

    public static Uri parse(String uri) {
        return new UriParser().parse(uri);
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
}
