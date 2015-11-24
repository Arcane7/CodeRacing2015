import model.Car;
import model.Game;
import model.Move;
import model.World;

public class NitroControl implements Control {
    @Override
    public void doSmth(Move move, Car self, World world, Game game) {
        if (self.getNitroChargeCount() > 0 && world.getTick() > game.getInitialFreezeDurationTicks()) {
            MovingPoint currentPoint = Utils.getMap().getCurrentPoint(self, world, game);
            MovingPoint nextTurn = Utils.getMap().getNextTurn(currentPoint);
            if (nextTurn.getIndex() > currentPoint.getIndex() + 5) {
                move.setUseNitro(true);
            }
        }
    }
}
