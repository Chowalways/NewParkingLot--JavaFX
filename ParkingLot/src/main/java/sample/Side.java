package sample;

public enum Side {
    LEFT, RIGHT, TOP, BOTTOM, INSIDE, OUTSIDE, TOPLEFT, TOPRIGHT, BOTTOMLEFT, BOTTOMRIGHT, NONE;


    public static Side calcMovingSide(double currentX, double currentY, double newX, double newY) {

        // Top Left, Top Right, Bottom Left, Bottom Right, Top, Bottom, Left, Right
        if(currentX - newX > 0 && currentY - newY > 0 ) {
            return TOPLEFT;
        } else if(currentX - newX < 0 && currentY - newY > 0) {
            return TOPRIGHT;
        } else if(currentX - newX > 0 && currentY - newY < 0 ) {
            return BOTTOMLEFT;
        } else if(currentX - newX < 0 && currentY - newY < 0) {
            return BOTTOMRIGHT;
        } else if(currentY - newY > 0) {
            return TOP;
        } else if (currentY - newY < 0) {
            return BOTTOM;
        } else if(currentX - newX > 0) {
            return LEFT;
        } else  {
//            if (currentX - newX < 0)
            return RIGHT;
        }
    }

    public Boolean compare(Side side) {
        switch(this) {
            case TOPLEFT:
                if(side == TOPLEFT || side == LEFT || side == TOP) {
                    return true;
                }
                break;
            case TOPRIGHT:
                if(side == TOPRIGHT || side == RIGHT || side == TOP) {
                    return true;
                }
                break;
            case TOP:
                if(side == TOP) {
                    return true;
                }
                break;
            case BOTTOM:
                if(side == BOTTOM) {
                    return true;
                }
                break;
            case LEFT:
                if(side == LEFT) {
                    return true;
                }
                break;
            case RIGHT:
                if(side == RIGHT) {
                    return true;
                }
                break;
            case BOTTOMLEFT:
                if(side == BOTTOMLEFT || side == BOTTOM || side == LEFT) {
                    return true;
                }
                break;
            case BOTTOMRIGHT:
                if(side == BOTTOMRIGHT || side == BOTTOM || side == RIGHT) {
                    return true;
                }
                break;
        }

        return false;
    }
}
