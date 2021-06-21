package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class OptionsController extends Controller {

    public Result options(String path) {

        return ok("").withHeaders(
                "Access-Control-Allow-Origin" , "*",
                "Access-Control-Allow-Methods" , "GET, POST, PUT, DELETE, OPTIONS",
                "Access-Control-Allow-Headers" , "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
                "Access-Control-Allow-Credentials" , "true",
                "Access-Control-Max-Age" , Integer.toString(60 * 60 * 24)
        );
    }
}