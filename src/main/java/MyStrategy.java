import model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.*;

public final class MyStrategy implements Strategy {

    private static boolean initialized = false;
    private static List<Control> controls = new ArrayList<>();

    @Override
    public void move(Car self, World world, Game game, Move move) {
        try {
            init(world, game);


//            setBrakes(self, move, goal);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void init(World world, Game game) {
        if (!initialized) {
            initialized = true;
            Utils.getMap().buildMap(world, game);

            controls.add(new ForwardControl());
            controls.add(new BackControl());
        }
    }

    private void setBrakes(Car self, Move move, MovingPoint goal) {

        double speedModule = hypot(self.getSpeedX(), self.getSpeedY());
        double distanceToGoal = self.getDistanceTo(goal.getPoint().getX(), goal.getPoint().getY());
        if (distanceToGoal < 1000 && speedModule > 10) {
            move.setBrake(true);
        }

    }


}
