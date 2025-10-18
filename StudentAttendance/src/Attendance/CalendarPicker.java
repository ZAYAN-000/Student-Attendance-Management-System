package Attendance;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class CalendarPicker extends JFrame {
    private JTextField targetField;
    private Calendar calendar = Calendar.getInstance();
    private JLabel monthYearLabel;
    private JPanel daysPanel;

    public CalendarPicker(JTextField field) {
        this.targetField = field;
        setTitle("Select Date");
        setSize(350, 400);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setLocationRelativeTo(null);

        // Top panel with month/year and navigation
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.decode("#37474F"));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton prevBtn = new JButton("<");
        prevBtn.setBackground(Color.decode("#DEE4E7"));
        prevBtn.setForeground(Color.decode("#37474F"));
        prevBtn.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        monthYearLabel = new JLabel();
        monthYearLabel.setForeground(Color.WHITE);
        monthYearLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton nextBtn = new JButton(">");
        nextBtn.setBackground(Color.decode("#DEE4E7"));
        nextBtn.setForeground(Color.decode("#37474F"));
        nextBtn.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        topPanel.add(prevBtn);
        topPanel.add(monthYearLabel);
        topPanel.add(nextBtn);

        // Days header
        JPanel headerPanel = new JPanel(new GridLayout(1, 7));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            label.setPreferredSize(new Dimension(50, 20));
            headerPanel.add(label);
        }

        // Days panel
        daysPanel = new JPanel(new GridLayout(6, 7));
        daysPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        add(topPanel);
        add(headerPanel);
        add(daysPanel);

        updateCalendar();
    }

    private void updateCalendar() {
        daysPanel.removeAll();

        String[] months = {"January", "February", "March", "April", "May", "June",
                           "July", "August", "September", "October", "November", "December"};
        monthYearLabel.setText(months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));

        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = temp.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get previous month's last days
        Calendar prevMonth = (Calendar) temp.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Fill empty cells with previous month's days
        for (int i = 0; i < firstDayOfWeek; i++) {
            JLabel prevLabel = new JLabel(String.valueOf(daysInPrevMonth - firstDayOfWeek + i + 1), SwingConstants.CENTER);
            prevLabel.setForeground(Color.GRAY);
            prevLabel.setPreferredSize(new Dimension(50, 30));
            daysPanel.add(prevLabel);
        }

        // Current month days
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setBackground(Color.WHITE);
            dayBtn.setForeground(Color.BLACK);
            dayBtn.setFocusPainted(false);
            dayBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            dayBtn.setPreferredSize(new Dimension(50, 30));
            final int selectedDay = day;

            dayBtn.addActionListener(e -> {
                calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                String date = String.format("%04d-%02d-%02d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        selectedDay);
                targetField.setText(date);
                dispose();
            });

            daysPanel.add(dayBtn);
        }

        // Fill remaining cells with next month's days
        int totalCells = 42;
        int filledCells = firstDayOfWeek + daysInMonth;
        int nextMonthDay = 1;

        for (int i = filledCells; i < totalCells; i++) {
            JLabel nextLabel = new JLabel(String.valueOf(nextMonthDay++), SwingConstants.CENTER);
            nextLabel.setForeground(Color.GRAY);
            nextLabel.setPreferredSize(new Dimension(50, 30));
            daysPanel.add(nextLabel);
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }
}