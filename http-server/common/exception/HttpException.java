package common.exception;

public class HttpException extends RuntimeException {
    private final int statusCode;

    public HttpException(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

