import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Queue;

/**
 * Controls both the key presses and timer ticks in the game
 */
public class GameController {
    // contains each bind
    public Map<Integer, GameEventKind> binds;
    private Map<GameEventKind, Integer> revBinds;
    // contains the tick timestamps of each bind
    public Map<Integer, Integer> held;
    public Queue<GameEvent> eventQueue;
    public int currentTick;
    public int dasTickDelay;
    public int gravityTickDelay;
    private int gravityNextTick;

    public static final int DEFAULT_DAS_TICK_DELAY = 4;
    public static final int DEFAULT_GRAVITY_TICK_DELAY = 32;
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
        return binds;
    }

    public GameController() {
        this(defaultBinds(), DEFAULT_DAS_TICK_DELAY, DEFAULT_GRAVITY_TICK_DELAY);
    }

    public GameController(Map<Integer, GameEventKind> binds, int dasTickDelay, int gravityTickDelay) {
        this.binds = binds;
        generateRevBinds();
        this.dasTickDelay = dasTickDelay;
        this.gravityTickDelay = gravityTickDelay;
        held = new Hashtable<>(binds.size());
        eventQueue = new ArrayDeque<>();
        gravityNextTick = gravityTickDelay;
        currentTick = 0;
    }

    private void generateRevBinds() {
        revBinds = new Hashtable<>();
        for (Map.Entry<Integer, GameEventKind> entry : binds.entrySet()) {
            revBinds.put(entry.getValue(), entry.getKey());
        }
    }

    public void tick() {
        currentTick++;
        for (Map.Entry<Integer, GameEventKind> entry : binds.entrySet()) {
            if (currentTick >= held.getOrDefault(entry.getKey(), Integer.MAX_VALUE)) {
                eventQueue.add(new GameEvent(entry.getValue(), GameEventSource.WORLD));
            }
        }
        while (currentTick >= gravityNextTick) {
            eventQueue.add(new GameEvent(GameEventKind.TICK, GameEventSource.WORLD));
            gravityNextTick += gravityTickDelay;
        }
    }

    public void down(int key) {
        if (held.containsKey(key) && held.get(key) != 0) {
            return;
        }

        // cancel some binds
        int left = revBinds.get(GameEventKind.LEFT);
        int right = revBinds.get(GameEventKind.RIGHT);
        if (key == left) held.remove(right);
        if (key == right) held.remove(left);
        held.put(key, currentTick + dasTickDelay);

        if (binds.containsKey(key)) {
            eventQueue.add(new GameEvent(binds.get(key), GameEventSource.INPUT));
        }
    }

    public void up(int key) {
        held.remove(key);
    }
}
