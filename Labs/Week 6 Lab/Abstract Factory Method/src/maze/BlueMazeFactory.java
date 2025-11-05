package maze;

public class BlueMazeFactory extends MazeFactory {
    public Wall makeWall(){
        return new BlueWall();
    }

    public Door makeDoor(Room r1, Room r2){
        return new BrownDoor(r1, r2);
    }

    public Room makeRoom(int roomNum){
        return new GreenRoom(roomNum);
    }
}
