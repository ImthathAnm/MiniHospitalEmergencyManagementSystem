package linkedlist;

import models.Visit;

/** Manually linked visit history in the order visits were added. */
public class VisitLinkedList {
    private VisitNode head;
    private VisitNode tail;

    public boolean isEmpty() {
        return head == null;
    }

    /** Returns false if the visit ID already exists in this patient's history. */
    public boolean addVisit(Visit visit) {
        if (visit == null) {
            throw new IllegalArgumentException("Visit cannot be null.");
        }
        if (searchVisit(visit.getVisitID()) != null) {
            return false;
        }

        VisitNode newNode = new VisitNode(visit);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        return true;
    }

    public Visit searchVisit(int visitID) {
        VisitNode current = head;
        while (current != null) {
            if (current.visit.getVisitID() == visitID) {
                return current.visit;
            }
            current = current.next;
        }
        return null;
    }

    public boolean removeVisit(int visitID) {
        VisitNode previous = null;
        VisitNode current = head;
        while (current != null) {
            if (current.visit.getVisitID() == visitID) {
                if (previous == null) {
                    head = current.next; // Removing the first visit changes the head.
                } else {
                    previous.next = current.next; // Bypass the removed node.
                }
                if (current == tail) {
                    tail = previous; // Also becomes null when removing the only node.
                }
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public void displayVisits() {
        System.out.println("Visit History:");
        if (isEmpty()) {
            System.out.println("No visits found.");
            return;
        }
        VisitNode current = head;
        while (current != null) {
            current.visit.displayVisit();
            System.out.println();
            current = current.next;
        }
    }
}
