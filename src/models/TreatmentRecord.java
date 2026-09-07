package models;

/** Stores the patient's ID and name as a snapshot of a completed treatment. */
public class TreatmentRecord {
    private int patientID;
    private String patientName;
    private String treatment;
    private String date;

    public TreatmentRecord(int patientID, String patientName, String treatment, String date) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.treatment = treatment;
        this.date = date;
    }

    public int getPatientID() { return patientID; }
    public void setPatientID(int patientID) { this.patientID = patientID; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public void displayTreatment() {
        System.out.println("Patient ID: " + patientID);
        System.out.println("Patient Name: " + patientName);
        System.out.println("Treatment: " + treatment);
        System.out.println("Date: " + date);
    }
}
