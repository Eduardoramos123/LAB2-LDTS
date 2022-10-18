import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeroTester {

    @Test
    public void testPosition() {
        Hero hero = new Hero(10, 10, 5, 0);

        Position expected = new Position(10, 9);

        Assertions.assertEquals(expected, hero.moveUp());

        expected = new Position(10, 11);

        Assertions.assertEquals(expected, hero.moveDown());

        expected = new Position(11, 10);

        Assertions.assertEquals(expected, hero.moveRight());

        expected = new Position(9, 10);

        Assertions.assertEquals(expected, hero.moveLeft());

    }

    @Test
    public void testSetEnergy() {
        Hero hero = new Hero(10, 10, 5, 0);

        Assertions.assertEquals(hero.getEnergy(), 5);
    }

    @Test
    public void testDecrementEnergy() {
        Hero hero = new Hero(10, 10, 5, 0);
        hero.decrementEnergy();

        Assertions.assertEquals(hero.getEnergy(), 4);
    }

    @Test
    public void testIncrementScore() {
        Hero hero = new Hero(10, 10, 5, 0);
        hero.incrementScore();

        Assertions.assertEquals(hero.getScore(), 1);
    }

}
