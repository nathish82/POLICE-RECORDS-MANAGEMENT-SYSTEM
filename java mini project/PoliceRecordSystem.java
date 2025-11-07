import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PoliceRecordSystem {
    public static void main(String[] args) {
        new SignupPage();
    }
}

// SIGNUP PAGE
class SignupPage extends Frame implements ActionListener {
    TextField nameField, userField, passField;
    Button signupBtn, loginBtn;

    SignupPage() {
        setTitle("Police Records Management - Signup");
        setSize(400, 300);
        setLayout(null);
        setBackground(Color.lightGray);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); System.exit(0); }
        });

        Label title = new Label("Signup Form", Label.CENTER);
        title.setBounds(120, 40, 150, 30);
        add(title);

        Label nameLbl = new Label("Name:");
        nameLbl.setBounds(50, 90, 100, 25);
        add(nameLbl);
        nameField = new TextField();
        nameField.setBounds(160, 90, 180, 25);
        add(nameField);

        Label userLbl = new Label("Username:");
        userLbl.setBounds(50, 130, 100, 25);
        add(userLbl);
        userField = new TextField();
        userField.setBounds(160, 130, 180, 25);
        add(userField);

        Label passLbl = new Label("Password:");
        passLbl.setBounds(50, 170, 100, 25);
        add(passLbl);
        passField = new TextField();
        passField.setEchoChar('*');
        passField.setBounds(160, 170, 180, 25);
        add(passField);

        signupBtn = new Button("Signup");
        signupBtn.setBounds(160, 210, 80, 30);
        signupBtn.addActionListener(this);
        add(signupBtn);

        loginBtn = new Button("Go to Login");
        loginBtn.setBounds(250, 210, 90, 30);
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        add(loginBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String user = userField.getText();
        String pass = passField.getText();

        if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            showDialog("Error", "Please fill all fields before signing up.");
            return;
        }

        try {
            FileWriter fw = new FileWriter("userdata.txt", true);
            fw.write(user + "," + pass + "," + name + "\n");
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Dialog d = new Dialog(this, "Success", true);
        d.setLayout(new FlowLayout());
        d.add(new Label("Signup Successful! Click OK to go to Login."));
        Button okBtn = new Button("OK");
        okBtn.addActionListener(ev -> {
            d.dispose();
            dispose();
            new LoginPage();
        });
        d.add(okBtn);
        d.setSize(300, 120);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void showDialog(String title, String message) {
        Dialog d = new Dialog(this, title, true);
        d.setLayout(new FlowLayout());
        d.add(new Label(message));
        Button ok = new Button("OK");
        ok.addActionListener(ev -> d.dispose());
        d.add(ok);
        d.setSize(300, 120);
        d.setVisible(true);
    }
}

// LOGIN PAGE
class LoginPage extends Frame implements ActionListener {
    TextField userField, passField;
    Button loginBtn;

    LoginPage() {
        setTitle("Police Records Management - Login");
        setSize(400, 250);
        setLayout(null);
        setBackground(Color.cyan);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); System.exit(0); }
        });

        Label title = new Label("Login Form", Label.CENTER);
        title.setBounds(140, 40, 120, 30);
        add(title);

        Label userLbl = new Label("Username:");
        userLbl.setBounds(50, 100, 100, 25);
        add(userLbl);
        userField = new TextField();
        userField.setBounds(160, 100, 170, 25);
        add(userField);

        Label passLbl = new Label("Password:");
        passLbl.setBounds(50, 140, 100, 25);
        add(passLbl);
        passField = new TextField();
        passField.setEchoChar('*');
        passField.setBounds(160, 140, 170, 25);
        add(passField);

        loginBtn = new Button("Login");
        loginBtn.setBounds(160, 180, 80, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String user = userField.getText();
        String pass = passField.getText();

        try {
            BufferedReader br = new BufferedReader(new FileReader("userdata.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (user.equals(data[0]) && pass.equals(data[1])) {
                    br.close();
                    dispose();
                    new Dashboard(data[2]);
                    return;
                }
            }
            br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Dialog d = new Dialog(this, "Error", true);
        d.setLayout(new FlowLayout());
        d.add(new Label("Invalid Credentials! Try Again."));
        Button ok = new Button("OK");
        ok.addActionListener(ev -> d.dispose());
        d.add(ok);
        d.setSize(250, 120);
        d.setVisible(true);
    }
}

