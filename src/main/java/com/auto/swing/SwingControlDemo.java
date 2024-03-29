package com.auto.swing;

import com.auto.common.Constants;
import com.auto.service.AutoService;
import com.auto.service.impl.AutoServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

public class SwingControlDemo {


    public static final String CONFIG_PROPERTIES = "./config.properties";
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JButton showFileDialogButton;
    private JButton runButton;
    private String filePath;


    public SwingControlDemo() {
        prepareGUI();
    }

    public static void main(String[] args) {
        initConfigData();
        SwingControlDemo swingControlDemo = new SwingControlDemo();
        swingControlDemo.showFileChooserDemo();
        swingControlDemo.showRunButton();
        swingControlDemo.visibleMainFrame();
    }

    private static void initConfigData() {
        try {
            FileInputStream inputStream = new FileInputStream(CONFIG_PROPERTIES);
            Properties prop = new Properties();
            prop.load(inputStream);
            // get the property value and print it out
            Constants.CCLEANER_PATH = prop.getProperty("ccleaner.path");
            Constants.CLEANING_DURATION = Integer.valueOf(prop.getProperty("ccleaner.duration"));
            Constants.HSSCP_PATH = prop.getProperty("hsscp.path");
            Constants.WAITING_TIME = Integer.valueOf(prop.getProperty("waiting.time"));
            Constants.LOG_DIR = prop.getProperty("log.dir");
            Constants.sleepingDuration = Long.valueOf(prop.getProperty("sleepingDuration"));
            Constants.RUN_TEST = Boolean.valueOf(prop.getProperty("run.test"));
        } catch (FileNotFoundException e) {
            try (OutputStream output = new FileOutputStream(CONFIG_PROPERTIES)) {
                Properties prop = new Properties();
                // set the properties value
                prop.setProperty("ccleaner.path", Constants.CCLEANER_PATH);
                prop.setProperty("ccleaner.duration", String.valueOf(Constants.CLEANING_DURATION));
                prop.setProperty("hsscp.path", Constants.HSSCP_PATH);
                prop.setProperty("waiting.time", String.valueOf(Constants.WAITING_TIME));
                prop.setProperty("log.dir", Constants.LOG_DIR);
                prop.setProperty("sleepingDuration", String.valueOf(Constants.sleepingDuration));
                prop.setProperty("run.test", String.valueOf(Constants.RUN_TEST));
                // save properties to project root folder
                prop.store(output, null);

                System.out.println(prop);

            } catch (IOException io) {
                io.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void visibleMainFrame() {
        mainFrame.setVisible(true);
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Swing Auto Tool");
        mainFrame.setSize(400, 200);
        mainFrame.setLayout(new GridLayout(3, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);

        statusLabel.setSize(350, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        mainFrame.setResizable(false);

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        visibleMainFrame();
    }

    private void showFileChooserDemo() {
        headerLabel.setText("Please choose [accounts].txt file and click Run to start");

        final JFileChooser fileDialog = new JFileChooser();
        showFileDialogButton = new JButton("Select File");
        showFileDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(mainFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                    statusLabel.setText("File Selected :"
                            + file.getName());
                    runButton.setEnabled(true);
                    filePath = file.getPath();
                } else {
                    statusLabel.setText("Select file cancelled by user.");
                    runButton.setEnabled(false);
                }
            }
        });
        controlPanel.add(showFileDialogButton);

    }

    public void showRunButton() {
        runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runButton.setEnabled(false);

                AutoService autoService = new AutoServiceImpl();
                if (StringUtils.isNotBlank(filePath) && filePath.endsWith(".txt")) {
                    String runResult = autoService.autoLogin(filePath);
                    if(Constants.SUCCESS.equals(runResult)){
                        showAlert("Run success...!", "Finish", JOptionPane.INFORMATION_MESSAGE);
                    }else {
                        showAlert("Run success fail... : ", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }else {
                    showAlert("filePath incorrect: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                }

                runButton.setEnabled(true);
            }

        });

        runButton.setEnabled(false);
        controlPanel.add(runButton);
    }

    private void showAlert(String message, String title, int type) {
        JOptionPane.showMessageDialog(mainFrame,
                message,
                title,
                type);
    }


}
