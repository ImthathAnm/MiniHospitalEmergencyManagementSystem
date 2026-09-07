package models;

import linkedlist.VisitLinkedList;

/** A registered patient and their own visit history. */
public class Patient {
    private int patientID;
    private String patientName;
    private int age;
    private String contactNumber;
    private String medicalCondition;
    private final VisitLinkedList visitHistory;

    public Patient(int patientID, String patientName, int age,
            String contactNumber, String medicalCondition) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.age = age;
        this.contactNumber = contactNumber;
        this.medicalCondition = medicalCondition;
        this.visitHistory = new VisitLinkedList();
    }

    public int getPatientID() { return patientID; }

    // The ID is the BST key: do not change it while this patient is in the tree.
    public void setPatientID(int patientID) { this.patientID = patientID; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getMedicalCondition() { return medicalCondition; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }
    public VisitLinkedList getVisitHistory() { return visitHistory; }

    public void displayPatient() {
        System.out.println("ID: " + patientID);
        System.out.println("Name: " + patientName);
        System.out.println("Age: " + age);
        System.out.println("Contact: " + contactNumber);
        System.out.println("Condition: " + medicalCondition);
    }
}
