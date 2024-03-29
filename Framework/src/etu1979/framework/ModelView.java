package etu1979.framework;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem(String key , Object value) {
        if( this.getData() == null ){
            this.data = new HashMap<>();
        }

        this.data.put(key, value);
    }

    public ModelView(String view) {
        this.view = view;
    }

    public ModelView() {
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
