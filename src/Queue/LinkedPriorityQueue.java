package Queue;

import java.util.Iterator;

public class LinkedPriorityQueue<T> implements PriorityQueueADT<T>
{
    private int count;
    private PriorityNode<T> front, rear;

    /**
     * Creates an empty queue.
     */
    public LinkedPriorityQueue()
    {
        count = 0;
        front = rear = null;
    }

    /**
     * Adds the specified element to the rear of this queue.
     *
     * @param element  the element to be added to the rear of this queue
     */
    public void enqueue (T element)
    {
        PriorityNode<T> node = new PriorityNode<T>(element);

        if (isEmpty())
            front = node;
        else
            rear.setNext (node);

        rear = node;
        count++;
    }

    /**
     * Adds the specified element to the priority queue in the proper location based on priority.
     *
     * @param element  the element to be added to the rear of this queue
     */
    public void enqueue (T element, int p)
    {
        //Create the Queue.Queue.PriorityNode to be added to the queue
        PriorityNode<T> toQueue = new PriorityNode<T>(element, p);
        toQueue.setPriority(p);

        //If the queue is empty we set both the front and the rear to be this element.
        if(isEmpty()) {
            front = toQueue;
            rear = toQueue;
        }

        else {

            //Try to find a predecessor. If we find one, add new node after it.
            try {
                PriorityNode<T> predecessor = findPredecessorToPrioritySpot(p);
                toQueue.setNext(predecessor.getNext());
                predecessor.setNext(toQueue);
            }

            //Indication that the node should be first.
            catch (IndexOutOfBoundsException e) {
                toQueue.setNext(front);
                front = toQueue;
            }

            //Indication that the node should be last.
            catch (NullPointerException e) {
                rear.setNext(toQueue);
                rear = toQueue;
            }
        }
        count++;
    }

    /**
     * Finds and returns the element in the queue that is the predecessor of the element we wish to enqueue.
     * Bases priorites on the maximum priority taking precedent.
     *
     * Throws an IndexOutOfBoundsException if the element we wish to insert should go at the beginning.
     * Throws a NullPointerException if the element we wish to insert should go at the end.
     *
     * @param p the priority of the element that we are inserting into the queue
     * @return the Queue.Queue.PriorityNode in the queue that the node we wish to insert should follow
     */
    private PriorityNode findPredecessorToPrioritySpot(double p) throws NullPointerException
    {
        if(p > front.getPriority()){
            throw new IndexOutOfBoundsException();
        }

        else{
            PriorityNode current = front;

            while (current.getNext().getPriority() > p) {
                current = current.getNext();
            }

            return current;
        }
    }

    /**
     * Removes the element at the front of this queue and returns a
     * reference to it. Throws an Queue.Queue.EmptyCollectionException if the
     * queue is empty.
     *
     * @return                           the element at the front of this queue
     * @throws EmptyCollectionException  if an empty collection exception occurs
     */
    public T dequeue() throws EmptyCollectionException
    {
        if (isEmpty())
            throw new EmptyCollectionException("queue");

        T result = front.getElement();
        front = front.getNext();
        count--;

        if (isEmpty())
            rear = null;

        return result;
    }

    /**
     * Returns a reference to the element at the front of this queue.
     * The element is not removed from the queue.  Throws an
     * Queue.Queue.EmptyCollectionException if the queue is empty.
     *
     * @return                            a reference to the first element in
     *                                    this queue
     * @throws EmptyCollectionsException  if an empty collection exception occurs
     */
    public T first() throws EmptyCollectionException
    {
        return front.getElement();
    }

    public int lastPriority(){

        if(count < 10)
            return 0;

        else{
            PriorityNode current = front;
            for(int i = 1; i < 10; ++i){
                current = current.getNext();
            }

            return current.getPriority();
        }
    }

    public int firstPriority(){
        if(count == 0)
            return 0;
        return front.getPriority();
    }

    public PriorityNode<T> getFront(){
        return front;
    }

    /**
     * Returns true if this queue is empty and false otherwise.
     *
     * @return  true if this queue is empty and false if otherwise
     */
    public boolean isEmpty()
    {
        return count == 0;
    }

    /**
     * Returns the number of elements currently in this queue.
     *
     * @return  the integer representation of the size of this queue
     */
    public int size()
    {
        return count;
    }

    /**
     * Returns a string representation of this queue.
     *
     * @return  the string representation of this queue
     */

    public Iterator iterator(){
        return new LinkedIterator(front, count);
    }

    public String toString()
    {
        if(isEmpty())
            return "Queue is empty.";

        PriorityNode current = front;
        String stringRepresentation = "";
        for(int i = 0; i < count && i <= 9; ++i){
            stringRepresentation += current.toString() + "\n";
            current = current.getNext();
        }

        return stringRepresentation;
    }
}