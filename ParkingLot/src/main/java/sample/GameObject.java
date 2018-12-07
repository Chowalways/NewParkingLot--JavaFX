package sample;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;

public class GameObject {
    private Node view;
    private Point2D velocity = new Point2D(1, 0);
    private boolean move = false;
    private boolean reverse = false;

    private boolean alive = true;

    public GameObject(Node view) {
        this.view = view;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public void toggleStop() {
        move = !move;
    }

    public void toggleReverse() {
        reverse = !reverse;
    }

    public void update() {
        double newX = view.getTranslateX(),
                newY = view.getTranslateY();

        if(reverse){
            newX -= velocity.getX();
            newY -= velocity.getY();
        } else {
            newX += velocity.getX();
            newY += velocity.getY();
        }

        if(move) {
            if(checkX(newX))
                view.setTranslateX(newX);
            if(checkY(newY))
                view.setTranslateY(newY);
        }
    }

    private boolean checkX(double x) {
        Bounds parentBounds = view.getParent().getLayoutBounds();
        return parentBounds.getMaxX() - view.getLayoutBounds().getWidth() > x &&
                parentBounds.getMinX() < x;
    }

    private boolean checkY(double y) {
        Bounds parentBounds = view.getParent().getLayoutBounds();

        return parentBounds.getMaxY() - view.getLayoutBounds().getHeight() >= y &&
                parentBounds.getMinY() <= y;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return !alive;
    }

    public double getRotate() {
        return view.getRotate();
    }

    public void rotateRight() {
        view.setRotate((view.getRotate() + 5) % 360);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
        System.out.println(getRotate());
        System.out.println(getVelocity());
    }

    public void rotateLeft() {
        view.setRotate((view.getRotate() - 5) % 360);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
        System.out.println(getRotate());
    }

    public final <T extends Event> void addEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
        view.addEventFilter(eventType, eventFilter);
    }

    public final <T extends Event> void removeEventFilter(
            final EventType<T> eventType,
            final EventHandler<? super T> eventFilter) {
        view.removeEventFilter(eventType, eventFilter);
    }

    public boolean isColliding(GameObject other) {
        if(other == this) {
            return false;
        }
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }
}
