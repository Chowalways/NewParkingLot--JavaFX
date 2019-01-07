package Unit;

import Abstract.GameObject;
import CheckSystem.CheckInSystem;
import CheckSystem.Other.Check;
import CheckSystem.PersonCheck;
import Unit.Enum.Direction;
import Unit.Enum.Side;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Office extends GameObject {

    private ArrayList<Room> officeRooms = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Door> door = new ArrayList<>();
    private ArrayList<PunchMachine> punchCardMachines = new ArrayList<>();
    private CheckInSystem checkInSystem;
    private Pane pane;

    public Office(Pane pane) {
        super(pane);
        this.pane = pane;

        Check check = new PersonCheck(0);
        checkInSystem = new CheckInSystem(check);
    }

    //OfficeArea
    public void generateOffice(){
        System.out.println("Office");
        int OfficePadding = 20;
        int x = OfficePadding;
        int y = OfficePadding;

        // Wall Size
        int wallPadding = OfficePadding - 10;
        int roomWidth = x + Room.WIDTH + 200;
        int roomHeight = y + 200;

        // Generate TOP Wall
        for (int i = 0; i * Wall.LONG < roomWidth; i ++ ) {
            spawnHorizontalWall(i * Wall.LONG + wallPadding, 0 + wallPadding);
        }

        // Generate Bottom Wall
        for (int i = 0; i * Wall.LONG < roomWidth; i ++ ) {
            spawnHorizontalWall(i * Wall.LONG + wallPadding, roomHeight + wallPadding);
        }

        // Generate LEFT Wall
        for (int i = 0; i * Wall.LONG < roomHeight; i ++ ) {
            spawnVerticalWall(0 + wallPadding, i * Wall.LONG + wallPadding);
        }

        // Generate RIGHT Wall
        for (int i = 0; i * Wall.LONG < roomHeight; i ++ ) {
            spawnVerticalWall(roomWidth + wallPadding + 10, i * Wall.LONG + wallPadding);
        }

        spawnHorizontalDoor(55, roomHeight + wallPadding - 10);

        for (Wall wall : walls) {
            for (Door door : this.door) {
                if(door.isColliding(wall) == Side.INSIDE) {
                    wall.setAlive(false);
                    pane.getChildren().remove(wall.getView());
                }
            }
        }
        walls.removeIf(GameObject::isDead);
    }

    public ArrayList<Wall> getWalls() {
        return (ArrayList<Wall>) walls.clone();
    }

    void spawnHorizontalWall(int x, int y) {
        Wall wall = Wall.creteWall(Direction.HORIZONTAL);
        addWall(wall, x, y);
    }

    void spawnVerticalWall(int x, int y) {
        Wall wall = Wall.creteWall(Direction.VERTICAL);
        addWall(wall, x, y);
    }

    private void addWall(Wall wall, int x, int y) {
        walls.add(wall);
        addGameObject(wall, x, y);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());
    }

    public ArrayList<Room> getRoom() {
        return (ArrayList<Room>) officeRooms.clone();
    }

    public ArrayList<Door> getDoor() {
        return (ArrayList<Door>) door.clone();
    }

    public ArrayList<PunchMachine> getPunchCardMachines() {
        return this.punchCardMachines;
    }

    void spawnHorizontalDoor(int x, int y) {
        Door door = Door.createDoor(Direction.HORIZONTAL);
        door.connectSystem(checkInSystem);
        addGate(door, x, y);
    }

    void spawnVerticalDoor(int x, int y) {
        Door door = Door.createDoor(Direction.VERTICAL);
        door.connectSystem(checkInSystem);
        addGate(door, x, y);
    }

    private void addGate(Door door, int x, int y) {
        this.door.add(door);
        addGameObject(door, x, y);
    }

    public Door checkPersonNearbyDoor(PersonObject person) {
        for (Door door : this.door) {
            if(door.isColliding(person) != Side.NONE) {
                return door;
            }
        }
        return null;
    }


}
