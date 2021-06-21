package models;

import models.enums.DifficultValue;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Difficult extends BaseModel {

    @Required(message = "Missing value field and it is mandatory")
    @NotBlank(message = "Value field cannot be empty")
    private String value;

    public Difficult(String value) {
        DifficultValue validDifficult = getDifficultAsEnum(value);
        this.value = getDifficultAsString(validDifficult);
    }

    public DifficultValue getValue() {
        return getDifficultAsEnum(value);
    }

    public void setValue(DifficultValue value) {
        this.value = getDifficultAsString(value);
    }

    public void setValue(String value) {
        DifficultValue validDifficult = getDifficultAsEnum(value);
        this.value = getDifficultAsString(validDifficult);
    }

    public static String getDifficultAsString(DifficultValue difficult) {
        String result = "UNKNOWN";
        switch (difficult) {
            case EASY:
                result = "EASY";
                break;
            case MEDIUM:
                result = "MEDIUM";
                break;
            case HARD:
                result = "HARD";
                break;
            case UNKNOWN:
                result = "UNKNOWN";
                break;
        }
        return result;
    }

    public static DifficultValue getDifficultAsEnum(String difficult) {
        DifficultValue result;
        String difficultValue = difficult.toUpperCase();
        switch (difficultValue) {
            case "EASY":
                result = DifficultValue.EASY;
                break;
            case "MEDIUM":
                result = DifficultValue.MEDIUM;
                break;
            case "HARD":
                result = DifficultValue.HARD;
                break;
            default:
                result = DifficultValue.UNKNOWN;
                break;
        }
        return result;
    }
}
