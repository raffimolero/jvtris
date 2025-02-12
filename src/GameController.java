import java.util.*;
import java.util.Queue;

/**
 * Controls both the key presses and timer ticks in the game
 */
public class GameController {
    public GameSettings settings;
    private int gravityNextTick;
    // contains the tick timestamps of each bind
    public Map<Integer, Integer> heldKeys;
    public Queue<GameEvent> eventQueue;
    public int currentTick;

    public GameController(GameSettings settings) {
        this.settings = settings;
        heldKeys = new Hashtable<>(settings.getBinds().size());
        eventQueue = new ArrayDeque<>();
        gravityNextTick = settings.gravity / settings.tickMs;
        currentTick = 0;
    }

    public void tick() {
        currentTick++;
        for (Map.Entry<Integer, GameEventKind> entry : settings.getBinds().entrySet()) {
            if (currentTick >= heldKeys.getOrDefault(entry.getKey(), Integer.MAX_VALUE)) {
                eventQueue.add(new GameEvent(entry.getValue(), GameEventSource.WORLD));
            }
        }
        while (currentTick >= gravityNextTick) {
            eventQueue.add(new GameEvent(GameEventKind.TICK, GameEventSource.WORLD));
            gravityNextTick += settings.gravity / settings.tickMs;
        }
    }

    public void down(int key) {
        if (heldKeys.containsKey(key) && heldKeys.get(key) != 0) {
            return;
        }

        // cancel some binds
        int left = settings.getRevBinds().get(GameEventKind.LEFT);
        int right = settings.getRevBinds().get(GameEventKind.RIGHT);
        if (key == left) heldKeys.remove(right);
        if (key == right) heldKeys.remove(left);
        heldKeys.put(key, currentTick + settings.das / settings.tickMs);

        if (settings.getBinds().containsKey(key)) {
            eventQueue.add(new GameEvent(settings.getBinds().get(key), GameEventSource.INPUT));
        }
    }

    public void up(int key) {
        heldKeys.remove(key);
    }
}
