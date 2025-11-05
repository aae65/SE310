package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import maze.ui.MazeViewer;

public class MazeGameDriver {
    String filePath;
    
    public static Maze loadMaze(final String path, MazeFactory factory) {
        Maze maze = new Maze();
        File file = new File(path);
        Scanner sc;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Hashtable<String, List<Direction>> doors = new Hashtable<>();
        while (sc.hasNextLine()) {
            List<String> line = List.of(sc.nextLine().split(" "));
            if (line.get(0).equals("room")) {
                int num = Integer.parseInt(line.get(1));
                Room room;
                if (maze.getRoom(num) == null) {
                    room = factory.makeRoom(num);
                    maze.addRoom(room);
                } else {
                    room = maze.getRoom(num);
                }
                if (maze.getNumberOfRooms() == 1) {
                    maze.setCurrentRoom(room);
                }

                String edge = line.get(2);
                if (edge.contains("d")) {
                    if (doors.containsKey(edge)) {
                        doors.get(edge).add(Direction.North);
                    } else {
                        doors.put(edge, new ArrayList<>());
                        doors.get(edge).add(Direction.North);
                    }
                } else if (edge.equals("wall")) {
                    room.setSide(Direction.North, factory.makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = factory.makeRoom(neighborNum);
                        maze.addRoom(newRoom);
                        room.setSide(Direction.North, maze.getRoom(neighborNum));
                        maze.getRoom(neighborNum).setSide(Direction.South, room);
                    } else {
                        maze.getRoom(neighborNum).setSide(Direction.South, room);
                        room.setSide(Direction.North, maze.getRoom(neighborNum));
                    }
                }

                edge = line.get(3);
                if (edge.contains("d")) {
                    if (doors.containsKey(edge)) {
                        doors.get(edge).add(Direction.South);
                    } else {
                        doors.put(edge, new ArrayList<>());
                        doors.get(edge).add(Direction.South);
                    }
                } else if (edge.equals("wall")) {
                    room.setSide(Direction.South, factory.makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = factory.makeRoom(neighborNum);
                        maze.addRoom(newRoom);
                        room.setSide(Direction.South, maze.getRoom(neighborNum));
                        maze.getRoom(neighborNum).setSide(Direction.North, room);
                    } else {
                        maze.getRoom(neighborNum).setSide(Direction.North, room);
                        room.setSide(Direction.South, maze.getRoom(neighborNum));
                    }
                }

                edge = line.get(4);
                if (edge.contains("d")) {
                    if (doors.containsKey(edge)) {
                        doors.get(edge).add(Direction.East);
                    } else {
                        doors.put(edge, new ArrayList<>());
                        doors.get(edge).add(Direction.East);
                    }
                } else if (edge.equals("wall")) {
                    room.setSide(Direction.East, factory.makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = factory.makeRoom(neighborNum);
                        maze.addRoom(newRoom);
                        room.setSide(Direction.East, maze.getRoom(neighborNum));
                        maze.getRoom(neighborNum).setSide(Direction.West, room);
                    } else {
                        maze.getRoom(neighborNum).setSide(Direction.West, room);
                        room.setSide(Direction.East, maze.getRoom(neighborNum));
                    }
                }

                edge = line.get(5);
                if (edge.contains("d")) {
                    if (doors.containsKey(edge)) {
                        doors.get(edge).add(Direction.West);
                    } else {
                        doors.put(edge, new ArrayList<>());
                        doors.get(edge).add(Direction.West);
                    }
                } else if (edge.equals("wall")) {
                    room.setSide(Direction.West, factory.makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = factory.makeRoom(neighborNum);
                        maze.addRoom(newRoom);
                        room.setSide(Direction.West, maze.getRoom(neighborNum));
                        maze.getRoom(neighborNum).setSide(Direction.East, room);
                    } else {
                        maze.getRoom(neighborNum).setSide(Direction.East, room);
                        room.setSide(Direction.West, maze.getRoom(neighborNum));
                    }
                }

            } else {
                if (line.size() > 1) {
                    String doorName = line.get(1);
                    Room r1 = maze.getRoom(Integer.parseInt(line.get(2)));
                    Room r2 = maze.getRoom(Integer.parseInt(line.get(3)));

                    Door door = factory.makeDoor(r1, r2);
                    if (r1.getSide(doors.get(doorName).get(0)) == null) {
                        r1.setSide(doors.get(doorName).get(0), door);
                        r2.setSide(doors.get(doorName).get(1), door);
                    } else {
                        r1.setSide(doors.get(doorName).get(1), door);
                        r2.setSide(doors.get(doorName).get(0), door);
                    }
                    door.setOpen(line.get(4).equals("open"));
                }
            }
        }
        return maze;
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("What color maze do you want? (Red or Blue): ");
        String input = scanner.nextLine();
        Maze maze;
        MazeFactory factory = null;
        switch (input) {
            case "Red":
                factory = new RedMazeFactory();
                break;
            case "Blue":
                factory = new BlueMazeFactory();
                break;
        }
        maze = loadMaze("large.maze", factory);
        MazeViewer viewer = new MazeViewer(maze);
        viewer.run();
    }
}
