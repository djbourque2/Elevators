import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Queue;

/* Unused Code removed for being overcomplicated
class Q {//this is an unused/template parent class that will give more flexibility in adaptive queue implementations
    public void enQueue(Object i){}
    public Object deQueue(){return 0;}
    public Object peek(){return 0;}
}

class Lnode {
    Object obj;
    Lnode next;
    Lnode prev;

    public Lnode(Object obj){//remember to always tie in Lnodes correctly
        this.obj = obj;
        this.next = null;
        this.prev = null;
    }

    public Lnode getNext(){
        return next;
    }

    public void setNext(Lnode n){
        next = n;
        n.prev = next;
    }
}

class LinkedQ extends Q{
    public Lnode front, back;
    public LinkedQ(){
        front = null;
        back = null;
    }

    public void enQueue(Object pass){
        Lnode f = new Lnode(pass);
        if (front == null){
            front = f;
            back = front;
        } else {
            back.setNext(f);//todo: find out why this isn't working
            back = f;
        }
    }

    public Object deQueue(){
        Object ret = front.obj;
        this.front = this.front.next;
        return ret;
    }

    public Object peek(){
        return front.obj;
    }
}

class ArrayQ extends Q{
    int front, back, cap;
    Object[] arr;

    public ArrayQ(int cap){
        front = 0;
        back = 0;
        this.cap = cap;
        arr = new Object[cap];
    }

    public void enQueue(Object n){
        if (back < cap){
            arr[back] = n;
            back++;
        }
    }
    public Object deQueue(){
        Object ret = arr[front];
        if (front == back){
            return null;
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

    public Object peek(){
        return arr[front];
    }
}
 */

class Elevators {
    //todo: set semi-configurable states/values, constructor
    /*
     * curr_floor,  current floor position
     * curr_direct, current direction (up = true, down = false)
     * num_pass,    number of passengers
     * max_pass,    maximum number of passengers
     */
    // passFloor_stack,  determines the order in which the passengers are dropped off first
    // queue state, determines whether passFloor_ is array-based or link-based,
    int curr_floor, num_pass, max_pass;
    boolean curr_direct, q_state;
    //Queue<Integer> passFloor_stack;//todo: make array versions and linked versions
    AbstractList passFloor_stack;


    public Elevators(int curr_floor, int num_pass, int max_pass, boolean q_state){
        this.curr_floor = curr_floor;
        this.num_pass = num_pass;
        this.max_pass = max_pass;
        this.q_state = q_state;
        curr_direct = true;
        if (q_state){
            passFloor_stack = new ArrayList<Passenger>(max_pass);
        } else {
            passFloor_stack = new LinkedList<Passenger>();
        }
    }

    public void change_direction(){
        curr_direct = !curr_direct;
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

    AbstractList pass_pending;

    public Floor(boolean s, int cap){//if true, array, if false, linked
        if (s){
            pass_pending = new ArrayList<Passenger>(cap);
        } else {
            pass_pending = new LinkedList();
        }
    }
}

class Passenger {
    int floor;
    long time;

    public Passenger(int floor){
        this.floor = floor;
        time = System.currentTimeMillis();
    }
}

public class Elev {

    public static void gen_pass(int floors, double passengers, int curr, AbstractList pass_pending){//todo: adapt to AbstractList instead of Q
        Random rand = new Random();
        if (rand.nextDouble() <= passengers){
            int n = rand.nextInt(floors + 1) + 0;
            Passenger passenger = new Passenger(n);
            if (n != curr){
                pass_pending.add(passenger);
            }
        }
    }

