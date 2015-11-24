import model.Car;
import model.Game;
import model.Move;
import model.World;

public interface Control {

    void doSmth(Move move, Car self, World world, Game game);
}
