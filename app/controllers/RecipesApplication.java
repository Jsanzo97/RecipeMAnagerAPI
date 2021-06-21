package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.Constants;
import common.Tuple;
import database.Database;
import models.*;
import models.enums.CategoryValue;
import models.enums.DifficultValue;
import models.enums.IngredientTypeValue;
import models.enums.SubcategoryValue;
import operationResults.ErrorResult;
import operationResults.ErrorType;
import operationResults.OperationResult;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;

import static common.Utils.*;

public class RecipesApplication extends Controller  {

    @Inject
    FormFactory formFactory;

    private final MessagesApi messagesApi;

    @Inject
    RecipesApplication(MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    @Transactional
    public Result signUp(Http.Request request) {
        Result response;

        if (request.body().asJson() == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            Form<User> form = formFactory.form(User.class).bindFromRequest(request);

            if (form.hasErrors()) {
                response = processFormErrorAsJson(form, request);
            } else {
                OperationResult operationResult = Database.signUp(form.get(), messagesApi.preferred(request));
                response = processOperationResultAsJson(operationResult, request);
            }
        }

        return response;
    }

    public Result logIn(Http.Request request) {
        Result response;

        if (request.body().asJson() == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            Form<User> form = formFactory.form(User.class).bindFromRequest(request);

            if (form.hasErrors()) {
                response = processFormErrorAsJson(form, request);
            } else {
                OperationResult operationResult = Database.logIn(form.get(), messagesApi.preferred(request));
                response = processOperationResultAsJson(operationResult, request);
            }
        }

        return response;
    }

    @Transactional
    public Result createRecipe(Http.Request request, String username) {
        Result response;

        if (request.body().asJson() == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            Form<Recipe> form = formFactory.form(Recipe.class).bindFromRequest(request);

            if (form.hasErrors()) {
                response = processFormErrorAsJson(form, request);
            } else {
                OperationResult operationResult = Database.createRecipe(
                        username,
                        form.get(),
                        messagesApi.preferred(request)
                );

                response = processOperationResultAsJson(operationResult, request);
            }
        }

        return response;
    }

    public Result getRecipes(Http.Request request, String username) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult = Database.getRecipesFromUser(username, messagesApi.preferred(request));
        return processRecipesResult(operationResult, request);
    }

    public Result getRecipeWithName(Http.Request request, String username, String name) {
        String recipeNameParsed = name.replace("%20", " ");
        Tuple<OperationResult, Recipe> operationResult = Database.getRecipeByName(username, recipeNameParsed, messagesApi.preferred(request));
        return processRecipeResult(operationResult, request, messagesApi);
    }

