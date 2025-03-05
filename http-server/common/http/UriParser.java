package common.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import format.Parser;

public class UriParser implements Parser<Uri>{
    private static final Pattern URI_PATTERN = Pattern.compile(
            "^(?<scheme>[a-zA-Z][a-zA-Z\\d+.-]*)://(?<host>[^:/?#]+)(:(?<port>\\d+))?" +
            "(?<path>/[^?#]*)?(\\?(?<query>[^#]*))?$"
        );

    @Override
    public Uri parse(String uri) {
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

        return Uri.of(scheme, host, port, path, query);
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
