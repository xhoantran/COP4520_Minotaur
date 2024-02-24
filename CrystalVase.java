import java.util.concurrent.ConcurrentLinkedQueue;

public class CrystalVase {
    private static final boolean DEBUG = false;
    private static final int NUM_GUESTS = 50;
    private static final ConcurrentLinkedQueue<Thread> queue = new ConcurrentLinkedQueue<>();
    private static int numNotifiedGuests = 0;
    private static final long START_TIME = System.nanoTime();

    public static void main(String[] args) {
        // Create and start guest threads
        Thread[] guests = new Thread[NUM_GUESTS];
        for (int i = 0; i < NUM_GUESTS; i++) {
            guests[i] = new Thread(new Guest(), "Guest " + i);
            guests[i].start();
        }

        // Wait for all guest threads to finish
        for (int i = 0; i < NUM_GUESTS; i++) {
            try {
                guests[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        log("All guests have viewed the vase!");
        log("Number of guests notified: " + numNotifiedGuests);
        System.out.println("Time taken: " + (System.nanoTime() - START_TIME) / 1000000 + "ms");
    }

    private static void log(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    static class Guest implements Runnable {
        private int backOffTime = 0;
        private static final int INCREMENT_BACKOFF = 5;
        private static final int MAX_BACKOFF_TIME = 50;

        /*
         * Guests join the queue to view the vase
         */
        private static synchronized void enqueue() {
            queue.add(Thread.currentThread());
            log(Thread.currentThread().getName() + " has joined the queue.");
        }

        /*
         * Guests leave the queue after viewing the vase
         */
        private static void dequeue() {
            queue.remove();
            String nextInLine = queue.peek() == null ? "None" : queue.peek().getName();
            log(Thread.currentThread().getName() + " has left the queue.\nNext in line: " + nextInLine + "\n---");
        }

        @Override
        public void run() {
            // Join the queue
            enqueue(); 

            // Implement the backoff strategy to avoid busy waiting
            while (queue.peek() != Thread.currentThread()) {
                try {
                    Thread.sleep(backOffTime); // Back off before retrying
                    backOffTime = (backOffTime + INCREMENT_BACKOFF) % MAX_BACKOFF_TIME;
                } catch (InterruptedException e) {
                    numNotifiedGuests++;
                    log(Thread.currentThread().getName() + " was notified.");
                }
            }

            // View the vase
            enterShowroom(); 

            // Leave the queue
            dequeue();

            // Notify the next in line
            if (queue.peek() != null) {
                queue.peek().interrupt();
            }
        }

        // Simulate viewing the vase
        private void enterShowroom() {
            log(Thread.currentThread().getName() + " is viewing the vase...");
        }

        @Override
        public String toString() {
            return Thread.currentThread().getName();
        }
    }
}
