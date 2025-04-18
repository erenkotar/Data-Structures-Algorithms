import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please provide input and output file paths.");
            return;
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        String result = null;

        try (PrintStream out = new PrintStream(new FileOutputStream(outputFile));
             Scanner scanner = new Scanner(inputFile)) {
            FeedManager feed = new FeedManager();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;  // skip empty lines
                String[] parts = line.split("\\s+");
                String command = parts[0];
                
                switch (command) {
                    case "create_user":
                        result = feed.create_user(parts[1]).toString();
                        out.println(result);
                        break;
                    case "follow_user":
                        result = feed.follow_user(parts[1], parts[2]).toString();
                        out.println(result);
                        break;
                    case "unfollow_user":
                        result = feed.unfollow_user(parts[1], parts[2]).toString();
                        out.println(result);
                        break;
                    case "create_post":
                            result = feed.create_post(parts[1], parts[2], parts[3]).toString();
                            out.println(result);
                            break;
                    case "see_post":
                        result = feed.see_post(parts[1], parts[2]).toString();
                        out.println(result);
                        break;
                    case "see_all_posts_from_user":
                            result = feed.see_all_posts_from_user(parts[1], parts[2]).toString();
                            out.println(result);
                            break;
                    case "toggle_like":
                        result = feed.toggle_like(parts[1], parts[2]).toString();
                        out.println(result);
                        break;
                    case "generate_feed":
                        result = feed.generate_feed(parts[1], parts[2]).toString();
                        out.println(result);
                        break;
                    case "scroll_through_feed":
                        String userId = parts[1];
                        int numPosts = Integer.parseInt(parts[2]);
                        int[] likes = new int[numPosts];
                        if (parts.length != 3 + numPosts) { // check if the number of parameters is correct
                            System.out.println("error: Incorrect number of parameters for scroll_through_feed");
                            break;
                        }
                        for (int i = 0; i < numPosts; i++) { // read the likes from the input
                            likes[i] = Integer.parseInt(parts[3 + i]);  // start reading likes from the fourth part
                        }
                        result = feed.scroll_through_feed(userId, numPosts, likes).toString();
                        out.println(result);
                        break;
                    case "sort_posts":
                        result = feed.sort_posts(parts[1]).toString();
                        out.println(result);
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing integer: " + e.getMessage());
        }
    }
}
