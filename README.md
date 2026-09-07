# Mini Hospital Emergency Management System

A beginner-friendly Java console application for a Data Structures and Algorithms assignment. It manages patient records, a FIFO emergency waiting queue, completed treatment records, and each patient's visit history.

All four data structures are implemented manually with node classes and references. The project uses no Collection Framework classes, external libraries, database, or build tool. `Scanner` handles console input; it is not a collection.

## Data structures used

| Structure | Purpose | Operations and time complexity |
| --- | --- | --- |
| Binary search tree | Store patients using patient ID as the key | Insert, search, delete: O(h); inorder display: O(n) |
| Queue | Store emergency patients in arrival order | Enqueue, dequeue, peek: O(1); search/display: O(q) |
| Stack | Store completed treatments, newest added first | Push/pop: O(1); display: O(t) |
| Singly linked list | Keep a separate visit history for each patient | Search, remove, display: O(v); add: O(v) including duplicate-ID check |

Here, h is the tree height; n, q, t, and v are the numbers of patients, queued patients, treatment records, and visits in one patient's list. This is an ordinary, unbalanced BST: a reasonably balanced tree has height O(log n), but a skewed tree can have height O(n). Recursive deletion and traversal use O(h) call-stack space. The linked list has a tail pointer, so attaching a new node after the duplicate check is O(1).

## Features

- Add, search, delete, and display patients in ascending ID order.
- Add registered patients to the emergency queue and treat the first waiting patient.
- Automatically push a treatment record when a queued patient is treated.
- Add a completed treatment manually, view all treatments, and remove the latest record.
- Add, search, remove, and display visits for a selected patient.
- Reject duplicate patient IDs and duplicate visit IDs within one patient's list.
- Reject duplicate emergency entries through the menu.
- Handle empty structures and missing records without crashing.
- Validate integer input, positive IDs, ages from 0 to 130, nonblank fields, and calendar dates in `YYYY-MM-DD` format.
- Preserve contact numbers as strings, including their leading zeros.
- Load the three requested sample patients every time the program starts.

| Patient ID | Name | Age | Contact | Condition |
| --- | --- | --- | --- | --- |
| 101 | Ahmed | 25 | 0771234567 | Fever |
| 102 | Rimas | 22 | 0712345678 | Injury |
| 103 | Mohamed | 35 | 0755555555 | Diabetes |

## Project structure

```text
MiniHospitalEmergencyManagementSystem/
|-- src/
|   |-- models/
|   |   |-- Patient.java
|   |   |-- TreatmentRecord.java
|   |   `-- Visit.java
|   |-- bst/
|   |   |-- PatientNode.java
|   |   `-- PatientBST.java
|   |-- queue/
|   |   |-- QueueNode.java
|   |   `-- EmergencyQueue.java
|   |-- stack/
|   |   |-- StackNode.java
|   |   `-- TreatmentStack.java
|   |-- linkedlist/
|   |   |-- VisitNode.java
|   |   `-- VisitLinkedList.java
|   `-- Main.java
|-- tests/
|   |-- PatientBSTTest.java
|   |-- QueueStackTest.java
|   |-- VisitLinkedListTest.java
|   `-- MainTest.java
|-- .gitignore
|-- DEMO_SCRIPT.md
`-- README.md
```

## How to run

Install a JDK, version 8 or newer, and make sure `java` and `javac` are on your PATH. Open a terminal in `MiniHospitalEmergencyManagementSystem`, the folder containing this README.

Compile and run using PowerShell, Command Prompt, macOS, or Linux:

```text
mkdir out
javac -d out src/Main.java src/models/*.java src/bst/*.java src/queue/*.java src/stack/*.java src/linkedlist/*.java
java -cp out Main
```

If `out` already exists, skip `mkdir out`. Keep the terminal open, type a menu number, and press Enter. Enter each requested value on a new line. Select `0` to exit.

## Testing

Compile the application first, then compile the standalone Java tests:

```text
mkdir test-out
javac -cp out -d test-out tests/*.java
```

If `test-out` already exists, skip its `mkdir` command. On Windows, run:

```text
java -cp "out;test-out" PatientBSTTest
java -cp "out;test-out" QueueStackTest
java -cp "out;test-out" VisitLinkedListTest
java -cp "out;test-out" MainTest
```

On macOS/Linux, replace each `"out;test-out"` with `"out:test-out"`.

The tests cover BST ordering and all deletion cases, duplicate IDs, queue FIFO and stack LIFO behavior, reuse of emptied structures, linked-list head/middle/tail removal, separate patient histories, all menu options, invalid input, and graceful end of input. Tests throw `AssertionError` on failure; `-ea` is not required.

