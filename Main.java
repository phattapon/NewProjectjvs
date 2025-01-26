import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    // Enum สำหรับ Finite State Machine
    enum FSMState {
        CLOCK, CALCULATOR, SIMULATOR, WORKOUT
    }

    // เปลี่ยน currentState เป็น instance variable ของคลาส Main
    private FSMState currentState = FSMState.CLOCK;

    // การจัดการเครื่องคิดเลข
    static class Calculator {
        private StringBuilder display;

        public Calculator() {
            display = new StringBuilder();
        }

        public String getDisplay() {
            return display.toString();
        }

        public void pressKey(String key) {
            if (key.equals("=")) {
                try {
                    // คำนวณผลลัพธ์จากการแสดงผล
                    double result = evaluate(display.toString());
                    display.setLength(0);  // Clear the display
                    display.append(result);
                } catch (Exception e) {
                    display.setLength(0);
                    display.append("Error");
                }
            } else if (key.equals("C")) {
                display.setLength(0);  // Clear display
            } else {
                display.append(key);  // เพิ่มตัวเลขหรือสัญลักษณ์
            }
        }

        private double evaluate(String expression) {
            // การประเมินสูตรที่ง่าย
            return new Object() {
                int pos = -1;
                char ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : (char) -1;
                }

                boolean eat(char target) {
                    if (ch == target) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (;;) {
                        if (eat('+')) {
                            x += parseTerm();
                        } else if (eat('-')) {
                            x -= parseTerm();
                        } else {
                            return x;
                        }
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (;;) {
                        if (eat('*')) {
                            x *= parseFactor();
                        } else if (eat('/')) {
                            x /= parseFactor();
                        } else {
                            return x;
                        }
                    }
                }

                double parseFactor() {
                    if (eat('+')) {
                        return parseFactor();
                    }
                    if (eat('-')) {
                        return -parseFactor();
                    }

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) {
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                        while ((ch >= '0' && ch <= '9') || ch == '.') {
                            nextChar();
                        }
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }

                    return x;
                }
            }.parse();
        }
    }

    // การแสดงผลเวลาปัจจุบัน
    static class Clock {
        private JLabel clockLabel;
        private Timer timer;

        public Clock(JLabel clockLabel) {
            this.clockLabel = clockLabel;
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateClock();
                }
            });
            timer.start();
        }

        private void updateClock() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
            Date now = new Date();
            clockLabel.setText(dateFormat.format(now));
        }
    }

    // ฟีเจอร์การออกกำลังกาย
    static class Workout {
        private double weight;

        public Workout(double weight) {
            this.weight = weight;
        }

        public double calculateCaloriesBurned(double duration, String workoutType) {
            double MET = workoutType.equals("Running") ? 9.8 : 3.8; // ค่า MET สำหรับวิ่งหรือเดิน
            return 0.0175 * MET * weight * duration;
        }
    }

    // Main App
    public static void main(String[] args) {
        JFrame frame = new JFrame("Multi-Feature App");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // สร้าง instance ของ Main
        Main app = new Main();

        // Clock Panel
        JLabel clockLabel = new JLabel("", SwingConstants.CENTER);
        clockLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        frame.add(clockLabel, BorderLayout.NORTH);

        // Calculator Panel
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new GridLayout(4, 4));
        Calculator calculator = new Calculator();
        JTextField calcDisplay = new JTextField(calculator.getDisplay());
        calcDisplay.setEditable(false);
        frame.add(calcDisplay, BorderLayout.CENTER);

        String[] buttons = {"7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"};

        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    calculator.pressKey(button.getText());
                    calcDisplay.setText(calculator.getDisplay());
                }
            });
            calculatorPanel.add(button);
        }

        JButton clearButton = new JButton("C");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculator.pressKey("C");
                calcDisplay.setText(calculator.getDisplay());
            }
        });
        calculatorPanel.add(clearButton);
        frame.add(calculatorPanel, BorderLayout.SOUTH);

        // Simulator Panel (Optional simple example)
        JPanel simulatorPanel = new JPanel();
        simulatorPanel.add(new JLabel("Simulator running..."));
        simulatorPanel.setVisible(false);
        frame.add(simulatorPanel, BorderLayout.CENTER);

        // Workout Panel
        JPanel workoutPanel = new JPanel();
        workoutPanel.setLayout(new BorderLayout());
        JTextArea workoutResult = new JTextArea(5, 20);
        workoutResult.setEditable(false);
        workoutPanel.add(new JScrollPane(workoutResult), BorderLayout.CENTER);

        JTextField weightInput = new JTextField(5);
        workoutPanel.add(new JLabel("Enter weight (kg):"), BorderLayout.NORTH);
        workoutPanel.add(weightInput, BorderLayout.SOUTH);
        workoutPanel.setVisible(false);

        // Button Panel for FSM
        JPanel buttonPanel = new JPanel();
        JButton clockButton = new JButton("Clock");
        clockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.currentState = FSMState.CLOCK;
                clockLabel.setText("");
                simulatorPanel.setVisible(false);
                calculatorPanel.setVisible(false);
                workoutPanel.setVisible(false);
            }
        });

        JButton calculatorButton = new JButton("Calculator");
        calculatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.currentState = FSMState.CALCULATOR;
                calculatorPanel.setVisible(true);
                simulatorPanel.setVisible(false);
                clockLabel.setText("");
                workoutPanel.setVisible(false);
            }
        });

        JButton simulatorButton = new JButton("Simulator");
        simulatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.currentState = FSMState.SIMULATOR;
                calculatorPanel.setVisible(false);
                clockLabel.setText("");
                simulatorPanel.setVisible(true);
                workoutPanel.setVisible(false);
            }
        });

        JButton workoutButton = new JButton("Workout");
        workoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.currentState = FSMState.WORKOUT;
                workoutPanel.setVisible(true);
                simulatorPanel.setVisible(false);
                calculatorPanel.setVisible(false);
                clockLabel.setText("");
            }
        });

        buttonPanel.add(clockButton);
        buttonPanel.add(calculatorButton);
        buttonPanel.add(simulatorButton);
        buttonPanel.add(workoutButton);
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Initialize Clock
        new Clock(clockLabel);

        // Workout Calculation
        workoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String weightStr = weightInput.getText();
                if (!weightStr.isEmpty()) {
                    double weight = Double.parseDouble(weightStr);
                    Workout workout = new Workout(weight);
                    double caloriesBurned = workout.calculateCaloriesBurned(30, "Running");  // สมมุติว่าเลือก Running
                    workoutResult.setText("Calories burned (30 mins): " + caloriesBurned + " kcal");
                }
            }
        });

        // Set default state
        clockButton.doClick();

        // Final frame setup
        frame.setVisible(true);
    }
}
