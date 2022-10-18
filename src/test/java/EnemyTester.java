import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnemyTester {

    @Test
    public void testMonsterMove()  {
        Monster monster = new Monster(0, 0);
        Position position = new Position(10, 10);

        Position monster_position = monster.move(position);

        boolean check1 = monster_position.equals(new Position(1, 0));

        Assertions.assertTrue(check1);

        monster = new Monster(10, 10);
        position = new Position(0, 0);

        monster_position = monster.move(position);

        boolean check2 = monster_position.equals(new Position(9, 10));

        Assertions.assertTrue(check2);

        monster = new Monster(0, 0);
        position = new Position(0, 10);

        monster_position = monster.move(position);

        boolean check3 = monster_position.equals(new Position(0, 1));

        Assertions.assertTrue(check3);

        monster = new Monster(0, 10);
        position = new Position(0, 0);

        monster_position = monster.move(position);

        boolean check4 = monster_position.equals(new Position(0, 9));

        Assertions.assertTrue(check4);

        monster = new Monster(0, 0);
        position = new Position(0, 0);

        monster_position = monster.move(position);

        boolean check5 = monster_position.equals(new Position(0, 0));

        Assertions.assertTrue(check5);
    }

    @Test
    public void testHazardXSwitch() {
        HazardX hazard = new HazardX(10, 10, 1);
        Position hazard_position = hazard.move();

        boolean check1 = hazard_position.equals(new Position(11, 10));

        Assertions.assertTrue(check1);

        hazard = new HazardX(10, 10, 1);
        hazard.switchDirection();
        hazard_position = hazard.move();

        boolean check2 = hazard_position.equals(new Position(9, 10));

        Assertions.assertTrue(check2);

    }

    @Test
    public void testHazardYSwitch() {
        HazardY hazard = new HazardY(10, 10, 1);
        Position hazard_position = hazard.move();

        boolean check1 = hazard_position.equals(new Position(10, 11));

        Assertions.assertTrue(check1);

        hazard = new HazardY(10, 10, 1);
        hazard.switchDirection();
        hazard_position = hazard.move();

        boolean check2 = hazard_position.equals(new Position(10, 9));

        Assertions.assertTrue(check2);

    }


}
