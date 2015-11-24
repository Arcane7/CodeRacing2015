import model.Car;
import model.Game;
import model.Move;
import model.World;

public class BackControl implements Control {

    private static int back = -1;
    private static int stayCtr = -1;
    private static int restrictBackCtr = -1;

    public BackControl() {

    }

    @Override
    public void doSmth(Move move, Car self, World world, Game game) {

        /*
        if (Math.abs(speedModule) < 2 &&
                world.getTick() > game.getInitialFreezeDurationTicks()+10) {
            stayCtr++;
            if(stayCtr > 5) {
                back = 30;
                stayCtr = -20;
            }
        }

        if (back > 0) {
            back--;
            move.setEnginePower(-1D);
            move.setWheelTurn(-angleToGoal);
            if(Math.abs(speedModule) < 2 && back <= 20){
                stayCtr = -10;
                back = -1;
            }
        } else {
         */
    }
}
