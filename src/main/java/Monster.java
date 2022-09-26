import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Monster extends Element{
    Position position;

    public Monster(int x, int y) {
        position = new Position(x, y);
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }

    public Position move(Position hero_position) {
        if (hero_position.getX() < position.getX()) {
            return new Position(position.getX() - 1, position.getY());
        }
        if (hero_position.getX() > position.getX()) {
            return new Position(position.getX() + 1, position.getY());
        }
        if (hero_position.getY() < position.getY()) {
            return new Position(position.getX(), position.getY() - 1);
        }
        if (hero_position.getY() > position.getY()) {
            return new Position(position.getX(), position.getY() + 1);
        }
        return new Position(position.getX(), position.getY());
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#FF0000"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "M");
    }
}
