import linkedlist.VisitLinkedList;
import models.Patient;
import models.Visit;

/** Standalone regression tests; see the README for compilation and run commands. */
public class VisitLinkedListTest {
    public static void main(String[] args) {
        VisitLinkedList visits = new VisitLinkedList();
        check(visits.isEmpty(), "A new list must be empty.");
        check(visits.searchVisit(1) == null, "Search in an empty list must return null.");
        check(!visits.removeVisit(1), "Removal from an empty list must return false.");
        visits.displayVisits();

        boolean nullRejected = false;
        try {
            visits.addVisit(null);
        } catch (IllegalArgumentException exception) {
            nullRejected = true;
        }
        check(nullRejected, "A null visit must be rejected.");

        Visit first = visit(1);
        Visit second = visit(2);
        Visit third = visit(3);
        Visit fourth = visit(4);
        check(visits.addVisit(first), "Append first visit.");
        check(visits.addVisit(second), "Append second visit.");
        check(visits.addVisit(third), "Append third visit.");
        check(visits.addVisit(fourth), "Append fourth visit.");
        check(!visits.isEmpty(), "A populated list must not be empty.");
        check(visits.searchVisit(1) == first, "Find the head visit.");
        check(visits.searchVisit(2) == second, "Find a middle visit.");
        check(visits.searchVisit(4) == fourth, "Find the tail visit.");
        check(visits.searchVisit(99) == null, "Missing visits must return null.");
        check(!visits.addVisit(visit(2)), "Duplicate IDs must be rejected.");
        check(visits.searchVisit(2) == second, "Duplicate insertion must keep the original.");
        check(!visits.removeVisit(99), "Removing a missing ID must return false.");

        check(visits.removeVisit(1), "Remove the head.");
        check(visits.searchVisit(1) == null, "The removed head must be absent.");
        check(visits.removeVisit(3), "Remove a middle node.");
        check(visits.searchVisit(3) == null, "The removed middle node must be absent.");
        check(visits.searchVisit(2) == second, "Middle removal must preserve earlier nodes.");
        check(visits.searchVisit(4) == fourth, "Middle removal must preserve later nodes.");
        check(visits.removeVisit(4), "Remove the tail.");
        check(visits.searchVisit(4) == null, "The removed tail must be absent.");

        Visit replacementTail = visit(5);
        check(visits.addVisit(replacementTail), "Append after tail removal.");
        check(visits.searchVisit(5) == replacementTail, "The new tail must stay reachable.");
        check(visits.removeVisit(2), "Remove the remaining old node.");
        check(visits.removeVisit(5), "Remove the final node.");
        check(visits.isEmpty(), "Removing every node must empty the list.");
        check(visits.addVisit(first), "An emptied list must accept a new visit.");
        check(visits.searchVisit(1) == first, "The reused list's head must be reachable.");
        check(visits.removeVisit(1) && visits.isEmpty(), "Remove the reused list's only node.");

        Patient ahmed = new Patient(101, "Ahmed", 25, "0771234567", "Fever");
        Patient rimas = new Patient(102, "Rimas", 22, "0712345678", "Injury");
        check(ahmed.getVisitHistory() != rimas.getVisitHistory(), "Patients need separate lists.");
        check(ahmed.getVisitHistory().addVisit(first), "Add Ahmed's visit.");
        check(rimas.getVisitHistory().isEmpty(), "Ahmed's visit must not appear for Rimas.");
        Visit rimasVisit = visit(1);
        check(rimas.getVisitHistory().addVisit(rimasVisit), "Different patients may reuse visit IDs.");
        check(ahmed.getVisitHistory().searchVisit(1) == first, "Keep Ahmed's original visit.");
        check(rimas.getVisitHistory().searchVisit(1) == rimasVisit, "Find Rimas's own visit.");
        check(ahmed.getVisitHistory().removeVisit(1), "Remove Ahmed's visit.");
        check(rimas.getVisitHistory().searchVisit(1) == rimasVisit, "Keep Rimas's history intact.");

        System.out.println("VisitLinkedListTest passed.");
    }

    private static Visit visit(int id) {
        return new Visit(id, "2026-09-06", "Dr. Silva", "Fever", "Rest");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
