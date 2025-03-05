package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import common.http.HttpHeaders;

public class HttpRequest {
	private final HttpStartLine startLine;
	private final HttpHeaders headers;
	private final HttpRequestBody body;

	private HttpRequest(HttpStartLine startLine, HttpHeaders headers, HttpRequestBody body) {
		this.startLine = startLine;
		this.headers = headers;
		this.body = body;
	}

	public static HttpRequest of(HttpStartLine startLine, HttpHeaders headers, HttpRequestBody body) {
		return new HttpRequest(startLine, headers, body);
	}
	
	public static HttpRequest parse(BufferedReader reader) throws IOException {
		// 1. Start Line 파싱
		String rawStartLine = reader.readLine();
		HttpStartLine startLine = new StartLineParser().parse(rawStartLine);
		
		// 2. Headers 파싱
		HttpHeaders headers = new HttpHeaders(reader);
		
		// 3. Body (빈칸 이후의 모든 내용) 파싱
		StringBuilder bodyBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			bodyBuilder.append(line).append("\n");
		}
		
		return new HttpRequest(startLine, headers, HttpRequestBody.of(bodyBuilder.toString()));
	}
	
	
}
