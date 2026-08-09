package view;

import entity.GameState;

import interface_adapter.SaveGameController;
import interface_adapter.SaveGameViewModel;
import interface_adapter.ResumeGameController;
import interface_adapter.ResumeGameViewModel;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;

public class SaveResumeView {
    private SaveGameController saveGameController;
    private SaveGameViewModel saveGameViewModel;
    private ResumeGameController resumeGameController;
    private ResumeGameViewModel resumeGameViewModel;

    private GameState gameState;

    private JFrame frame;
    private JTextField saveNameField;
    private JLabel messageLabel;
    private DefaultListModel<String> saveNameList;
    private JList<String> savedGamesList;

    public SaveResumeView(SaveGameController saveGameController,
                          SaveGameViewModel saveGameViewModel,
                          ResumeGameController resumeGameController,
                          ResumeGameViewModel resumeGameViewModel,
                          GameState gameState) {
        this.saveGameController = saveGameController;
        this.saveGameViewModel = saveGameViewModel;
        this.resumeGameController = resumeGameController;
        this.resumeGameViewModel = resumeGameViewModel;
        this.gameState = gameState;

        frame = new JFrame();

        frame.setTitle("Save / Resume Game");
        frame.setSize(500,350);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //top
        JPanel topPanel = new JPanel();
        JLabel saveNameLabel = new JLabel("Save Name: ");
        saveNameField = new JTextField(10);
        JButton saveButton = new JButton("Save");
        JButton resumeButton = new JButton("Resume");

        topPanel.add(saveNameLabel);
        topPanel.add(saveNameField);
        topPanel.add(saveButton);
        topPanel.add(resumeButton);

        //center
        this.saveNameList = new DefaultListModel<>();
        this.savedGamesList = new JList<>(saveNameList);

        savedGamesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        savedGamesList.addListSelectionListener(event -> {
            String selectedSave = savedGamesList.getSelectedValue();
            if (selectedSave != null) {
                saveNameField.setText(selectedSave);
            }
        });

        JScrollPane scrollPane = new JScrollPane(savedGamesList);
        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.add(new JLabel("Saved Games"), BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //bottom
        messageLabel = new JLabel("");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageLabel, BorderLayout.NORTH);

        //main
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        saveButton.addActionListener(event -> {

            String saveName = saveNameField.getText();

            saveGameController.execute(saveName, this.gameState);

            if(!saveGameViewModel.getOverwriteMessage().equals("")) {
                overwrite(saveGameController, saveGameViewModel);
            }

            else if (!saveGameViewModel.getError().equals("")) {
                messageLabel.setText(saveGameViewModel.getError());
            }
            else{
                messageLabel.setText(saveGameViewModel.getMessage());
                refreshSaveList();
            }
        });

        resumeButton.addActionListener(event -> {
            String saveName = saveNameField.getText();
            GameState loadedGame = resumeGameController.execute(saveName);

            if  (loadedGame == null) {
                messageLabel.setText(resumeGameViewModel.getErrorMessage());
            }
            else {
                this.gameState = loadedGame;
                messageLabel.setText(resumeGameViewModel.getMessage());
            }
        });
        refreshSaveList();
        frame.setVisible(true);

    }

    private void overwrite(SaveGameController saveGameController, SaveGameViewModel saveGameViewModel) {
        JFrame overwriteFrame = new JFrame();
        overwriteFrame.setTitle("Overwrite");
        overwriteFrame.setSize(250,100);
        overwriteFrame.setDefaultCloseOperation(overwriteFrame.EXIT_ON_CLOSE);

        JPanel overwritePanel = new JPanel();
        JLabel overwriteLabel = new JLabel(saveGameViewModel.getOverwriteMessage());
        JButton overwriteYes = new JButton("Yes");
        JButton overwriteNo = new JButton("No");

        overwritePanel.add(overwriteLabel);
        overwritePanel.add(overwriteYes);
        overwritePanel.add(overwriteNo);

        overwriteYes.addActionListener(event -> {
            String saveName = saveNameField.getText();
            saveGameController.overwrite(saveName, this.gameState);

            messageLabel.setText(saveGameViewModel.getMessage());

            refreshSaveList();
            overwriteFrame.dispose();
        });

        overwriteNo.addActionListener(event -> {
            messageLabel.setText("Please Enter Another Name");
            saveNameField.setText("");

            overwriteFrame.dispose();
        });

        overwriteFrame.add(overwritePanel);
        overwriteFrame.setVisible(true);
    }

    private void refreshSaveList() {

        saveNameList.clear();

        ArrayList<String> saves =
                resumeGameController.getSaveNames();

        for (String saveName : saves) {
            saveNameList.addElement(saveName);
        }
    }

}
