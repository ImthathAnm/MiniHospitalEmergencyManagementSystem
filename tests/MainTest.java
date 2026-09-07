import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Runs complete console sessions using scripted input, with no external libraries. */
public class MainTest {
    private static int checks;

    public static void main(String[] args) throws Exception {
        testPatientMenu();
        testQueueAndTreatmentMenu();
        testVisitMenu();
        testInputValidationAndEndOfInput();
        System.out.println("MainTest passed (" + checks + " checks).");
    }

    private static void testPatientMenu() throws Exception {
        String output = run("4\n1\n104\nSara\n28\n0770000000\nHeadache\n"
                + "1\n104\n2\n104\n3\n104\n2\n104\n3\n999\n0\n");
        contains(output, "Sample patients 101, 102, and 103 loaded successfully.");
        int first = output.indexOf("ID: 101");
        int second = output.indexOf("ID: 102");
        int third = output.indexOf("ID: 103");
        check(first >= 0 && first < second && second < third, "Sample patients must display sorted.");
        contains(output, "Patient Added Successfully!");
        contains(output, "A patient with this ID already exists.");
        contains(output, "Patient Found:\nID: 104\nName: Sara");
        contains(output, "Contact: 0770000000");
        contains(output, "Patient Deleted Successfully!");
        check(count(output, "Patient Not Found!") == 2, "Deleted and unknown patients must be absent.");
        contains(output, "Thank you for using the system. Goodbye!");
    }

    private static void testQueueAndTreatmentMenu() throws Exception {
        String output = run("6\n7\n9\n10\n5\n999\n5\n101\n5\n101\n5\n102\n"
                + "3\n101\n6\n7\nFirst treatment\n2026-09-06\n6\n"
                + "7\nSecond treatment\n2026-09-06\n9\n10\n"
                + "8\n103\nManual treatment\n2026-09-06\n9\n10\n10\n10\n"
                + "5\n103\n6\n0\n");
        contains(output, "No emergency patients are waiting.");
        contains(output, "Emergency queue is empty. No patient to treat.");
        contains(output, "No treatment records found.");
        contains(output, "Treatment history is empty. No record to remove.");
        contains(output, "Patient Not Found!");
        contains(output, "Patient is already in the emergency queue.");
        contains(output, "Treat this patient before deletion.");
        contains(output, "1. Ahmed (ID: 101)\n2. Rimas (ID: 102)");
        contains(output, "Emergency Queue:\n1. Rimas (ID: 102)");
        check(count(output, "Patient Treated Successfully!") == 2, "Both queued patients must be treated.");
        int historyStart = output.indexOf("Treatment History:\nTreatment 1:");
        String history = output.substring(historyStart);
        check(history.indexOf("Second treatment") < history.indexOf("First treatment"),
                "The latest completed treatment must display first.");
        contains(output, "Removed Latest Treatment:\nPatient ID: 102");
        contains(output, "Treatment Record Added Successfully!");
        contains(output, "Removed Latest Treatment:\nPatient ID: 103");
        contains(output, "Emergency Queue:\n1. Mohamed (ID: 103)");
    }

    private static void testVisitMenu() throws Exception {
        String output = run("14\n101\n12\n101\n1\n13\n101\n1\n"
                + "11\n101\n1\n2026-09-06\nDr. Silva\nFever review\nObservation\n"
                + "11\n101\n1\n12\n101\n1\n14\n101\n14\n102\n"
                + "11\n102\n1\n2026-09-06\nDr. Perera\nInjury review\nFollow-up\n"
                + "13\n101\n1\n12\n101\n1\n12\n102\n1\n0\n");
        contains(output, "No visits found.");
        check(count(output, "Patient Visit Added Successfully!") == 2, "Each patient can use visit ID 1.");
        contains(output, "This visit ID already exists for this patient.");
        contains(output, "Visit Found for Ahmed:");
        contains(output, "Doctor: Dr. Silva");
        contains(output, "Patient: Rimas (ID: 102)\nVisit History:\nNo visits found.");
        contains(output, "Patient Visit Removed Successfully!");
        check(count(output, "Visit Not Found!") == 3, "Missing and removed visits must be reported.");
        contains(output, "Visit Found for Rimas:");
        contains(output, "Doctor: Dr. Perera");
    }

    private static void testInputValidationAndEndOfInput() throws Exception {
        String output = run("hello\n15\n999999999999999999999\n1\n-1\n104\n\nSara\n"
                + "131\n-2\n28\n0770000000\nHeadache\n"
                + "8\n104\n\nObservation\n2026-02-30\n06/09/2026\n2026-09-06\n0\n");
        contains(output, "Please enter a whole number between 0 and 14.");
        contains(output, "Please enter a whole number between 1 and 2147483647.");
        contains(output, "Please enter a whole number between 0 and 130.");
        contains(output, "This field cannot be empty.");
        check(count(output, "Please enter a valid date in YYYY-MM-DD format.") == 2,
                "Impossible dates and the wrong format must be rejected.");
        contains(output, "Treatment Record Added Successfully!");
        contains(run(""), "Input closed. Goodbye!");
        String interrupted = run("5\n101\n7\nObservation\n");
        contains(interrupted, "Input closed. Goodbye!");
        check(!interrupted.contains("Patient Treated Successfully!"),
                "An incomplete treatment must not report success.");
        String partialPatient = run("1\n104\nSara\n");
        check(!partialPatient.contains("Patient Added Successfully!"),
                "An incomplete patient must not report success.");
    }

    private static String run(String input) throws Exception {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream captured = new PrintStream(buffer, true, "UTF-8");
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(captured);
            Main.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
            captured.close();
        }
        return buffer.toString("UTF-8").replace("\r\n", "\n");
    }

    private static int count(String text, String part) {
        int count = 0;
        int position = 0;
        while ((position = text.indexOf(part, position)) >= 0) {
            count++;
            position += part.length();
        }
        return count;
    }

    private static void contains(String text, String expected) {
        check(text.contains(expected), "Expected console output: " + expected);
    }

    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
