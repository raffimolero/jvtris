import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Queue {
    ArrayList<Piece> queue;
    int targetLen;

    Queue(int targetLength) {
        targetLen = targetLength;
    }

    public Piece nextPiece() {
        Piece out = queue.remove(queue.size() - 1);
        refill();
        return out;
    }

    void refill() {
        while (queue.size() < targetLen) {
            addBag();
        }
    }

    void addBag() {
        List<Piece> pieces = Arrays.asList(Piece.PIECES);
        Collections.shuffle(pieces);
        queue.addAll(pieces);
    }
}
