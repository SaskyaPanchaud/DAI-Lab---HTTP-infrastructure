package dai.http.api;

public class Quote {
    public String author = "";
    public String quote = "";

    public Quote() {
    }

    public Quote(String author, String quote) {
        this.author = author;
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }
}