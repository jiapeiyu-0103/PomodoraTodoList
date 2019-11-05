package pomodoro;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.*;

public class CountDownTimer {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private JTabbedPane pane;


    public static void main(String[] args) {
        new CountDownTimer();
    }

    public CountDownTimer() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }


                JFrame frame = new JFrame("POMODORO");
                frame.setSize(WIDTH,HEIGHT);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pane = new JTabbedPane();
                pane.setTabPlacement(JTabbedPane.TOP);
                pane.addTab("Pomodoro", new TestPane());
                pane.addTab("ShortBreak", new ShortTimeBreak());
                pane.addTab("LongBreak", new LongTimeBreak());
                frame.add(pane);
                frame.setVisible(true);
                frame.pack();
                frame.setLocationRelativeTo(null);

            }
        });
    }

    public class TestPane extends JPanel {

        private Timer timer;
        private long startTime = -1;
        private long duration = 25 * 60 * 1000;
        private JLabel label;

        public TestPane() {

            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(50, 300, 100, 300);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long clockTime = now - startTime;
                    if (clockTime >= duration) {
                        clockTime = duration;
                        timer.stop();
                    }
                    SimpleDateFormat df = new SimpleDateFormat("mm:ss");
                    label.setText(df.format(duration - clockTime));
                }
            });
            label = new JLabel("25:00");
            add(label, gbc);
            timer.setInitialDelay(0);
            JButton startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!timer.isRunning()) {
                        startTime = -1;
                        timer.start();
                        startButton.setText("Start");
                    }
                }
            });
            add(startButton, gbc);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 300);
        }

    }
}