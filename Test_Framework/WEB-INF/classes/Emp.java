package etu1979.framework.model;

import etu1979.framework.Annotation.URL;
import etu1979.framework.ModelView;

public class Emp {
    @URL(value = "Emp-great")
    public ModelView great() {
        return new ModelView("Greating.jsp");
    }

    @URL(value = "Emp-test")
    public ModelView test() {
        return new ModelView("Wassup");
    }
}