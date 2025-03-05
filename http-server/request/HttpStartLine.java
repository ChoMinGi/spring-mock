package request;

import common.http.HttpMethod;
import common.http.HttpVersion;
import common.http.Uri;

public class HttpStartLine {
    private final HttpMethod method;
    private final Uri uri;
    private final HttpVersion version;

    private HttpStartLine(HttpMethod method, Uri uri, HttpVersion version) {
        this.method = method;
        this.uri = uri;
        this.version = version;
    }

    public static HttpStartLine of(HttpMethod method, Uri uri, HttpVersion version) {
        return new HttpStartLine(method, uri, version);
    }
    
    public static HttpStartLine parse(String input) {
        return new StartLineParser().parse(input);
    }
}
