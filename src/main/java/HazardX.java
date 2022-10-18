import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class HazardX extends Element{
    Position position;
    int direction;

    public HazardX(int x, int y, int direction) {
        position = new Position(x, y);
        this.direction = direction;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Position move() {
        return new Position(position.getX() + direction, position.getY());
    }

    public void switchDirection() {
        if (direction == 1) {
            direction = -1;
        }
        else {
            direction = 1;
        }
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#FF0000"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "Q");
    }
}
