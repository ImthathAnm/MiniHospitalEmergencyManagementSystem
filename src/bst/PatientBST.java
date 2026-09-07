package bst;

import models.Patient;

/**
 * An ordinary binary search tree ordered by patient ID.
 * Search, insert and delete take O(h) time, where h is the tree height.
 * This tree is not balanced, so h can equal the number of patients.
 */
public class PatientBST {
    private PatientNode root;

    public PatientBST() {
        root = null;
    }

    /**
     * Adds a patient, returning false if the ID already exists.
     * Do not change a patient's ID while the patient is stored in the tree.
     */
    public boolean insert(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }

        if (root == null) {
            root = new PatientNode(patient);
            return true;
        }

        PatientNode current = root;
        while (true) {
            if (patient.getPatientID() == current.patient.getPatientID()) {
                return false;
            }

            if (patient.getPatientID() < current.patient.getPatientID()) {
                if (current.left == null) {
                    current.left = new PatientNode(patient);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new PatientNode(patient);
                    return true;
                }
                current = current.right;
            }
        }
    }

    /** Returns the stored patient, or null when no matching ID exists. */
    public Patient search(int patientID) {
        PatientNode current = root;
        while (current != null) {
            if (patientID == current.patient.getPatientID()) {
                return current.patient;
            }

            if (patientID < current.patient.getPatientID()) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    /** Removes the matching patient, returning false when the ID is absent. */
    public boolean delete(int patientID) {
        if (search(patientID) == null) {
            return false;
        }

        // Deleting the root can produce a new root, so keep the returned link.
        root = deleteNode(root, patientID);
        return true;
    }

    /** Returns the new root of the subtree after deletion. */
    private PatientNode deleteNode(PatientNode node, int patientID) {
        if (node == null) {
            return null;
        }

        if (patientID < node.patient.getPatientID()) {
            node.left = deleteNode(node.left, patientID);
        } else if (patientID > node.patient.getPatientID()) {
            node.right = deleteNode(node.right, patientID);
        } else {
            // No left child: replace this node by its right child (or null).
            if (node.left == null) {
                return node.right;
            }

            // No right child: replace this node by its left child.
            if (node.right == null) {
                return node.left;
            }

            // Two children: use the smallest patient in the right subtree.
            PatientNode successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }

            // Keep the whole Patient object, including that patient's visits.
            node.patient = successor.patient;
            node.right = deleteNode(node.right, successor.patient.getPatientID());
        }

        return node;
    }

    /** Displays every patient in ascending ID order, taking O(n) time. */
    public void inorderTraversal() {
        if (isEmpty()) {
            System.out.println("No patients found.");
            return;
        }
        inorderTraversal(root);
    }

    private void inorderTraversal(PatientNode node) {
        if (node == null) {
            return;
        }

        // Left subtree, current patient, right subtree gives sorted IDs.
        inorderTraversal(node.left);
        node.patient.displayPatient();
        inorderTraversal(node.right);
    }

    public boolean isEmpty() {
        return root == null;
    }
}
