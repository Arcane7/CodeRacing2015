import model.Car;
import model.Game;
import model.Move;
import model.World;

public class OilControl implements Control {
    @Override
    public void doSmth(Move move, Car self, World world, Game game) {
        if (self.getOilCanisterCount() > 0) {
            MovingPoint currentPoint = Utils.getMap().getCurrentPoint(self, world, game);

            if(currentPoint.isTurn()){
                move.setSpillOil(true);
            }
        }
    }
}
