import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Map;

public class GameSettings {
    public Skin skin = Skin.DEFAULT;
    public KickTable kickTable = new KickTable();
    private Map<Integer, GameEventKind[]> macros;
    private Map<Integer, GameEventKind> binds;
    private Map<GameEventKind, Integer> revBinds;
    public int das = 50;
    public int gravity = 500;
    public int tickMs = 1000 / 120;
    public int garbageHeight = 10;

    private static Map<Integer, GameEventKind> defaultBinds() {
        Map<Integer, GameEventKind> binds = new Hashtable<>(9);
        binds.put(KeyEvent.VK_A, GameEventKind.HOLD);
        binds.put(KeyEvent.VK_S, GameEventKind.ROTATE_CC);
        binds.put(KeyEvent.VK_D, GameEventKind.ROTATE_180);
        binds.put(KeyEvent.VK_F, GameEventKind.ROTATE_CW);
        binds.put(KeyEvent.VK_NUMPAD4, GameEventKind.LEFT);
        binds.put(KeyEvent.VK_NUMPAD6, GameEventKind.RIGHT);
        binds.put(KeyEvent.VK_RIGHT, GameEventKind.SOFT_DROP);
        binds.put(KeyEvent.VK_SPACE, GameEventKind.HARD_DROP);
        binds.put(KeyEvent.VK_NUMPAD0, GameEventKind.RESET);

        return binds;
    }

    private static Map<Integer, GameEventKind[]> defaultMacros() {
        Integer[][] keys = new Integer[][]{
                new Integer[]{
                        KeyEvent.VK_1,
                        KeyEvent.VK_2,
                        KeyEvent.VK_3,
                        KeyEvent.VK_4,
                        KeyEvent.VK_5,
                        KeyEvent.VK_6,
                        KeyEvent.VK_7,
                        KeyEvent.VK_8,
                        KeyEvent.VK_9,
                        KeyEvent.VK_0,
                },
                new Integer[]{
                        KeyEvent.VK_Q,
                        KeyEvent.VK_W,
                        KeyEvent.VK_E,
                        KeyEvent.VK_R,
                        KeyEvent.VK_T,
                        KeyEvent.VK_Y,
                        KeyEvent.VK_U,
                        KeyEvent.VK_I,
                        KeyEvent.VK_O,
                        KeyEvent.VK_P,
                        KeyEvent.VK_OPEN_BRACKET,
                },
                new Integer[]{
                        KeyEvent.VK_A,
                        KeyEvent.VK_S,
                        KeyEvent.VK_D,
                        KeyEvent.VK_F,
                        KeyEvent.VK_G,
                        KeyEvent.VK_H,
                        KeyEvent.VK_J,
                        KeyEvent.VK_K,
                        KeyEvent.VK_L,
                        KeyEvent.VK_SEMICOLON,
                },
                new Integer[]{
                        KeyEvent.VK_Z,
                        KeyEvent.VK_X,
                        KeyEvent.VK_C,
                        KeyEvent.VK_V,
                        KeyEvent.VK_B,
                        KeyEvent.VK_N,
                        KeyEvent.VK_M,
                        KeyEvent.VK_COMMA,
                        KeyEvent.VK_PERIOD,
                        KeyEvent.VK_SLASH,
                },
        };
        Map<Integer, GameEventKind[]> macros = new Hashtable<>(10 * 4);
        int[] rots = {2, 3, 0, 1};
        for (int r = 0; r < 4; r++) {
            int rot = rots[r];
            for (int m = 0; m < 10; m++) {
                int mov = m - 4;
                GameEventKind[] macro = new GameEventKind[rot + Math.abs(mov) + 1];
                int next = 0;
                for (int i = 0; i < rot; i++, next++) macro[next] = GameEventKind.ROTATE_CW;
                if (mov < 0) for (int i = 0; i > mov; i--, next++) macro[next] = GameEventKind.LEFT;
                if (mov > 0) for (int i = 0; i < mov; i++, next++) macro[next] = GameEventKind.RIGHT;
                macro[next] = GameEventKind.HARD_DROP;
                macros.put(keys[r][m], macro);
            }
        }

        macros.put(KeyEvent.VK_SPACE, new GameEventKind[]{ GameEventKind.HOLD });
        macros.put(KeyEvent.VK_NUMPAD7, new GameEventKind[]{ GameEventKind.ROTATE_CC });
        macros.put(KeyEvent.VK_NUMPAD9, new GameEventKind[]{ GameEventKind.ROTATE_CW });
        return macros;
    }

    public Map<Integer, GameEventKind> getBinds() {
        return binds;
    }

    public Map<Integer, GameEventKind[]> getMacros() {
        return macros;
    }

    public void setBind(int bind, GameEventKind event) {
        binds.put(bind, event);
        generateRevBinds();
    }

    public void unbind(int bind) {
        binds.remove(bind);
        generateRevBinds();
    }

    public Map<GameEventKind, Integer> getRevBinds() {
        return revBinds;
    }

    public GameSettings() {
        binds = defaultBinds();
//        macros = defaultMacros();
        macros = new Hashtable<>();
        generateRevBinds();
    }

    private void generateRevBinds() {
        revBinds = new Hashtable<>();
        for (Map.Entry<Integer, GameEventKind> entry : binds.entrySet()) {
            revBinds.put(entry.getValue(), entry.getKey());
        }
    }
}
