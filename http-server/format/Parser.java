package format;

import java.text.ParseException;

@FunctionalInterface
public interface Parser<T> {
    static final String DEFAULT_PATH = "/";
    
    T parse(String input) throws ParseException;
}