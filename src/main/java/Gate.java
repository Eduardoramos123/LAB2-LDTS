import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

public class Gate extends Element{
    Position position;
    boolean lock = true;
    int level;

    public Gate(int x, int y, int level) {
        position = new Position(x, y);
        this.level = level;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }

    public void unlock() {
        lock = false;
    }

    public int getLevel() {
        return level;
    }



    public void draw(TextGraphics graphics) {
        if (lock) {
            graphics.setForegroundColor(TextColor.Factory.fromString("#686868"));
        }
        else {
            graphics.setForegroundColor(TextColor.Factory.fromString("#8AFF8A"));
        }
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "G");
    }
}
