package br.edu.ifsp.spo.java.cards.rules;

import br.edu.ifsp.spo.java.cards.core.Player;
import br.edu.ifsp.spo.java.cards.items.Card;
import br.edu.ifsp.spo.java.cards.items.Rank;
import br.edu.ifsp.spo.java.cards.items.Suit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

record SampleCardSet(List<Card> cards, int score) { }

abstract class SamplePlayerFactory {

    public static Player buildPlayerWith(List<Card> hand){
        //Uso isto para criar um jogador com nome único.
        var player = new Player("Sample Random Player " + UUID.randomUUID());

        //Adiciono as cartas na mão desse jogador
        hand.forEach(player::receiveCard);

        return player;
    }
}

abstract class CardsSamples {

    public static final SampleCardSet emptySet = new SampleCardSet(new ArrayList<Card>(), 0);

    public static final SampleCardSet blackjackSet1 = new SampleCardSet(new ArrayList<Card>(Arrays.asList(
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.HEARTS, Rank.EIGHT),
            new Card(Suit.SPADES, Rank.TWO),
            new Card(Suit.DIAMONDS, Rank.QUEEN)
    )), 21);

    public static final SampleCardSet blackjackSet2 = new SampleCardSet(new ArrayList<Card>(Arrays.asList(
            new Card(Suit.HEARTS, Rank.TWO),
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.QUEEN)
    )), 21);

    public static final SampleCardSet bustedSet1 = new SampleCardSet(new ArrayList<>(Arrays.asList(
            new Card(Suit.HEARTS, Rank.QUEEN),
            new Card(Suit.SPADES, Rank.KING),
            new Card(Suit.DIAMONDS, Rank.SEVEN)
    )), 27);

    public static final SampleCardSet bustedSet2 = new SampleCardSet(new ArrayList<>(Arrays.asList(
            new Card(Suit.HEARTS, Rank.NINE),
            new Card(Suit.SPADES, Rank.NINE),
            new Card(Suit.DIAMONDS, Rank.KING)
    )), 28);

    public static final SampleCardSet regularSet1 = new SampleCardSet(new ArrayList<>(Arrays.asList(
            new Card(Suit.HEARTS, Rank.EIGHT),
            new Card(Suit.SPADES, Rank.KING)
    )), 18);

    public static final SampleCardSet regularSet1b = new SampleCardSet(new ArrayList<>(Arrays.asList(
            new Card(Suit.HEARTS, Rank.NINE),
            new Card(Suit.SPADES, Rank.NINE)
    )), 18);

    public static final SampleCardSet regularSet2 = new SampleCardSet(new ArrayList<>(Arrays.asList(
            new Card(Suit.HEARTS, Rank.SEVEN),
            new Card(Suit.SPADES, Rank.SIX),
            new Card(Suit.HEARTS, Rank.SEVEN)
    )), 20);

    public static Stream<SampleCardSet> sampleSets(){
        return Stream.of(
                CardsSamples.blackjackSet1,
                CardsSamples.bustedSet1,
                CardsSamples.regularSet1,
                CardsSamples.emptySet
        );
    }
}

class BasicScorerTest {
    @ParameterizedTest
    @DisplayName("calculateScore with multiple scenarios should return the correct score")
    @MethodSource("br.edu.ifsp.spo.java.cards.rules.CardsSamples#sampleSets")
    void calculateScore(SampleCardSet sampleSet){
        //Setup
        var scorer = new BasicScorer();

        //Execute
        var result =  scorer.calculateScore(sampleSet.cards());

        //Assert
        assertEquals(sampleSet.score(), result);
    }

    @Test
    @DisplayName("calculateScore with a null list of cards should return 0")
    void calculateScoreWhenNull() {
        //Setup
        var scorer = new BasicScorer();

        //Execute
        var result = scorer.calculateScore(null);

        //Assert
        assertEquals(0, result);
    }

    @Test
    @DisplayName("calculateTurnScore should return 10 points for each player when there is a draw and nobody is busted")
    void calculateTurnScoreRegularDraw(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.regularSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.regularSet1b;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, 10,
                player2, 10
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when player1 bellow 21 and player2 is busted, player1 should obtain the score of the cards and player 2 should obtain -5")
    void calculateTurnScorePlayer1OkPlayer2Busted(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.regularSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.bustedSet1;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, player1Set.score(),
                player2, -5
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when player2 bellow 21 and player1 is busted, player2 should obtain the score of the cards and player 1 should obtain -5")
    void calculateTurnScorePlayer1BustedPlayer2Ok(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.bustedSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.regularSet1;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, -5,
                player2, player2Set.score()
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when both are busted, player2 they should obtain minus the number of points above 21")
    void calculateTurnScoreBothBusted(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.bustedSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.bustedSet2;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, 21 - player1Set.score(),
                player2, 21 - player2Set.score()
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when both are with 21, both should obtain 21 points")
    void calculateTurnScoreBoth21(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.blackjackSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.blackjackSet2;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, 21,
                player2, 21
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when player1 has 21 and player2 has bellow 21, player1 receives 30 points and player2 receives 0")
    void calculateTurnScorePlayer1Has21AndPlayer2HasBellow21(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.blackjackSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.regularSet1;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, 30,
                player2, 0
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when player2 has 21 and player1 has bellow 21, player2 receives 30 points and player1 receives 0")
    void calculateTurnScorePlayer2Has21AndPlayer1HasBellow21(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.regularSet2;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.blackjackSet2;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        var expectedResult = new HashMap<>(Map.of(
                player1, 0,
                player2, 30
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when both are bellow 21 but player1 has more points than player2, player1 receives the difference of cards score")
    void calculateTurnScoreBothBellowWinningPlayer1(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.regularSet2;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.regularSet1;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        //Esta asserção é para verificar que a configuração respeita o cenário (para evitar de quebrar o teste se a configuração do conjunto for alterada
        assertTrue(player1Set.score() > player2Set.score());

        var expectedResult = new HashMap<>(Map.of(
                player1, player1Set.score() - player2Set.score(),
                player2, 0
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("calculateTurnScore, when both are bellow 21 but player2 has more points than player1, player2 receives the difference of cards score")
    void calculateTurnScoreBothBellowWinningPlayer2(){
        //Setup
        var scorer = new BasicScorer();

        var player1Set = CardsSamples.regularSet1;
        var player1 = SamplePlayerFactory.buildPlayerWith(player1Set.cards());

        var player2Set = CardsSamples.regularSet2;
        var player2 = SamplePlayerFactory.buildPlayerWith(player2Set.cards());

        //Esta asserção é para verificar que a configuração respeita o cenário (para evitar de quebrar o teste se a configuração do conjunto for alterada
        assertTrue(player1Set.score() < player2Set.score());

        var expectedResult = new HashMap<>(Map.of(
                player1, 0,
                player2, player2Set.score() - player1Set.score()
        ));

        //Execute
        var result = scorer.calculateTurnScore(player1, player2);

        //Assert
        assertEquals(expectedResult, result);
    }
}