import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Game {

    public static Terminal terminal;
    public static Screen screen;
    public static Arena arena = new Arena(39, 19);
    String path1 = "C:\\Users\\Eduardo\\Desktop\\LDTS\\LAB2\\Level1.txt";
    String path2 = "C:\\Users\\Eduardo\\Desktop\\LDTS\\LAB2\\Level2.txt";
    String path3 = "C:\\Users\\Eduardo\\Desktop\\LDTS\\LAB2\\Level3.txt";
    String path4 = "C:\\Users\\Eduardo\\Desktop\\LDTS\\LAB2\\Level4.txt";
    String path5 = "C:\\Users\\Eduardo\\Desktop\\LDTS\\LAB2\\Level5.txt";


    private void getLevelFromFile(String path) throws IOException {
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int i = 0;
        int width = 0;
        int height = 0;
        int level = -1;
        int height_aco = 0;
        List<Wall> walls = new ArrayList<>();
        List<Element> monsters = new ArrayList<>();
        List<Coin> coins = new ArrayList<>();
        List<Gate> gates= new ArrayList<>();


        Hero hero = null;

        while ((st = br.readLine()) != null) {
            if (i == 0) {
                width = parseInt(st);
            }
            else if (i == 1) {
                height = parseInt(st);
            }
            else if (i == 2) {
                level = parseInt(st);
            }
            else {
                for (int j = 0; j < st.length(); j++) {
                    if (st.charAt(j) == '#') {
                        walls.add(new Wall(j, height_aco));
                    }
                    else if (st.charAt(j) == 'X') {
                        hero = new Hero(j, height_aco, 5, 0);
                    }
                    else if (st.charAt(j) == 'M') {
                        monsters.add(new Monster(j, height_aco));
                    }
                    else if (st.charAt(j) == 'Q') {
                        monsters.add(new HazardX(j, height_aco, 1));
                    }
                    else if (st.charAt(j) == 'H') {
                        monsters.add(new HazardY(j, height_aco, 1));
                    }
                    else if (st.charAt(j) == 'O') {
                        coins.add(new Coin(j, height_aco));
                    }
                    else if (st.charAt(j) == 'G') {
                        gates.add(new Gate(j, height_aco, level));
                    }
                }
                height_aco = height_aco + 1;
            }
            i++;
        }
        arena = new Arena(width, height, walls, hero, monsters, coins, gates);
    }



    public void run(int i) throws IOException {

        if (i == 1) {
            getLevelFromFile(path1);
        }
        else if (i == 2) {
            getLevelFromFile(path2);
        }
        else if (i == 3) {
            getLevelFromFile(path3);
        }
        else if (i == 4) {
            getLevelFromFile(path4);
        }
        else if (i == 5) {
            getLevelFromFile(path5);
        }


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

                int check = arena.processKey(key);

                if (check == -1) {
                    draw_lost();
                    Thread.sleep(4000);
                    screen.close();
                    break;
                }
                else if (check != 0) {
                    screen.close();
                    run(check);
                    return;
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

    private void draw_win() throws IOException {
        screen.clear();
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.Factory.fromString("#FFFF33"));

        graphics.setBackgroundColor(TextColor.Factory.fromString("#000000"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(39 + 2, 19 + 2), ' ');

        graphics.setForegroundColor(TextColor.Factory.fromString("#8B0000"));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(new TerminalPosition(15, 10), "You Won!");

        screen.refresh();
    }


}
