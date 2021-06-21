package common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import operationResults.ErrorResult;
import operationResults.ErrorType;
import operationResults.OperationResult;
import play.data.Form;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.util.*;

import static play.mvc.Results.*;

public class Utils {

    public static ArrayList<Ingredient> getIngredientsFromJSON(JsonNode jsonBody) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (JsonNode node : jsonBody) {
            String ingredientName = node.get(Constants.INGREDIENT_NAME).asText();
            String type = node.get(Constants.INGREDIENT_TYPE).asText();
            double calories = node.get(Constants.INGREDIENT_CALORIES).asDouble();
            IngredientType ingredientType = new IngredientType(type);
            ingredients.add(new Ingredient(ingredientName, ingredientType, calories));
        }
        return ingredients;
    }

    public static ArrayList<Subcategory> getSubcategoriesFromJSON(JsonNode jsonBody) {
        ArrayList<Subcategory> subcategories = new ArrayList<>();
        for (JsonNode node : jsonBody) {
            String subcategoryName = node.get(Constants.SUBCATEGORY).asText();
            Subcategory subcategory = new Subcategory(subcategoryName);
            subcategories.add(subcategory);
        }
        return subcategories;
    }

    public static Result processOperationResultAsJson(OperationResult operationResult, Http.Request request) {
        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            if (operationResult instanceof ErrorResult) {
                return status(operationResult.getCode(), views.xml.error.render(operationResult.getDescription()));
            } else {
                return ok(views.xml.ok.render(operationResult.getDescription()));
            }
        } else {
            ObjectNode node = Json.newObject();
            if (operationResult instanceof ErrorResult) {
                node.put(Constants.ERROR_DESCRIPTION, operationResult.getDescription());
                return status(operationResult.getCode(), node);
            } else {
                node.put(Constants.MESSAGE, operationResult.getDescription());
                return ok(node);
            }
        }
    }

    public static Result processStandardError(ErrorType type, String description, Http.Request request) {
        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            return status(type.getCode(), views.xml.error.render(description));
        } else {
            ObjectNode node = Json.newObject();
            node.put(Constants.ERROR_DESCRIPTION, description);
            return status(type.getCode(), node);
        }

    }

    public static Result processFormErrorAsJson(Form<?> form, Http.Request request) {
        StringBuilder description = new StringBuilder();
        for (JsonNode element : form.errorsAsJson()) {
            String text = element.get(0).textValue();
            description.append(text);
            description.append(", ");
        }

        description.delete(description.length() - 2, description.length());

        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            return badRequest(views.xml.error.render(description.toString()));
        } else {
            ObjectNode node = Json.newObject();
            node.put(Constants.ERROR_DESCRIPTION, description.toString());
            return badRequest(node);
        }

    }

    public static Result processRecipeResult(Tuple<OperationResult, Recipe> operationResult, Http.Request request, MessagesApi messagesApi) {
        if (operationResult.left() instanceof ErrorResult) {
            return processOperationResultAsJson(operationResult.left(), request);
        } else if (operationResult.right() == null) {
            OperationResult noRecipeFound = new ErrorResult(ErrorType.RECIPE_WITH_NAME_NO_EXISTS, messagesApi.preferred(request).at("RECIPE_NOT_EXISTS_ERROR_DESCRIPTION"));
            return processOperationResultAsJson(noRecipeFound, request);
        } else {
            if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
                return ok(views.xml.recipe.render(Collections.singletonList(operationResult.right())));
            } else {
                JsonNode recipeNode = convertToJson((operationResult.right()));
                return ok(recipeNode);
            }
        }
    }

    public static Result processRecipesResult(Tuple<OperationResult, ArrayList<Recipe>> operationResult, Http.Request request) {
        if (operationResult.left() instanceof ErrorResult) {
            return processOperationResultAsJson(operationResult.left(), request);
        } else {
            if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
                return ok(views.xml.recipe.render(operationResult.right()));
            } else {
                JsonNode node = convertToJson(operationResult.right());
                return ok(node);
            }
        }
    }

    public static JsonNode convertToJson(Recipe recipe) {
        ObjectNode recipeNode = Json.newObject();
        recipeNode.put(Constants.RECIPE_NAME, recipe.getName());
        recipeNode.put(Constants.RECIPE_DESCRIPTION, recipe.getDescription());
        recipeNode.put(Constants.RECIPE_DURATION_IN_HOURS, recipe.getDurationInHours());
        recipeNode.put(Constants.RECIPE_DIFFICULT, Difficult.getDifficultAsString(recipe.getDifficult().getValue()));

        ArrayNode ingredients = Json.newArray();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ObjectNode ingredientNode = Json.newObject();
            ingredientNode.put(Constants.INGREDIENT_NAME, ingredient.getName());
            ingredientNode.put(Constants.INGREDIENT_TYPE, IngredientType.getIngredientTypeAsString(ingredient.getType().getValue()));
            ingredientNode.put(Constants.INGREDIENT_CALORIES, ingredient.getCalories());
            ingredients.add(ingredientNode);
        }

        recipeNode.set(Constants.RECIPE_INGREDIENTS, ingredients);
        recipeNode.put(Constants.RECIPE_CATEGORY, Category.getCategoryAsString(recipe.getCategory().getValue()));

        ArrayNode subcategories = Json.newArray();
        for (Subcategory subcategory : recipe.getSubcategories()) {
            ObjectNode subcategoryNode = Json.newObject();
            subcategoryNode.put(Constants.SUBCATEGORY, Subcategory.getSubcategoryAsString(subcategory.getValue()));
            subcategories.add(subcategoryNode);
        }

        recipeNode.set(Constants.RECIPE_SUBCATEGORIES, subcategories);

        return recipeNode;
    }

    public static JsonNode convertToJson(ArrayList<Recipe> recipes) {
        ObjectNode node = Json.newObject();
        ArrayNode array = Json.newArray();

        for (Recipe recipe : recipes) {
            JsonNode recipeNode = Utils.convertToJson(recipe);
            array.add(recipeNode);
        }

        node.set(Constants.RECIPE_KEY, array);
        return node;
    }
}