// DASHBOARD PAGE
class Dashboard extends Frame implements ActionListener {
    Button firBtn, criminalBtn, viewBtn, logoutBtn;
    String officerName;

    Dashboard(String name) {
        officerName = name;
        setTitle("Police Records Dashboard");
        setSize(500, 400);
        setBackground(Color.white);
        setLayout(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        Label welcome = new Label("Welcome Officer " + name + "!", Label.CENTER);
        welcome.setBounds(100, 60, 300, 30);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcome);

        firBtn = new Button("Add New FIR");
        firBtn.setBounds(150, 120, 200, 30);
        firBtn.addActionListener(this);
        add(firBtn);

        criminalBtn = new Button("Add Criminal Record");
        criminalBtn.setBounds(150, 170, 200, 30);
        criminalBtn.addActionListener(this);
        add(criminalBtn);

        viewBtn = new Button("View All Records");
        viewBtn.setBounds(150, 220, 200, 30);
        viewBtn.addActionListener(this);
        add(viewBtn);

        logoutBtn = new Button("Logout");
        logoutBtn.setBounds(150, 270, 200, 30);
        logoutBtn.addActionListener(this);
        add(logoutBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == firBtn) new FIRPage();
        else if (e.getSource() == criminalBtn) new CriminalPage();
        else if (e.getSource() == viewBtn) new RecordViewer();
        else if (e.getSource() == logoutBtn) {
            dispose();
            new LoginPage();
        }
    }
}

// FIR ENTRY PAGE
class FIRPage extends Frame implements ActionListener {
    TextField caseIdField, nameField, crimeField, dateField, statusField;
    Button saveBtn;

    FIRPage() {
        setTitle("File New FIR");
        setSize(400, 350);
        setLayout(null);
        setBackground(Color.lightGray);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Label title = new Label("FIR Entry Form", Label.CENTER);
        title.setBounds(100, 40, 200, 30);
        add(title);

        Label idLbl = new Label("Case ID:");
        idLbl.setBounds(50, 90, 100, 25);
        add(idLbl);
        caseIdField = new TextField();
        caseIdField.setBounds(160, 90, 170, 25);
        add(caseIdField);

        Label nameLbl = new Label("Complainant:");
        nameLbl.setBounds(50, 130, 100, 25);
        add(nameLbl);
        nameField = new TextField();
        nameField.setBounds(160, 130, 170, 25);
        add(nameField);

        Label crimeLbl = new Label("Crime Type:");
        crimeLbl.setBounds(50, 170, 100, 25);
        add(crimeLbl);
        crimeField = new TextField();
        crimeField.setBounds(160, 170, 170, 25);
        add(crimeField);

        Label dateLbl = new Label("Date:");
        dateLbl.setBounds(50, 210, 100, 25);
        add(dateLbl);
        dateField = new TextField();
        dateField.setBounds(160, 210, 170, 25);
        add(dateField);

        Label statusLbl = new Label("Status:");
        statusLbl.setBounds(50, 250, 100, 25);
        add(statusLbl);
        statusField = new TextField();
        statusField.setBounds(160, 250, 170, 25);
        add(statusField);

        saveBtn = new Button("Save");
        saveBtn.setBounds(160, 290, 80, 30);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            FileWriter fw = new FileWriter("firdata.txt", true);
            fw.write(caseIdField.getText() + "," + nameField.getText() + "," +
                     crimeField.getText() + "," + dateField.getText() + "," +
                     statusField.getText() + "\n");
            fw.close();
            showDialog("Success", "FIR saved successfully.");
            caseIdField.setText(""); nameField.setText("");
            crimeField.setText(""); dateField.setText(""); statusField.setText("");
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void showDialog(String t, String m) {
        Dialog d = new Dialog(this, t, true);
        d.setLayout(new FlowLayout());
        d.add(new Label(m));
        Button ok = new Button("OK");
        ok.addActionListener(ev -> d.dispose());
        d.add(ok);
        d.setSize(250, 120);
        d.setVisible(true);
    }
}

// CRIMINAL RECORD ENTRY PAGE
class CriminalPage extends Frame implements ActionListener {
    TextField nameField, crimeField, arrestDateField, caseIdField;
    Button saveBtn;

