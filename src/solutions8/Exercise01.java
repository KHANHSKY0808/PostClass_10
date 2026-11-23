package designbook.solutions8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import designbook.chapter8.Card;
import designbook.chapter8.Rank;
import designbook.chapter8.Suit;

public class Exercise01 {

	public static void main(String[] args) {
		ObservableDeck1 deck = new ObservableDeck1();
		deck.addObserver(new DrawLogger1());
		deck.addObserver(new SizeStatus1());
		deck.shuffle();
		deck.draw();
		deck.draw();
		deck.push(Card.get(Rank.ACE, Suit.SPADES)); // them: minh hoa su kien push
	}
}

interface DeckObserver1 {

	void shuffled();
	void cardDrawn(Card pCard);
	void cardPushed(Card pCard);
}

class ObservableDeck1 {

	private final List<Card> aCards = new ArrayList<>();
	private final List<DeckObserver1> aObservers = new ArrayList<>();

	public ObservableDeck1() {
		shuffle();
	}

	public void addObserver(DeckObserver1 pObserver) {
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
		for (DeckObserver1 observer : aObservers) {
			observer.shuffled();
		}
	}

	public void push(Card pCard) {
		assert pCard != null;
		aCards.add(pCard);
		for (DeckObserver1 observer : aObservers) {
			observer.cardPushed(pCard);
		}
	}

	public Card draw() {
		assert !isEmpty();
		Card card = aCards.remove(aCards.size() - 1);
		for (DeckObserver1 observer : aObservers) {
			observer.cardDrawn(card);
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

class DrawLogger1 implements DeckObserver1 {

	@Override
	public void shuffled() {}

	@Override
	public void cardDrawn(Card pCard) {
		System.out.println(pCard + " drawn");
	}

	@Override
	public void cardPushed(Card pCard) {}
}

class SizeStatus1 implements DeckObserver1 {

	private int aSize = -1;

	@Override
	public void shuffled() {
		aSize = 52;
		System.out.println(aSize);
	}

	@Override
	public void cardDrawn(Card pCard) {
		if (aSize >= 0) {
			aSize--;
			System.out.println(aSize);
		}
		else {
			System.out.println("Size unknown");
		}
	}

	@Override
	public void cardPushed(Card pCard) {
		if (aSize >= 0) {
			aSize++;
			System.out.println(aSize);
		}
		else {
			System.out.println("Size unknown");
		}
	}
}
