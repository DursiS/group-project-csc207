package view;

import entity.GameState;

import interface_adapter.SaveGameController;
import interface_adapter.SaveGameViewModel;
import interface_adapter.ResumeGameController;
import interface_adapter.ResumeGameViewModel;

import javax.swing.*;
import java.awt.FlowLayout;

public class SaveResumeView {
    private SaveGameController saveGameController;
    private SaveGameViewModel saveGameViewModel;
    private ResumeGameController resumeGameController;
    private ResumeGameViewModel resumeGameViewModel;

    private GameState gameState;

    private JFrame frame;
    private JTextField saveNameField;
    private JLabel messageLabel;

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
        frame.setSize(450,150);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel saveNameLabel = new JLabel("Save Name: ");
        saveNameField = new JTextField(10);
        JButton saveButton = new JButton("Save");
        JButton resumeButton = new JButton("Resume");
        messageLabel = new JLabel("");

        panel.add(saveNameLabel);
        panel.add(saveNameField);
        panel.add(saveButton);
        panel.add(resumeButton);
        panel.add(messageLabel);

        frame.add(panel);

        saveButton.addActionListener(event -> {
            String saveName = saveNameField.getText();

            saveGameController.execute(saveName, this.gameState);

            if (!saveGameViewModel.getError().equals("")) {
                messageLabel.setText(saveGameViewModel.getError());
            }
            else{
                messageLabel.setText(saveGameViewModel.getMessage());
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

        frame.setVisible(true);

    }

}
