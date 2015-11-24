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
        Point adjustPointForTurn = findTargetPoint(game, nextTurn);
        double speedModule = hypot(self.getSpeedX(), self.getSpeedY());
        double distanceToTurn = self.getDistanceTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());

        if (distanceToTurn < 700) {
            MovingPoint turnAfterNext = Utils.getMap().getNextTurn(nextTurn);
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

    private Point findTargetPoint(Game game, MovingPoint point) {
        Point waypoint1Center = point.getPoint().clone();

        double cornerTileOffset = 0.2D * game.getTrackTileSize();

        switch (point.getDirectionBefore()) {
            case UP:
                switch (point.getDirectionAfter()) {
                    case LEFT:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                    case RIGHT:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                }
                break;
            case DOWN:
                switch (point.getDirectionAfter()) {
                    case LEFT:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                    case RIGHT:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                }
                break;
            case LEFT:
                switch (point.getDirectionAfter()) {
                    case UP:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                    case DOWN:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                }
                break;
            case RIGHT:
                switch (point.getDirectionAfter()) {
                    case UP:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                    case DOWN:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                }
                break;
        }

        return waypoint1Center;
    }

}
