import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Queue;

class Lnode {
    int num;
    protected Lnode next;

    Lnode(int num){
        this.num = num;
        next = null;
    }

    Lnode getNext(){
        return next;
    }

    void setNext(Lnode n){
        next = n;
    }
}

class LinkedQ {
    Lnode front, back;
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
            back.setNext(f);//todo: ????????????

        }
    }

}

class ArrayQ {
    int front, back, cap;
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


    public Elevators(int curr_floor, int num_pass, int max_pass, boolean q_state){
        this.curr_floor = curr_floor;
        this.num_pass = num_pass;
        this.max_pass = max_pass;
        this.q_state = q_state;
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

    Queue<Integer> pass_pending;

    public Floor(){//todo: constructor
        pass_pending = new PriorityQueue<Integer>();
    }

    public void gen_pass(int floors, int passengers){
        Random rand = new Random();
        if (rand.nextDouble() <= passengers){
            pass_pending.add(rand.nextInt(floors + 1) + 0);
        }
    }
}

public class Elev {
    public static void main(String[]args) {
        //read from args[0] and configure the following elements:
        /*
         * structures          default = linked
         * floors              default = 32
         * passengers          default = 0.03
         * elevators           default = 1
         * elevatorCapacity    default = 10
         * duration            default = 500
         */

        String structures = "linked";
        int floors = 32; //floors
        double psgr = 0.03; //passengers
        int elev_num = 1; //elevators
        int elev_Cap = 10; //elevatorCapacity
        int dur = 500; //duration

        Floor floor1 = new Floor();

        floor1.pass_pending.add(1);

        if (args[0] != null){
            try (FileReader reader = new FileReader(args[0])){

                Properties prop = new Properties();
                prop.load(reader);

                if (prop.containsKey("floors")) floors = (int) prop.get("floors");
                if (prop.containsKey("passengers")) psgr = (double) prop.get("passengers");
                if (prop.containsKey("elevators")) elev_num = (int) prop.get("elevators");
                if (prop.containsKey("elevatorCapacity")) elev_Cap = (int) prop.get("elevatorCapacity");
                if (prop.containsKey("duration")) dur = (int) prop.get("duration");

                if (prop.containsKey("structures")) structures = (String) prop.get("structures");
                //todo: set a flag that tells whether or not to use linked structures or array structures

            } catch (FileNotFoundException e) {
                //continue as normal
            } catch (IOException e) {
                //continue as normal
            }
        }
    }
}