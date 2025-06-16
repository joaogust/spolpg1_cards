package br.edu.ifsp.spo.java.cards.rules;

import java.util.List;
import java.util.Map;

import br.edu.ifsp.spo.java.cards.core.Player;
import br.edu.ifsp.spo.java.cards.items.Card;

//Esta interface serve para permitir que o jogo possua múltiplas regras de pontuação. Dessa forma, no jogo eu consigo
//usar um mecanismo de pontuação sem me preocupar com como ele funciona internamente. Para isso, utilizo o conceito chamado
// "Polimorfismo"
public interface Scorer {
    int calculateScore(List<Card> cards);

    Map<Player, Integer> calculateTurnScore(Player player1, Player player2);
}
