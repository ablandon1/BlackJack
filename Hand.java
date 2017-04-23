
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;


/*
 * Instance of player hand
 *
 * @author Andres Blandon
 * @created 4/17/17
 */
public class Hand {

    private ObservableList<Node> cards;
    private SimpleIntegerProperty value = new SimpleIntegerProperty();
    private Rank rank;
    private int aces = 0;

    public Hand(ObservableList<Node> cards) {
        this.cards = cards;
    }

    public void takeCard(Card card) {
        cards.add(card);

        if (value.get() + card.value > 21 && aces > 0) {
            value.set(value.get() + card.value - 10);    //then count ace as '1' not '11'
            aces--;
        }
        else {
            value.set(value.get() + card.value);
        }
    }

    public void reset() {
        cards.clear();
        value.set(0);
        aces = 0;
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }
}