package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GameFactory implements EntityFactory {

                @Spawns("enemy")
        public Entity newEnemy(SpawnData data){
                    return FXGL.entityBuilder(data)
                    .type(EntityType.ENEMY)
                    .viewWithBBox("monster.png")
                            .collidable()
                    .build();
        }

        @Spawns("bullet")
        public Entity newBullet(SpawnData data){
            Point2D dir = data.get("dir");
            return FXGL.entityBuilder(data)
                    .type(EntityType.BULLET)
                    .viewWithBBox("bullet.png")
                    .with(new ProjectileComponent(dir,1000))
                    .with(new OffscreenCleanComponent())
                    .collidable()
                    .build();
        }
    @Spawns("rocket")
    public Entity newRocket(SpawnData data){
        Point2D dir = data.get("dir");
        return FXGL.entityBuilder(data)
                .type(EntityType.ROCKET)
                .viewWithBBox("rocket.png")
                .with(new ProjectileComponent(dir,1000))
                .with(new OffscreenCleanComponent())
                .collidable()
                .build();
    }
        @Spawns("Explosion")
        public Entity newExplosion(SpawnData data) {
            play("explosion.wav");
            return entityBuilder()
                    .at(data.getX() - 40, data.getY() - 40)
                    .view(texture("explosion.png", 80 * 16, 80).toAnimatedTexture(16, Duration.seconds(0.5)).play())
                    .with(new ExpireCleanComponent(Duration.seconds(1)))
                    .build();
        }



    }




