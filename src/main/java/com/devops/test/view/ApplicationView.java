package com.devops.test.view;

import com.devops.test.services.BD_Services;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ApplicationView {
    private final BD_Services bdServices = new BD_Services();

    private void centerDialog(JDialog dialog) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - dialog.getWidth()) / 2;
        int y = (screenSize.height - dialog.getHeight()) / 2;
        dialog.setLocation(x, y);
    }
    private void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    private void showAddStudentDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Student", true);
        dialog.setLayout(new GridLayout(4, 2));

        JTextField txtMatricule = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtAge = new JTextField();
        JButton btnSave = new JButton("Save");

        dialog.add(new JLabel("Matricule:"));
        dialog.add(txtMatricule);
        dialog.add(new JLabel("Name:"));
        dialog.add(txtName);
        dialog.add(new JLabel("Age:"));
        dialog.add(txtAge);
        dialog.add(new JLabel());
        dialog.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                this.bdServices.storeStudent(txtMatricule.getText(), txtName.getText(), Integer.parseInt(txtAge.getText()));
                JOptionPane.showMessageDialog(dialog, "Student added successfully!");
                dialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Failed to add student: " + ex.getMessage());
            }
        });

        dialog.setSize(300, 200);
        centerDialog(dialog);
        dialog.setVisible(true);
    }

    private void showAddLectureDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Lecture", true);
        dialog.setLayout(new GridLayout(4, 2));

        JTextField txtCode = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtCoefficient = new JTextField();
        JButton btnSave = new JButton("Save");

        dialog.add(new JLabel("Code:"));
        dialog.add(txtCode);
        dialog.add(new JLabel("Name:"));
        dialog.add(txtName);
        dialog.add(new JLabel("Coefficient:"));
        dialog.add(txtCoefficient);
        dialog.add(new JLabel());
        dialog.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                this.bdServices.storeLecture(txtCode.getText(), txtName.getText(), Double.parseDouble(txtCoefficient.getText()));
                JOptionPane.showMessageDialog(dialog, "Lecture added successfully!");
                dialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Failed to add lecture: " + ex.getMessage());
            }
        });

        dialog.setSize(300, 200);
        centerDialog(dialog);
        dialog.setVisible(true);
    }

    private void showAddNoteDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Note", true);
        dialog.setLayout(new GridLayout(4, 2));

        JComboBox<String> comboStudent = new JComboBox<>();
        JComboBox<String> comboLecture = new JComboBox<>();
        JTextField txtValue = new JTextField();
        JButton btnSave = new JButton("Save");

        try {
            for (String student : this.bdServices.getStudents()) {
                System.out.println(student);
                comboStudent.addItem(student);
            }
            for (String lecture : this.bdServices.getLectures()) comboLecture.addItem(lecture);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        dialog.add(new JLabel("Student:"));
        dialog.add(comboStudent);
        dialog.add(new JLabel("Lecture:"));
        dialog.add(comboLecture);
        dialog.add(new JLabel("Value:"));
        dialog.add(txtValue);
        dialog.add(new JLabel());
        dialog.add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                String matricule = comboStudent.getSelectedItem().toString().split(" - ")[0];
                String code = comboLecture.getSelectedItem().toString().split(" - ")[0];

                this.bdServices.addNote(matricule, code, Double.parseDouble(txtValue.getText()));
                JOptionPane.showMessageDialog(dialog, "Note added successfully!");
                dialog.dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Failed to add note: " + ex.getMessage());
            }
        });

        dialog.setSize(300, 200);
        centerDialog(dialog);
        dialog.setVisible(true);
    }

    private void displayResults(String title, List<Object[]> data) {
        String[] columns = {"Matricule", "Name", "Score"};
        Object[][] tableData = data.toArray(new Object[0][]);

        JTable table = new JTable(tableData, columns);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setSize(500, 300);
        dialog.add(scrollPane);
        centerDialog(dialog);
        dialog.setVisible(true);
    }

    private void displayClassAverage() {
        try {
            double average = this.bdServices.getClassAverage();
            JOptionPane.showMessageDialog(null, "Class Average: " + average);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving class average: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void startMenu(JFrame parent) {
        JDialog menuDialog = new JDialog(parent, "View Notes Menu", false);
        menuDialog.setSize(400, 300);
        menuDialog.setLayout(new GridLayout(4, 1));

        JButton btnOrderedScores = new JButton("Ordered Scores");
        JButton btnFirstAndLast = new JButton("First and Last");
        JButton btnClassAverage = new JButton("Class Average");
        JButton btnAdmittedStudents = new JButton("Admitted Students");

        btnOrderedScores.addActionListener(e -> {
            try {
                displayResults("Ordered Scores", this.bdServices.getOrderedScores());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnFirstAndLast.addActionListener(e -> {
            try {
                displayResults("First and Last Students", this.bdServices.getFirstAndLastStudent());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnClassAverage.addActionListener(e -> {
            displayClassAverage();
        });

        btnAdmittedStudents.addActionListener(e -> {
            try {
                displayResults("Admitted Students", this.bdServices.getAdmittedStudents());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        menuDialog.add(btnOrderedScores);
        menuDialog.add(btnFirstAndLast);
        menuDialog.add(btnClassAverage);
        menuDialog.add(btnAdmittedStudents);

        centerDialog(menuDialog);
        menuDialog.setVisible(true);
    }

    public void startApplication() {
        JFrame mainFrame = new JFrame("Notes Management");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton btnAddStudent = new JButton("Add Student");
        JButton btnAddLecture = new JButton("Add Lecture");
        JButton btnAddNote = new JButton("Add Note");
        JButton btnViewNotes = new JButton("View Notes");

        buttonPanel.add(btnAddStudent);
        buttonPanel.add(btnAddLecture);
        buttonPanel.add(btnAddNote);
        buttonPanel.add(btnViewNotes);

        mainFrame.add(buttonPanel, BorderLayout.SOUTH);

        btnAddStudent.addActionListener(e -> showAddStudentDialog(mainFrame));
        btnAddLecture.addActionListener(e -> showAddLectureDialog(mainFrame));
        btnAddNote.addActionListener(e -> showAddNoteDialog(mainFrame));
        btnViewNotes.addActionListener(e -> startMenu(mainFrame));

        mainFrame.setVisible(true);
        centerWindow(mainFrame);
    }


    public void bootstrapApp(){
        SwingUtilities.invokeLater(() -> {
            ApplicationView interfaceGraphique = new ApplicationView();
            interfaceGraphique.startApplication();
        });
    }

}
