package common.exception;

public class InvalidQueryParameterException extends HttpException{
    public InvalidQueryParameterException(String message) {
        super("Invalid query parameter: " + message);
    }
}
