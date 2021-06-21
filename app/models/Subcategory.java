package models;

import models.enums.SubcategoryValue;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Subcategory extends BaseModel {

    @Required(message = "Missing value field and it is mandatory")
    @NotBlank(message = "Value field cannot be empty")
    private String value;

    public Subcategory(String value) {
        SubcategoryValue validSubcategory = getSubcategoryAsEnum(value);
        this.value = getSubcategoryAsString(validSubcategory);
    }

    public SubcategoryValue getValue() {
        return getSubcategoryAsEnum(value);
    }

    public void setValue(SubcategoryValue value) {
        this.value = getSubcategoryAsString(value);
    }

    public void setValue(String value) {
        SubcategoryValue validSubcategory = getSubcategoryAsEnum(value);
        this.value = getSubcategoryAsString(validSubcategory);
    }

    public static String getSubcategoryAsString(SubcategoryValue subcategoryValue) {
        String result = "UNKNOWN";
        switch (subcategoryValue) {
            case COLD:
                result = "COLD";
                break;
            case HOT:
                result = "HOT";
                break;
            case SPICY:
                result = "SPICY";
                break;
            case SWEET:
                result = "SWEET";
                break;
            case SALTY:
                result = "SALTY";
                break;
            case ACID:
                result = "ACID";
                break;
            case SOUR:
                result = "SOUR";
                break;
            case ROAST:
                result = "ROAST";
                break;
            case COOKED:
                result = "COOKED";
                break;
            case FRIED:
                result = "FRIED";
                break;
            case RAW:
                result = "RAW";
                break;
            case VEGAN:
                result = "VEGAN";
                break;
            case UNKNOWN:
                result = "UNKNOWN";
                break;
        }
        return result;
    }

    public static SubcategoryValue getSubcategoryAsEnum(String subcategory) {
        SubcategoryValue result;
        String subcategoryValue = subcategory.toUpperCase();
        switch (subcategoryValue) {
            case "COLD":
                result = SubcategoryValue.COLD;
                break;
            case "HOT":
                result = SubcategoryValue.HOT;
                break;
            case "SPICY":
                result = SubcategoryValue.SPICY;
                break;
            case "SWEET":
                result = SubcategoryValue.SWEET;
                break;
            case "SALTY":
                result = SubcategoryValue.SALTY;
                break;
            case "ACID":
                result = SubcategoryValue.ACID;
                break;
            case "SOUR":
                result = SubcategoryValue.SOUR;
                break;
            case "ROAST":
                result = SubcategoryValue.ROAST;
                break;
            case "COOKED":
                result = SubcategoryValue.COOKED;
                break;
            case "FRIED":
                result = SubcategoryValue.FRIED;
                break;
            case "RAW":
                result = SubcategoryValue.RAW;
                break;
            case "VEGAN":
                result = SubcategoryValue.VEGAN;
                break;
            default:
                result = SubcategoryValue.UNKNOWN;
                break;
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subcategory) {
            return ((Subcategory) obj).value.toUpperCase().equals(this.value.toUpperCase());
        } else {
            return false;
        }
    }
}
