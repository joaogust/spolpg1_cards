package br.edu.ifsp.spo.java.cards.items;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Card {
    private final Suit suit;
    private final Rank rank;
    private final String wayFileCard;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.wayFileCard = createWayFileCard();
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public String getWayFileCard() {
        return wayFileCard;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        try (BufferedReader leitor = new BufferedReader(new FileReader(wayFileCard))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                str.append(linha).append("\n");
            }
        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return suit == card.suit && rank == card.rank;
    }

    private String createWayFileCard() {
        StringBuilder str = new StringBuilder();

        str.append("src/resources/cards/");

        switch (suit) {
            case HEARTS -> str.append("hearts/");
            case DIAMONDS -> str.append("diamonds/");
            case CLUBS -> str.append("clubs/");
            case SPADES -> str.append("spades/");
        }

        switch (rank) {
            case ACE -> str.append("ace.txt");
            case TWO -> str.append("two.txt");
            case THREE -> str.append("three.txt");
            case FOUR -> str.append("four.txt");
            case FIVE -> str.append("five.txt");
            case SIX -> str.append("six.txt");
            case SEVEN -> str.append("seven.txt");
            case EIGHT -> str.append("eight.txt");
            case NINE -> str.append("nine.txt");
            case TEN -> str.append("ten.txt");
            case JACK -> str.append("jack.txt");
            case QUEEN -> str.append("queen.txt");
            case KING -> str.append("king.txt");
        }
        return str.toString();
    }
}