Verified with JDK 23.0.2, including compilation against the Java 8 API using `--release 8`. All four test programs passed. No Java 8 runtime was used for these checks.

## Sample output

At startup:

```text
Sample patients 101, 102, and 103 loaded successfully.

====================================
 Mini Hospital Emergency Management
====================================
1. Add Patient
2. Search Patient
3. Delete Patient
4. Display All Patients

5. Add Emergency Patient
6. Display Emergency Queue
7. Treat Next Patient

8. Add Treatment Record
9. View Treatment History
10. Remove Last Treatment

11. Add Patient Visit
12. Search Patient Visit
13. Remove Patient Visit
14. View Patient Visit History

0. Exit
```

Example excerpts after performing the corresponding operations:

```text
Patient Added Successfully!

Patient Found:
ID: 101
Name: Ahmed
Age: 25
Contact: 0771234567
Condition: Fever

Emergency Queue:
1. Ahmed (ID: 101)
2. Rimas (ID: 102)

Treatment History:
Treatment 1:
Patient ID: 101
Patient Name: Ahmed
Treatment: Observation completed
Date: 2026-09-06

Visit History:
Visit ID: 5001
Visit Date: 2026-09-06
Doctor: Dr. Silva
Diagnosis: Follow-up review
Treatment: Visit completed
```

## Implementation and design decisions

- **Separation of responsibilities:** model classes store information; each data structure manages its own node links; `Main` handles prompts and coordinates operations. Fields are private in models and structures. Node fields are package-private so their structure can update links without exposing them to other packages.
- **Patient visits:** `Patient` owns one additional `VisitLinkedList` field. Selecting a patient before a visit operation keeps histories separate without changing the required fields of `Visit`. Visit IDs are unique within a patient's history; different patients can use the same visit ID.
- **BST deletion:** a leaf is removed directly; a node with one child is replaced by that child; a node with two children receives the smallest patient from its right subtree. Moving the whole patient reference preserves the successor patient's visits.
- **Key setters:** the required ID setters are available, but callers must not change patient or visit IDs while stored. `Main` never changes those keys. Changing a patient ID in place would break BST ordering.
- **Queue consistency:** only registered patients can be queued. A queued patient cannot be deleted through the menu until treated. Queue duplicate checking takes O(q), although the underlying enqueue itself takes O(1).
- **Completed treatments:** option 7 reads valid details, dequeues a patient, then pushes a treatment. Option 8 records a separate completed treatment without changing the queue. A stack pop removes a history record; it does not undo treatment or return anyone to the queue. Records are ordered by insertion, not by their entered dates.
- **Visit records:** adding a visit stores its treatment text inside that visit; it does not also push a completed-treatment record. Those are separate menu operations.
- **Deletion behavior:** deleting a patient removes access to that patient's visit history. Existing treatment records retain the patient's ID and name as historical snapshots.
- **Input:** all input uses `Scanner.nextLine()` to avoid leftover newline issues. Numeric parsing and date validation retry invalid entries. When input ends, the application exits cleanly. Add/treat operations collect all required values before changing the structures.
- **Assignment scope:** all information lives in memory and resets after exit. Emergency order is FIFO as required by the assignment; no severity-priority algorithm or persistent storage is implemented.

## GitHub development milestones

The following are the requested milestones for a development history. They are a plan, not a claim that these commits already exist. This delivered folder contains the completed implementation; a GitHub repository has not been created or uploaded for you. Record commits that accurately describe your own work and show your actual repository history during the video.

| Milestone | Suggested commit message |
| --- | --- |
| 1 | Created project structure |
| 2 | Added Patient model and BST implementation |
| 3 | Added BST search and delete |
| 4 | Implemented Emergency Queue |
| 5 | Implemented Treatment Stack |
| 6 | Implemented Patient Visit Linked List |
| 7 | Added Main menu and testing |
| 8 | Added README documentation |

Initialize Git inside this project folder when preparing your submission, especially if a parent folder already belongs to another repository. The `.gitignore` excludes compiled output and local IDE files. Commit the Java sources, tests, README, and demo script. Follow your course's rules for acknowledging assistance.

## Video demonstration

Use [DEMO_SCRIPT.md](DEMO_SCRIPT.md) for an approximately 8-9 minute narration, exact menu inputs, and screen actions covering all eight video requirements. Replace the personal and repository placeholders and rehearse once before recording.
