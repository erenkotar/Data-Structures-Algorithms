import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class MapManagerTest {
    private MapManager magicalMap;
    
    public MapManagerTest() {
        this.magicalMap = new MapManager();
    }

    public void loadAllData(String nodePath, String edgePath, String objPath) throws FileNotFoundException {
        readNodes(nodePath);
        readEdges(edgePath);
        readObjectives(objPath);
    }

    private void readNodes(String filePath) throws FileNotFoundException {
        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(new File(filePath));
        String initLine = scanner.nextLine();
        String[] initParts = initLine.split(" ");
        int width = Integer.parseInt(initParts[0]);
        int height = Integer.parseInt(initParts[1]);
        magicalMap.setGridSize(width, height);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int type = Integer.parseInt(parts[2]);
            magicalMap.addNode(x, y, type);
            magicalMap.setType(type, false);

        }
        scanner.close();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("\nTotal execution time (readNodes): " + duration + " ms");
    }

    private void readEdges(String filePath) throws FileNotFoundException {
        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(new File(filePath));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            String[] nodeKeys = parts[0].split(",");
            double weight = Double.parseDouble(parts[1]);
            magicalMap.addEdge(nodeKeys[0], nodeKeys[1], weight);
        }
        scanner.close();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("\nTotal execution time (readEdges): " + duration + " ms");
    }

    private void readObjectives(String filePath) throws FileNotFoundException {
        long startTime = System.nanoTime();
        Scanner scanner = new Scanner(new File(filePath));
        magicalMap.setSightRadius(scanner.nextInt());
        int startX = scanner.nextInt();
        int startY = scanner.nextInt();
        magicalMap.setStartingNode(startX, startY);

        scanner.nextLine(); // Consume the newline character

        int objIdx = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            MapObj obj = new MapObj(x, y);
            obj.setObjIdx(objIdx++);
            
            if (parts.length > 2) {
                for (int i = 2; i < parts.length; i++) {
                    obj.addOption(Integer.parseInt(parts[i]));
                }
            }
            magicalMap.addObjective(obj);
        }
        scanner.close();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("\nTotal execution time (readObjectives): " + duration + " ms");
    }

    private static class TestResult {
        private List<String> errors;
        private List<LineMismatch> mismatches;
        private int totalLines;
        
        public TestResult() {
            this.errors = new ArrayList<>();
            this.mismatches = new ArrayList<>();
        }
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public void addMismatch(int lineNumber, String expected, String actual) {
            mismatches.add(new LineMismatch(lineNumber, expected, actual));
        }
        
        public void setTotalLines(int totalLines) {
            this.totalLines = totalLines;
        }
        
        public void printReport() {
            if (!errors.isEmpty()) {
                System.out.println("\n=== Errors ===");
                for (String error : errors) {
                    System.out.println(error);
                }
            }
            
            if (!mismatches.isEmpty()) {
                System.out.println("\n=== Line Mismatches ===");
                for (LineMismatch mismatch : mismatches) {
                    System.out.println("Line " + mismatch.lineNumber + ":");
                    System.out.println("  Expected: " + mismatch.expected);
                    System.out.println("  Actual:   " + mismatch.actual);
                    System.out.println();
                }
            }
            
            int matchedLines = totalLines - mismatches.size();
            System.out.println("\n=== Summary ===");
            System.out.println("Total lines: " + totalLines);
            System.out.println("Matched lines: " + matchedLines);
            System.out.println("Mismatched lines: " + mismatches.size());
            
            if (totalLines > 0) {
                System.out.printf("Match percentage: %.2f%%\n", 
                                (double) matchedLines / totalLines * 100);
            }
            
            if (errors.isEmpty() && mismatches.isEmpty() && totalLines > 0) {
                System.out.println("\nTest passed! All output lines match expected results.");
            } else {
                System.out.println("\nTest failed. Please check the details above.");
            }
        }
    }
    
    private static class LineMismatch {
        int lineNumber;
        String expected;
        String actual;
        
        public LineMismatch(int lineNumber, String expected, String actual) {
            this.lineNumber = lineNumber;
            this.expected = expected;
            this.actual = actual;
        }
    }

    private static TestResult compareOutputs(String actualOutputPath, String expectedOutputPath) {
        TestResult result = new TestResult();
        
        File actualFile = new File(actualOutputPath);
        File expectedFile = new File(expectedOutputPath);
        
        // Check file existence
        if (!actualFile.exists()) {
            result.addError("Actual output file does not exist: " + actualOutputPath);
            return result;
        }
        if (!expectedFile.exists()) {
            result.addError("Expected output file does not exist: " + expectedOutputPath);
            return result;
        }
        
        // Check if files are empty
        if (actualFile.length() == 0) {
            result.addError("Actual output file is empty: " + actualOutputPath);
            return result;
        }
        if (expectedFile.length() == 0) {
            result.addError("Expected output file is empty: " + expectedOutputPath);
            return result;
        }

        try (BufferedReader actualReader = new BufferedReader(new FileReader(actualFile));
             BufferedReader expectedReader = new BufferedReader(new FileReader(expectedFile))) {
            
            List<String> actualLines = new ArrayList<>();
            List<String> expectedLines = new ArrayList<>();
            
            // Read all lines
            String line;
            while ((line = actualReader.readLine()) != null) {
                // Only add non-empty lines
                if (!line.trim().isEmpty()) {
                    actualLines.add(line.trim());
                }
            }
            while ((line = expectedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    expectedLines.add(line.trim());
                }
            }
            
            System.out.println("Debug: Found " + actualLines.size() + " lines in actual output");
            System.out.println("Debug: Found " + expectedLines.size() + " lines in expected output");
            
            // Compare line counts
            if (actualLines.size() != expectedLines.size()) {
                result.addError(String.format("Line count mismatch! Expected: %d lines, but got: %d lines", 
                              expectedLines.size(), actualLines.size()));
            }
            
            // Compare contents
            int maxLines = Math.max(actualLines.size(), expectedLines.size());
            for (int i = 0; i < maxLines; i++) {
                String actual = i < actualLines.size() ? actualLines.get(i) : "[NO LINE]";
                String expected = i < expectedLines.size() ? expectedLines.get(i) : "[NO LINE]";
                
                if (!actual.equals(expected)) {
                    result.addMismatch(i + 1, expected, actual);
                }
            }
            
            result.setTotalLines(maxLines);
            
        } catch (IOException e) {
            result.addError("Error reading output files: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }

    public static void main(String[] args) {
        long startTime;
        long endTime;
        long duration;

        startTime = System.nanoTime();
        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/4-4/4-4-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/4-4/4-4-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/4-4/nodes-4-4.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/4-4/edges-4-4.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/4-4/obj-4-4.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-32-32/1st-32-32-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-32-32/1st-32-32-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-32-32/1st-nodes-32-32.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-32-32/1st-edges-32-32.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-32-32/1st-obj-32-32.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-64-64/1st-64-64-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-64-64/1st-64-64-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-64-64/1st-nodes-64-64.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-64-64/1st-edges-64-64.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/1st-64-64/1st-obj-64-64.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-32-32/2nd-32-32-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-32-32/2nd-32-32-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-32-32/2nd-nodes-32-32.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-32-32/2nd-edges-32-32.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-32-32/2nd-obj-32-32.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-64-64/2nd-64-64-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-64-64/2nd-64-64-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-64-64/2nd-64-64-nodes.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-64-64/2nd-64-64-edges.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/2nd-64-64/2nd-64-64-obj.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/128-128/128-128-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/128-128/128-128-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/128-128/nodes-128-128.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/128-128/edges-128-128.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/small/128-128/obj-128-128.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-300-300/1st-300-300-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-300-300/1st-300-300-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-300-300/1st-nodes-300-300.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-300-300/1st-edges-300-300.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-300-300/1st-obj-300-300.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-400-400/1st-400-400-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-400-400/1st-400-400-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-400-400/test-nodes-400-400.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-400-400/test-edges-400-400.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/1st-400-400/test-obj-400-400.txt";

        // String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/2nd-400-400/2nd-400-400-actual.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/2nd-400-400/2nd-400-400-output.txt";
        // String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/2nd-400-400/2nd-nodes-400-400.txt";
        // String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/2nd-400-400/2nd-edges-400-400.txt";
        // String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/2nd-400-400/2nd-obj-400-400.txt";

        String actualOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/500-500-actual.txt";
        String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/500-500-output-withRadius20.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/500-500-output-withRadius100.txt";
        // String expectedOutputPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/500-500-output-withRadius500.txt";
        String nodePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/nodes-500-500.txt";
        String edgePath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/edges-500-500.txt";
        String objPath = "/Users/erenkotar/Documents/GitHub/MagicalMap/test_cases/large/500-500/obj-500-500.txt";

        try {
 
            // Run the test
            MapManagerTest mapTest = new MapManagerTest();
            mapTest.loadAllData(nodePath, edgePath, objPath);
            
            startTime = System.nanoTime();
            mapTest.magicalMap.startingVisibility();
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000;
            System.out.println("\nTotal execution time (startingVisibility): " + duration + " ms");
            
            // Get output
            startTime = System.nanoTime();
            StringBuilder output = mapTest.magicalMap.iterateObjs();
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000;
            System.out.println("\nTotal execution time (iterateObjs): " + duration + " ms");

            String outputStr = output.toString();
            
            // Debug output content
            System.out.println("Debug: Output length: " + outputStr.length());
            // if (outputStr.length() > 0) {
            //     System.out.println("Debug: First few characters: " + 
            //                      (outputStr.length() > 50 ? outputStr.substring(0, 50) : outputStr));
            // }
            
            // Write output
            try (PrintWriter writer = new PrintWriter(new FileWriter(actualOutputPath))) {
                writer.print(outputStr);
            }

            // Verify files
            File actualFile = new File(actualOutputPath);
            File expectedFile = new File(expectedOutputPath);
            
            System.out.println("Debug: Actual file exists: " + actualFile.exists() + 
                             ", size: " + actualFile.length());
            System.out.println("Debug: Expected file exists: " + expectedFile.exists() + 
                             ", size: " + expectedFile.length());

            // Compare outputs
            TestResult result = compareOutputs(actualOutputPath, expectedOutputPath);
            result.printReport();

            // Print execution time
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1_000_000;
            System.out.println("\nTotal execution time: " + duration + " ms");

        } catch (FileNotFoundException e) {
            System.out.println("Error with file operations: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error writing output file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}