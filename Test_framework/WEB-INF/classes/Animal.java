package model.object;

import etu1979.framework.Annotation.URL;

public class Animal {
    
    @URL( value = "Animal-manger" )
    public ModelView eat(){
        ModelView result = new ModelView();
        result.setUrl("/eat.jsp");
        return result;
    }

    @URL( value = "Animal-dormir" )
    public ModelView sleep(){
        ModelView result = new ModelView();
        result.setUrl("/sleep.jsp");
        return result;
    }

    @URL( value = "Animal-mourrir" )
    public ModelView die(){
        ModelView result = new ModelView();
        result.setUrl("/die.jsp");
        return result;
    }
    
}
