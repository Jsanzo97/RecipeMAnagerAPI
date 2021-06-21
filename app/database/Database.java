package database;

import common.Tuple;
import database.dao.RecipeDao;
import database.dao.UserDao;
import models.*;
import operationResults.ErrorResult;
import operationResults.OperationResult;
import play.i18n.Messages;

import java.util.ArrayList;

public class Database {

    private static final UserDao userDao = new UserDao();
    private static final RecipeDao recipeDao = new RecipeDao();

    public static OperationResult signUp(User user, Messages messages) {
        return userDao.signUp(user.getUsername(), user.getPassword(), messages);
    }

    public static OperationResult logIn(User user, Messages messages) {
        return userDao.logIn(user.getUsername(), user.getPassword(), messages);
    }

    public static Tuple<OperationResult, ArrayList<Recipe>> getRecipesFromUser(String username, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipesFromUser(user, messages);
    }

    public static OperationResult createRecipe(String username, Recipe recipe, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.saveRecipe(user, recipe, messages);
    }

    public static Tuple<OperationResult, Recipe> getRecipeByName(String username, String name, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipeWithName(user, name, messages);
    }

    public static Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithMoreDuration(String username, double duration, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipesWithMoreDuration(user, duration, messages);
    }

    public static Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithLessDuration(String username, double duration, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipesWithLessDuration(user, duration, messages);
    }

    public static Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithDifficult(String username, String difficult, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipesWithDifficult(user, difficult, messages);
    }

    public static Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithCategory(String username, String category, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipesWithCategory(user, category, messages);
    }

    public static Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithSubcategory(String username, String subcategory, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.getRecipesWithSubcategory(user, subcategory, messages);
    }

    public static OperationResult removeRecipeByName(String username, String name, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.removeRecipe(user, name, messages);
    }


    public static OperationResult updateRecipe(String username, Recipe recipe, Messages messages) {
        User user = userDao.findUserByUsername(username);
        return recipeDao.updateRecipe(user, recipe, messages);
    }

    public static OperationResult updateDescription(String username, String name, String description, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.setDescription(description);
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult updateDescription(String username, String name, double duration, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.setDurationInHours(duration);
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult updateDifficult(String username, String name, String difficult, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.setDifficult(new Difficult(difficult));
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult updateCategory(String username, String name, String category, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.setCategory(new Category(category));
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult addIngredientsToRecipe(String username, String name, ArrayList<Ingredient> ingredients, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.addIngredients(ingredients);
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult removeIngredientsFromRecipe(String username, String name, ArrayList<Ingredient> ingredients, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.removeIngredients(ingredients);
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult addSubcategoriesToRecipe(String username, String name, ArrayList<Subcategory> subcategories, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.addSubcategories(subcategories);
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }

    public static OperationResult removeSubcategoriesFromRecipe(String username, String name, ArrayList<Subcategory> subcategories, Messages messages) {
        User user = userDao.findUserByUsername(username);

        Tuple<OperationResult, Recipe> getRecipeResult = recipeDao.getRecipeWithName(user, name, messages);
        if (getRecipeResult.left() instanceof ErrorResult) {
            return getRecipeResult.left();
        } else {
            Recipe recipe = getRecipeResult.right();
            recipe.removeSubcategories(subcategories);
            return recipeDao.updateRecipe(user, recipe, messages);
        }
    }
}
