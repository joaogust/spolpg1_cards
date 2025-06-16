package br.edu.ifsp.spo.java.cards;

import br.edu.ifsp.spo.java.cards.core.Game;
import br.edu.ifsp.spo.java.cards.items.Card;
import br.edu.ifsp.spo.java.cards.items.Rank;
import br.edu.ifsp.spo.java.cards.items.Suit;
import br.edu.ifsp.spo.java.cards.ui.TerminalGameUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {
        var vinteUm = new Game(new TerminalGameUI());

        vinteUm.play();
    }
}