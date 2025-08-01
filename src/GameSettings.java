import java.awt.event.KeyEvent;
import java.util.Hashtable;
import java.util.Map;

public class GameSettings {
    public Skin skin = Skin.DEFAULT;
    public KickTable kickTable = new KickTable();
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

    public Map<Integer, GameEventKind> getBinds() {
        return binds;
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
        generateRevBinds();
    }

    private void generateRevBinds() {
        revBinds = new Hashtable<>();
        for (Map.Entry<Integer, GameEventKind> entry : binds.entrySet()) {
            revBinds.put(entry.getValue(), entry.getKey());
        }
    }
}
