import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import bst.PatientBST;
import linkedlist.VisitLinkedList;
import models.Patient;
import models.TreatmentRecord;
import models.Visit;
import queue.EmergencyQueue;
import stack.TreatmentStack;

/** Connects the four manually implemented data structures through a console menu. */
public class Main {
    private final Scanner scanner;
    private final PatientBST patients = new PatientBST();
    private final EmergencyQueue emergencyQueue = new EmergencyQueue();
    private final TreatmentStack treatmentHistory = new TreatmentStack();

    private Main(Scanner scanner) {
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main application = new Main(scanner);
        application.loadSamplePatients();

        try {
            application.run();
        } catch (EndOfInputException exception) {
            // Piped input or a closed terminal can end before the user selects Exit.
            System.out.println("\nInput closed. Goodbye!");
        } finally {
            scanner.close();
        }
    }

    private void loadSamplePatients() {
        patients.insert(new Patient(102, "Rimas", 22, "0712345678", "Injury"));
        patients.insert(new Patient(101, "Ahmed", 25, "0771234567", "Fever"));
        patients.insert(new Patient(103, "Mohamed", 35, "0755555555", "Diabetes"));
        System.out.println("Sample patients 101, 102, and 103 loaded successfully.");
    }

    private void run() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readInt("Enter your choice: ", 0, 14);
            System.out.println();

            switch (choice) {
                case 1: addPatient(); break;
                case 2: searchPatient(); break;
                case 3: deletePatient(); break;
                case 4: patients.inorderTraversal(); break;
                case 5: addEmergencyPatient(); break;
                case 6: emergencyQueue.displayQueue(); break;
                case 7: treatNextPatient(); break;
                case 8: addTreatmentRecord(); break;
                case 9: treatmentHistory.displayStack(); break;
                case 10: removeLastTreatment(); break;
                case 11: addPatientVisit(); break;
                case 12: searchPatientVisit(); break;
                case 13: removePatientVisit(); break;
                case 14: viewPatientVisitHistory(); break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the system. Goodbye!");
                    break;
                default: break; // readInt accepts only the menu choices above.
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n====================================");
        System.out.println(" Mini Hospital Emergency Management");
        System.out.println("====================================");
        System.out.println("1. Add Patient");
        System.out.println("2. Search Patient");
        System.out.println("3. Delete Patient");
        System.out.println("4. Display All Patients");
        System.out.println();
        System.out.println("5. Add Emergency Patient");
        System.out.println("6. Display Emergency Queue");
        System.out.println("7. Treat Next Patient");
        System.out.println();
        System.out.println("8. Add Treatment Record");
        System.out.println("9. View Treatment History");
        System.out.println("10. Remove Last Treatment");
        System.out.println();
        System.out.println("11. Add Patient Visit");
        System.out.println("12. Search Patient Visit");
        System.out.println("13. Remove Patient Visit");
        System.out.println("14. View Patient Visit History");
        System.out.println();
        System.out.println("0. Exit");
    }

    private void addPatient() {
        int id = readInt("Patient ID: ", 1, Integer.MAX_VALUE);
        if (patients.search(id) != null) {
            System.out.println("A patient with this ID already exists.");
            return;
        }

        String name = readText("Patient Name: ");
        int age = readInt("Age (0-130): ", 0, 130);
        String contact = readText("Contact Number: ");
        String condition = readText("Medical Condition: ");
        patients.insert(new Patient(id, name, age, contact, condition));
        System.out.println("Patient Added Successfully!");
    }

    private void searchPatient() {
        Patient patient = readPatient();
        if (patient != null) {
            System.out.println("Patient Found:");
            patient.displayPatient();
        }
    }

    private void deletePatient() {
        Patient patient = readPatient();
        if (patient == null) {
            return;
        }
        // Do not leave a waiting emergency patient without a registered record.
        if (emergencyQueue.containsPatient(patient.getPatientID())) {
            System.out.println("Patient is waiting in the emergency queue. Treat this patient before deletion.");
            return;
        }
        patients.delete(patient.getPatientID());
        System.out.println("Patient Deleted Successfully! Their visit history was also removed.");
    }

    private void addEmergencyPatient() {
        Patient patient = readPatient();
        if (patient == null) {
            return;
        }
        if (emergencyQueue.containsPatient(patient.getPatientID())) {
            System.out.println("Patient is already in the emergency queue.");
            return;
        }
        emergencyQueue.enqueue(patient);
        System.out.println("Emergency Patient Added Successfully!");
    }

