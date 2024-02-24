public class BirthdayParty {
    private static final boolean DEBUG = true;
    private static final int NUM_GUESTS = 5;

    private static boolean cupcakeOnPlate = true;

    // This variable is used to indicate the guest that has picked by the Minotaur
    // to enter the labyrinth
    private static int pickedGuest = -1;


    public static void main(String[] args) {
        for (int i = 0; i < NUM_GUESTS; i++) {
            new Thread(new Guest(i, i == 0)).start();
        }

        new Thread(new Minotaur()).start();
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
                if (pickedGuest == -1) {
                    // Pick a guest to enter the labyrinth
                    int random = (int) (Math.random() * NUM_GUESTS);
                    log("--------------------\nMinotaur picked guest " + random + " to enter the labyrinth.");
                    pickedGuest = random;
                }
            }
        }
    }

    /*
     * Guests will enter the labyrinth and eat the cupcake if they are picked by the Minotaur
     */
    static class Guest implements Runnable {
        private int guestNumber;
        private boolean counter;
        private boolean eaten = false;
        private int numGuestsEaten = 0;

        public Guest(int guestNumber, boolean counter) {
            this.counter = counter;
            this.guestNumber = guestNumber;
        }

        @Override
        public void run() {
            while (true) {
                // If the guest is not the one picked by the Minotaur, then wait
                if (guestNumber == pickedGuest) {
                    log("Guest " + guestNumber + " enters the labyrinth.");

                    // If the guest is the counter and the plate is empty
                    if (counter && !cupcakeOnPlate) {

                        // Someone has eaten the cupcake, increment the counter
                        numGuestsEaten++;
                        log(numGuestsEaten + " guests have eaten.");

                        // If all guests have eaten, then announce it
                        if (numGuestsEaten == NUM_GUESTS) {
                            log("All guests have eaten.");
                            System.exit(0);
                        }

                        // Put another cupcake on the plate
                        log("Put another cupcake on the plate.");
                        cupcakeOnPlate = true;
                    } else if (!eaten && cupcakeOnPlate) {
                        // If the guest has not eaten and the cupcake is on the plate, then eat the cupcake
                        cupcakeOnPlate = false;
                        eaten = true;
                        log("Guest " + guestNumber + " eats the cupcake.");
                    }

                    // Else, the guest leaves the labyrinth
                    log("Guest " + guestNumber + " leaves the labyrinth.");
                    pickedGuest = -1;
                } else {
                    try {
                        // This is to prevent deadlock
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
