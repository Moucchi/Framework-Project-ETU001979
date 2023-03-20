package etu1979.framework.model;

import etu1979.framework.Annotation.URL;

public class Animal {
    
    @URL( value = "Animal-manger" )
    public void eat(){}

    @URL( value = "Animal-dormir" )
    public void sleep(){}

    @URL( value = "Animal-mourrir" )
    public void die(){}
}
