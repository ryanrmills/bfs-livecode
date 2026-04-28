import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Search {
    public static void main(String[] args){
        // Location myLocation = new Location(5, 88);
        // System.out.println(myLocation.row());
        char[][] grid = {
            {'o', 'o', 'o', 'o', 'c', 'w', 'c', 'o'},
            {'w', 'o', 'o', 'w', 'w', 'c', 'w', 'o'},
            {'o', 'o', 'o', 'o', 'R', 'w', 'o', 'o'},
            {'o', 'o', 'w', 'w', 'w', 'o', 'o', 'o'},
            {'o', 'o', 'o', 'o', 'c', 'o', 'o', 'o'}
        };

        nearestCheese(grid);
    }
     /**
     * Finds the location of the nearest reachable cheese from the rat's position.
     * Returns a 2-element int array: [row, col] of the closest 'c'. If there are multiple
     * cheeses that are tied for the same shortest distance to reach, return
     * any one of them.
     * 
     * 'R' - the rat's starting position (exactly one)
     * 'o' - open space the rat can walk on
     * 'w' - wall the rat cannot pass through
     * 'c' - cheese that the rat wants to reach
     * 
     * If no rat is found, throws EscapedRatException.
     * If more than one rat is found, throws CrowdedMazeException.
     * If no cheese is reachable, throws HungryRatException
     *
     * oooocwco
     * woowwcwo
     * ooooRwoo
     * oowwwooo
     * oooocooo
     *
     * The method will return [0,4] as the nearest cheese.
     *
     * @param maze 2D char array representing the maze
     * @return int[] location of the closest cheese in row, column format
     * @throws EscapedRatException if there is no rat in the maze
     * @throws CrowdedMazeException if there is more than one rat in the maze
     * @throws HungryRatException if there is no reachable cheese
     */
    public static Location nearestCheese(char[][] maze) throws EscapedRatException, CrowdedMazeException, HungryRatException {
        Location start = ratLocation(maze);

        Queue<Location> queue = new LinkedList<>();

        queue.add(start);

        Set<Location> visited = new HashSet<>();

        Map<Location, Location> prevs = new HashMap<>();

        while (!queue.isEmpty()){
            Location current = queue.poll();

            if (maze[current.row()][current.col()] == 'c'){
                List<Location> path = new ArrayList<>();

                Location pointer = current;

                while (!pointer.equals(start)){
                    path.add(pointer);
                    pointer = prevs.get(pointer);
                }

                path.add(start);

                System.out.println(path.reversed());

                return current;
            }

            for (Location neighbor : neighbors(maze, current)){
                if (!visited.contains(neighbor)) {
                    prevs.put(neighbor, current);
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        throw new HungryRatException();
    }

    public static List<Location> neighbors(char[][] maze, Location current){
        List<Location> result = new ArrayList<>();
        int[][] moves = new int[][]{
            {-1, 0}, //UP
            {1, 0}, //DOWN
            {0, -1}, //LEFT
            {0, 1} //RIGHT
        };

        for (int[] move : moves){
            int newR = current.row() + move[0];
            int newC = current.col() + move[1];

            if (
                newR >= 0 && newR < maze.length && 
                newC >= 0 && newC < maze[0].length &&
                maze[newR][newC] != 'w'
            ){
                result.add(new Location(newR, newC));
            }
        }

        return result;
    }

    public static Location ratLocation(char[][] maze) throws EscapedRatException, CrowdedMazeException {
        Location location = null;
        for (int r = 0; r < maze.length; r++){
            for (int c = 0; c < maze[r].length; c++){
                if (maze[r][c] == 'R'){
                    if (location == null) {
                        location = new Location(r, c);
                    } else {
                        throw new CrowdedMazeException();
                    }
                    
                }
            }
        }
        
        if (location == null) throw new EscapedRatException();
        return location;
    }
}