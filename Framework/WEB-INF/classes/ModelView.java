public class ModelView {

    String url;

    public ModelView() {
        this.getClass().getDeclaredMethod("frev", null).invoke(url, null)
    }

    public ModelView(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}