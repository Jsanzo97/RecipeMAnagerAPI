package models;

import models.enums.IngredientTypeValue;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class IngredientType extends BaseModel {

    @Required(message = "Missing value field and it is mandatory")
    @NotBlank(message = "Value field cannot be empty")
    private String value;

    public IngredientType(String value) {
        IngredientTypeValue validIngredientType = getIngredientTypeAsEnum(value);
        this.value = getIngredientTypeAsString(validIngredientType);
    }

    public IngredientTypeValue getValue() {
        return getIngredientTypeAsEnum(value);
    }

    public void setValue(IngredientTypeValue value) {
        this.value = getIngredientTypeAsString(value);
    }

    public void setValue(String value) {
        IngredientTypeValue validCategory = getIngredientTypeAsEnum(value);
        this.value = getIngredientTypeAsString(validCategory);
    }

    public static String getIngredientTypeAsString(IngredientTypeValue type) {
        String result = "UNKNOWN";
        switch (type) {
            case MEAT:
                result = "MEAT";
                break;
            case FISH:
                result ="FISH";
                break;
            case VEGETABLE:
                result = "VEGETABLE";
                break;
            case LEGUME:
                result = "LEGUME";
                break;
            case FRUIT:
                result = "FRUIT";
                break;
            case OIL:
                result = "OIL";
                break;
            case LIQUID:
                result = "LIQUID";
                break;
            case UNKNOWN:
                result = "UNKNOWN";
                break;
        }
        return result;
    }

    public static IngredientTypeValue getIngredientTypeAsEnum(String type) {
        IngredientTypeValue result;
        String typeValue = type.toUpperCase();
        switch (typeValue) {
            case "MEAT":
                result = IngredientTypeValue.MEAT;
                break;
            case "FISH":
                result = IngredientTypeValue.FISH;
                break;
            case "VEGETABLE":
                result = IngredientTypeValue.VEGETABLE;
                break;
            case "LEGUME":
                result = IngredientTypeValue.LEGUME;
                break;
            case "FRUIT":
                result = IngredientTypeValue.FRUIT;
                break;
            case "OIL":
                result = IngredientTypeValue.OIL;
                break;
            case "LIQUID":
                result = IngredientTypeValue.LIQUID;
                break;
            default:
                result = IngredientTypeValue.UNKNOWN;
                break;
        }
        return result;
    }

}
