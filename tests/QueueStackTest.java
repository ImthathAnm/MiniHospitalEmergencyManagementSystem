import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import models.Patient;
import models.TreatmentRecord;
import queue.EmergencyQueue;
import stack.TreatmentStack;

/** Standalone tests; no testing framework or Collection Framework is needed. */
public class QueueStackTest {
    private static int checks;

    public static void main(String[] args) {
        testQueue();
        testStack();
        System.out.println("QueueStackTest passed (" + checks + " checks).");
    }

    private static void testQueue() {
        EmergencyQueue queue = new EmergencyQueue();
        Patient first = new Patient(101, "First Patient", 25, "0771234567", "Fever");
        Patient second = new Patient(102, "Second Patient", 22, "0712345678", "Injury");
        Patient third = new Patient(103, "Third Patient", 35, "0755555555", "Diabetes");

        check(queue.isEmpty(), "A new queue must be empty.");
        check(queue.peek() == null, "Peeking at an empty queue must return null.");
        check(queue.dequeue() == null, "Removing from an empty queue must return null.");
        check(!queue.containsPatient(101), "An empty queue must not contain a patient.");
        check(captureQueue(queue).contains("No emergency patients are waiting."),
                "An empty queue must show a clear message.");

        boolean rejectedNull = false;
        try {
            queue.enqueue(null);
        } catch (IllegalArgumentException exception) {
            rejectedNull = true;
        }
        check(rejectedNull, "The queue must reject a null patient.");
        check(queue.isEmpty(), "Rejecting null must leave the queue empty.");

        queue.enqueue(first);
        queue.enqueue(second);
        queue.enqueue(third);
        check(!queue.isEmpty(), "The queue must contain enqueued patients.");
        check(queue.peek() == first, "Peek must return the first patient without removal.");
        check(queue.containsPatient(101), "The queue must find the front patient.");
        check(queue.containsPatient(102), "The queue must find a middle patient.");
        check(queue.containsPatient(103), "The queue must find the rear patient.");
        check(!queue.containsPatient(999), "The queue must reject an unknown ID.");

        String output = captureQueue(queue);
        check(output.contains("Emergency Queue:"), "The queue must show its heading.");
        check(appearsBefore(output, "First Patient", "Second Patient")
                && appearsBefore(output, "Second Patient", "Third Patient"),
                "The queue display must follow FIFO order.");

        check(queue.dequeue() == first, "FIFO must remove the first patient first.");
        check(!queue.containsPatient(101), "A removed patient must no longer be waiting.");
        check(queue.peek() == second, "Peek must advance to the next patient.");
        check(queue.dequeue() == second, "FIFO must remove the second patient next.");
        check(queue.dequeue() == third, "FIFO must remove the last patient last.");
        check(queue.isEmpty(), "The queue must become empty after all removals.");
        check(queue.peek() == null, "An emptied queue must have no front patient.");
        check(queue.dequeue() == null, "Repeated empty removal must return null.");

        // Reusing the queue catches errors caused by a stale rear pointer.
        queue.enqueue(second);
        queue.enqueue(first);
        check(queue.peek() == second, "A reused queue must have the correct front.");
        check(queue.dequeue() == second, "A reused queue must preserve FIFO order.");
        check(queue.dequeue() == first, "A reused queue must preserve its last patient.");
        check(queue.isEmpty(), "The reused queue must empty correctly.");
    }

    private static void testStack() {
        TreatmentStack stack = new TreatmentStack();
        TreatmentRecord first = new TreatmentRecord(101, "Ahmed", "First treatment", "2026-09-01");
        TreatmentRecord second = new TreatmentRecord(102, "Rimas", "Second treatment", "2026-09-02");
        TreatmentRecord third = new TreatmentRecord(103, "Mohamed", "Third treatment", "2026-09-03");

        check(stack.isEmpty(), "A new stack must be empty.");
        check(stack.pop() == null, "Removing from an empty stack must return null.");
        check(captureStack(stack).contains("No treatment records found."),
                "An empty stack must show a clear message.");

        boolean rejectedNull = false;
        try {
            stack.push(null);
        } catch (IllegalArgumentException exception) {
            rejectedNull = true;
        }
        check(rejectedNull, "The stack must reject a null record.");
        check(stack.isEmpty(), "Rejecting null must leave the stack empty.");

        stack.push(first);
        stack.push(second);
        stack.push(third);
        check(!stack.isEmpty(), "The stack must contain pushed records.");

        String output = captureStack(stack);
        check(output.contains("Treatment History"), "The stack must show its heading.");
        check(appearsBefore(output, "Third treatment", "Second treatment")
                && appearsBefore(output, "Second treatment", "First treatment"),
                "The stack display must show the latest treatment first.");

        check(stack.pop() == third, "LIFO must remove the latest treatment first.");
        check(stack.pop() == second, "LIFO must remove the previous treatment next.");
        check(stack.pop() == first, "LIFO must remove the oldest treatment last.");
        check(stack.isEmpty(), "The stack must become empty after all removals.");
        check(stack.pop() == null, "Repeated empty removal must return null.");

        stack.push(second);
        stack.push(first);
        check(stack.pop() == first, "A reused stack must preserve LIFO order.");
        check(stack.pop() == second, "A reused stack must preserve its oldest record.");
        check(stack.isEmpty(), "The reused stack must empty correctly.");
    }

    private static String captureQueue(EmergencyQueue queue) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        PrintStream capturedOutput = new PrintStream(buffer);
        try {
            System.setOut(capturedOutput);
            queue.displayQueue();
        } finally {
            System.setOut(originalOutput);
            capturedOutput.close();
        }
        return buffer.toString();
    }

    private static String captureStack(TreatmentStack stack) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        PrintStream capturedOutput = new PrintStream(buffer);
        try {
            System.setOut(capturedOutput);
            stack.displayStack();
        } finally {
            System.setOut(originalOutput);
            capturedOutput.close();
        }
        return buffer.toString();
    }

    private static boolean appearsBefore(String output, String earlier, String later) {
        int earlierPosition = output.indexOf(earlier);
        int laterPosition = output.indexOf(later);
        return earlierPosition >= 0 && laterPosition > earlierPosition;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        checks++;
    }
}
