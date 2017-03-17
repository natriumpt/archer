package org.academiadecodigo.hackathon.archer.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.academiadecodigo.hackathon.archer.ArcherGame;

/**
 * Created by codecadet on 17/03/17.
 */
public class Hud {

    public Stage stage;
    private Viewport viewport;

    private Integer score;
    private Integer timeCounter;
    private Integer timeToBeat;

    private Label scoreLabel;
    private Label scoreTextLabel;
    private Label timeLabel;
    private Label timeTextLabel;
    private Label timeToBeatLabel;
    private Label timeToBeatTextLabel;

    public Hud(SpriteBatch spriteBatch) {

        score = 0;
        timeCounter = 0;
        timeToBeat = 300;

        viewport = new FitViewport(ArcherGame.V_WIDTH, ArcherGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();

        initLabels();

        prepareTable(table);

        stage.addActor(table);


    }

    private void prepareTable(Table table) {
        table.top();
        table.setFillParent(true);
        table.add(timeToBeatTextLabel).expandX().padTop(5);
        table.add(scoreTextLabel).expandX().padTop(5);
        table.add(timeTextLabel).expandX().padTop(5);
        table.row();
        table.add(timeToBeatLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(timeLabel).expandX();
    }

    private void initLabels() {
        scoreLabel = new Label(String.format("%05d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label(String.format("%03d", timeCounter), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeToBeatLabel = new Label(String.format("%03d", timeToBeat), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreTextLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeTextLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeToBeatTextLabel = new Label("TIME PASSED", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    }
}
