import model.Car;
import model.Game;
import model.Move;
import model.World;

import static java.lang.StrictMath.hypot;

public class ForwardControl implements Control {

    @Override
    public void doSmth(Move move, Car self, World world, Game game) {
        setMovement(self, move, world, game);
    }

    private void setMovement(Car self, Move move, World world, Game game) {
        MovingPoint currentPoint = Utils.getMap().getCurrentPoint(self, world, game);
        MovingPoint nextTurn = Utils.getMap().getNextTurn(currentPoint);
        MovingPoint turnAfterNext = Utils.getMap().getNextTurn(nextTurn);

        Point adjustPointForTurn = Utils.adjustTargetPoint(game, nextTurn);
        double speedModule = hypot(self.getSpeedX(), self.getSpeedY());
        double distanceToTurn = self.getDistanceTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());

        boolean is180Turn = is180Turn(currentPoint, turnAfterNext);
        if (distanceToTurn < 700 && !is180Turn) {
            adjustPointForTurn = turnAfterNext.getPoint();
        }
        double angleToGoal = self.getAngleTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());

        double speedDistance = speedModule * 65;
        if (distanceToTurn < speedDistance && speedModule > 15) {
            move.setEnginePower(-1D);
        } else {
            move.setEnginePower(1D);
        }

        move.setWheelTurn(angleToGoal);
    }

    private boolean is180Turn(MovingPoint currentPoint, MovingPoint turnAfterNext) {
        return Utils.isOpposite(currentPoint.getDirectionAfter(), turnAfterNext.getDirectionAfter());
    }

}
