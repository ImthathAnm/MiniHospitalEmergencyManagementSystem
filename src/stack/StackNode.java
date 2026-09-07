package stack;

import models.TreatmentRecord;

public class StackNode {
    TreatmentRecord record;
    StackNode next;

    public StackNode(TreatmentRecord record) {
        this.record = record;
        this.next = null;
    }
}
