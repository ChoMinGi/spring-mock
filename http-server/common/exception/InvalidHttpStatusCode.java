package common.exception;

public class InvalidHttpStatusCode extends HttpException {
    public InvalidHttpStatusCode() {
        super("Invalid HTTP status code");
    }

    public InvalidHttpStatusCode(String message) {
        super("Invalid HTTP status code: " + message);
    }
    
}
