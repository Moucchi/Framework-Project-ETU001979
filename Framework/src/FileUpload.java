package etu1979.framework;

public class FileUpload {
    String fileName;
    String path;
    byte[] bytes;

    public FileUpload() {
    }

    public FileUpload(String fileName, String path, byte[] bytes) {
        this.fileName = fileName;
        this.path = path;
        this.bytes = bytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String toString() {
        return this.getFileName() + " [ " + this.getBytes().length + " ]";
    }
}
