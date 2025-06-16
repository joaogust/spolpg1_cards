package br.edu.ifsp.spo.java.cards.rules;

import br.edu.ifsp.spo.java.cards.core.Player;
import br.edu.ifsp.spo.java.cards.items.Card;

import java.util.List;
import java.util.Map;

public class AceElevenScorer implements Scorer{

    @Override
    public int calculateScore(List<Card> cards) {
        var score = 0;

        for(Card card : cards){
            score += switch(card.getRank()){
                case ACE -> 11;
                case TWO -> 2;
                case THREE -> 3;
                case FOUR -> 4;
                case FIVE -> 5;
                case SIX -> 6;
                case SEVEN -> 7;
                case EIGHT -> 8;
                case NINE -> 9;
                case TEN, JACK, QUEEN, KING -> 10;
            };
        }

        return score;
    }

    @Override
    public Map<Player, Integer> calculateTurnScore(Player player1, Player player2) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
