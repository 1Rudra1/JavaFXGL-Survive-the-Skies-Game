package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PlayerComponent extends Component {

    private final Vec2 acceleration = new Vec2(6, 0);
    private EntityType weapon = EntityType.BULLET;
    private int count;

    //moves screen
    @Override
    public void onUpdate(double tpf) {
        acceleration.x += tpf * 0.1;
        acceleration.y += tpf * 10;

        if (acceleration.y < -5)
            acceleration.y = -5;

        if (acceleration.y > 5)
            acceleration.y = 5;

        entity.translate(acceleration.x, acceleration.y);

        if (entity.getBottomY() > getAppHeight()) {
            FXGL.<SkiesApp>getAppCast().requestNewGame();
        }
    }

    //jumps
    public void jump() {
        acceleration.addLocal(0, -7);
        play("jump.wav");
    }

    //shoots based on weapon
    public void shoot() {
        Point2D center = entity.getCenter();

        Vec2 dir = Vec2.fromAngle(entity.getRotation());

        if(weapon ==  EntityType.BULLET) {
            FXGL.spawn("bullet", new SpawnData(center.getX(), center.getY()).put("dir", dir.toPoint2D()));
        }
        else if(weapon ==  EntityType.ROCKET && geti("score2") >=22){
            FXGL.spawn("rocket", new SpawnData(center.getX(), center.getY()).put("dir", dir.toPoint2D()));
            inc("score2", -22);
        }
        else{
            FXGL.spawn("bullet", new SpawnData(center.getX(), center.getY()).put("dir", dir.toPoint2D()));
        }

    }

    //switches weapons
    public void changeWeapon() {
        if(count%2==0) {
            weapon = EntityType.ROCKET;
            set("rocketMode", "on");
        }
        else {
            weapon = EntityType.BULLET;
            set("rocketMode", "off");
        }
        count++;
    }

}
