package dai.http.api;

import io.javalin.*;

public class Main {
    public static void main(String[] args) {
        //Javalin app = Javalin.create().start(12345);
        Javalin app = Javalin.create().start(80);

        QuoteController quoteController = new QuoteController();

        // FIXME : endpoints
        app.get("/api/quotes", quoteController::getAll);
        app.get("/api/quotes/{id}", quoteController::getOne);
        // TODO : faire formulaires HTML / JavaScript pour post, put et delete (pour l'instant que get)
        app.post("/api/quotes/", quoteController::create);
        app.put("/api/quotes/{id}", quoteController::update);
        app.delete("/api/quotes/{id}", quoteController::delete);
    }
}