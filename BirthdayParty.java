import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BirthdayParty {
    private static final boolean DEBUG = true;
    private static final int NUM_GUESTS = 5;
    private static final Lock mutex = new ReentrantLock(true);

    // This shared variable is used by the Minotaur's servant to count the 
    // number of guests that have been eaten 
    private static int numGuestsEaten = 0;

    // This variable is used to indicate whether the cupcake is on the plate
    private static boolean cupcakeOnPlate = true;

    // This variable is used to indicate the guest that has picked by the Minotaur
    // to enter the labyrinth
    private static int pickedGuest = -1;


    public static void main(String[] args) {
        for (int i = 0; i < NUM_GUESTS; i++) {
            new Thread(new Guest(i)).start();
        }

        new Thread(new Minotaur()).start();
    }

    /*
     * Guests ask the servant for the cupcake
     */
    private static void askForCupcake(int guestNumber) {
        // This method is used by the guests to ask for the cupcake
        if (!cupcakeOnPlate) {
            log("Guest " + guestNumber + " asks for the cupcake.");
            cupcakeOnPlate = true;
        }
    }


    /*
     * Guests ask the servant if all guests have been eaten
     */
    private static boolean askIfAllEatCupcake() {
        return numGuestsEaten == NUM_GUESTS;
    }


    /*
     * This method is used to log messages
     */
    private static void log(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }

    /*
     * Minotaur will pick a guest to enter the labyrinth if no guest is picked
     */
    static class Minotaur implements Runnable {
        @Override
        public void run() {
            while (true) {
                mutex.lock();
                if (pickedGuest == -1) {
                    // Pick a guest to enter the labyrinth
                    pickedGuest = (int) (Math.random() * NUM_GUESTS);
                    log("--------------------");
                    log("Minotaur picked guest " + pickedGuest + " to enter the labyrinth.");
                }
                mutex.unlock();
            }
        }
    }

    /*
     * Guests will enter the labyrinth and eat the cupcake if they are picked by the Minotaur
     */
    static class Guest implements Runnable {
        private int guestNumber;
        private boolean eaten = false;

        public Guest(int guestNumber) {
            this.guestNumber = guestNumber;
        }

        @Override
        public void run() {
            while (true) {
                mutex.lock();

                // If the guest is not the one picked by the Minotaur, then wait
                if (guestNumber == pickedGuest) {
                    log("Guest " + guestNumber + " enters the labyrinth.");

                    // If the guest ate the cupcake, then leave the labyrinth
                    if (!eaten) {

                        // If the cupcake is not on the plate, then ask for new one
                        if (!cupcakeOnPlate) {
                            askForCupcake(guestNumber);
                        }

                        log("Guest " + guestNumber + " eats the cupcake.");
                        eaten = true;

                        numGuestsEaten++;
                        cupcakeOnPlate = false;

                        if (askIfAllEatCupcake()) {
                            log("Servant confirms that all guests have tried the cupcake.");
                            System.exit(0);
                        }
                    }

                    pickedGuest = -1;
                    log("Guest " + guestNumber + " leaves the labyrinth.");
                }

                mutex.unlock();
            }
        }
    }
}
