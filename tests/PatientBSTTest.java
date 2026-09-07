import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import bst.PatientBST;
import linkedlist.VisitLinkedList;
import models.Patient;

/** Standalone behavior tests; see the README for compilation and run commands. */
public class PatientBSTTest {
    private static int checks = 0;

    public static void main(String[] args) {
        testEmptyTree();
        testInsertionAndSearch();
        testLeafAndOneChildDeletion();
        testTwoChildrenDeletion();
        testRootAndLastPatientDeletion();
        System.out.println("PatientBSTTest passed (" + checks + " checks).");
    }

    private static void testEmptyTree() {
        PatientBST tree = new PatientBST();
        check(tree.isEmpty(), "A new tree must be empty.");
        check(tree.search(10) == null, "Searching an empty tree returns null.");
        check(!tree.delete(10), "Deleting from an empty tree returns false.");
        check(captureInorder(tree).trim().length() > 0,
                "An empty traversal should explain that there are no patients.");

        boolean rejected = false;
        try {
            tree.insert(null);
        } catch (IllegalArgumentException exception) {
            rejected = true;
        }
        check(rejected, "Inserting null must be rejected.");
        check(tree.isEmpty(), "A rejected insert must leave the tree empty.");
    }

    private static void testInsertionAndSearch() {
        PatientBST tree = new PatientBST();
        Patient first = patient(40);
        check(tree.insert(first), "The first patient should be inserted.");
        check(tree.insert(patient(20)), "A smaller ID should be inserted.");
        check(tree.insert(patient(60)), "A larger ID should be inserted.");
        check(tree.insert(patient(10)), "A deeper patient should be inserted.");
        check(tree.insert(patient(30)), "Another deeper patient should be inserted.");
        check(!tree.isEmpty(), "A populated tree is not empty.");
        check(!tree.insert(patient(40)), "Duplicate IDs must be rejected.");
        check(tree.search(40) == first, "A duplicate must not replace the patient.");
        check(tree.search(10).getPatientID() == 10, "Search must follow left links.");
        check(tree.search(60).getPatientID() == 60, "Search must follow right links.");
        check(tree.search(99) == null, "An absent patient must return null.");
        checkOrder(tree, "10 20 30 40 60");
        check(!tree.delete(99), "Deleting an absent ID must return false.");
        checkOrder(tree, "10 20 30 40 60");
    }

    private static void testLeafAndOneChildDeletion() {
        PatientBST tree = treeWith(50, 30, 70, 20, 40, 60, 80, 65);
        check(tree.delete(20), "A leaf should be deleted.");
        check(tree.search(20) == null, "A deleted leaf must not be found.");
        checkOrder(tree, "30 40 50 60 65 70 80");

        check(tree.delete(30), "A node with only a right child should be deleted.");
        check(tree.search(40) != null, "The right child must be preserved.");
        checkOrder(tree, "40 50 60 65 70 80");

        check(tree.delete(80), "A right-side leaf should be deleted.");
        check(tree.delete(70), "A node with only a left child should be deleted.");
        check(tree.search(60) != null, "The left child must be preserved.");
        check(tree.search(65) != null, "Descendants of a promoted child must survive.");
        checkOrder(tree, "40 50 60 65");
    }

    private static void testTwoChildrenDeletion() {
        PatientBST immediate = treeWith(50, 30, 70, 80);
        Patient immediateSuccessor = immediate.search(70);
        check(immediate.delete(50), "A root with two children should be deleted.");
        check(immediate.search(50) == null, "The original root must be removed.");
        check(immediate.search(70) == immediateSuccessor,
                "An immediate successor must retain its Patient object.");
        checkOrder(immediate, "30 70 80");

        PatientBST deeper = treeWith(50, 30, 80, 60, 90, 55, 57, 65);
        Patient successor = deeper.search(55);
        VisitLinkedList visits = successor.getVisitHistory();
        check(deeper.delete(50), "A root with a deeper successor should be deleted.");
        check(deeper.search(50) == null, "The deleted root must be absent.");
        check(deeper.search(55) == successor, "Successor identity must be preserved.");
        check(deeper.search(55).getVisitHistory() == visits,
                "The successor must keep the same visit history.");
        check(deeper.search(57) != null, "A successor's right child must survive.");
        checkOrder(deeper, "30 55 57 60 65 80 90");

        check(deeper.delete(80), "A non-root node with two children should be deleted.");
        check(deeper.search(80) == null, "The deleted internal patient must be absent.");
        checkOrder(deeper, "30 55 57 60 65 90");
    }

    private static void testRootAndLastPatientDeletion() {
        PatientBST rightChild = treeWith(10, 20);
        check(rightChild.delete(10), "A root with only a right child should be deleted.");
        checkOrder(rightChild, "20");
        check(rightChild.delete(20), "The last patient should be deleted.");
        check(rightChild.isEmpty(), "Removing the last patient must empty the tree.");
        check(!rightChild.delete(20), "Deleting the same ID again should return false.");
        check(rightChild.insert(patient(30)), "Insertion must work after emptying the tree.");
        checkOrder(rightChild, "30");

        PatientBST leftChild = treeWith(20, 10);
        check(leftChild.delete(20), "A root with only a left child should be deleted.");
        checkOrder(leftChild, "10");
    }

    private static PatientBST treeWith(int... ids) {
        PatientBST tree = new PatientBST();
        for (int id : ids) {
            tree.insert(patient(id));
        }
        return tree;
    }

    private static Patient patient(int id) {
        return new DisplayTestPatient(id);
    }

    private static String captureInorder(PatientBST tree) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        PrintStream captured = new PrintStream(output);
        try {
            System.setOut(captured);
            tree.inorderTraversal();
        } finally {
            System.setOut(original);
            captured.close();
        }
        return output.toString();
    }

    private static void checkOrder(PatientBST tree, String expected) {
        String actual = captureInorder(tree).trim();
        check(expected.equals(actual),
                "Expected sorted IDs [" + expected + "], but received [" + actual + "].");
    }

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    // A short display makes traversal order testable without depending on labels.
    private static class DisplayTestPatient extends Patient {
        DisplayTestPatient(int id) {
            super(id, "Patient " + id, 25, "0771234567", "Fever");
        }

        @Override
        public void displayPatient() {
            System.out.print(getPatientID() + " ");
        }
    }
}
