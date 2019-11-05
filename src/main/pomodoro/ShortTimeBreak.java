package pomodoro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class ShortTimeBreak extends JPanel {

    private Timer timer;
    private long startTime = -1;
    private long duration = 5 * 60 * 1000;
    private JLabel label;

    public ShortTimeBreak() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(100, 300, 100, 300);
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
        label = new JLabel("5:00");
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