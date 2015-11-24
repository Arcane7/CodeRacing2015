import model.Car;
import model.Game;
import model.Move;
import model.World;

import static java.lang.StrictMath.hypot;

public class StuckControl implements Control {

    private int backTicks = -1;
    private int stuckCtr = -1;
    private double backAngle;

    @Override
    public void doSmth(Move move, Car self, World world, Game game) {
        MovingPoint currentPoint = Utils.getMap().getCurrentPoint(self, world, game);
        MovingPoint nextTurn = Utils.getMap().getNextTurn(currentPoint);

        double speedModule = hypot(self.getSpeedX(), self.getSpeedY());

        if (Math.abs(speedModule) < 2 &&
                backTicks < 0 &&
                world.getTick() > game.getInitialFreezeDurationTicks()+10) {
            stuckCtr++;
            if(stuckCtr > 10) {
                backTicks = 30;
                stuckCtr = -1;

                Point adjustPointForTurn = Utils.adjustTargetPoint(game, nextTurn);
                backAngle = self.getAngleTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());
            }
        }

        if (backTicks >= 0) {
            backTicks--;
            move.setEnginePower(-1D);
            move.setWheelTurn(-backAngle);
            if(Math.abs(speedModule) < 2 && backTicks <= 20){
                stuckCtr = -1;
                backTicks = -1;
            }
        }
    }
}
