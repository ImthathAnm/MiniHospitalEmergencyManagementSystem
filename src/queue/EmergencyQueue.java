package queue;

import models.Patient;

/** FIFO queue: patients enter at the rear and leave from the front. */
public class EmergencyQueue {
    private QueueNode front;
    private QueueNode rear;

    public boolean isEmpty() {
        return front == null;
    }

    public void enqueue(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        QueueNode newNode = new QueueNode(patient);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        rear = newNode;
    }

    public Patient dequeue() {
        if (isEmpty()) {
            return null;
        }
        Patient patient = front.patient;
        front = front.next;
        if (front == null) {
            rear = null; // An empty queue must not keep an old rear pointer.
        }
        return patient;
    }

    /** Read the next patient without removing them. */
    public Patient peek() {
        return isEmpty() ? null : front.patient;
    }

    public boolean containsPatient(int patientID) {
        QueueNode current = front;
        while (current != null) {
            if (current.patient.getPatientID() == patientID) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void displayQueue() {
        System.out.println("Emergency Queue:");
        if (isEmpty()) {
            System.out.println("No emergency patients are waiting.");
            return;
        }
        QueueNode current = front;
        int position = 1;
        while (current != null) {
            System.out.println(position + ". " + current.patient.getPatientName()
                    + " (ID: " + current.patient.getPatientID() + ")");
            current = current.next;
            position++;
        }
    }
}
