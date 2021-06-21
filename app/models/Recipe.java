package models;

import play.data.validation.Constraints.Required;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe extends BaseModel {

    @Required(message = "Missing name field value and it is mandatory")
    @NotBlank(message = "Name field value cannot be empty")
    private String name;

    @Required(message = "Missing description field value and it is mandatory")
    @NotBlank(message = "Description field value cannot be empty")
    @Column(length = 100000)
    private String description;

    @Required(message = "Missing duration field value and it is mandatory")
    @DecimalMin(value = "0.0", message = "Minimum value for duration hours is 0")
    private double durationInHours;

    @Required(message = "Missing difficult field value and it is mandatory")
    @OneToOne(cascade = CascadeType.ALL)
    private Difficult difficult;

    @Required(message = "Missing ingredients field value and it is mandatory")
    @Size(min = 1, message = "There should be at least one element")
    @OneToMany(cascade = CascadeType.ALL)
    @Valid
    private List<Ingredient> ingredients;

    @Required(message = "Missing category field value and it is mandatory")
    @OneToOne(cascade = CascadeType.ALL)
    private Category category;

    @Required(message = "Missing subcategories field value and it is mandatory")
    @Size(min = 1, message = "There should be at least one element")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Subcategory> subcategories;

    public
    Recipe(String name, String description, double durationInHours, String difficult, ArrayList<Ingredient> ingredients, String categoryValue, ArrayList<Subcategory> subcategories) {
        this.name = name;
        this.description = description;
        this.durationInHours = durationInHours;
        this.difficult = new Difficult(difficult);
        this.ingredients = ingredients;
        this.category = new Category(categoryValue);
        this.subcategories = subcategories;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getDurationInHours() {
        return durationInHours;
    }

    public Difficult getDifficult() {
        return difficult;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Category getCategory() {
        return category;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDurationInHours(double durationInHours) {
        this.durationInHours = durationInHours;
    }

    public void setDifficult(Difficult difficult) {
        this.difficult = difficult;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSubcategories(ArrayList<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public void addIngredients(ArrayList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (!this.ingredients.contains(ingredient)) {
                this.ingredients.add(ingredient);
            }
        }
    }

    public void removeIngredients(ArrayList<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (this.ingredients.contains(ingredient)) {
                this.ingredients.remove(ingredient);
            }
        }
    }

    public void addSubcategories(ArrayList<Subcategory> subcategories) {
        for (Subcategory subcategory : subcategories) {
            if (!this.subcategories.contains(subcategory)) {
                this.subcategories.add(subcategory);
            }
        }
    }

    public void removeSubcategories(ArrayList<Subcategory> subcategories) {
        for (Subcategory subcategory : subcategories) {
            if (this.subcategories.contains(subcategory)) {
                this.subcategories.remove(subcategory);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringIngredients = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            stringIngredients.append(ingredient.toString()).append("\t\n");
        }

        StringBuilder stringSubcategories = new StringBuilder();
        for (Subcategory subcategory : subcategories) {
            stringSubcategories.append(Subcategory.getSubcategoryAsString(subcategory.getValue())).append("\t\n");
        }

        return "Recipe { " + "\n" +
                "\tname = " + name + "\n" +
                "\tdescription = " + description + "\n" +
                "\tdurationInHours = " + durationInHours + "\n" +
                "\tdifficult = " + Difficult.getDifficultAsString(difficult.getValue()) + "\n" +
                "\t\tingredients = " + stringIngredients + "\n" +
                "\tcategory = " + Category.getCategoryAsString(category.getValue()) + "\n" +
                "\t\tsubcategories = " + stringSubcategories + "\n" +
                '}';
    }
}
