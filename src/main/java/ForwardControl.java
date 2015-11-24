import model.*;

import static java.lang.StrictMath.hypot;

public class ForwardControl implements Control {

    @Override
    public void doSmth(Move move, Car self, World world, Game game) {
        setMovement(self, move, world, game);
    }

    private void setMovement(Car self, Move move, World world, Game game) {
        MovingPoint currentPoint = Utils.getMap().getCurrentPoint(self, world, game);
        MovingPoint prevPoint = Utils.getMap().getPrevPoint(currentPoint);
        MovingPoint nextTurn = Utils.getMap().getNextTurn(currentPoint);
        MovingPoint turnAfterNext = Utils.getMap().getNextTurn(nextTurn);

        Point adjustPointForTurn = Utils.adjustTargetPoint(game, nextTurn);
        double speedModule = hypot(self.getSpeedX(), self.getSpeedY());
        double distanceToTurn = self.getDistanceTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());

        setSpeed(self, adjustPointForTurn, move, prevPoint, currentPoint, nextTurn, turnAfterNext, speedModule, distanceToTurn);

        setTurn(self, move, turnAfterNext, adjustPointForTurn, speedModule, distanceToTurn);
    }

    private void setSpeed(Car self, Point adjustPointForTurn, Move move, MovingPoint prevPoint, MovingPoint currentPoint, MovingPoint nextTurn, MovingPoint turnAfterNext, double speedModule, double distanceToTurn) {
        boolean ladder = isLadder(currentPoint, nextTurn, turnAfterNext);
        boolean sequentialTurns = sequentialTurns(nextTurn, turnAfterNext);
        boolean is180Turn = is180Turn(prevPoint, currentPoint, nextTurn, turnAfterNext);
        double angleToGoal = self.getAngleTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());

        if (speedModule > 7 && is180Turn) {
            if(speedModule < 5){
                move.setEnginePower(0.5D);
            } else {
                move.setEnginePower(-0.5D);
            }
        } else {
            if (ladder) {
                if (speedModule > 20) {
                    move.setEnginePower(-1D);
                } else {
                    move.setEnginePower(1D);
                }
            } else {
                double speedDistance = speedModule * 65;
                if (distanceToTurn < speedDistance && speedModule > 15) {
                    move.setEnginePower(-1D);
                } else {
                    move.setEnginePower(1D);
                }
            }
        }
    }

    private boolean isLadder(MovingPoint currentPoint, MovingPoint nextTurn, MovingPoint turnAfterNext) {
        if (currentPoint.isTurn() && sequentialTurns(currentPoint, nextTurn) && sequentialTurns(nextTurn, turnAfterNext)) {
            if (currentPoint.getDirectionAfter() == turnAfterNext.getDirectionAfter() &&
                    currentPoint.getDirectionBefore() == turnAfterNext.getDirectionBefore()) {
                return true;
            }
        }
        return false;
    }

    private void setTurn(Car self, Move move, MovingPoint turnAfterNext, Point adjustPointForTurn, double speedModule, double distanceToTurn) {
        if (distanceToTurn < 700 && speedModule > 10) {
            adjustPointForTurn = turnAfterNext.getPoint();
        }

        double angleToGoal = self.getAngleTo(adjustPointForTurn.getX(), adjustPointForTurn.getY());
        move.setWheelTurn(angleToGoal);
    }

    private boolean sequentialTurns(MovingPoint nextTurn, MovingPoint turnAfterNext) {
        return Math.abs(turnAfterNext.getIndex() - nextTurn.getIndex()) == 1;
    }

    private boolean is180Turn(MovingPoint prevPoint, MovingPoint currentPoint, MovingPoint nextTurn, MovingPoint turnAfterNext) {
        return (sequentialTurns(nextTurn, turnAfterNext) && Utils.isOpposite(currentPoint.getDirectionAfter(), turnAfterNext.getDirectionAfter())) ||
                (sequentialTurns(currentPoint, nextTurn) && Utils.isOpposite(currentPoint.getDirectionBefore(), nextTurn.getDirectionAfter())) ||
                (sequentialTurns(prevPoint, currentPoint) && Utils.isOpposite(prevPoint.getDirectionBefore(), currentPoint.getDirectionAfter()));
    }

}
