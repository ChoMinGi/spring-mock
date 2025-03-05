package common.exception;

public class MethodNotAllowedException extends HttpException {
    public MethodNotAllowedException() {
        super("Method Not Allowed");
    }
}
