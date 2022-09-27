import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {

    public static Terminal terminal;
    public static Screen screen;
    public static Arena arena = new Arena(39, 19);

    public void run()
    {
        try {
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            terminal = terminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null);
            screen.startScreen();
            screen.doResizeIfNecessary();

            while (true) {
                draw();
                KeyStroke key = screen.readInput();
                if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
                    screen.close();
                    break;
                }
                else if (key.getKeyType() == KeyType.EOF) {
                    screen.close();
                    break;
                }
                if (arena.processKey(key)) {
                    draw_lost();
                    Thread.sleep(4000);
                    screen.close();
                    break;
                }
            }



        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void draw() throws IOException {
        screen.clear();
        TextGraphics graphics = screen.newTextGraphics();
        arena.draw(graphics);
        screen.refresh();
    }

    private void draw_lost() throws IOException {
        screen.clear();
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));

        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(39 + 2, 19 + 2), ' ');

        graphics.setForegroundColor(TextColor.Factory.fromString("#8B0000"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(15, 10), "You Died");

        screen.refresh();
    }


}
