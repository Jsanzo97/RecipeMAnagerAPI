package models;

import play.data.validation.Constraints.Required;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

@Entity
public class Ingredient extends BaseModel {

    @Required(message = "Missing name field value and it is mandatory")
    @NotBlank(message = "Name field value cannot be empty")
    private String name;

    @Required(message = "Missing type field value and it is mandatory")
    @OneToOne(cascade = CascadeType.ALL)
    private IngredientType type;

    @Required(message = "Missing calories field value and it is mandatory")
    @DecimalMin(value = "0.0", message = "Minimum value for calories is 0")
    private double calories;

    public Ingredient(String name, IngredientType type, double calories) {
        this.name = name;
        this.type = type;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public IngredientType getType() {
        return type;
    }

    public double getCalories() {
        return calories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(IngredientType type) {
        this.type = type;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Ingredient { " + "\n" +
                "\tname = " + name + "\n" +
                "\ttype = " + IngredientType.getIngredientTypeAsString(type.getValue()) + "\n" +
                "\tcalories = " + calories + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ingredient) {
            return ((Ingredient) obj).getName().toUpperCase().equals(this.name.toUpperCase());
        } else {
            return false;
        }
    }
}
