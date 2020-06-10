package rs.edu.raf.mtomic.paclike.agent.player;

import rs.edu.raf.mtomic.paclike.Direction;
import rs.edu.raf.mtomic.paclike.FieldState;
import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.MathUtils;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.agent.PlayingAgent;

import java.util.ArrayList;
import java.util.Collections;
import rs.edu.raf.mtomic.Chromosome;


public class PlayerOne extends Player {
    private Runnable nextMove = this::goLeft;
    private Chromosome chromosome;

    private ArrayList<Double> distances = new ArrayList<>();

    private double bdp=0;
    private Direction bestDirection;


    public PlayerOne(GameState gameState) {
        super(gameState);
    }

    public PlayerOne(GameState gameState, Chromosome chromosome) {
        super(gameState);
        this.chromosome = chromosome;
    }

    @Override
    protected Runnable generateNextMove() {

        for(AvailableStruct avSt : this.getAvailableFields()){
            double currentdp = 0;
            for (PlayingAgent a:gameState.getAgents().subList(0,4)){
                distances.add(MathUtils.distance(avSt.gridPosition.getKey(),avSt.gridPosition.getValue(),a.getGridX(),a.getGridY()));
                }
            Collections.sort(distances);
            if(gameState.getFields()[avSt.gridPosition.getKey()][avSt.gridPosition.getValue()].equals(FieldState.PELLET))currentdp+=chromosome.getClosestPellet();
            currentdp += distances.get(0) * chromosome.getCoef1() + distances.get(1) * chromosome.getCoef2() + distances.get(2) * chromosome.getCoef3() + distances.get(3) * chromosome.getCoef4();
            if(currentdp > bdp){
                bdp = currentdp;
                bestDirection = avSt.direction;
            }
            distances.clear();
        }

        if(bestDirection == Direction.LEFT) nextMove = this::goLeft;
        else if(bestDirection == Direction.RIGHT) nextMove = this::goRight;
        else if(bestDirection == Direction.UP) nextMove = this::goUp;
        else if(bestDirection == Direction.DOWN) nextMove = this::goDown;

        bdp = 0;
        return nextMove;
    }

    @Override
    public String toString() {
        return "PlayerOne{" +
                "nextMove=" + nextMove +
                ", chromosome=" + chromosome +
                ", distances=" + distances +
                ", bdp=" + bdp +
                ", bestDirection=" + bestDirection +
                '}';
    }
}

