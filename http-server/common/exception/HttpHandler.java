package common.exception;

public class HttpHandler {
    public static String handleExcpetion(HttpException e) {
        return "HTTP " + e.getStatusCode() + " " + e.getMessage();
    }

    public static String handleExcpetion(Exception e) {
        return "HTTP 500 Error " + e.getMessage();
    }
}