    public static int find_pass(int cur, AbstractList<Integer> flr, boolean isGreater){
        for (int i = 0; i < flr.size(); i++) {
            if (isGreater){
                if (flr.get(i) > cur) {
                    return i;
                }
            } else {
                if (flr.get(i) < cur) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void all_load(Elevators elevator, AbstractList flrs){
        while (elevator.passFloor_stack.indexOf(elevator.curr_floor) != -1){//unloading phase
            System.out.println(System.currentTimeMillis()-((Passenger) flrs.get(elevator.curr_floor)).time);
            elevator.passFloor_stack.remove(elevator.passFloor_stack.indexOf(elevator.curr_floor));
        }
        Floor f = (Floor) flrs.get(elevator.curr_floor);
        while (find_pass(elevator.curr_floor, f.pass_pending, elevator.curr_direct) != -1){//loading phase
            int c = find_pass(elevator.curr_floor, f.pass_pending, elevator.curr_direct);
            elevator.passFloor_stack.add(f.pass_pending.get(c));
            f.pass_pending.remove(c);
        }
    }

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

        //System.out.println("test");

        boolean state = false;//internal variable

        try (FileReader reader = new FileReader(args[0])){
            Properties prop = new Properties();
            prop.load(reader);

            if (prop.containsKey("floors")) floors = Integer.parseInt(prop.getProperty("floors"));
            if (floors < 2) floors = 32;
            if (prop.containsKey("passengers")) psgr = Double.parseDouble(prop.getProperty("passengers"));
            if ((psgr < 0)||(psgr > 1.0)) psgr = 0.03;
            if (prop.containsKey("elevators")) elev_num = Integer.parseInt(prop.getProperty("elevators"));
            if (elev_num < 1) elev_num = 1;
            if (prop.containsKey("elevatorCapacity")) elev_Cap = Integer.parseInt(prop.getProperty("elevatorCapacity"));
            if (elev_Cap < 1) elev_Cap = 10;
            if (prop.containsKey("duration")) dur = Integer.parseInt(prop.getProperty("duration"));
            if (dur < 1) dur = 500;

            if (prop.containsKey("structures")) structures = prop.getProperty("structures");
            if (structures.equals("linked")){
                state = false;
            } else if (structures.equals("array")){
                state = true;
            } else {
                state = false;
            }

        } catch (FileNotFoundException e) {
            //continue as normal
        } catch (IOException e) {
            //continue as normal
        } catch (ArrayIndexOutOfBoundsException e) {
            //continue as normal
        }

        /**-------------------- THIS IS WHERE THE FUN BEGINS --------------------**/

        //Setting up the Elevators
        AbstractList elevL;
        if (state){
            elevL = new ArrayList<Elevators>(elev_Cap);
        } else {
            elevL = new LinkedList<Elevators>();
        }

        for (int i = 0; i < elev_num; i++) {
            Elevators nElev = new Elevators(0, 0, elev_Cap, state);
            elevL.add(nElev);
        }

        //Setting up Floors
        AbstractList flrL;
        if (state){
            flrL = new ArrayList<Floor>(floors);
        } else {
            flrL = new LinkedList<Floor>();
        }

        for (int i = 0; i < -1; i++) {
            Floor inta = new Floor(state, 50);
            flrL.add(inta);
        }

        /**
         * There are 2 main lists, elevL and flrL, which are abstractLists
         * elevL is a list of Elevators which each have a queue for passenger destinations
         * flrL is a list of Floors which each have a queue for waiting passengers
         */

        for (int i = 0; i < dur; i++) {//each loop represents a tick

            //Passenger Appearance
            for (int k = 0; k < flrL.size(); k++) {
                gen_pass(floors-1, psgr, k, ((Floor) flrL.get(k)).pass_pending);
            }

            for (int j = 0; j < 5; j++) {//Elevator Stuff

                //Elevator load/unload
                for (int k = 0; k < elevL.size()-1; k++) {
                    all_load((Elevators) elevL.get(k), flrL);
                }

                //Elevator advances forward/backward one floor
                for (int k = 0; k < elevL.size(); k++) {
                    if (((Elevators) elevL.get(k)).curr_direct){
                        if (((Elevators) elevL.get(k)).curr_floor < floors-1){
                            ((Elevators) elevL.get(k)).curr_floor++;
                        } else {
                            ((Elevators) elevL.get(k)).curr_direct = !((Elevators) elevL.get(k)).curr_direct;
                            ((Elevators) elevL.get(k)).curr_floor--;
                        }
                    } else {
                        if (((Elevators) elevL.get(k)).curr_floor > 0){
                            ((Elevators) elevL.get(k)).curr_floor--;
                        } else {
                            ((Elevators) elevL.get(k)).curr_direct = !((Elevators) elevL.get(k)).curr_direct;
                            ((Elevators) elevL.get(k)).curr_floor++;
                        }
                    }
                }
            }
        }
        //End
    }
}