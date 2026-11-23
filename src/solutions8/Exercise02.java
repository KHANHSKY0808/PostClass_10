package designbook.solutions8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import designbook.chapter8.Card;
import designbook.chapter8.Rank;
import designbook.chapter8.Suit;

public class Exercise02 {

	public static void main(String[] args) {
		ObservableDeck2 deck = new ObservableDeck2();
		deck.addObserver(new DrawLogger2());
		deck.addObserver(new SizeStatus2());
		deck.shuffle();
		deck.draw();
		deck.draw();
		deck.push(Card.get(Rank.ACE, Suit.SPADES)); // them: minh hoa su kien push
	}
}

class ObservableDeck2 implements DeckView {

	private final List<Card> aCards = new ArrayList<>();
	private final List<DeckObserver2> aObservers = new ArrayList<>();
	private Optional<Card> aLastDrawn = Optional.empty();

	public ObservableDeck2() {
		shuffle();
	}

	public Card getLastDrawn() {
		return aLastDrawn.get();
	}

	public void addObserver(DeckObserver2 pObserver) {
		assert pObserver != null;
		aObservers.add(pObserver);
	}

	public void shuffle() {
		aCards.clear();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				aCards.add(Card.get(rank, suit));
			}
		}
		Collections.shuffle(aCards);
		for (DeckObserver2 observer : aObservers) {
			observer.shuffled(this);
		}
	}

	public void push(Card pCard) {
		assert pCard != null;
		aCards.add(pCard);
		for (DeckObserver2 observer : aObservers) {
			observer.cardPushed(this);
		}
	}

	public Card draw() {
		assert !isEmpty();
		Card card = aCards.remove(aCards.size() - 1);
		aLastDrawn = Optional.of(card);
		for (DeckObserver2 observer : aObservers) {
			observer.cardDrawn(this);
		}
		return card;
	}

	public boolean isEmpty() {
		return aCards.isEmpty();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(aCards);
	}
}

interface DeckObserver2 {

	void shuffled(DeckView pDeckView);
	void cardDrawn(DeckView pDeckView);
	void cardPushed(DeckView pDeckView);
}

class DrawLogger2 implements DeckObserver2 {

	@Override
	public void shuffled(DeckView pDeckView) {}

	@Override
	public void cardDrawn(DeckView pDeckView) {
		System.out.println(pDeckView.getLastDrawn() + " drawn");
	}

	@Override
	public void cardPushed(DeckView pDeckView) {}
}

class SizeStatus2 implements DeckObserver2 {

	@Override
	public void shuffled(DeckView pDeckView) {
		System.out.println(pDeckView.size());
	}

	@Override
	public void cardDrawn(DeckView pDeckView) {
		System.out.println(pDeckView.size());
	}

	@Override
	public void cardPushed(DeckView pDeckView) {
		System.out.println(pDeckView.size());
	}
}

interface DeckView {

	List<Card> getCards();
	boolean isEmpty();
	Card getLastDrawn();

	default int size() {
		return getCards().size();
	}
}
