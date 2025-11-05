/*
 * SimpleMazeGame.java
 * Copyright (c) 2008, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import maze.ui.MazeViewer;

/**
 * 
 * @author Sunny
 * @version 1.0
 * @since 1.0
 */
public class MazeGameCreator
{
	/**
	 * Creates a small maze.
	 */
	public Maze createMaze()
	{
		
		Maze maze = new Maze();
		Room room1 = makeRoom(0);
        Room room2 = makeRoom(1);
        maze.addRoom(room1);
        maze.addRoom(room2);
        Door door = makeDoor(room1, room2);
        room1.setSide(Direction.North, makeWall());
        room1.setSide(Direction.East, makeWall());
        room1.setSide(Direction.West, makeWall());
        room1.setSide(Direction.South, door);
        room2.setSide(Direction.North, door);
        room2.setSide(Direction.East, makeWall());
        room2.setSide(Direction.South, makeWall());
        room2.setSide(Direction.West, makeWall());

		return maze;
		

	}

	public static Maze loadMaze(final String path) {
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
                    room = makeRoom(num);
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
                    room.setSide(Direction.North, makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = makeRoom(neighborNum);
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
                    room.setSide(Direction.South, makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = makeRoom(neighborNum);
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
                    room.setSide(Direction.East, makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = makeRoom(neighborNum);
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
                    room.setSide(Direction.West, makeWall());
                } else {
                    int neighborNum = Integer.parseInt(edge);
                    if (maze.getRoom(neighborNum) == null) {
                        Room newRoom = makeRoom(neighborNum);
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

                    Door door = makeDoor(r1, r2);
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

    public static Wall makeWall() {
        return new Wall();
    }

    public static Door makeDoor(Room r1, Room r2) {
        return new Door(r1, r2);
    }

    public static Room makeRoom(int roomNum) {
        return new Room(roomNum);
    }

	public static void main(String[] args)
	{
        Scanner scanner = new Scanner(System.in);
        System.out.print("What color maze do you want? (Red, Blue, Basic): ");
        String input = scanner.nextLine();
        Maze maze;
        if (input.equals("Red")) {
            maze = RedMazeGameCreator.loadMaze("large.maze");
            MazeViewer viewer = new MazeViewer(maze);
            viewer.run();
        }
        if (input.equals("Blue")) {
            maze = BlueMazeGameCreator.loadMaze("large.maze");
            MazeViewer viewer = new MazeViewer(maze);
            viewer.run();
        }
        if (input.equals("Basic")) {
            maze = loadMaze("large.maze");
            MazeViewer viewer = new MazeViewer(maze);
            viewer.run();
        }
	}
}
