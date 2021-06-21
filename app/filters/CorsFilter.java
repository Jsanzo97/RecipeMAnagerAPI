package filters;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

@Singleton
public class CorsFilter extends Filter {

    @Inject
    public CorsFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(Function<RequestHeader, CompletionStage<Result>> next, RequestHeader rh) {

        return next.apply(rh).thenApply(result ->
            result.withHeaders(
                    "Access-Control-Allow-Origin", "*",
                    "Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE, HEAD",
                    "Access-Control-Allow-Headers", "Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With",
                    "Access-Control-Allow-Credentials", "true"
            )
        );
    }
}
