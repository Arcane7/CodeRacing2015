import model.*;

import java.util.ArrayList;
import java.util.List;

public final class MyStrategy implements Strategy {

    private static boolean initialized = false;
    private static List<Control> controls = new ArrayList<>();

    @Override
    public void move(Car self, World world, Game game, Move move) {
        try {
            init(world, game);

            for (Control control : controls) {
                control.doSmth(move, self, world, game);
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void init(World world, Game game) {
        if (!initialized) {
            initialized = true;
            Utils.getMap().buildMap(world, game);

            controls.add(new ForwardControl());
            controls.add(new BrakeControl());
            controls.add(new StuckControl());
            controls.add(new OilControl());
            controls.add(new ShootControl());
            controls.add(new NitroControl());
        }
    }

}
