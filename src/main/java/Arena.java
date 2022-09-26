import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {

    int width;
    int height;
    private Hero hero = new Hero(10, 10);
    private Monster monster = new Monster(15, 15);
    private List<Wall> walls;
    private List<Coin> coins;

    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.walls = createWalls();
        this.coins = createCoins();
    }

    private List<Wall> createWalls() {
        List<Wall> walls = new ArrayList<>();
        for (int c = 0; c < width + 1; c++) {
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height));
        }
        for (int r = 1; r < height + 1; r++) {
            walls.add(new Wall(0, r));
            walls.add(new Wall(width, r));
        }
        return walls;
    }

    private List<Coin> createCoins() {
        Random random = new Random();
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            coins.add(new Coin(random.nextInt(width - 2) +
                    1, random.nextInt(height - 2) + 1));
        return coins;
    }

    private void retrieveCoins(Position position) {
        ArrayList<Coin> clone = new ArrayList<>();
        for (Coin coin: coins) {
            if (!coin.getPosition().equals(position)) {
                clone.add(coin);
            }
        }
        coins = clone;
    }

    private boolean canHeroMove(Position position) {

        for (Wall wall: walls) {
            if (wall.getPosition().equals(position)) {
                return false;
            }
        }

        for (Coin coin: coins) {
            if (coin.getPosition().equals(position)) {
                retrieveCoins(position);
            }
        }

        if (height < position.getY()) {
            return false;
        }
        if (position.getY() < 0) {
            return false;
        }

        if (width < position.getX()) {
            return false;
        }
        if (position.getX() < 0) {
            return false;
        }

        return true;
    }

    private void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
        }
    }

    private void moveMonster(Position position) {
        monster.setPosition(position);
    }

    private boolean verifyMonsterCollisions(Position hero_pos, Position most_pos) {
        if (hero_pos.equals(most_pos)) {
            return true;
        }
        return false;
    }

    public boolean processKey(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp) {
            moveHero(hero.moveUp());
            moveMonster(monster.move(hero.moveUp()));
            if (verifyMonsterCollisions(hero.moveUp(), monster.move(hero.moveUp()))) {
                System.out.println("You lost!!");
                return true;
            }
            return false;
        }
        else if (key.getKeyType() == KeyType.ArrowDown) {
            moveHero(hero.moveDown());
            moveMonster(monster.move(hero.moveDown()));
            if (verifyMonsterCollisions(hero.moveDown(), monster.move(hero.moveDown()))) {
                System.out.println("You lost!!");
                return true;
            }
            return false;
        }
        else if (key.getKeyType() == KeyType.ArrowLeft) {
            moveHero(hero.moveLeft());
            moveMonster(monster.move(hero.moveLeft()));
            if (verifyMonsterCollisions(hero.moveLeft(), monster.move(hero.moveLeft()))) {
                System.out.println("You lost!!");
                return true;
            }
            return false;
        }
        else if (key.getKeyType() == KeyType.ArrowRight) {
            moveHero(hero.moveRight());
            moveMonster(monster.move(hero.moveRight()));
            if (verifyMonsterCollisions(hero.moveRight(), monster.move(hero.moveRight()))) {
                System.out.println("You lost!!");
                return true;
            }
            return false;
        }
        return false;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width + 2, height + 2), ' ');

        hero.draw(graphics);

        monster.draw(graphics);

        for (Wall wall : walls) {
            wall.draw(graphics);
        }

        for (Coin coin : coins) {
            coin.draw(graphics);
        }

    }
}
