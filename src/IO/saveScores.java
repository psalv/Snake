package IO;

import Queue.LinkedPriorityQueue;
import Queue.PriorityNode;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class saveScores {

    private BufferedWriter out;
    private LinkedPriorityQueue queue;
    private String filename = "scores.txt";

    public saveScores(LinkedPriorityQueue queue){

        this.queue = queue;

        try {
            out = new BufferedWriter(new FileWriter(filename));
        }
        catch (FileNotFoundException e){
            System.out.println("File " + filename + " not found.");
            System.exit(0);
        }

        catch (IOException e){
            System.out.println("File " + filename + " not found.");
            System.exit(0);
        }



        saveDataFromQueue();

        try {
            out.close();
        }
        catch(IOException e){
            System.out.println("Trouble closing file.");
            System.exit(0);
        }

    }

    public void saveDataFromQueue(){

        PriorityNode current = queue.getFront();
        int count = 0;

        try {
            while (current != null && count != 10) {
                ++count;
                String toWrite = current.getElement() + " " + String.valueOf((int)current.getPriority()) + "\n";
                out.write(toWrite);
                current = current.getNext();
            }
        }
        catch(IOException e){
            System.out.println("File cannot be written too.");
            System.exit(0);
        }
    }
}
