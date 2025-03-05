package common.http;

import common.exception.InvalidHttpVersion;

public class HttpVersion {
    public static final String HTTP_1_1 = "HTTP/1.1";
    public static final HttpVersion DEFAULT = new HttpVersion(HTTP_1_1);

    private final String scheme;
    private final int major;
    private final int minor;
    private final String raw;

    public HttpVersion(String raw) {
        this.raw = raw;
        String[] parts = raw.split("/");
        if (parts.length != 2) {
            throw new InvalidHttpVersion(raw);
        }
        
        this.scheme = parts[0];
        String[] versionParts = parts[1].split("\\.");
        if (versionParts.length != 2) {
            throw new InvalidHttpVersion(raw);
        }

        this.major = Integer.parseInt(versionParts[0]);
        this.minor = Integer.parseInt(versionParts[1]);
    }

    public HttpVersion(String scheme, int major, int minor) {
        this.scheme = scheme;
        this.major = major;
        this.minor = minor;
        this.raw = scheme + "/" + major + "." + minor;
    }

    public HttpVersion(int major, int minor) {
        this("HTTP", major, minor);
    }

    public String getScheme() {
        return scheme;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getRaw() {
        return raw;
    }

    @Override
    public String toString() {
        return raw;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        HttpVersion that = (HttpVersion) obj;
        return major == that.major && minor == that.minor && scheme.equals(that.scheme);
    }

    @Override
    public int hashCode() {
        int result = scheme.hashCode();
        result = 31 * result + major;
        result = 31 * result + minor;
        return result;
    }
}
