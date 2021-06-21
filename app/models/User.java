package models;


import play.data.validation.Constraints.Required;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseModel {

    @Required(message = "Missing username field value and it is mandatory")
    @NotBlank(message = "Username field value cannot be empty")
    private String username;

    @Required(message = "Missing password field value and it is mandatory")
    @NotBlank(message = "Password field value cannot be empty")
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Recipe> recipes;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.recipes = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) { this.password = password; }
}
