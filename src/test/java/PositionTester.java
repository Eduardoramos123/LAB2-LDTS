import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionTester {

    @Test
    public void testPosition() {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(0, 0);
        boolean test = position1.equals(position2);

        Assertions.assertTrue(test);
    }
}
