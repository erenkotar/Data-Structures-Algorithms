import java.io.*;
import java.util.Scanner;

public class Main {
    private static MapManager magicalMap;

    public static void main(String[] args) {
        if (args.length != 4) {
            // check exception
            System.out.println("Usage: java Main <nodes_file> <edges_file> <objectives_file> <output_file>");
            return;
        }

        try {
            // create map manager
            magicalMap = new MapManager();  // Initial capacity of 100
            
            // read input files
            readNodes(args[0]);
            readEdges(args[1]);
            readObjectives(args[2]);

            // process the map
            magicalMap.startingVisibility();
            
            // get output and write to file
            StringBuilder output = magicalMap.iterateObjs();
            writeOutput(args[3], output.toString());

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: IO Exception - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void readNodes(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        
        // read grid size from first line
        String[] gridSize = scanner.nextLine().trim().split(" ");
        int width = Integer.parseInt(gridSize[0]);
        int height = Integer.parseInt(gridSize[1]);
        magicalMap.setGridSize(width, height); // set grid size

        // read nodes
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int type = Integer.parseInt(parts[2]);
            magicalMap.addNode(x, y, type); // add node to map
            magicalMap.setType(type, false); // add type into typeStatus hash table in MapGraph class
        }
        scanner.close();
    }

    private static void readEdges(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split(" ");
            String[] nodes = parts[0].split(",");
            double weight = Double.parseDouble(parts[1]);
            
            magicalMap.addEdge(nodes[0], nodes[1], weight); // add edge to map
        }
        scanner.close();
    }

    private static void readObjectives(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        
        // read sight radius
        int sightRadius = scanner.nextInt();
        magicalMap.setSightRadius(sightRadius);
        
        // read starting position
        int startX = scanner.nextInt();
        int startY = scanner.nextInt();
        magicalMap.setStartingNode(startX, startY);
        
        scanner.nextLine(); 
        
        // read objectives
        int objIdx = 1; // index should start from 1
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            
            MapObj obj = new MapObj(x, y);
            obj.setObjIdx(objIdx++);
            
            // if there are additional numbers in line, they are wizard options
            if (parts.length > 2) {
                for (int i = 2; i < parts.length; i++) {
                    // remaining parts are wizard options
                    obj.addOption(Integer.parseInt(parts[i]));
                }
            }
            
            magicalMap.addObjective(obj);
        }
        scanner.close();
    }

    private static void writeOutput(String filePath, String content) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
        }
    }
}