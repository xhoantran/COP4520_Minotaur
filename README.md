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

### Correctness

- Synchronization:

  - The use of a global integer and thread-safe mechanisms ensures that only one guest interacts with the labyrinth at a time.
  - Guest 0's role as the 'cake monitor' provides a clear condition for determining when all guests have visited.

- Logic: The structured eating pattern (guest 0 replenishes, others eat once) guarantees that the logic of "all guests have visited" is met when guest 0 requests the final cake.

### Efficiency

- Minimal Blocking: Guests mostly proceed independently without being blocked, except for the inherent synchronization point of accessing the labyrinth.
- The runtime complexity is non deterministic due to the nature of thread scheduling and the fact that the Minotaur's random pick of the next guest is not guaranteed to be fair.

### Experimental Evaluation

| Number of Guests | Time (ms) |
| :--------------: | :-------: |
|        5         |    40     |
|        10        |    130    |
|        20        |    359    |
|        50        |   1868    |
|       100        |   8159    |

The time increases exponentially as the number of guests increases.

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

### Correctness

- Order and Fairness: The queue intrinsically guarantees that guests will be able to view the vase in the order they arrive.
- Synchronization: The queue, combined with signaling between guests, ensures only one guest is viewing the vase at a time.

### Efficiency

- Reduced Contention: The queue eliminates the chaotic checking of the open door policy.
- Overhead: There's a slight overhead in managing the queue data structure itself.

### Experimental Evaluation

| Number of Guests | Time (ms) |
| :--------------: | :-------: |
|        5         |    15     |
|        10        |    16     |
|        20        |    17     |
|        50        |    17     |
|       100        |    21     |
|       200        |    24     |
|       500        |    39     |
|       1000       |    65     |

The time remains relatively constant as the number of guests increases. This is due to the fact that the Minotaur only has to let one guest in at a time, and the queue is managed efficiently.
