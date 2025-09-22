package org.recipe_system.Controller;

import org.recipe_system.Model.User;
import org.recipe_system.Model.System;

public class Controller {
    private System system;
    private User currentUser;

    public Controller(System system) {
        this.system = system;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

}
