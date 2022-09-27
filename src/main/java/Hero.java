import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class Hero extends Element{
    Position position;
    private int energy;
    private int score;

    public Hero(int x, int y, int energy, int score) {
        position = new Position(x, y);
        this.energy = energy;
        this.score = score;
    }

    public Position moveUp() {
        return new Position(position.getX(), position.getY()- 1);
    }
    public Position moveDown() {
        return new Position(position.getX(), position.getY()+ 1);
    }
    public Position moveLeft() {
        return new Position(position.getX()- 1, position.getY());
    }
    public Position moveRight() {
        return new Position(position.getX()+ 1, position.getY());
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public int getEnergy() {
        return energy;
    }
    public void decrementEnergy() {
        energy = energy - 1;
    }

    public void incrementScore() {
        score = score + 1;
    }
    public int getScore() {
        return score;
    }

    public void draw(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(position.getX(), position.getY()), "X");
    }


}
