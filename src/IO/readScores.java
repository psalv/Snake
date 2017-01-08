package IO;

import Queue.LinkedPriorityQueue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class readScores {

    private BufferedReader in;
    private String filename = "scores.txt";

    private LinkedPriorityQueue<String> queue = new LinkedPriorityQueue();

    /**
     * Reads from the file scores.txt and adds them to a priority queue based on their scores.
     */
    public readScores(){
        try {
            in = new BufferedReader(new FileReader(filename));

            readDataToQueue();

            in.close();
        }

        catch (FileNotFoundException ee){
            System.out.println("File " + filename + " not found.");
            System.exit(0);
        }

        catch(IOException e){
            System.out.println("Trouble closing file.");
            System.exit(0);
        }


    }

    /**
     * Adds scores to a priority queue. Names attached to scores can be multiple words long, uses last entry of a line as the priority.
     */
    private void readDataToQueue(){

        try {
            String line = in.readLine();

            while (!(line == null)) {
                String[] l = line.split(" ");
                int s = l.length;

                String name = "";
                for(int i = 0; i < s - 1; ++i) {
                    if (name.equals(""))
                        name += l[i];
                    else
                        name +=  " " + l[i];
                }

                queue.enqueue(name, Integer.parseInt(l[s - 1]));
                line = in.readLine();

            }
        }
        catch(IOException e){
            System.out.println("File cannot be read.");
        }

    }

    /**
     * Returns the priority queue containing the scores (in order).
     * @return a priority queue of scores
     */
    public LinkedPriorityQueue getQueue(){
//        System.out.println(queue);
        return queue;
    }

}
