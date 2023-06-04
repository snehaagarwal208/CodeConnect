import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
public class MemoryGame extends JFrame {
    private Map<String, String> questionAnswerMap;
    private ArrayList<JButton> questionButtons;
    private ArrayList<JButton> answerButtons;
    private JPanel questionPanel,answerPanel;
    private int currentQuestionIndex,correctAnswerCount,wrongAnswerCount;
    private boolean isQuestionSelected;
    private int buttonWidth = 500;
    private Timer timer;
    private JLabel timerLabel;
    private JButton startButton;
    private JButton stopButton;
    private int seconds;

    public MemoryGame() {
        setTitle("Java Memory game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questionAnswerMap = new HashMap<>();
        initializeQuestionAnswerPairs();
         timerLabel = new JLabel("00:00:00", SwingConstants.CENTER);

        // Create the start button
        startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());

        // Create the stop button
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonListener());

        JLabel lab = new JLabel("You need to complete this in 2 minutes");
        // Add the timer label, start button, and stop button to the top (north) position
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(timerLabel);
        topPanel.add(startButton);
        topPanel.add(stopButton);
        topPanel.add(lab);
        add(topPanel, BorderLayout.NORTH);

        // Create the timer and set its delay to 1 second
        timer = new Timer(1000, new TimerListener());
        //  panels
        questionPanel = new JPanel(new GridLayout(3,3,10,10));
        answerPanel = new JPanel(new GridLayout(3,3,10,10));

        //  question buttons
        questionButtons = new ArrayList<>();
        for (String question : questionAnswerMap.keySet()) {
            JButton button = createButton(question);
            button.addActionListener(new QuestionButtonListener());
            questionButtons.add(button);
            questionPanel.add(button);
        }

        // answer buttons
        answerButtons = new ArrayList<>();
        ArrayList<String> shuffledAnswers = new ArrayList<>(questionAnswerMap.values());
        Collections.shuffle(shuffledAnswers);
        for (String answer : shuffledAnswers) {
            JButton button = createButton(answer);
            button.addActionListener(new AnswerButtonListener());
            button.setEnabled(true);
            answerButtons.add(button);
            answerPanel.add(button);
        }

    
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(questionPanel);
        contentPanel.add(answerPanel);

        add(contentPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
     

    }
    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();  
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
        return new ImageIcon(resizedImage);
    }

    private JButton createButton(String text) {

        
        JButton button = new JButton(text);
        button.setForeground(button.getBackground());
        // button.setOpaque(false);
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setPreferredSize(new Dimension(buttonWidth, 100)); 
        button.setBorder(BorderFactory.createLineBorder(Color.black, 1)); 

        return button;
    }

    private void initializeQuestionAnswerPairs() {
        Set<Integer> numbers = new HashSet<Integer>();
        Random rand = new Random();

        while (numbers.size()<6) {
            int num = rand.nextInt(21);
            numbers.add(num);
        }
        for (int num : numbers) {
            questionAnswerMap.put("qu" + num, "ans" + num);
        }
             
    }

    private class QuestionButtonListener implements ActionListener {
    
        public void actionPerformed(ActionEvent e) {
            JButton questionButton = (JButton) e.getSource();
            // questionButton.setEnabled(false);
            currentQuestionIndex = questionButtons.indexOf(questionButton);
            ImageIcon icon = new ImageIcon("C:/Users/antima/Downloads/photos/" + questionButton.getText() + ".png");
 
            if(questionButton.getIcon()==null)  questionButton.setIcon(resizeIcon(icon, 360 ,200));
            enableAnswerButtons();
        }
    }

    private class AnswerButtonListener implements ActionListener {
     
        public void actionPerformed(ActionEvent e) {
            JButton answerButton = (JButton) e.getSource();
            String selectedAnswer = answerButton.getText();
            String correctAnswer = questionAnswerMap.get(questionButtons.get(currentQuestionIndex).getText());
            
            if (!isQuestionSelected) {
                JOptionPane.showMessageDialog(MemoryGame.this, "Please select a question first.");
                return;
            }
            //change the path according to your folder location
            ImageIcon icon = new ImageIcon("C:/Users/antima/Downloads/photos/" + answerButton.getText() + ".png");
            answerButton.setIcon(resizeIcon(icon, 360  , 250));
            if (selectedAnswer.equals(correctAnswer)) {
                JOptionPane.showMessageDialog(MemoryGame.this, "yayy!! Correct answer!");
              
                isQuestionSelected = false;
                clearAnswerSelection(); 
                correctAnswerCount++; 
            } 
            else {
                
                JOptionPane.showMessageDialog(MemoryGame.this, "OOPS! Wrong answer! Try again.");
                wrongAnswerCount++;
                questionButtons.get(currentQuestionIndex).setIcon(null);
                answerButton.setIcon(null);
            }
            if(correctAnswerCount == 6) JOptionPane.showMessageDialog(MemoryGame.this, getResult());
        }
    }
    private class StartButtonListener implements ActionListener {
      
        public void actionPerformed(ActionEvent e) {
            timer.start();
            enableAnswerButtons();
        }
    }

    private class StopButtonListener implements ActionListener {
      
        public void actionPerformed(ActionEvent e) {
            timer.stop();
            disableAnswerButtons();
        }
    }

    private class TimerListener implements ActionListener {
     
        public void actionPerformed(ActionEvent e) {
            seconds++;
            int hour = seconds / 3600;
            int minute = (seconds % 3600) / 60;
            int second = seconds % 60;
            timerLabel.setText(String.format("%02d:%02d:%02d", hour, minute, second));
            if(minute == 2) JOptionPane.showMessageDialog(MemoryGame.this, "Time UP! Game Over");
        }
    }

    public String getResult(){
        
        if(wrongAnswerCount < 2){
            return "Correct ans : " + correctAnswerCount + "\nWrong ans : " + wrongAnswerCount + " \nCongrats! You are selected for Microsoft, bro party when??";
        }
        else if(wrongAnswerCount >=2 && wrongAnswerCount< 4) return "Correct ans : " + correctAnswerCount + "\nWrong ans : " + wrongAnswerCount + "\nCongrats! You are selected for Amazon, \nkindly report to rizwan sir";
        else if(wrongAnswerCount >=4 && wrongAnswerCount< 6) return "Correct ans : " + correctAnswerCount + "\nWrong ans : " + wrongAnswerCount + "\nCongrats! You are requested to contact sriram sir";
        return "Correct ans : " + correctAnswerCount + "\nWrong ans : " + wrongAnswerCount + "\nYou are not elegible for any company";
    }
    

    private void enableAnswerButtons() {
        for (JButton answerButton : answerButtons) {
            answerButton.setEnabled(true);
        }
        isQuestionSelected = true;
    }

    private void disableAnswerButtons() {
        for (JButton answerButton : answerButtons) {
            answerButton.setEnabled(false);
        }
    }

    private void clearAnswerSelection() {
        for (JButton answerButton : answerButtons) {
            answerButton.setSelected(false);
        }
    }

    public static void main(String[] args) {
        new MemoryGame();
    }
}
