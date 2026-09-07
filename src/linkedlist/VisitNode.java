package linkedlist;

import models.Visit;

/** Each node stores one visit and a link to the next visit. */
public class VisitNode {
    Visit visit;
    VisitNode next;

    public VisitNode(Visit visit) {
        this.visit = visit;
        this.next = null;
    }
}
