package etu1979.framework.model;

import etu1979.framework.Annotation.URL;
import etu1979.framework.ModelView;

public class Emp {
    String nom ;
    int age;

    public Emp() {
    }

    @URL(value = "Hello")
    public ModelView great() {
        return new ModelView("Greating.jsp");
    }

    @URL(value = "Test")
    public ModelView test() {
        return new ModelView("Wassup");
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAge(String age) {
        int toBeUsed = Integer.valueOf(age);
        this.age = toBeUsed;
    }
}
