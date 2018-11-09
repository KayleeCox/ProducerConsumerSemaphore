import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;

public class MainGUI {
    int numChildren = 1;
    int money = 0;
    int childWait = 1000;
    int parentSalary = 100;
    int parentWait = 1000;
    int numIceCream = 5;
    Random rand = new Random(20);
    int randomNum;
    Children child;
    Parents mom;
    Parents dad;

    ArrayList<Children> childList = new ArrayList();
    Semaphore semaphore = new Semaphore(1);
    private JPanel mainPanel;
    private JLabel inputNumChild;
    private JTextField textField1;
    private JButton startButton;
    private JButton speedUpButton;
    private JButton slowDownButton;
    private JButton makeMoreMoneyButton;
    private JButton eatMoreIceCreamButton;
    private JTextArea textArea1;
    private JButton murderTheFamilyButton;
    private JTextArea textArea2;


    public class Children extends Thread{
        public void run() {
            while (true) {
                try {
                    semaphore.acquire();
                    randomNum = rand.nextInt(numIceCream - 1) + 1;
                    if (money >= randomNum) {
                        money -= randomNum;
                        textArea1.append("\nWithdrawal of $" + randomNum);
                        textArea1.append("\nAccount Balance: $" + money);
                        semaphore.release();
                        Thread.sleep(childWait);
                    } else {
                        textArea2.append("\n Not enough money for ice cream!");
                        semaphore.release();
                        Thread.sleep(childWait);
                    }
                } catch(InterruptedException e) {
                    break;
                }
            }

        }

    }

    public class Parents extends Thread{
        public void run() {
                while (true) {
                    try{
                        semaphore.acquire();
                        randomNum = rand.nextInt(parentSalary - 10) + 10;
                        money += randomNum;
                        textArea1.append("\nDeposit of $" + randomNum);
                        textArea1.append("\nAccount Balance: $" + money);
                        semaphore.release();
                        Thread.sleep(parentWait);
                    }catch(InterruptedException e){
                        break;
                    }
                }


        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainGUI() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mom = new Parents();
                dad = new Parents();
                mom.start();
                dad.start();
                String num = textField1.getText();
                numChildren = Integer.parseInt(num);
                for( int i = 0; i < numChildren; i++){
                    childList.add(i, new Children());
                    child = childList.get(i);
                    child.start();
                }
            }
        });
        speedUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.append("\nFaster! Faster!");
                if(parentWait == 0) {
                    textArea2.append("\nParents are working as fast as they can!");
                }
                else parentWait -= 500;
                if(childWait == 0) {
                    textArea2.append("\nChildren are eating as fast as they can!");
                }
                else childWait -= 500;
            }

        });
        slowDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.append("\nSlow down there, pals!");
                parentWait += 500;
                childWait += 500;
            }
        });
        makeMoreMoneyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.append("\nThe parents got a raise!");
                parentSalary += 50;

            }
        });
        eatMoreIceCreamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea2.append("\nThe children are getting hungrier.");
                numIceCream += 10;
            }
        });

        murderTheFamilyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mom.interrupt();
                dad.interrupt();
                for(int i = 0; i < childList.size(); i++)
                {
                    child = childList.get(i);
                    child.interrupt();
                }
                childList.clear();
                numChildren = 1;
                money = 0;
                childWait = 1000;
                parentSalary = 100;
                parentWait = 1000;
                numIceCream = 5;
                textArea2.append("\nOh, no! The family has been murdered. \nPress 'Start' to try again with a new family");


            }
        });
    }
}
