package minesweeper.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.time.Duration;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import minesweeper.model.Game.Result;
import minesweeper.model.Tile;

/**
 * The Game class is a view for a Minesweeper game.
 *
 * @author Cicio Ionut
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class Play extends JPanel implements Observer {

    private JLabel time, flags, mines;
    private Navigator navigator;
    private Canvas canvas;
    private JButton end;

    /**
     * Class constructor.
     */
    Play(Navigator navigator) {
        setLayout(new BorderLayout());

        this.navigator = navigator;

        add(new JPanel(new GridBagLayout()) {
            {
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                GridBagConstraints constraints = new GridBagConstraints();

                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.anchor = GridBagConstraints.LINE_START;

                add(new JPanel(new GridLayout(1, 3, 10, 10)) {
                    {
                        add(time = Factory.label("time: 0s"));
                        add(mines = Factory.label("mines: 0"));
                        add(flags = Factory.label("flags: 0"));
                    }
                }, constraints);

                constraints.gridx = 1;
                constraints.fill = GridBagConstraints.BOTH;

                add(new JPanel(), constraints);

                constraints.anchor = GridBagConstraints.LINE_END;
                constraints.fill = GridBagConstraints.NONE;
                constraints.gridx = 2;

                end = new JButton("end");
                end.addActionListener(e -> navigator.navigate(Screen.Menu));
                add(end, constraints);
            }
        }, BorderLayout.NORTH);

        add(canvas = new Canvas(), BorderLayout.CENTER);
    }

    /**
     * Returns the canvas of the game.
     *
     * @return the canvas of the game
     */
    public Canvas canvas() {
        return canvas;
    }

    /**
     * Returns the end button of the game.
     *
     * @return the end button of the game
     */
    public JButton end() {
        return end;
    }

    /**
     * Updates when notified by a game.
     *
     * @param o   the game
     * @param arg either the result of the game, a duration, a tile or the game
     *            itself
     */
    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof minesweeper.model.Game game))
            return;

        switch (arg) {
            case Result result ->
                navigator.navigate(switch (result) {
                    case Loss -> Screen.Loss;
                    case Victory -> Screen.Victory;
                    case Terminated -> Screen.Menu;
                });
            case Duration duration ->
                time.setText("time: " + duration.toSeconds() + "s");
            case Tile tile -> {
                flags.setText("flags: " + game.flags());
                repaint();
            }
            default -> {
                time.setText("time: " + game.duration().toSeconds() + "s");
                flags.setText("flags: " + game.flags());
                mines.setText("mines: " + game.mines);
                repaint();
            }
        }
    }

}
