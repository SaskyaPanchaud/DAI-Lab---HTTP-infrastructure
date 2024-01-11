package dai.http.api;

import java.util.concurrent.ConcurrentHashMap;
import io.javalin.http.Context;

public class QuoteController {
    private ConcurrentHashMap<Integer, Quote> quotes = new ConcurrentHashMap<>();
    private int nextId = 0;

    private void addQuote(Quote quote) {
        quotes.put(nextId++, quote);
    }

    public QuoteController() {
        addQuote(new Quote("Gandhi", "La vie est un mystère qu'il faut vivre, et non un problème à résoudre."));
        addQuote(new Quote("Confucius", "Choisissez un travail que vous aimez et vous n'aurez pas à travailler un seul jour de votre vie."));
        addQuote(new Quote("Albert Einstein", "Tout le monde est un génie. Mais si vous jugez un poisson à sa capacité de grimper à un arbre, il vivra toute sa vie en croyant qu'il est stupide."));
        addQuote(new Quote("Abbé Pierre", "Un sourire coûte moins cher que l'électricité, mais donne autant de lumière."));
        addQuote(new Quote("Jacques Prévert", "Il faudrait essayer d'être heureux, ne serait-ce que pour donner l'exemple."));
        addQuote(new Quote("Platon", "La musique donne une âme à nos coeurs et des ailes à la pensée."));
        addQuote(new Quote("Coluche", "La vie mettra des pierres sur ta route. A toi de décider d'en faire des murs ou des ponts."));
        addQuote(new Quote("Nikola Tesla", "Si vous voulez trouver les secrets de l'univers, pensez en termes d'énergie, de fréquence, d'information et de vibration."));
        addQuote(new Quote("Oscar Wilde", "La beauté est dans les yeux de celui qui regarde."));
        addQuote(new Quote("Socrate", "Tout ce que je sais, c'est que je ne sais rien."));
        addQuote(new Quote("Jean-Claude Van Damme", "Si tu téléphones à une voyante et qu'elle ne décroche pas avant que ça sonne, raccroche."));
        addQuote(new Quote("Albert Einstein", "Deux choses sont infinies : l'Univers et la bêtise humaine. Mais en ce qui concerne l'Univers, je n'en ai pas encore acquis la certitude absolue."));
        addQuote(new Quote("Jean-Paul Sartre", "Nous sommes nos choix."));
        addQuote(new Quote("Archimède", "Donnez-moi un point fixe et un levier et je soulèverai la Terre."));
        addQuote(new Quote("Antoine de Saint-Exupéry", "On ne voit bien qu'avec le coeur. L'essentiel est invisible pour les yeux."));
        addQuote(new Quote("Victor Hugo", "La liberté commence où l'ignorance finit."));
        addQuote(new Quote("Pierre de Coubertin", "Le sport va chercher la peur pour la dominer, la fatigue pour en triompher, la difficulté pour la vaincre."));
        addQuote(new Quote("Voltaire", "Le bonheur est souvent la seule chose qu'on puisse donner sans l'avoir et c'est en le donnant qu'on l'acquiert."));
        addQuote(new Quote("Bill Gates", "Ce n'est pas votre faute si vous êtes né pauvre. En revanche, si vous mourrez pauvre, c'est votre erreur."));
        addQuote(new Quote("Pythagore", "Un homme n'est jamais si grand que lorsqu'il est à genoux pour aider un enfant."));
    }

    public void getOne(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        if (!quotes.containsKey(id)) {
            ctx.json("This quote doesn't exist...").status(404);
            return;
        }
        ctx.json(quotes.get(id));
    }

    public void getAll(Context ctx) {
        ctx.json(quotes);
        System.out.println("-- NEW REQUEST --");
    }

    public void create(Context ctx) {
        Quote quote = ctx.bodyAsClass(Quote.class);
        addQuote(quote);
        ctx.status(201);
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        quotes.remove(id);
        ctx.status(204);
    }

    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Quote quote = ctx.bodyAsClass(Quote.class);
        quotes.put(id, quote);
        ctx.status(200);
    }
}