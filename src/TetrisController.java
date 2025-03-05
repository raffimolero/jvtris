import java.util.*;
import java.util.Queue;

/**
 * Controls both the key presses and timer ticks in the game
 */
public class TetrisController {
    public TetrisGrid tetris;
    public Queue<GameEvent> eventQueue;
    private int gravityNextTime;
    private int graceTimeEnd;
    // contains the tick timestamps of each bind
    public Map<Integer, Integer> heldKeys;
    public int currentTime;

    public TetrisController(TetrisGrid tetris) {
        this.tetris = tetris;
        eventQueue = new ArrayDeque<>();
        heldKeys = new Hashtable<>(tetris.settings.getBinds().size());
        gravityNextTime = 0;
        currentTime = 0;
    }

    public void tick() {
        currentTime += tetris.settings.tickMs;
        while (currentTime >= gravityNextTime) {
            eventQueue.add(new GameEvent(GameEventKind.TICK, GameEventSource.WORLD));
            gravityNextTime += tetris.settings.gravity;
        }
        for (Map.Entry<Integer, GameEventKind> entry : tetris.settings.getBinds().entrySet()) {
            if (currentTime >= heldKeys.getOrDefault(entry.getKey(), Integer.MAX_VALUE)) {
                eventQueue.add(new GameEvent(entry.getValue(), GameEventSource.WORLD));
            }
        }
        runEvents();
    }

    public void down(int key) {
        if (heldKeys.containsKey(key) && heldKeys.get(key) != 0) {
            return;
        }

        // cancel some binds
        int left = tetris.settings.getRevBinds().get(GameEventKind.LEFT);
        int right = tetris.settings.getRevBinds().get(GameEventKind.RIGHT);
        if (key == left) heldKeys.remove(right);
        if (key == right) heldKeys.remove(left);
        int repeatDelay = (key == left || key == right) ? tetris.settings.das : 0;
        heldKeys.put(key, currentTime + repeatDelay);

        boolean isValidBind = tetris.settings.getBinds().containsKey(key);
        if (isValidBind) {
            eventQueue.add(new GameEvent(tetris.settings.getBinds().get(key), GameEventSource.INPUT));
        }

        runEvents();

    }

    public void up(int key) {
        heldKeys.remove(key);
        runEvents();
    }

    private void runEvents() {
        while (true) {
            GameEvent event = eventQueue.poll();
            if (event == null) break;
            if (tetris.input(event)) {
                if (tetris.onGround()) {
                    gravityNextTime = currentTime + tetris.settings.gravity;
                }
            }
        }
    }
}
