package edu.insightr.spellmonger;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;


public class Deck {

    Random randomGenerator;

    private String deckOwner; // type Player?
    private int size;
    private List<Card> deck = new ArrayList<Card>();

    Deck(int size, String playerName) {
        this.size = size;
        this.deck = createDeck(size, playerName);
        this.setDeckOwner(playerName);
    }

    public void addToList(List<Card> list, Card card, int dupli) {
        for (int i = 0; i < dupli; i++) {
            card.setOwner(deckOwner);
            list.add(card);
        }
    }

    public List<Card> createDeck(int size, String playerName) {

        List<Card> possibleCards = new ArrayList<>();

        int creatureNumber = (int) (size * 0.75); // ~75% of crea in a deck
        int rituolOrEnchantNumber = (int) (size * 0.25); // ~25% of ritual or enchant in a deck

        int differentCrea = 5;
        int differentRituOrEnch = 5;

        int uniqueCreaNumber = (int) (creatureNumber / differentCrea);
        int uniqueRituolNumber = (int) (rituolOrEnchantNumber / differentRituOrEnch);

        int cardMissingNumber = size - (uniqueCreaNumber * differentCrea + uniqueRituolNumber * differentRituOrEnch);

        for (int i = 0; i < uniqueCreaNumber; i++) {
            Bear bear = new Bear("bear", playerName);
            Eagle eagle = new Eagle("eagle", playerName);
            Wolf wolf = new Wolf("wolf", playerName);
            Fox fox = new Fox("fox", playerName);
            Dragon dragon = new Dragon("dragon", playerName);

            addToList(possibleCards, wolf, uniqueCreaNumber);
            addToList(possibleCards, eagle, uniqueCreaNumber);
            addToList(possibleCards, fox, uniqueCreaNumber);
            addToList(possibleCards, bear, uniqueCreaNumber);
            addToList(possibleCards, dragon, uniqueCreaNumber);
        }

        for (int i = 0; i < uniqueRituolNumber; i++) {
            Curse curse = new Curse("curse", playerName);
            Blessing blessing = new Blessing("blessing", playerName);
            EnergyDrain energyDrain = new EnergyDrain("energy drain");
            VaultOverclocking vault = new VaultOverclocking("vault overclocking", playerName);
            SkinStrengthening skinStrength = new SkinStrengthening("skin strengthening", playerName);

            addToList(possibleCards, energyDrain, uniqueRituolNumber);
            addToList(possibleCards, blessing, uniqueRituolNumber);
            addToList(possibleCards, curse, uniqueRituolNumber);
            addToList(possibleCards, vault, uniqueRituolNumber);
            addToList(possibleCards, skinStrength, uniqueRituolNumber);
        }

        randomGenerator = new Random();
        int randIndex = randomGenerator.nextInt(uniqueCreaNumber * differentCrea + uniqueRituolNumber * differentRituOrEnch);
        addToList(possibleCards, possibleCards.get(randIndex), cardMissingNumber);

        Collections.shuffle(possibleCards);

        return possibleCards;
    }

    public int getSize() {
        return size;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public String getDeckOwner() {
        return deckOwner;
    }

    public void setDeckOwner(String deckOwner) {
        this.deckOwner = deckOwner;
    }
}