package common.exception;

public class InvalidHttpVersion extends HttpException {
    public InvalidHttpVersion() {
        super("Invalid HTTP version");
    }

    public InvalidHttpVersion(String message) {
        super("Invalid HTTP version: " + message);
    }
    
}
