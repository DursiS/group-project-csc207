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

import java.util.function.Consumer;

import java.util.ArrayList;

public class SaveResumeView extends JPanel{
    private SaveGameController saveGameController;
    private SaveGameViewModel saveGameViewModel;
    private ResumeGameController resumeGameController;
    private ResumeGameViewModel resumeGameViewModel;

    private GameState gameState;
    private String currentSaveName;

    private JTextField saveNameField;
    private JLabel messageLabel;
    private DefaultListModel<String> saveNameList;
    private JList<String> savedGamesList;

    private Consumer<GameState> gameStateChangeHandler;

    public SaveResumeView(SaveGameController saveGameController,
                          SaveGameViewModel saveGameViewModel,
                          ResumeGameController resumeGameController,
                          ResumeGameViewModel resumeGameViewModel,
                          GameState gameState,
                          Consumer<GameState> gameStateChangeHandler) {
        this.saveGameController = saveGameController;
        this.saveGameViewModel = saveGameViewModel;
        this.resumeGameController = resumeGameController;
        this.resumeGameViewModel = resumeGameViewModel;
        this.gameState = gameState;
        this.currentSaveName = null;
        this.gameStateChangeHandler = gameStateChangeHandler;

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

        centerPanel.add(new JLabel("Saved Games: " +
                        resumeGameController.getSaveNames().size()),
                BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        //bottom
        messageLabel = new JLabel("");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageLabel, BorderLayout.NORTH);

        //main
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);

        saveGame(saveGameController, saveGameViewModel, saveButton, saveNameField, null,
                null);

        resumeGame(resumeGameController, resumeGameViewModel, resumeButton);

        //closing
        refreshSaveList();

    }

    private void resumeGame(ResumeGameController resumeGameController,
                            ResumeGameViewModel resumeGameViewModel,
                            JButton resumeButton) {
        resumeButton.addActionListener(event -> {
            String saveName = saveNameField.getText();
            GameState loadedGame = resumeGameController.execute(saveName);

            if  (loadedGame == null) {
                messageLabel.setText(resumeGameViewModel.getErrorMessage());
            }
            else {
                this.gameState = loadedGame;
                this.currentSaveName = saveName;
                gameStateChangeHandler.accept(loadedGame);
                messageLabel.setText(resumeGameViewModel.getMessage());
            }
        });
    }

    private void saveGame(SaveGameController saveGameController,
                          SaveGameViewModel saveGameViewModel,
                          JButton saveButton,
                          JTextField textField,
                          JFrame frameToClose,
                          JFrame mainFrameToClose) {
        saveButton.addActionListener(event -> {

            String saveName = textField.getText();

            saveGameController.execute(saveName, this.gameState);

            if(!saveGameViewModel.getOverwriteMessage().equals("")) {
                overwrite(saveGameController, saveGameViewModel, textField,
                        frameToClose,
                        mainFrameToClose);
            }

            else if (!saveGameViewModel.getError().equals("")) {
                messageLabel.setText(saveGameViewModel.getError());
            }
            else{
                this.currentSaveName = saveGameViewModel.getSavedName();
                messageLabel.setText(saveGameViewModel.getMessage());
                refreshSaveList();

                if (frameToClose != null) {
                    frameToClose.dispose();
                }

                if (mainFrameToClose != null) {
                    mainFrameToClose.dispose();
                }
            }
        });
    }

    private void overwrite(SaveGameController saveGameController,
                           SaveGameViewModel saveGameViewModel,
                           JTextField textField,
                           JFrame frameToClose,
                           JFrame mainFrameToClose) {
        JFrame overwriteFrame = new JFrame();
        overwriteFrame.setTitle("Overwrite");
        overwriteFrame.setSize(250,100);
        overwriteFrame.setDefaultCloseOperation(overwriteFrame.DISPOSE_ON_CLOSE);

        JPanel overwritePanel = new JPanel();
        JLabel overwriteLabel = new JLabel(saveGameViewModel.getOverwriteMessage());
        JButton overwriteYes = new JButton("Yes");
        JButton overwriteNo = new JButton("No");

        overwritePanel.add(overwriteLabel);
        overwritePanel.add(overwriteYes);
        overwritePanel.add(overwriteNo);

        overwriteYes.addActionListener(event -> {
            String saveName = textField.getText();
            saveGameController.overwrite(saveName, this.gameState);

            this.currentSaveName = saveGameViewModel.getSavedName();

            messageLabel.setText(saveGameViewModel.getMessage());

            refreshSaveList();
            overwriteFrame.dispose();
        });

        overwriteNo.addActionListener(event -> {
            messageLabel.setText("Please Enter Another Name");
            textField.setText("");

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

    public void exitGame(JFrame mainFrame) {

        if (currentSaveName != null) {
            mainFrame.dispose();
            return;
        }
        JFrame closePrompt = new JFrame();
        closePrompt.setDefaultCloseOperation(closePrompt.DISPOSE_ON_CLOSE);
        JPanel closePanel = new JPanel(new BorderLayout());
        JLabel closeLabel = new JLabel("Do You Want To Save?");
        JPanel buttonPanel = new JPanel();
        JButton closeYes = new JButton("Yes");
        JButton closeNo = new JButton("No");

        buttonPanel.add(closeNo);
        buttonPanel.add(closeYes);

        closePanel.add(closeLabel, BorderLayout.NORTH);
        closePanel.add(buttonPanel, BorderLayout.CENTER);

        closePrompt.add(closePanel);
        closePrompt.pack();

        closeYes.addActionListener(event -> {
            closePrompt.dispose();
            JFrame anotherSaveFrame = new JFrame();
            anotherSaveFrame.setDefaultCloseOperation(anotherSaveFrame.DISPOSE_ON_CLOSE);
            JPanel anotherSavePanel = new JPanel();
            JLabel anotherLabel = new JLabel("Save Name: ");
            JTextField anotherSaveTextField = new JTextField(10);
            JButton anotherSaveButton = new JButton("Save");

            anotherSavePanel.add(anotherLabel);
            anotherSavePanel.add(anotherSaveTextField);
            anotherSavePanel.add(anotherSaveButton);
            anotherSaveFrame.add(anotherSavePanel);
            anotherSaveFrame.pack();

            saveGame(saveGameController,
                    saveGameViewModel,
                    anotherSaveButton,
                    anotherSaveTextField,
                    anotherSaveFrame,
                    mainFrame);

            anotherSaveFrame.setVisible(true);
        });

        closeNo.addActionListener(event -> {
            closePrompt.dispose();
            mainFrame.dispose();
        });

        closePrompt.setVisible(true);
    }

}
