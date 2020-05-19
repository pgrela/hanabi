package com.pgrela.neural.ui.rps;

import com.pgrela.neural.fast.Main;
import com.pgrela.neural.ui.Utils;
import com.pgrela.neural.ui.rps.game.Figure;
import com.pgrela.neural.ui.rps.game.RPSGame;
import com.pgrela.neural.ui.rps.game.RPSGameSpectator;
import com.pgrela.neural.ui.rps.game.Score;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class RockPaperScissorsUI implements RPSGameSpectator {
    public static final String RPS_NN = "rps.nn";
    private JButton rockButton;
    private JPanel root;
    private JButton paperButton;
    private JButton scissorsButton;
    private JLabel nnLastRound;
    private JButton newGameButton;
    private JCheckBox showNNPredictionsCheckBox;
    private JButton saveNNButton;
    private JComboBox fileSelect;
    private JButton loadNNButton;
    private JButton refreshButton;
    private JTextField newNnName;
    private JLabel gamesScore;
    private JLabel humanScore;
    private JLabel nnScore;
    private JLabel humanPlaysPaperPrediction;
    private JLabel humanPlaysScissorsPrediction;
    private JLabel humanPlaysRockPrediction;
    private JButton resetGamesButton;
    private JLabel humanLastRound;

    private Game game;
    private int humanWins;
    private int nnWins;
    private Image rockImage;
    private Image paperImage;
    private Image scissorsImage;
    private Image smallRockImage;
    private Image smallPaperImage;
    private Image smallScissorsImage;

    public RockPaperScissorsUI() {
        this.game = new Game(this);
        updatePredictions();
        rockButton.addActionListener(e -> game.humanPlays(Figure.ROCK));
        paperButton.addActionListener(e -> game.humanPlays(Figure.PAPER));
        scissorsButton.addActionListener(e -> game.humanPlays(Figure.SCISSORS));
        showNNPredictionsCheckBox.addActionListener(e -> togglePredictions(showNNPredictionsCheckBox.isSelected()));
        newGameButton.addActionListener(e -> newGame());
        resetGamesButton.addActionListener(e -> resetGamesScore());
        newNnName.setText(RPS_NN);
        refreshFileDropdown();
        refreshButton.addActionListener(e -> refreshFileDropdown());
        saveNNButton.addActionListener(e -> saveNN());
        loadNNButton.addActionListener(e -> loadNN());

        setImages();
        setImage(rockButton, rockImage);
        setImage(paperButton, paperImage);
        setImage(scissorsButton, scissorsImage);
    }

    private void setImage(JButton rockButton, Image rockImage) {
        rockButton.setIcon(new ImageIcon(rockImage.getScaledInstance(125, 125, Image.SCALE_SMOOTH)));
    }

    private void setImages() {
        try {
            rockImage = loadImage("rock.png");
            smallRockImage = rockImage.getScaledInstance(32,32, Image.SCALE_SMOOTH);
            paperImage = loadImage("paper.png");
            smallPaperImage = paperImage.getScaledInstance(32,32, Image.SCALE_SMOOTH);
            scissorsImage = loadImage("scissors.png");
            smallScissorsImage = scissorsImage.getScaledInstance(32,32, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage loadImage(String s) throws IOException {
        return ImageIO.read(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(s)));
    }

    private void loadNN() {
        game.loadNetwork(fileSelect.getSelectedItem().toString());
        updatePredictions();
    }

    private void saveNN() {
        game.saveNN(newNnName.getText());
    }

    private void refreshFileDropdown() {
        Utils.dirDropdown(fileSelect, Main.SAVE_DIR, RPS_NN);
    }

    private void resetGamesScore() {
        nnWins=0;
        humanWins=0;
        updateGameScores();
        newGame();
    }

    private void togglePredictions(boolean selected) {
        humanPlaysPaperPrediction.setVisible(selected);
        humanPlaysRockPrediction.setVisible(selected);
        humanPlaysScissorsPrediction.setVisible(selected);
    }

    private void newGame() {
        RPSGame rpsGame = game.newGame();
        scoreChanged(rpsGame);
        toggleGameButtons(true);
    }

    public Container getRootPanel() {
        return root;
    }

    @Override
    public void scoreChanged(RPSGame game) {
        Score score = game.getScore();
        humanScore.setText(String.valueOf(score.getHuman()));
        nnScore.setText(String.valueOf(score.getOpponent()));
        updatePredictions();
    }

    private void updatePredictions() {
        Map<Figure, Double> predictions = this.game.getPredictions();
        humanPlaysPaperPrediction.setText(String.format("%.0f%%", 100 * predictions.get(Figure.PAPER)));
        humanPlaysRockPrediction.setText(String.format("%.0f%%", 100 * predictions.get(Figure.ROCK)));
        humanPlaysScissorsPrediction.setText(String.format("%.0f%%", 100 * predictions.get(Figure.SCISSORS)));
    }

    @Override
    public void gameFinished(RPSGame rpsGame) {
        if (rpsGame.getScore().didHumanWin()) {
            ++humanWins;
        } else {
            ++nnWins;
        }
        updateGameScores();
        toggleGameButtons(false);
    }

    Map<Figure, Supplier<Image>> images = new HashMap<>();
    {
        images.put(Figure.PAPER, ()->smallPaperImage);
        images.put(Figure.ROCK, ()->smallRockImage);
        images.put(Figure.SCISSORS, ()->smallScissorsImage);
    }

    @Override
    public void turnPlayed(RPSGame game, Figure human, Figure opponent) {
        nnLastRound.setIcon(new ImageIcon(images.get(opponent).get()));
        humanLastRound.setIcon(new ImageIcon(images.get(human).get()));
    }


    private void updateGameScores() {
        gamesScore.setText(String.format("%d - %d", humanWins, nnWins));
    }

    private void toggleGameButtons(boolean enabled) {
        rockButton.setEnabled(enabled);
        paperButton.setEnabled(enabled);
        scissorsButton.setEnabled(enabled);
    }
}
