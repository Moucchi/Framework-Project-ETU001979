package etu1979.framework.model;

import etu1979.framework.Annotation.URL;

public class Emp {
    @URL(value = "Hello")
    public String great() {
        return "Wassup";
    }

    @URL(value = "Nope")
    public String deny() {
        return "Hell no .";
    }
}
