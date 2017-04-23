import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/* Details of each card
*
* @author Andres Blandon
* @created 4/17/17
*/
public class Card extends Parent {

    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;

    

      public String displayName() {
            return String.valueOf(value);
        }
    

    private Suit suit;
    Rank rank;
    int value;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;

        Rectangle bg = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(Color.WHITE);

        Text text1 = new Text(rank.toString());
        text1.setFont(Font.font(18));
        text1.setX(CARD_WIDTH - text1.getLayoutBounds().getWidth() - 10);
        text1.setY(text1.getLayoutBounds().getHeight());

        Text text2 = new Text(text1.getText());
        text2.setFont(Font.font(18));
        text2.setX(10);
        text2.setY(CARD_HEIGHT - 10);

   

        getChildren().addAll(bg, text1, text2);
    }

    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }
}
