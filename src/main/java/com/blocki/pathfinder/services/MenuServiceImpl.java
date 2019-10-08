package com.blocki.pathfinder.services;

import com.blocki.pathfinder.models.singletons.GameState;
import com.blocki.pathfinder.models.singletons.Menu;
import javafx.scene.control.RadioButton;

public class MenuServiceImpl implements MenuService {

    private final Menu  menu = Menu.getInstance();

    private final GameState gameState = GameState.getInstance();

    @Override
    public void chooseAlgorithm(RadioButton radioButton) {

        if(gameState.getCurrentState() == GameState.STATE.WAITING) {

            try {

                menu.setChosenAlgorithm(Menu.ALGORITHM_TYPE.valueOf(radioButton.getText()));
            }
            catch (IllegalArgumentException ex) {

                ex.printStackTrace();
            }
        }
    }

    @Override
    public void chooseHeuristics(RadioButton radioButton) {

        if(gameState.getCurrentState() == GameState.STATE.WAITING) {

            try {

                menu.setChosenHeuristics(Menu.ALGORITHM_TYPE.valueOf(radioButton.getText()));
            }
            catch (IllegalArgumentException ex) {

                ex.printStackTrace();
            }
        }
    }

    @Override
    public void switchDiagonalSearch() {

        if(gameState.getCurrentState() == GameState.STATE.WAITING) {

            menu.setDiagonalSearch(!menu.isDiagonalSearch());
        }
    }

    @Override
    public void switchInstantSearch() {

        if(gameState.getCurrentState() == GameState.STATE.WAITING) {

            menu.setInstantSearch(!menu.isInstantSearch());
        }
    }

    @Override
    public void switchDontCrossCorners() {

        if(gameState.getCurrentState() == GameState.STATE.WAITING) {

            menu.setDontCutCorners(!menu.isDontCutCorners());
        }
    }

    @Override
    public void setSliderSpeed(int value) {

        menu.setSliderValue(value);
    }
}
