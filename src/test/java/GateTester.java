import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GateTester {

    @Test
    public void testLevel() {
        Gate gate = new Gate(0, 0, 2);

        Assertions.assertEquals(gate.getLevel(), 2);
    }

    @Test
    public void testUnlock() {
        Gate gate = new Gate(0, 0, 2);

        Assertions.assertEquals(gate.lock, true);

        gate.unlock();

        Assertions.assertEquals(gate.lock, false);
    }
}