    CriminalPage() {
        setTitle("Add Criminal Record");
        setSize(400, 300);
        setLayout(null);
        setBackground(Color.orange);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        Label title = new Label("Criminal Record Entry", Label.CENTER);
        title.setBounds(100, 40, 200, 30);
        add(title);

        Label nameLbl = new Label("Name:");
        nameLbl.setBounds(50, 90, 100, 25);
        add(nameLbl);
        nameField = new TextField();
        nameField.setBounds(160, 90, 170, 25);
        add(nameField);

        Label crimeLbl = new Label("Crime:");
        crimeLbl.setBounds(50, 130, 100, 25);
        add(crimeLbl);
        crimeField = new TextField();
        crimeField.setBounds(160, 130, 170, 25);
        add(crimeField);

        Label dateLbl = new Label("Arrest Date:");
        dateLbl.setBounds(50, 170, 100, 25);
        add(dateLbl);
        arrestDateField = new TextField();
        arrestDateField.setBounds(160, 170, 170, 25);
        add(arrestDateField);

        Label caseLbl = new Label("Case ID:");
        caseLbl.setBounds(50, 210, 100, 25);
        add(caseLbl);
        caseIdField = new TextField();
        caseIdField.setBounds(160, 210, 170, 25);
        add(caseIdField);

        saveBtn = new Button("Save");
        saveBtn.setBounds(160, 250, 80, 30);
        saveBtn.addActionListener(this);
        add(saveBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            FileWriter fw = new FileWriter("criminaldata.txt", true);
            fw.write(nameField.getText() + "," + crimeField.getText() + "," +
                     arrestDateField.getText() + "," + caseIdField.getText() + "\n");
            fw.close();
            showDialog("Success", "Criminal record saved.");
            nameField.setText(""); crimeField.setText("");
            arrestDateField.setText(""); caseIdField.setText("");
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void showDialog(String t, String m) {
        Dialog d = new Dialog(this, t, true);
        d.setLayout(new FlowLayout());
        d.add(new Label(m));
        Button ok = new Button("OK");
        ok.addActionListener(ev -> d.dispose());
        d.add(ok);
        d.setSize(250, 120);
        d.setVisible(true);
    }
}

// VIEW RECORDS PAGE
class RecordViewer extends Frame {
    TextArea displayArea;

    RecordViewer() {
        setTitle("All Records Viewer");
        setSize(600, 400);
        setLayout(new BorderLayout());
        setBackground(Color.white);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { dispose(); }
        });

        displayArea = new TextArea();
        add(displayArea, BorderLayout.CENTER);

        StringBuilder sb = new StringBuilder();
        sb.append("==== FIR RECORDS ====\n");
        readFile("firdata.txt", sb);
        sb.append("\n==== CRIMINAL RECORDS ====\n");
        readFile("criminaldata.txt", sb);

        displayArea.setText(sb.toString());
        setVisible(true);
    }

    private void readFile(String filename, StringBuilder sb) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            br.close();
        } catch (Exception e) {
            sb.append("(No data found in ").append(filename).append(")\n");
        }
    }
}
