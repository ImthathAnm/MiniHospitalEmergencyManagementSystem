package bst;

import models.Patient;

/** One patient and links to the smaller and larger patient IDs. */
public class PatientNode {
    Patient patient;
    PatientNode left;
    PatientNode right;

    public PatientNode(Patient patient) {
        this.patient = patient;
        this.left = null;
        this.right = null;
    }
}
