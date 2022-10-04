import com.googlecode.lanterna.SGR;
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
    private Hero hero = new Hero(10, 10, 5, 0);
    //private HazardX hazard = new HazardX(2,2, 1);
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Element> monsters = new ArrayList<>();
    private List<Gate> gates = new ArrayList<>();
    private List<Sword> swords = new ArrayList<>();


    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        this.walls = createWalls();
        this.coins = createCoins();
    }

    public Arena(int width, int height, List<Wall> walls, Hero hero, List<Element> monsters, List<Coin> coins, List<Gate> gates, List<Sword> swords) {
        this.width = width;
        this.height = height;
        this.walls = walls;
        this.hero = hero;
        this.monsters = monsters;
        this.coins = coins;
        this.gates = gates;
        this.swords = swords;
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

        for (int i = 1; i < 10; i++) {
            walls.add(new Wall(i, 6));
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

        for (Gate gate: gates) {
            if (gate.getPosition().equals(position) && gate.lock) {
                return false;
            }
        }

        for (Coin coin: coins) {
            if (coin.getPosition().equals(position)) {
                retrieveCoins(position);
                hero.incrementScore();
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

    private boolean canHeroMoveModified(Position position) {

        for (Wall wall: walls) {
            if (wall.getPosition().equals(position)) {
                return false;
            }
        }

        for (Gate gate: gates) {
            if (gate.getPosition().equals(position)) {
                return false;
            }
        }

        return true;
    }

    private void moveHero(Position position) {
        if (canHeroMove(position)) {
            hero.setPosition(position);
        }
    }

    private void moveMonster(Position position, Monster mon) {
        mon.setPosition(position);
    }
    private void moveHazard(Position position, Element haz) {
        haz.setPosition(position);
    }

    private boolean verifyMonsterCollisions(Position hero_pos, Position most_pos) {
        if (hero_pos.equals(most_pos)) {
            return true;
        }
        return false;
    }

    boolean moveProtocolMonster(Position position) {
        for (Element mon : monsters) {
            if (mon instanceof HazardX) {
                HazardX hax = (HazardX) mon;
                Position hazard_pos = hax.move();

                if (!canHeroMoveModified(hazard_pos)) {
                    hax.switchDirection();
                    moveHazard(hax.move(), mon);
                }
                else {
                    moveHazard(hazard_pos, mon);
                }

                if (verifyMonsterCollisions(position, hax.position)) {
                    hero.decrementEnergy();
                    if (hero.getEnergy() == 0) {
                        System.out.println("You lost!!");
                        return true;
                    }
                    System.out.println("Ouchhh!");
                }
            }
            else if (mon instanceof HazardY) {
                HazardY hax = (HazardY) mon;
                Position hazard_pos = hax.move();

                if (!canHeroMoveModified(hazard_pos)) {
                    hax.switchDirection();
                    moveHazard(hax.move(), hax);
                }
                else {
                    moveHazard(hazard_pos, hax);
                }

                if (verifyMonsterCollisions(position, hax.position)) {
                    hero.decrementEnergy();
                    if (hero.getEnergy() == 0) {
                        System.out.println("You lost!!");
                        return true;
                    }
                    System.out.println("Ouchhh!");
                }
            }
            else if (mon instanceof Monster) {
                Monster m = (Monster) mon;
                if (canHeroMove(position)) {
                    moveMonster(m.move(position), m);
                }
                else {
                    moveMonster(m.move(hero.position), m);
                }
                if (verifyMonsterCollisions(position, m.move(position))) {
                    hero.decrementEnergy();
                    if (hero.getEnergy() == 0) {
                        System.out.println("You lost!!");
                        return true;
                    }
                    System.out.println("Ouchhh!");
                }
            }
        }
        return false;
    }

    private boolean heroInGate(Position position) {
        for (Gate gate: gates) {
            if (gate.getPosition().equals(position) && !gate.lock) {
                return true;
            }
        }
        return false;
    }

    private boolean heroInSword(Position position) {
        for (Sword sword: swords) {
            if (sword.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public int processKey(KeyStroke key) {
        if (key.getKeyType() == KeyType.ArrowUp) {
            Position new_hero = hero.moveUp();
            moveHero(new_hero);

            if (moveProtocolMonster(hero.position)) {
                return -1;
            }

            if (coins.isEmpty()) {
                for (Gate gate : gates) {
                    gate.unlock();
                }
            }

            if (heroInGate(hero.position)) {
                return gates.get(0).level;
            }

            if (heroInSword(hero.position)) {
                return -2;
            }

            return 0;
        }
        else if (key.getKeyType() == KeyType.ArrowDown) {
            Position new_hero = hero.moveDown();
            moveHero(new_hero);

            if (moveProtocolMonster(hero.position)) {
                return -1;
            }

            if (coins.isEmpty()) {
                for (Gate gate : gates) {
                    gate.unlock();
                }
            }

            if (heroInGate(hero.position)) {
                return gates.get(0).level;
            }

            if (heroInSword(hero.position)) {
                return -2;
            }

            return 0;
        }
        else if (key.getKeyType() == KeyType.ArrowLeft) {
            Position new_hero = hero.moveLeft();
            moveHero(new_hero);

            if (moveProtocolMonster(hero.position)) {
                return -1;
            }

            if (coins.isEmpty()) {
                for (Gate gate : gates) {
                    gate.unlock();
                }
            }

            if (heroInGate(hero.position)) {
                return gates.get(0).level;
            }

            if (heroInSword(hero.position)) {
                return -2;
            }

            return 0;
        }
        else if (key.getKeyType() == KeyType.ArrowRight) {
            Position new_hero = hero.moveRight();
            moveHero(new_hero);

            if (moveProtocolMonster(hero.position)) {
                return -1;
            }

            if (coins.isEmpty()) {
                for (Gate gate : gates) {
                    gate.unlock();
                }
            }

            if (heroInGate(hero.position)) {
                return gates.get(0).level;
            }

            if (heroInSword(hero.position)) {
                return -2;
            }

            return 0;
        }
        return 0;
    }

    public int getEnergy() {
        return hero.getEnergy();
    }

    public int getScore() {
        return hero.getScore();
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width + 2, height + 2), ' ');

        hero.draw(graphics);

        for (Element m : monsters) {
            m.draw(graphics);
        }

        for (Wall wall : walls) {
            wall.draw(graphics);
        }

        for (Coin coin : coins) {
            coin.draw(graphics);
        }

        for (Gate gate : gates) {
            gate.draw(graphics);
        }

        for (Sword sword : swords) {
            sword.draw(graphics);
        }

        //Score

        graphics.setForegroundColor(TextColor.Factory.fromString("#FFA500"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(0, 0), "Score: " + hero.getScore() + " ");

        //Energy
        if (hero.getEnergy() > 3) {
            graphics.setForegroundColor(TextColor.Factory.fromString("#00FF00"));
            graphics.enableModifiers(SGR.BOLD);
            graphics.putString(new TerminalPosition(0, 19), "Energy: " + hero.getEnergy() + " ");
        }
        else if (hero.getEnergy() <= 3 && hero.getEnergy() > 1) {
            graphics.setForegroundColor(TextColor.Factory.fromString("#EC9B00"));
            graphics.enableModifiers(SGR.BOLD);
            graphics.putString(new TerminalPosition(0, 19), "Energy: " + hero.getEnergy() + " ");
        }
        else {
            graphics.setForegroundColor(TextColor.Factory.fromString("#EC0000"));
            graphics.enableModifiers(SGR.BOLD);
            graphics.putString(new TerminalPosition(0, 19), "Energy: " + hero.getEnergy() + " ");
        }


    }
}
