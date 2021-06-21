package models;

import models.enums.CategoryValue;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Category extends BaseModel {

    @Required(message = "Missing value field and it is mandatory")
    @NotBlank(message = "Value field cannot be empty")
    private String value;

    public Category(String value) {
        CategoryValue validCategory = getCategoryAsEnum(value);
        this.value = getCategoryAsString(validCategory);
    }

    public CategoryValue getValue() {
        return getCategoryAsEnum(value);
    }

    public void setValue(CategoryValue value) {
        this.value = getCategoryAsString(value);
    }

    public void setValue(String value) {
        CategoryValue validCategory = getCategoryAsEnum(value);
        this.value = getCategoryAsString(validCategory);
    }

    public static String getCategoryAsString(CategoryValue categoryValue) {
        String result = "UNKNOWN";
        switch (categoryValue) {
            case STARTERS:
                result = "STARTER";
                break;
            case MAIN_COURSE:
                result = "MAIN_COURSE";
                break;
            case DRINK:
                result = "DRINK";
                break;
            case DESSERT:
                result = "DESSERT";
                break;
            case UNKNOWN:
                result = "UNKNOWN";
                break;
        }
        return result;
    }

    public static CategoryValue getCategoryAsEnum(String category) {
        CategoryValue result;
        String categoryValue = category.toUpperCase();
        switch (categoryValue) {
            case "STARTER":
                result = CategoryValue.STARTERS;
                break;
            case "MAIN_COURSE":
                result = CategoryValue.MAIN_COURSE;
                break;
            case "DRINK":
                result = CategoryValue.DRINK;
                break;
            case "DESSERT":
                result = CategoryValue.DESSERT;
                break;
            default:
                result = CategoryValue.UNKNOWN;
                break;
        }
        return result;
    }

}
