package common.http;

public class Path {
    private final String path;

    private Path(String path) {
        this.path = path;
    }

    public static Path of(String path) {
        return new Path(path);
    }

    public String value() {
        return path;
    }

    public String getExtension() {
        int index = path.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return path.substring(index + 1);
    }

    public String getFileName() {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return path;
        }
        return path.substring(index + 1);
    }

    public String getFileNameWithoutExtension() {
        String fileName = getFileName();
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }
        return fileName.substring(0, index);
    }

    public String getParent() {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return "";
        }
        return path.substring(0, index);
    }

    @Override
    public String toString() {
        return path;
    }

}
