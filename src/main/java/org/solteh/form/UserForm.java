package org.solteh.form;

import org.solteh.entity.User;
import org.solteh.entity.UserState;

public class UserForm {
    private String username;
    private String password;
    private boolean active;
    private UserState state;

    private boolean isNew = false;

    public UserForm() {
        isNew = true;
    }

    public UserForm(User user) {
        this.username = user.getUserName();
        this.password = user.getEncrytedPassword();
        this.active = user.isActive();
        this.state = user.getUserState();
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }
}
