package models;

/** One visit, stored in the owning patient's singly linked list. */
public class Visit {
    private int visitID;
    private String visitDate;
    private String doctorName;
    private String diagnosis;
    private String treatment;

    public Visit(int visitID, String visitDate, String doctorName,
            String diagnosis, String treatment) {
        this.visitID = visitID;
        this.visitDate = visitDate;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    public int getVisitID() { return visitID; }

    // Keep IDs unchanged while stored, so the list's duplicate-ID rule is preserved.
    public void setVisitID(int visitID) { this.visitID = visitID; }

    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public void displayVisit() {
        System.out.println("Visit ID: " + visitID);
        System.out.println("Visit Date: " + visitDate);
        System.out.println("Doctor: " + doctorName);
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Treatment: " + treatment);
    }
}
