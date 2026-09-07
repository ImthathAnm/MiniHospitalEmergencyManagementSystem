package stack;

import models.TreatmentRecord;

/** LIFO stack: the most recently added treatment is at the top. */
public class TreatmentStack {
    private StackNode top;

    public boolean isEmpty() {
        return top == null;
    }

    public void push(TreatmentRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Treatment record cannot be null.");
        }
        StackNode newNode = new StackNode(record);
        newNode.next = top;
        top = newNode;
    }

    public TreatmentRecord pop() {
        if (isEmpty()) {
            return null;
        }
        TreatmentRecord record = top.record;
        top = top.next;
        return record;
    }

    public void displayStack() {
        System.out.println("Treatment History:");
        if (isEmpty()) {
            System.out.println("No treatment records found.");
            return;
        }
        StackNode current = top;
        int position = 1;
        while (current != null) {
            System.out.println("Treatment " + position + ":");
            current.record.displayTreatment();
            System.out.println();
            current = current.next;
            position++;
        }
    }
}
