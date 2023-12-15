package Lab4;

import Lab4.enums.PhilosophersState;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import java.util.*;

@Getter
@Setter
public class PhilosophersDinner extends Thread {
    public static HashMap<Integer, Philosopher> philosophers = new HashMap<>();
    public static List<Boolean> philosophersList = new ArrayList<>();

    public static void threadsJoining(Thread[] threads) throws InterruptedException {
        for (Thread thread: threads) {
            thread.join();
        }
        System.out.println('\n');
    }

    public static void startDinner() throws InterruptedException {
        Thread[] threads = new Thread[5];
        for (int i = 1; i <= 5; i++) {
            philosophers.put(i, new Philosopher(i));
            philosophersList.add(i - 1, false);
            threads[i - 1] = philosophers.get(i);
        }

        for (int i = 1; i <= 5 ; i++) {
            threads[i - 1].start();
        }
        threadsJoining(threads);

        while (true){
            for (int i = 0; i < threads.length; i++) {
                Thread thread = new Thread(philosophers.get(i + 1));
                threads[i] = thread;
                threads[i].start();
            }
            threadsJoining(threads);

            if (!philosophersList.contains(false)){
                System.out.println("Все наелись!");
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        startDinner();
    }

    @Getter
    @Setter
    static class Philosopher extends Thread{
        private volatile PhilosophersState philosophersState;
        private volatile int orderNumber;

        public Philosopher(int orderNumber){
            this.orderNumber = orderNumber;
            this.philosophersState = PhilosophersState.THINK;
        }

        @SneakyThrows
        @Override
        public synchronized void run() {
                int left;
                int right;

                if (orderNumber == 1 || orderNumber == 5) {
                    if (orderNumber == 1) {
                        left = orderNumber + 1;
                        right = 5;
                    } else {
                        left = 1;
                        right = 4;
                    }
                } else {
                    left = orderNumber + 1;
                    right = orderNumber - 1;
                }

                Philosopher currentPhilosopher = philosophers.get(orderNumber);
                Philosopher rightPhilosopher = philosophers.get(right);
                Philosopher leftPhilosopher = philosophers.get(left);
                if (currentPhilosopher.getPhilosophersState().equals(PhilosophersState.PUTFORKSBACK)){
                    philosophersList.set(orderNumber - 1, true);
                    currentPhilosopher.setPhilosophersState(PhilosophersState.THINK);
                    Thread.sleep(2000);
                }
                else if (currentPhilosopher.getPhilosophersState().equals(PhilosophersState.EAT)){
                    currentPhilosopher.setPhilosophersState(PhilosophersState.PUTFORKSBACK);
                }
                else if (currentPhilosopher.getPhilosophersState().equals(PhilosophersState.TAKERIGHTFORK)){
                    currentPhilosopher.setPhilosophersState(PhilosophersState.EAT);
                }
                else if(currentPhilosopher.getPhilosophersState().equals(PhilosophersState.TAKELEFTFORK)){
                    currentPhilosopher.setPhilosophersState(PhilosophersState.TAKERIGHTFORK);
                }
                else if (currentPhilosopher.getPhilosophersState().equals(PhilosophersState.THINK)) {
                    if (rightPhilosopher.getPhilosophersState().equals(PhilosophersState.THINK)
                            && leftPhilosopher.getPhilosophersState().equals(PhilosophersState.THINK)){
                        currentPhilosopher.setPhilosophersState(PhilosophersState.TAKELEFTFORK);
                    }
                }
                System.out.println("Order Number: " + orderNumber + " State: " + philosophersState);
                Thread.sleep(500);
        }

        @Override
        public String toString() {
            return "Philosopher{" +
                    "state=" + philosophersState +
                    ", orderNumber=" + orderNumber +
                    '}';
        }
    }
}
