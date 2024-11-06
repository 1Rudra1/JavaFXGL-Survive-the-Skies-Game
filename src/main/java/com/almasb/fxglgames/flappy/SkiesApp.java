package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxglgames.flappy.EntityType.*;


public class SkiesApp extends GameApplication {

    private PlayerComponent playerComponent;

    private boolean requestNewGame = false;
    private boolean killedEnemy = false;
    private boolean gameStarted = false;
    private int lives =  3;

    //Makes a menu
    @Override
    protected void initSettings(GameSettings settings) {
        var isRelease = false;
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setTitle("Survive The Skies");
        settings.setVersion("1.2.1");
        settings.setIntroEnabled(isRelease);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(isRelease);
        settings.setFullScreenFromStart(isRelease);
        settings.setProfilingEnabled(false);
        settings.setApplicationMode(isRelease ? ApplicationMode.RELEASE : ApplicationMode.DEVELOPER);
        settings.setFontUI("game_font_7.ttf");
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    //Makes variables
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("stageColor", Color.BLACK);
        vars.put("score", 0);
        vars.put("score2", 0);
        vars.put("lives", lives);
        vars.put("rockets", 0);
        vars.put("rocketMode", "off");
    }

    //allows key input
    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                playerComponent.jump();
            }
        }, KeyCode.SPACE, VirtualButton.UP);

        getInput().addAction(new UserAction("shoot") {
            @Override
            protected void onActionBegin() {
                playerComponent.shoot();
            }
        }, KeyCode.S);

        getInput().addAction(new UserAction("Change Weapon") {
            @Override
            protected void onActionBegin() {
                playerComponent.changeWeapon();
            }
        }, KeyCode.A);
    }

    //Music
    @Override
    protected void onPreInit() {
        loopBGM("bgm.mp3");
    }

    //Basic game
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new GameFactory());
        initBackground();
        initPlayer();
    }

    //Collisions
    @Override
    protected void initPhysics() {

        onCollisionBegin(PLAYER, WALL, (player, wall) -> {
            lives--;
            requestNewGame();
        });

        onCollisionBegin(PLAYER, ENEMY, (player, enemy) -> {
            lives--;
            requestNewGame();
        });

        onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy)->{
            bullet.removeFromWorld();
            spawn("Explosion", enemy.getCenter());
            enemy.removeFromWorld();
            killedEnemy = true;
        });

        onCollisionBegin(EntityType.ROCKET, EntityType.ENEMY, (rocket, enemy)->{
            rocket.removeFromWorld();
            spawn("Explosion", enemy.getCenter());
            enemy.removeFromWorld();
        });

        onCollisionBegin(EntityType.ROCKET, EntityType.ENEMY, (rocket, enemy)->{
            rocket.removeFromWorld();
            spawn("Explosion", enemy.getCenter());
            enemy.removeFromWorld();
        });

            onCollisionBegin(EntityType.ROCKET, WALL, (rocket, wall) -> {
                rocket.removeFromWorld();
                spawn("Explosion", wall.getCenter());
                wall.removeFromWorld();
            });
    }

    //Text
    @Override
    protected void initUI() {
        Text uiScore = new Text("");
        uiScore.setFont(Font.font(62));
        uiScore.setTranslateX(getAppWidth() - 200);
        uiScore.setTranslateY(60);
        uiScore.fillProperty().bind(getop("stageColor"));
        uiScore.textProperty().bind(getip("score").asString());

        Text uiScore2 = new Text("");
        uiScore2.setFont(Font.font(62));
        uiScore2.setTranslateX(getAppWidth() - 1630);
        uiScore2.setTranslateY(62);
        uiScore2.fillProperty().bind(getop("stageColor"));
        uiScore2.textProperty().bind(getip("score2").asString());

        Text uiScore3 = new Text("Points:");
        uiScore3.setFont(Font.font(62));
        uiScore3.setTranslateX(getAppWidth() - 1820);
        uiScore3.setTranslateY(60);
        uiScore3.fillProperty().bind(getop("stageColor"));

        Text uiScore4 = new Text("Length:");
        uiScore4.setFont(Font.font(62));
        uiScore4.setTranslateX(getAppWidth() - 430);
        uiScore4.setTranslateY(60);
        uiScore4.fillProperty().bind(getop("stageColor"));

        Text uiScore5 = new Text("Lives:");
        uiScore5.setFont(Font.font(62));
        uiScore5.setTranslateX(getAppWidth() - 1430);
        uiScore5.setTranslateY(60);
        uiScore5.fillProperty().bind(getop("stageColor"));

        Text uiScore6 = new Text("");
        uiScore6.setFont(Font.font(62));
        uiScore6.setTranslateX(getAppWidth() - 1250);
        uiScore6.setTranslateY(60);
        uiScore6.fillProperty().bind(getop("stageColor"));
        uiScore6.textProperty().bind(getip("lives").asString());

        Text uiScore7 = new Text("Rockets: ");
        uiScore7.setFont(Font.font(62));
        uiScore7.setTranslateX(getAppWidth() - 1030);
        uiScore7.setTranslateY(60);
        uiScore7.fillProperty().bind(getop("stageColor"));

        Text uiScore8 = new Text("");
        uiScore8.setFont(Font.font(62));
        uiScore8.setTranslateX(getAppWidth() - 780);
        uiScore8.setTranslateY(60);
        uiScore8.fillProperty().bind(getop("stageColor"));
        uiScore8.textProperty().bind(getip("rockets").asString());

        Text uiScore9 = new Text("");
        uiScore9.setFont(Font.font(40));
        uiScore9.setTranslateX(getAppWidth() - 1530);
        uiScore9.setTranslateY(150);
        uiScore9.fillProperty().bind(getop("stageColor"));
        uiScore9.textProperty().bind(getsp("rocketMode"));

        Text uiScore10 = new Text("Rocket Mode ON/OFF: ");
        uiScore10.setFont(Font.font(32));
        uiScore10.setTranslateX(getAppWidth() - 1850);
        uiScore10.setTranslateY(150);
        uiScore10.fillProperty().bind(getop("stageColor"));

        addUINode(uiScore);
        addUINode(uiScore2);
        addUINode(uiScore3);
        addUINode(uiScore4);
        addUINode(uiScore5);
        addUINode(uiScore6);
        addUINode(uiScore7);
        addUINode(uiScore8);
        addUINode(uiScore9);
        addUINode(uiScore10);


        Group dpadView = getInput().createVirtualDpadView();
        addUINode(dpadView, 0, 625);

        if(!gameStarted) {
            Text goodLuck = getUIFactoryService().newText("Reach The End With As Many Points As Possible!", Color.YELLOW, 38);
            addUINode(goodLuck);

            centerText(goodLuck);

            animationBuilder()
                    .duration(Duration.seconds(2))
                    .autoReverse(true)
                    .repeat(2)
                    .fadeIn(goodLuck)
                    .buildAndPlay();
        }

    }

    //Updates variables
    @Override
    protected void onUpdate(double tpf) {
        inc("score", +1);
        set("rockets", geti("score2")/22);

        if(killedEnemy){
            inc("score2", +11);
        }

        if(geti("lives") == 0){
            getGameController().gotoMainMenu();
            getDialogService().showMessageBox("GAME OVER");
            lives = 3;
            gameStarted = true;
        }

        if (geti("score") == 1200) {
            showGameOver();
        }

        killedEnemy = false;

        if (requestNewGame) {
            requestNewGame = false;
            getGameController().startNewGame();
        }
    }

    //changes background
    private void initBackground() {
        Rectangle rect = new Rectangle(getAppWidth(), getAppHeight(), Color.LIGHTSKYBLUE);

        Entity bg = entityBuilder()
                .view(rect)
                .with("rect", rect)
                .with(new ColorChangingComponent())
                .buildAndAttach();

        bg.xProperty().bind(getGameScene().getViewport().xProperty());
        bg.yProperty().bind(getGameScene().getViewport().yProperty());
    }

    //creates player
    private void initPlayer() {
        playerComponent = new PlayerComponent();

        Entity player = entityBuilder()
                .at(100, 100)
                .type(PLAYER)
                .bbox(new HitBox(BoundingShape.box(170, 60)))
                .view("player.png")
                .collidable()
                .with(playerComponent, new CloudBuildingComponent())
                .build();
        getGameScene().getViewport().setBounds(0, 0, Integer.MAX_VALUE, getAppHeight());
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 3, getAppHeight() / 2);

        spawnWithScale(player, Duration.seconds(0.86), Interpolators.BOUNCE.EASE_OUT());
    }

    //boolean
    public void requestNewGame() {
        requestNewGame = true;
    }

    //makes a message box
    private void showGameOver() {
        getDialogService().showInputBox("You Won! Thanks for playing! \n\n Your score: " + geti("score2") + "\nRemaining Lives: " + geti("lives") + "\n\nEnter your name", s -> s.matches("[a-zA-Z]*"), name -> {
            getGameController().gotoMainMenu();
        });
        showCredits();
    }
    //makes a message box
    private void showCredits() {
        getDialogService().showMessageBox("This Game Was Brought To You By: Rudra Patel");
    }
    //plays game
    public static void main(String[] args) {
        launch(args);
    }


}
