package request;

import common.http.HttpMethod;
import common.http.HttpStatusCode;
import common.http.HttpVersion;
import common.http.Uri;
import common.http.UriParser;
import format.Parser;

public class StartLineParser implements Parser<HttpStartLine> {
    @Override
    public HttpStartLine parse(String input) {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            return null;
        }
        HttpMethod method = HttpMethod.of(parts[0]);
        Uri uri = new UriParser().parse(parts[1]);
        HttpVersion version = new HttpVersion(parts[2]);
        
        return HttpStartLine.of(method, uri, version);
    }
}