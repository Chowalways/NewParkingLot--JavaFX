package Abstract;

import com.sun.javafx.application.PlatformImpl;
import javafx.animation.AnimationTimer;
import javafx.scene.Parent;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

public abstract class GameScene {

    protected GameScene(Parent parent) {createGameScene(parent);}

    private boolean lock = false;

    protected final void createGameScene(Parent parent) {
        preInit(parent);
        generate();
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        bindControl();
                    }
                }, 200
        );
    }

    protected abstract void preInit(Parent parent);

    protected abstract void generate();

    protected abstract void bindControl();

    protected abstract void onUpdate();

    public final void startTimer() {
        // Update scene timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(lock)
                    return;
                lock = true;
                onUpdate();
                lock = false;
            }
        };
        timer.start();
    }
}
