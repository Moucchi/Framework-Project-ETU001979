package etu1979.framework.model;

import etu1979.framework.Annotation.URL;
import etu1979.framework.ModelView;

public class Emp {
    @URL(value = "Hello")
    public String great() {
        return "Wassup";
    }

    @URL(value = "Test")
    public ModelView test() {
        return new ModelView("Wassup");
    }
}
