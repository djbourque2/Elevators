import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Queue;

class Q {//this is a parent class that will give me more flexibility in my queue implementations
    public void enQueue(int i){}
    public int deQueue(){return 0;}
    public int peek(){return 0;}
}

class Lnode {
    int num;
    Lnode next;

    public Lnode(int num){
        this.num = num;
        this.next = null;
    }

    public Lnode getNext(){
        return next;
    }

    public void setNext(Lnode n){
        next = n;
    }
}

class LinkedQ extends Q{
    public Lnode front, back;
    public LinkedQ(){
        front = null;
        back = null;
    }

    public void enQueue(int pass){
        Lnode f = new Lnode(pass);
        if (front == null){
            front = f;
            back = front;
        } else {
            back.setNext(f);//todo: find out why this isn't working
            back = f;
        }
    }

    public int deQueue(){
        int ret = front.num;
        this.front = this.front.next;
        return ret;
    }

    public int peek(){
        return front.num;
    }
}

class ArrayQ extends Q{
    int front, back, cap;
    int[] arr;

    public ArrayQ(int cap){
        front = 0;
        back = 0;
        this.cap = cap;
        arr = new int[cap];
    }

    public void enQueue(int n){
        if (back < cap){
            arr[back] = n;
            back++;
        }
    }
    public int deQueue(){
        int ret = arr[front];
        if (front == back){
            return -1;
        } else {
            for (int i = 0; i < back-1; i++) {
                arr[i] = arr[i-1];
            }
            if (back < cap){
                arr[back] = 0;
            }
            back--;
        }
        return front;
    }

    public int peek(){
        return arr[front];
    }
}

class Elevators {
    //todo: set semi-configurable states/values, constructor
    /*
     * curr_floor,  current floor position
     * num_pass,    number of passengers
     * max_pass,    maximum number of passengers
     */
    // passFloor_stack,  determines the order in which the passengers are dropped off first
    // queue state, determines whether passFloor_ is array-based or link-based,
    int curr_floor, num_pass, max_pass;
    boolean q_state;
    //Queue<Integer> passFloor_stack;//todo: make array versions and linked versions
    Q passFloor_stack;


    public Elevators(int curr_floor, int num_pass, int max_pass, boolean q_state){
        this.curr_floor = curr_floor;
        this.num_pass = num_pass;
        this.max_pass = max_pass;
        this.q_state = q_state;
        if (q_state){
            passFloor_stack = new ArrayQ(max_pass);
        } else {
            passFloor_stack = new LinkedQ();
        }
    }
}

class Floor {
    //use case: in an array/linked list, where an elevator iterates forward/backward through the array to simulate moving
    /*
     * pass_pending,    queue for waiting passengers
     */
    /* functions:
        gen_pass()  takes the passengers chance + others as parameters to add a new passenger (represented as an int)
                    to the pass_pending stack
     */

    Q pass_pending;

    public Floor(boolean s, int cap){//if true, array, if false, linked
        if (s){
            pass_pending = new ArrayQ(cap);
        } else {
            pass_pending = new LinkedQ();
        }
    }

    public void gen_pass(int floors, int passengers){
        Random rand = new Random();
        if (rand.nextDouble() <= passengers){
            pass_pending.enQueue(rand.nextInt(floors + 1) + 0);
        }
    }
}

public class Elev {
    public static void main(String[]args) {
        //read from args[0] and configure the following elements:
        /**
         * structures          default = linked
         * floors              default = 32
         * passengers          default = 0.03
         * elevators           default = 1
         * elevatorCapacity    default = 10
         * duration            default = 500
        **/

        String structures = "linked";
        int floors = 32; //floors
        double psgr = 0.03; //passengers
        int elev_num = 1; //elevators
        int elev_Cap = 10; //elevatorCapacity
        int dur = 500; //duration

        boolean state;

        ArrayQ n = new ArrayQ(10);
        n.enQueue(1);
        System.out.println(n.peek());
        n.enQueue(2);
        System.out.println(n.peek());
        n.deQueue();
        System.out.println(n.peek());


        try (FileReader reader = new FileReader(args[0])){
            Properties prop = new Properties();
            prop.load(reader);

            if (prop.containsKey("floors")) floors = Integer.parseInt(prop.getProperty("floors"));
            if (prop.containsKey("passengers")) psgr = Double.parseDouble(prop.getProperty("passengers"));
            if (prop.containsKey("elevators")) elev_num = Integer.parseInt(prop.getProperty("elevators"));
            if (prop.containsKey("elevatorCapacity")) elev_Cap = Integer.parseInt(prop.getProperty("elevatorCapacity"));
            if (prop.containsKey("duration")) dur = Integer.parseInt(prop.getProperty("duration"));

            if (prop.containsKey("structures")) structures = prop.getProperty("structures");
            //todo: set a flag that tells whether or not to use linked structures or array structures
            if (structures.equals("linked")){
                state = false;
            } else {
                state = true;
            }

        } catch (FileNotFoundException e) {
            //continue as normal
        } catch (IOException e) {
            //continue as normal
        } catch (ArrayIndexOutOfBoundsException e) {
            //continue as normal
        }


    }
}