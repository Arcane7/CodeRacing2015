import model.Car;
import model.Game;
import model.Move;
import model.World;

import static java.lang.StrictMath.hypot;

public class BrakeControl implements Control {
    @Override
    public void doSmth(Move move, Car self, World world, Game game) {

    }

    private void setBrakes(Car self, Move move, MovingPoint goal) {

        double speedModule = hypot(self.getSpeedX(), self.getSpeedY());
        double distanceToGoal = self.getDistanceTo(goal.getPoint().getX(), goal.getPoint().getY());
        if (distanceToGoal < 1000 && speedModule > 10) {
            move.setBrake(true);
        }

    }

}