    private void treatNextPatient() {
        Patient patient = emergencyQueue.peek();
        if (patient == null) {
            System.out.println("Emergency queue is empty. No patient to treat.");
            return;
        }

        System.out.println("Next Patient for Treatment:");
        patient.displayPatient();
        String treatment = readText("Treatment: ");
        String date = readDate("Treatment Date (YYYY-MM-DD): ");

        // Read valid details first, then remove the FIFO patient and record treatment.
        emergencyQueue.dequeue();
        treatmentHistory.push(new TreatmentRecord(patient.getPatientID(),
                patient.getPatientName(), treatment, date));
        System.out.println("Patient Treated Successfully! Treatment record added.");
    }

    private void addTreatmentRecord() {
        Patient patient = readPatient();
        if (patient == null) {
            return;
        }
        String treatment = readText("Treatment: ");
        String date = readDate("Treatment Date (YYYY-MM-DD): ");
        treatmentHistory.push(new TreatmentRecord(patient.getPatientID(),
                patient.getPatientName(), treatment, date));
        System.out.println("Treatment Record Added Successfully!");
    }

    private void removeLastTreatment() {
        TreatmentRecord record = treatmentHistory.pop();
        if (record == null) {
            System.out.println("Treatment history is empty. No record to remove.");
            return;
        }
        System.out.println("Removed Latest Treatment:");
        record.displayTreatment();
    }

    private void addPatientVisit() {
        Patient patient = readPatient();
        if (patient == null) {
            return;
        }
        VisitLinkedList visits = patient.getVisitHistory();
        int visitID = readInt("Visit ID: ", 1, Integer.MAX_VALUE);
        if (visits.searchVisit(visitID) != null) {
            System.out.println("This visit ID already exists for this patient.");
            return;
        }
        String date = readDate("Visit Date (YYYY-MM-DD): ");
        String doctor = readText("Doctor Name: ");
        String diagnosis = readText("Diagnosis: ");
        String treatment = readText("Treatment: ");
        visits.addVisit(new Visit(visitID, date, doctor, diagnosis, treatment));
        System.out.println("Patient Visit Added Successfully!");
    }

    private void searchPatientVisit() {
        Patient patient = readPatient();
        if (patient == null) {
            return;
        }
        int visitID = readInt("Visit ID: ", 1, Integer.MAX_VALUE);
        Visit visit = patient.getVisitHistory().searchVisit(visitID);
        if (visit == null) {
            System.out.println("Visit Not Found!");
        } else {
            System.out.println("Visit Found for " + patient.getPatientName() + ":");
            visit.displayVisit();
        }
    }

    private void removePatientVisit() {
        Patient patient = readPatient();
        if (patient == null) {
            return;
        }
        int visitID = readInt("Visit ID: ", 1, Integer.MAX_VALUE);
        if (patient.getVisitHistory().removeVisit(visitID)) {
            System.out.println("Patient Visit Removed Successfully!");
        } else {
            System.out.println("Visit Not Found!");
        }
    }

    private void viewPatientVisitHistory() {
        Patient patient = readPatient();
        if (patient != null) {
            System.out.println("Patient: " + patient.getPatientName()
                    + " (ID: " + patient.getPatientID() + ")");
            patient.getVisitHistory().displayVisits();
        }
    }

    private Patient readPatient() {
        int id = readInt("Patient ID: ", 1, Integer.MAX_VALUE);
        Patient patient = patients.search(id);
        if (patient == null) {
            System.out.println("Patient Not Found!");
        }
        return patient;
    }

    private int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            String text = readText(prompt);
            try {
                int value = Integer.parseInt(text);
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Retry for letters, decimals, or a number outside the int range.
            }
            System.out.println("Please enter a whole number between "
                    + minimum + " and " + maximum + ".");
        }
    }

    private String readDate(String prompt) {
        while (true) {
            String date = readText(prompt);
            try {
                if (date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                    LocalDate.parse(date); // Reject impossible dates, including invalid leap days.
                    return date;
                }
            } catch (DateTimeParseException exception) {
                // Keep prompting until both the format and calendar date are valid.
            }
            System.out.println("Please enter a valid date in YYYY-MM-DD format.");
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new EndOfInputException();
            }
            // Read complete lines everywhere to avoid Scanner's leftover-newline issue.
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("This field cannot be empty.");
        }
    }

    /** Signals the main method to exit cleanly when console input ends. */
    private static class EndOfInputException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
