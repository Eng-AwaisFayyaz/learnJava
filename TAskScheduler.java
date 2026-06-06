import java.util.*;

enum Priority {
    LOW, MEDIUM, HIGH, CRITICAL
}

enum Status {
    PENDING, RUNNING, COMPLETED
}

class Task implements Comparable<Task> {

    static int counter = 1;
    int id;
    String name;
    Priority priority;
    Status status;

    Task(String name, Priority priority) {
        this.id = counter++;
        this.name = name;
        this.priority = priority;
        this.status = Status.PENDING;
    }

    @Override
    public int compareTo(Task other) {
        return other.priority.ordinal() - this.priority.ordinal();
    }

    @Override
    public String toString() {
        return "[T-" + id + "] " + name + " | " + priority + " | " + status;
    }
}

class Scheduler {

    enum Mode { FIFO, PRIORITY }

    Mode mode;
    Queue<Task> fifoQueue;
    PriorityQueue<Task> priorityQueue;
    List<Task> completed;

    Scheduler(Mode mode) {
        this.mode = mode;
        this.fifoQueue = new LinkedList<>();
        this.priorityQueue = new PriorityQueue<>();
        this.completed = new ArrayList<>();
    }

    void submit(Task task) {
        if (mode == Mode.FIFO) {
            fifoQueue.add(task);
        } else {
            priorityQueue.add(task);
        }
        System.out.println("Submitted: " + task);
    }

    void runNext() {
        Task task;

        if (mode == Mode.FIFO) {
            task = fifoQueue.poll();
        } else {
            task = priorityQueue.poll();
        }

        if (task == null) {
            System.out.println("Queue is empty.");
            return;
        }

        task.status = Status.RUNNING;
        System.out.println("Running:   " + task);

        task.status = Status.COMPLETED;
        completed.add(task);
        System.out.println("Completed: " + task);
        System.out.println();
    }

    void runAll() {
        if (mode == Mode.FIFO) {
            while (!fifoQueue.isEmpty()) {
                runNext();
            }
        } else {
            while (!priorityQueue.isEmpty()) {
                runNext();
            }
        }
    }

    void printSummary() {
        System.out.println("Total completed: " + completed.size());
        for (Task t : completed) {
            System.out.println(t);
        }
    }
}

public class TAskScheduler {

    public static void main(String[] args) {

        System.out.println("===== FIFO MODE =====\n");

        Scheduler fifo = new Scheduler(Scheduler.Mode.FIFO);

        fifo.submit(new Task("Send Email",      Priority.LOW));
        fifo.submit(new Task("Monthly Report",  Priority.HIGH));
        fifo.submit(new Task("Database Backup", Priority.MEDIUM));
        fifo.submit(new Task("Process Payment", Priority.CRITICAL));

        System.out.println();
        fifo.runAll();
        fifo.printSummary();

        System.out.println("\n===== PRIORITY MODE =====\n");

        Scheduler prio = new Scheduler(Scheduler.Mode.PRIORITY);

        prio.submit(new Task("Send Email",      Priority.LOW));
        prio.submit(new Task("Monthly Report",  Priority.HIGH));
        prio.submit(new Task("Database Backup", Priority.MEDIUM));
        prio.submit(new Task("Process Payment", Priority.CRITICAL));
        prio.submit(new Task("Security Alert",  Priority.CRITICAL));

        System.out.println();
        prio.runAll();
        prio.printSummary();
    }
}