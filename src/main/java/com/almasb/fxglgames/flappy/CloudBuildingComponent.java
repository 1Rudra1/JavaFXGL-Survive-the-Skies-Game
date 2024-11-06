package com.almasb.fxglgames.flappy;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class CloudBuildingComponent extends Component {

    private double lastWall = 1000;

    @Override
    public void onUpdate(double tpf) {
        if (lastWall - entity.getX() < FXGL.getAppWidth()) {
            buildClouds();
        }
    }

    //makes clouds
    private void buildClouds() {
        double height = FXGL.getAppHeight();
        double distance = height / 2;

        for (int i = 1; i <= 10; i++) {
            double topHeight = Math.random() * (height - distance);
            entityBuilder()
                    .at(lastWall + i * 500, 0 + Math.random() * (distance)-10)
                    .type(EntityType.WALL)
                    .viewWithBBox("cloud.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();

            entityBuilder()
                    .at((lastWall + i * 500) + 11, 0 + topHeight + distance)
                    .type(EntityType.WALL)
                    .viewWithBBox("cloud.png")
                    .with(new CollidableComponent(true))
                    .buildAndAttach();

            FXGL.spawn("enemy", new SpawnData(lastWall + i * 450,0 + topHeight + distance - 300));
        }

        lastWall += 10 * 500;
    }

}
