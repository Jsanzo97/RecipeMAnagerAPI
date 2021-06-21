package database.dao;

import common.Tuple;
import io.ebean.Finder;
import models.*;
import models.enums.CategoryValue;
import models.enums.DifficultValue;
import models.enums.SubcategoryValue;
import operationResults.ErrorResult;
import operationResults.ErrorType;
import operationResults.OkResult;
import operationResults.OperationResult;
import play.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

public class RecipeDao {

    private final Finder<Long, Recipe> find = new Finder<>(Recipe.class);

    public OperationResult saveRecipe(User user, Recipe recipe, Messages messages) {
        if (user == null) {
            return new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION"));
        } else {
            Tuple<OperationResult, Recipe> recipeFound = getRecipeWithName(user, recipe.getName(), messages);

            if (recipeFound.right() != null) {
                return new ErrorResult(ErrorType.RECIPE_WITH_NAME_ALREADY_EXISTS, messages.at("RECIPE_ALREADY_EXISTS_ERROR_DESCRIPTION"));
            } else if (recipeFound.left() instanceof ErrorResult && (
                    ((ErrorResult) recipeFound.left()).getErrorType() == ErrorType.USER_WITH_USERNAME_NO_EXISTS ||
                    ((ErrorResult) recipeFound.left()).getErrorType() == ErrorType.RECIPE_WITH_NAME_ALREADY_EXISTS
                )
            ) {
                return recipeFound.left();
            } else {
                ArrayList<Recipe> recipes;
                if (user.getRecipes() == null) {
                    recipes = new ArrayList<>();
                } else {
                    recipes = new ArrayList<>(user.getRecipes());
                }
                recipes.add(recipe);

                user.setRecipes(recipes);
                user.save();
                recipe.save();

                return new OkResult(messages.at("OK_OPERATION_RESULT"));
            }
        }
    }

    public Tuple<OperationResult, ArrayList<Recipe>> getRecipesFromUser(User user, Messages messages) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            List<Recipe> recipes = find
                    .query()
                    .where()
                    .eq("user_id", user.getId())
                    .findList();
            operationResult = new Tuple<>(
                    new OkResult(messages.at("OK_OPERATION_RESULT")),
                    new ArrayList<>(recipes)
            );
        }
        return operationResult;
    }

    public Tuple<OperationResult, Recipe> getRecipeWithName(User user, String name, Messages messages) {
        Tuple<OperationResult, Recipe> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            Recipe recipe = find
                    .query()
                    .where()
                    .eq("name", name)
                    .eq("user_id", user.getId())
                    .findOne();
            if (recipe == null) {
                operationResult = new Tuple<>(
                        new ErrorResult(ErrorType.RECIPE_WITH_NAME_NO_EXISTS, messages.at("RECIPE_NOT_EXISTS_ERROR_DESCRIPTION")),
                        null
                );
            } else {
                operationResult = new Tuple<>(
                        new OkResult(messages.at("OK_OPERATION_RESULT")),
                        recipe
                );
            }
        }
        return operationResult;
    }

    public Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithMoreDuration(User user, double duration, Messages messages) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            List<Recipe> recipes = find
                    .query()
                    .where()
                    .ge("duration_in_hours", duration)
                    .eq("user_id", user.getId())
                    .findList();
            operationResult = new Tuple<>(
                    new OkResult(messages.at("OK_OPERATION_RESULT")),
                    new ArrayList<>(recipes)
            );
        }

        return operationResult;
    }

    public Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithLessDuration(User user, double duration, Messages messages) {
        Tuple<OperationResult, ArrayList<Recipe>> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            List<Recipe> recipes = find
                    .query()
                    .where()
                    .le("duration_in_hours", duration)
                    .eq("user_id", user.getId())
                    .findList();
            operationResult = new Tuple<>(
                    new OkResult(messages.at("OK_OPERATION_RESULT")),
                    new ArrayList<>(recipes)
            );
        }

        return operationResult;
    }

    public Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithDifficult(User user, String difficult, Messages messages) {
        DifficultValue validDifficult = Difficult.getDifficultAsEnum(difficult);

        Tuple<OperationResult, ArrayList<Recipe>> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            List<Recipe> recipes = find
                    .query()
                    .where()
                    .eq("user_id", user.getId())
                    .findList();

            ArrayList<Recipe> result = new ArrayList<>();
            for (Recipe recipe : recipes) {
                if (recipe.getDifficult().getValue() == validDifficult) {
                    result.add(recipe);
                }
            }

            operationResult = new Tuple<>(
                    new OkResult(messages.at("OK_OPERATION_RESULT")),
                    result
            );
        }

        return operationResult;
    }

    public Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithCategory(User user, String category, Messages messages) {
        CategoryValue validCategory = Category.getCategoryAsEnum(category);
        Tuple<OperationResult, ArrayList<Recipe>> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            List<Recipe> recipes = find
                    .query()
                    .where()
                    .eq("user_id", user.getId())
                    .findList();

            ArrayList<Recipe> result = new ArrayList<>();
            for (Recipe recipe : recipes) {
                if (recipe.getCategory().getValue() == validCategory) {
                    result.add(recipe);
                }
            }
            operationResult = new Tuple<>(
                    new OkResult(messages.at("OK_OPERATION_RESULT")),
                    result
            );
        }

        return operationResult;
    }

    public Tuple<OperationResult, ArrayList<Recipe>> getRecipesWithSubcategory(User user, String category, Messages messages) {
        SubcategoryValue validSubcategory = Subcategory.getSubcategoryAsEnum(category);
        Tuple<OperationResult, ArrayList<Recipe>> operationResult;

        if (user == null) {
            operationResult = new Tuple<>(
                    new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION")),
                    null
            );
        } else {
            List<Recipe> recipes = find
                    .query()
                    .where()
                    .eq("user_id", user.getId())
                    .findList();

            ArrayList<Recipe> result = new ArrayList<>();
            for (Recipe recipe : recipes) {
                for (Subcategory subcategory : recipe.getSubcategories()) {
                    if (subcategory.getValue() == validSubcategory) {
                        result.add(recipe);
                        break;
                    }
                }
            }
            operationResult = new Tuple<>(
                    new OkResult(messages.at("OK_OPERATION_RESULT")),
                    result
            );
        }

        return operationResult;
    }

    public OperationResult removeRecipe(User user, String name, Messages messages) {
        if (user == null) {
            return new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION"));
        } else {
            Tuple<OperationResult, Recipe> recipeFound = getRecipeWithName(user, name, messages);

            if (recipeFound.right() == null) {
                return new ErrorResult(ErrorType.RECIPE_WITH_NAME_NO_EXISTS, messages.at("RECIPE_NOT_EXISTS_ERROR_DESCRIPTION"));
            } else if (recipeFound.left() instanceof ErrorResult){
                return recipeFound.left();
            } else {
                user.getRecipes().removeIf(recipe -> recipe.getName().equals(name));
                user.save();
                recipeFound.right().delete();
                return new OkResult(messages.at("OK_OPERATION_RESULT"));
            }
        }
    }

    public OperationResult updateRecipe(User user, Recipe recipe, Messages messages) {
        if (user == null) {
            return new ErrorResult(ErrorType.USER_WITH_USERNAME_NO_EXISTS, messages.at("USERNAME_NOT_EXISTS_ERROR_DESCRIPTION"));
        } else {
            recipe.update();
            return new OkResult(messages.at("OK_OPERATION_RESULT"));
        }
    }
}
