# Recipe-Manager-API

This is a application that allow the user to create, remove, edit and consult cooking recipes.

## Running

Run this using [sbt](http://www.scala-sbt.org/).

```
sbt run
```

And then go to http://localhost:9000 to see the running web application.

## Controllers

There are several demonstration files available in this template.

- `RecipesApplication.java`:

  Handle all the HTTP requests and provides the responses.

- `OptionsController.java`:

  Deal with the CORS functionallity.

## Filters

- `CorsFilter.java`:

  CORS filter to improve the security.
