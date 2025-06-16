package br.edu.ifsp.spo.java.cards.rules;

import br.edu.ifsp.spo.java.cards.core.Player;
import br.edu.ifsp.spo.java.cards.items.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicScorer implements Scorer {

    private int calculateScore(Card card){
        //Este é um exemplo de um switch usado como expression (expressão). Neste caso, o switch retorna algo, que pode ser atribuído
        //a uma variável ou, como é o caso, usado como retorno. Observe que se eu uso a sintaxe de switch como statement (declaração),
        // que é a maneira "clássica", eu não consigo fazer isso
        return switch (card.getRank()) {
            case ACE -> 1;
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

    @Override
    public int calculateScore(List<Card> cards) {
        //Aqui uma forma de escrever a interação em uma lista:
        // - a transformação dela em outra "coisa": map transforma cada item da lista em alguma outra coisa, no caso, o
        // resultado do método "calculateScore"
        // - Com o resultado do map (uma lista de inteiros), eu aplico a função "reduce" que, basicamente, aglutina a lista
        // seguindo como regra a função passada como parâmetro. Observe que reduce recebe dois parâmetros: o primeiro, identity,
        // representa o elemento neutro da operação, ou seja, qual o valor inicial da operação. O segundo, a operação que será
        // aplicada, no caso a soma de dois inteiros
        // A forma "expandida" do reduce seria algo assim: .reduce(0, (subtotal, element) -> subtotal + element)
        // Como é uma operação comum, o Java já possui o Integer::sum como forma de fazer isso

        //Adicionei depois: Validação
        if(cards == null)
            return 0;

        return cards.stream().map(this::calculateScore).reduce(0, Integer::sum);
    }

    @Override
    public Map<Player, Integer> calculateTurnScore(Player player1, Player player2) {

        var handScorePlayer1 = this.calculateScore(player1.getHand());
        var handScorePlayer2 = this.calculateScore(player2.getHand());

        var turnScorePlayer1 = 0;
        var turnScorePlayer2 = 0;

        var result = new HashMap<Player, Integer>();

        if(handScorePlayer1 < 21 && handScorePlayer2 > 21){
            turnScorePlayer1 = handScorePlayer1;
            turnScorePlayer2 = -5;
        }

        if(handScorePlayer1 > 21 && handScorePlayer2 < 21) {
            turnScorePlayer1 = -5;
            turnScorePlayer2 = handScorePlayer2;
        }

        if(handScorePlayer1 > 21 && handScorePlayer2 > 21) {
            turnScorePlayer1 = 21 - handScorePlayer1;
            turnScorePlayer2 = 21 - handScorePlayer2;
        }

        if(handScorePlayer1 == 21 && handScorePlayer2 < 21){
            turnScorePlayer1 = 30;
            turnScorePlayer2 = 0;
        }

        if(handScorePlayer1 < 21 && handScorePlayer2 == 21){
            turnScorePlayer1 = 0;
            turnScorePlayer2 = 30;
        }

        if(handScorePlayer1 < 21 && handScorePlayer2 < 21 && (handScorePlayer1 > handScorePlayer2)){
            turnScorePlayer1 = handScorePlayer1 - handScorePlayer2;
            turnScorePlayer2 = 0;
        }

        if(handScorePlayer1 < 21 && handScorePlayer2 < 21 && (handScorePlayer1 < handScorePlayer2)){
            turnScorePlayer1 = 0;
            turnScorePlayer2 = handScorePlayer2 - handScorePlayer1;
        }

        if(handScorePlayer1 == 21 && handScorePlayer2 == 21){
            turnScorePlayer1 = 21;
            turnScorePlayer2 = 21;
        } else if(handScorePlayer1 == handScorePlayer2){
            turnScorePlayer1 = 10;
            turnScorePlayer2 = 10;
        }

        result.put(player1, turnScorePlayer1);
        result.put(player2, turnScorePlayer2);

        return result;



//        var scorePlayer1 = this.calculateScore(player1.getHand());
//        var scorePlayer2 = this.calculateScore(player2.getHand());
//
//        var turnScorePlayer1 = 0;
//        var turnScorePlayer2 = 0;
//
//        //TODO: Aqui eu vou calcular a pontuação com base nas regras definidas na aplicação
//
//        var results = new HashMap<Player, Integer>();
//
//        results.put(player1, turnScorePlayer1);
//        results.put(player2, turnScorePlayer2);

//        return results;
    }
}
