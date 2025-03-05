package request;

import java.util.Map;

public class HttpRequestBody {
	private final String body;
	
	private HttpRequestBody(String body) {
		this.body = body;
	}
	
	public static HttpRequestBody of(String body) {
		return new HttpRequestBody(body);
	}
}
