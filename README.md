# Assignment 2: The Minotaur

## How to Run

This project uses OpenJDK 18. You can download it [here](https://jdk.java.net/archive/).

- In the root directory, run `javac *.java` to compile the programs.
- Run `java BirthdayParty` to run the birthday party program.
- Run `java CrystalVase` to run the crystal vase program.

**Note**:

- You can specify the number of guests for both programs by changing the `NUM_GUESTS` variable in the respective source files.
- You can turn on/off debug mode for both programs by setting the `DEBUG` variable to `true` in the respective source files.

## Minotaur's Birthday Party

- Create a thread for the Minotaur with a global integer to keep track which guest is currently in the labyrinth.
- Create `NUM_GUESTS` threads to represent the guests.
- Guest 0 will be responsible to ask for another cake whenever it is found empty. Each other guest is to eat the cake the very first time they find it full, otherwise they are to leave it empty. When guest 0 asks for the `NUM_GUESTS` cake, they can safely assume that all guests have been in the labyrinth.

## Minotaur's Crystal Vase

### Open Door:

- Pros: Simple, no extra coordination needed.
- Cons:
  - Potentially chaotic with crowding, guests may repeatedly check.
  - There's no guarantee that all guests will see the vase.
  - Guest will not know when it's their turn to enter the room.

### Sign:

- Pros: Improves upon open door, reduces unnecessary attempts.
- Cons: Relies on guest cooperation; a non-compliant guest could disrupt the system.

### Queue:

- Pros: Ensures fairness (first-come, first-served), reduces crowding, guests don't repeatedly check.
- Cons: Slightly more overhead due to queue management.

### Here's how it works:

- Guests are added to a queue when they arrive at the door.
- The Minotaur will let the next guest in when the room is empty.
- Guests will leave the queue when they see the vase, and notify the next guest in line if there is one.
