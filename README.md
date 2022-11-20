# Bus Allocation

Correct seat update, bus allocation, and checkpoint processing. design &amp; programming in proper OOP and multithreading style.

## Requirements

1) Create file T1.txt, T2.txt, T3.txt. Each file contains 10 transactions to be processed by each TicketCounter thread in (4). Each line consists of transaction ID, name of tour group, number of passengers, and destination (A = airport bound, C = city bound).
2) Implement class BusLine with at least the following members:
- 2.1 Variables destination (airport or city) and maximum seats (all buses have the same number of max seats)
- 2.2 Method allocateBus : this method is called by TicketCounter to allocate a bus to a tour group:
  - Put a tour group in the latest Bus
  - If the latest Bus cannot accommodate all passengers in this group, then add a new Bus to the BusLine and put the remaining passengers in the new Bus
  - That is, all previous Buses must be full before a new Bus can be added. And passengers in 1 group may be put in more than 1 Buses.
- 2.3 Add more variables and methods as needed
3) (Optional) You may also implement class Bus (each BusLine has a number of Buses) and class Group (each Bus accommodates a number of tour groups)
4) Implement class TicketCounter that extends Thread or implements Runnable. It must have at least the following members
- 4.1 Variable BusLines : there will be 2 BusLines in the program, airport-bound and city-bound. BusLine objects may be put in an array or ArrayList. All threads must work on the same copy of BusLines
- 4.2 Add more variables and methods as needed
- 4.3 In this project, there will be 3 TicketCounter threads. Each thread processes transactions in each file in (1). To process each transaction:
  - Check the destination and call method allocateBus of the correct BusLine
  - Print thread’s activities with at least the following information: thread’s name, transaction ID, name of tour group, BusLine and Bus number, and number of passengers put in that Bus
  - Each transaction processing may result in multiple output lines, if multiple buses are allocated
  - If this transaction is also a checkpoint, Each TicketCounter thread must wait until all others also reach the checkpoint. Let 1 thread (any TicketCounter thread or main thread) report how many Buses have been allocated in each BusLine, with at least the following information: thread’s name, BusLine, and number of allocated Buses. All TicketCounter threads continue processing their transactions, starting from checkpoint
5) Implement class Simulation that acts as the main class. When the program starts:
- 5.1 Get the following input from user
  - Maximum seats (all Buses in all BusLines have equal capacity)
  - Transaction ID that will be used as the checkpoint
- 5.2 Create 2 BusLines, airport-bound and city-bound
- 5.3 Create 3 TicketCounter threads to process transactions in files T1.txt, T2.txt, and T3.txt separately
- 5.4 Once all TicketCounter threads complete their transaction processing, report the allocation of all Buses in all BusLines. Print at least the following: thread’s name, BusLine and Bus number, group names and number of passengers put in that Bus
6) In summary, your program must have
- 6.1 Transaction files : T1.txt, T2.txt, T3.txt – each file has 10 transactions, assuming no input error and no duplicate group names. But your program must still handle missing file(s)
- 6.2 Classes : BusLine, TicketCounter, Simulation (main class)
- 6.3 You may add more classes as needed

## Authors

- Natthawee Koengfak ([etnk125](https://github.com/etnk125))
- Nicharee Chalermsuksri ([nnichar](https://github.com/nnichar)) 