    public Result getRecipesWithMoreDuration(Http.Request request, String username, double duration) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult = Database.getRecipesWithMoreDuration(username, duration, messagesApi.preferred(request));
        return processRecipesResult(operationResult, request);
    }

    public Result getRecipesWithLessDuration(Http.Request request, String username, double duration) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult = Database.getRecipesWithLessDuration(username, duration, messagesApi.preferred(request));
        return processRecipesResult(operationResult, request);
    }

    public Result getRecipesWithDifficult(Http.Request request, String username, String difficult) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult = Database.getRecipesWithDifficult(username, difficult, messagesApi.preferred(request));
        return processRecipesResult(operationResult, request);
    }

    public Result getRecipesWithCategory(Http.Request request, String username, String category) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult = Database.getRecipesWithCategory(username, category, messagesApi.preferred(request));
        return processRecipesResult(operationResult, request);
    }

    public Result getRecipesWithSubcategory(Http.Request request, String username, String subcategory) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult = Database.getRecipesWithSubcategory(username, subcategory, messagesApi.preferred(request));
        return processRecipesResult(operationResult, request);
    }

    public Result removeRecipeWithName(Http.Request request, String username, String name) {
        OperationResult operationResult = Database.removeRecipeByName(username, name, messagesApi.preferred(request));
        return processOperationResultAsJson(operationResult, request);
    }

    @Transactional
    public Result updateRecipe(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                Tuple<OperationResult, Recipe> getRecipeResult = Database.getRecipeByName(username, name, messagesApi.preferred(request));

                if (getRecipeResult.left() instanceof ErrorResult || getRecipeResult.right() == null) {
                    response = processOperationResultAsJson(getRecipeResult.left(), request);
                } else {
                    Recipe recipe = getRecipeResult.right();
                    if (jsonBody.has(Constants.RECIPE_DESCRIPTION) && !jsonBody.get(Constants.RECIPE_DESCRIPTION).asText().equals("")) {
                        recipe.setDescription(jsonBody.get(Constants.RECIPE_DESCRIPTION).asText());
                    }

                    if (jsonBody.has(Constants.RECIPE_DURATION_IN_HOURS) && jsonBody.get(Constants.RECIPE_DURATION_IN_HOURS).asDouble() >= 0.0) {
                        recipe.setDurationInHours(jsonBody.get(Constants.RECIPE_DURATION_IN_HOURS).asDouble());
                    }

                    if (jsonBody.has(Constants.RECIPE_DIFFICULT)) {
                        Difficult difficult = new Difficult(jsonBody.get(Constants.RECIPE_DIFFICULT).asText());
                        recipe.setDifficult(difficult);
                    }

                    if (jsonBody.has(Constants.RECIPE_INGREDIENTS)) {
                        ArrayList<Ingredient> ingredients = getIngredientsFromJSON(jsonBody.get(Constants.RECIPE_INGREDIENTS));
                        recipe.addIngredients(ingredients);
                    }

                    if (jsonBody.has(Constants.RECIPE_CATEGORY)) {
                        Category category = new Category(jsonBody.get(Constants.RECIPE_CATEGORY).asText());
                        recipe.setCategory(category);
                    }

                    if (jsonBody.has(Constants.RECIPE_SUBCATEGORIES)) {
                        ArrayList<Subcategory> subcategories = getSubcategoriesFromJSON(jsonBody.get(Constants.RECIPE_SUBCATEGORIES));
                        recipe.addSubcategories(subcategories);
                    }

                    OperationResult updateRecipe = Database.updateRecipe(username, recipe, messagesApi.preferred(request));

                    response = processOperationResultAsJson(updateRecipe, request);
                }
            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result updateRecipeDescription(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                String newDescription = jsonBody.get(Constants.RECIPE_DESCRIPTION).asText();

                OperationResult updateDescriptionResult = Database.updateDescription(username, name, newDescription, messagesApi.preferred(request));
                response = processOperationResultAsJson(updateDescriptionResult, request);
            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result updateRecipeDuration(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                double newDuration = jsonBody.get(Constants.RECIPE_DURATION_IN_HOURS).asDouble();

                OperationResult updateDescriptionResult = Database.updateDescription(username, name, newDuration, messagesApi.preferred(request));
                response = processOperationResultAsJson(updateDescriptionResult, request);
            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result updateRecipeDifficult(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                String newDifficult = jsonBody.get(Constants.RECIPE_DIFFICULT).asText();

                OperationResult updateDescriptionResult = Database.updateDifficult(username, name, newDifficult, messagesApi.preferred(request));
                response = processOperationResultAsJson(updateDescriptionResult, request);
            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result addRecipeIngredients(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                JsonNode jsonIngredients = jsonBody.get(Constants.RECIPE_INGREDIENTS);
                ArrayList<Ingredient> ingredients = getIngredientsFromJSON(jsonIngredients);

                OperationResult addIngredientsResult = Database.addIngredientsToRecipe(username, name, ingredients, messagesApi.preferred(request));

                response = processOperationResultAsJson(addIngredientsResult, request);

            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result removeRecipeIngredients(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                JsonNode jsonIngredients = jsonBody.get(Constants.RECIPE_INGREDIENTS);

                ArrayList<Ingredient> ingredients = getIngredientsFromJSON(jsonIngredients);

                OperationResult removeIngredientsResult = Database.removeIngredientsFromRecipe(username, name, ingredients, messagesApi.preferred(request));

                response = processOperationResultAsJson(removeIngredientsResult, request);

            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result updateRecipeCategory(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                String newCategory = jsonBody.get(Constants.RECIPE_CATEGORY).asText();

                OperationResult updateDescriptionResult = Database.updateCategory(username, name, newCategory, messagesApi.preferred(request));
                response = processOperationResultAsJson(updateDescriptionResult, request);

            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result addRecipeSubcategories(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                JsonNode jsonSubcategories = jsonBody.get(Constants.RECIPE_SUBCATEGORIES);
                ArrayList<Subcategory> subcategories = getSubcategoriesFromJSON(jsonSubcategories);

                OperationResult addSubcategoriesResult = Database.addSubcategoriesToRecipe(username, name, subcategories, messagesApi.preferred(request));

                response = processOperationResultAsJson(addSubcategoriesResult, request);

            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    @Transactional
    public Result removeRecipeSubcategories(Http.Request request, String username, String name) {
        Result response;

        Http.RequestBody body = request.body();
        JsonNode jsonBody = body.asJson();

        if (jsonBody == null) {
            response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("BODY_IS_EMPTY_ERROR_DESCRIPTION"), request);
        } else {
            try {
                JsonNode jsonSubcategories = jsonBody.get(Constants.RECIPE_SUBCATEGORIES);
                ArrayList<Subcategory> subcategories = getSubcategoriesFromJSON(jsonSubcategories);

                OperationResult addSubcategoriesResult = Database.removeSubcategoriesFromRecipe(username, name, subcategories, messagesApi.preferred(request));

                response = processOperationResultAsJson(addSubcategoriesResult, request);

            } catch (Exception e) {
                response = processStandardError(ErrorType.EMPTY_BODY, messagesApi.preferred(request).at("WRONG_PARAMETERS_ERROR_DESCRIPTION"), request);
            }
        }

        return response;
    }

    public Result getDifficulties(Http.Request request) {
        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            ArrayList<String> stringDifficulties = new ArrayList<>();
            for (DifficultValue value : DifficultValue.values()) {
                stringDifficulties.add(Difficult.getDifficultAsString(value));
            }
            return ok(views.xml.difficulties.render(stringDifficulties));
        } else {
            ObjectNode node = Json.newObject();
            ArrayNode array = Json.newArray();
            for (DifficultValue value : DifficultValue.values()) {
                ObjectNode difficultNode = Json.newObject();
                difficultNode.put(Constants.RECIPE_DIFFICULT, Difficult.getDifficultAsString(value));
                array.add(difficultNode);
            }
            node.set(Constants.DIFFICULTIES_KEY, array);
            return ok(node);
        }
    }

    public Result getCategories(Http.Request request) {
        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            ArrayList<String> stringCategories = new ArrayList<>();
            for (CategoryValue value : CategoryValue.values()) {
                stringCategories.add(Category.getCategoryAsString(value));
            }
            return ok(views.xml.categories.render(stringCategories));
        } else {
            ObjectNode node = Json.newObject();
            ArrayNode array = Json.newArray();
            for (CategoryValue value : CategoryValue.values()) {
                ObjectNode categoryNode = Json.newObject();
                categoryNode.put(Constants.RECIPE_CATEGORY, Category.getCategoryAsString(value));
                array.add(categoryNode);
            }
            node.set(Constants.CATEGORIES_KEY, array);
            return ok(node);
        }
    }

    public Result getSubcategories(Http.Request request) {
        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            ArrayList<String> stringSubcategories = new ArrayList<>();
            for (SubcategoryValue value : SubcategoryValue.values()) {
                stringSubcategories.add(Subcategory.getSubcategoryAsString(value));
            }
            return ok(views.xml.subcategories.render(stringSubcategories));
        } else {
            ObjectNode node = Json.newObject();
            ArrayNode array = Json.newArray();
            for (SubcategoryValue value : SubcategoryValue.values()) {
                ObjectNode subcategoryNode = Json.newObject();
                subcategoryNode.put(Constants.SUBCATEGORY, Subcategory.getSubcategoryAsString(value));
                array.add(subcategoryNode);
            }
            node.set(Constants.SUBCATEGORIES_KEY, array);
            return ok(node);
        }
    }

    public Result getIngredientTypes(Http.Request request) {
        if (request.accepts(Http.MimeTypes.XML) && !request.accepts("*/*")) {
            ArrayList<String> stringIngredientTypes = new ArrayList<>();
            for (IngredientTypeValue value : IngredientTypeValue.values()) {
                stringIngredientTypes.add(IngredientType.getIngredientTypeAsString(value));
            }
            return ok(views.xml.ingredientTypes.render(stringIngredientTypes));
        } else {
            ObjectNode node = Json.newObject();
            ArrayNode array = Json.newArray();
            for (IngredientTypeValue value : IngredientTypeValue.values()) {
                ObjectNode ingredientTypeNode = Json.newObject();
                ingredientTypeNode.put(Constants.INGREDIENT_TYPE, IngredientType.getIngredientTypeAsString(value));
                array.add(ingredientTypeNode);
            }
            node.set(Constants.INGREDIENT_TYPES_KEY, array);
            return ok(node);
        }
    }
}
