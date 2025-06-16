package br.edu.ifsp.spo.java.cards.core;

import br.edu.ifsp.spo.java.cards.items.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {

    private String name;
    private List<Card> hand;
    private int score;

    public Player(String name) {
        this.hand = new ArrayList<>();
        this.name = name;
    }

    public void receiveCard(Card card) {
        this.hand.add(card);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Player: ").append(this.getName()).append("\n").append("- Current hand: \n").append(printHand());

        return str.toString();
    }

    public String getName() {
        return this.name;
    }

    public String printHand() {
        ArrayList<String[]> linhasDeCartas = new ArrayList<>();

        for (Card card : hand) {
            String[] linhas = card.toString().split("\n");
            linhasDeCartas.add(linhas);
        }

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < linhasDeCartas.get(0).length; i++) {
            for (String[] card : linhasDeCartas) {
                str.append(String.format("%-15s", card[i])).append("");
            }
            str.append("\n");
        }

        return str.toString();
    }

    public List<Card> getHand() {
        return this.hand;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Card> discardHand() {
        var discardedCards = new ArrayList<>(this.hand);

        this.hand.clear();

        return discardedCards;
    }
}
