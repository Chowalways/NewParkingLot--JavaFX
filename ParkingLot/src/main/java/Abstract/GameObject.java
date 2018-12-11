package Abstract;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import Unit.Enum.Side;

public abstract class GameObject {
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

    public final Point2D getVelocity() {
        return velocity;
    }

    public final void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public final void toggleStop() {
        move = !move;
    }

    public final void toggleReverse() {
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

    public Side getMoveSide() {

        double currentX = view.getTranslateX(),
                currentY = view.getTranslateY(),
                newX = currentX,
                newY = currentY;

        if(reverse){
            newX -= velocity.getX();
            newY -= velocity.getY();
        } else {
            newX += velocity.getX();
            newY += velocity.getY();
        }

        return Side.calcMovingSide(currentX, currentY, newX, newY);
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

    public boolean isMove() {
        return move;
    }

    public boolean isReverse() {
        return reverse;
    }

    public double getRotate() {
        return view.getRotate();
    }

    public void rotateRight() {
        view.setRotate((view.getRotate() + 5) % 360);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }

    public void rotateLeft() {
        view.setRotate((view.getRotate() - 5) % 360);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
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

    public Side isColliding(GameObject other) {
        int checkLoop = 5;

        if(other == this) {
            return Side.NONE;
        }

        Bounds selfBound = getView().getBoundsInParent();
        Bounds otherBound = other.getView().getBoundsInParent();
        Bounds temp;


        Boolean isCollision = selfBound.intersects(otherBound);

        if(isCollision) {
            return Side.INSIDE;
        }

        // Detect Left
        for(double i = 1; i < checkLoop; i += 0.5) {
            temp = new BoundingBox(selfBound.getMinX() - i, selfBound.getMinY() + 1, selfBound.getWidth(), selfBound.getHeight() - 2);
            if(temp.intersects(otherBound))
                return Side.LEFT;
        }

        // Detect Right
        for(double i = 1; i < checkLoop; i += 0.5) {
            temp = new BoundingBox(selfBound.getMinX() + i, selfBound.getMinY() + 1, selfBound.getWidth(), selfBound.getHeight() - 2);
            if(temp.intersects(otherBound))
                return Side.RIGHT;
        }

        // Detect Top
        for(double i = 1; i < checkLoop; i += 0.5) {
            temp = new BoundingBox(selfBound.getMinX() + 1, selfBound.getMinY() - i, selfBound.getWidth() - 2, selfBound.getHeight());
            if (temp.intersects(otherBound))
                return Side.TOP;
        }
        // Detect Bottom
        for(double i = 1; i < checkLoop; i += 0.5) {
            temp = new BoundingBox(selfBound.getMinX() + 1, selfBound.getMinY() + i, selfBound.getWidth() - 2, selfBound.getHeight());
            if (temp.intersects(otherBound))
                return Side.BOTTOM;
        }

//        Haven't this part implement
//        // Detect Top Left
//        for(double i = 1; i < checkLoop; i += 0.5) {
//            temp = new BoundingBox(selfBound.getMinX() - i, selfBound.getMinY() + 1, selfBound.getWidth(), selfBound.getHeight() - 2);
//            if(temp.intersects(otherBound))
//                return Side.LEFT;
//        }
//
//        // Detect Top Right
//        for(double i = 1; i < checkLoop; i += 0.5) {
//            temp = new BoundingBox(selfBound.getMinX() + i, selfBound.getMinY() + 1, selfBound.getWidth(), selfBound.getHeight() - 2);
//            if(temp.intersects(otherBound))
//                return Side.RIGHT;
//        }
//
//        // Detect Bottom Left
//        for(double i = 1; i < checkLoop; i += 0.5) {
//            temp = new BoundingBox(selfBound.getMinX() + 1, selfBound.getMinY() - i, selfBound.getWidth() - 2, selfBound.getHeight());
//            if (temp.intersects(otherBound))
//                return Side.TOP;
//        }
//        // Detect Bottom Right
//        for(double i = 1; i < checkLoop; i += 0.5) {
//            temp = new BoundingBox(selfBound.getMinX() + 1, selfBound.getMinY() + i, selfBound.getWidth() - 2, selfBound.getHeight());
//            if (temp.intersects(otherBound))
//                return Side.BOTTOM;
//        }
//
        
        return Side.NONE;
    }
}
