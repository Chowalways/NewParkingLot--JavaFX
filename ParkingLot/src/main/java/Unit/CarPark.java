package Unit;

import Unit.Enum.Direction;
import javafx.scene.layout.Pane;
import sample.GameObject;
import sample.Side;

import java.util.ArrayList;

public class CarPark extends GameObject {

    private ArrayList<ParkingLot> parkingLots = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Gate> gates = new ArrayList<>();
    private ArrayList<PaymentMachine> machines = new ArrayList<>();

    private Pane pane;
    public final int TotalParkingLot = 20;


    public CarPark(Pane pane) {
        super(pane);
        this.pane = pane;
    }

    public void generateParkingLot() {

        System.out.println("Spawn Car Park");
        int ParkingLotPadding = 20,
                x = ParkingLotPadding,
                y = ParkingLotPadding,
                carPerColumn = 5,
                xSpacing = 50,
                ySpacing = 10;

        // Generate Parking Lot
        for (int i = 0; i < TotalParkingLot; i++) {
            if (i != 0 && i % carPerColumn == 0) {
                y = ParkingLotPadding;
                x += ParkingLot.WIDTH + xSpacing;
            }
            spawnParkingLot(x, y);
            y += ParkingLot.HEIGHT + ySpacing;
        }

        // Wall Size
        int wallPadding = ParkingLotPadding - 10,
                parkingLotWidth = x + ParkingLot.WIDTH + 60,
                parkingLotHeight = y + 100;

        // Generate TOP Wall
        for (int i = 0; i * Wall.LONG < parkingLotWidth; i ++ ) {
            spawnHorizontalWall(i * Wall.LONG + wallPadding, 0 + wallPadding);
        }

        // Generate Bottom Wall
        for (int i = 0; i * Wall.LONG < parkingLotWidth; i ++ ) {
            spawnHorizontalWall(i * Wall.LONG + wallPadding, parkingLotHeight + wallPadding);
        }

        // Generate LEFT Wall
        for (int i = 0; i * Wall.LONG < parkingLotHeight; i ++ ) {
            spawnVerticalWall(0 + wallPadding, i * Wall.LONG + wallPadding);
        }

        // Generate RIGHT Wall
        for (int i = 0; i * Wall.LONG < parkingLotHeight; i ++ ) {
            spawnVerticalWall(parkingLotWidth + wallPadding + 10, i * Wall.LONG + wallPadding);
        }

        // generate Gate
        spawnHorizontalGate(55, parkingLotHeight + wallPadding - 10);

        // spawn Payment Machine
        spawnPaymentMachine(55, parkingLotHeight - 100 );

        // remove wall when collided with gate
        for (Wall wall : walls) {
            for (Gate gate : gates) {
                if(gate.isColliding(wall) == Side.INSIDE) {
                    wall.setAlive(false);
                    pane.getChildren().remove(wall.getView());
                }
            }
        }
        walls.removeIf(GameObject::isDead);
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return (ArrayList<ParkingLot>) parkingLots.clone();
    }

    public ArrayList<Wall> getWalls() {
        return (ArrayList<Wall>) walls.clone();
    }

    public ArrayList<Gate> getGates() {
        return (ArrayList<Gate>) gates.clone();
    }

    void spawnParkingLot(int x, int y) {
        ParkingLot parkingLot = ParkingLot.createCarPark();
        addParkingLot(parkingLot, x, y);
    }

    private void addParkingLot(ParkingLot object, double x, double y) {
        parkingLots.add(object);
        addGameObject(object, x, y);
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

    void spawnHorizontalGate(int x, int y) {
        Gate gate = Gate.createGate(Direction.HORIZONTAL);
        addGate(gate, x, y);
    }

    void spawnVerticalGate(int x, int y) {
        Gate gate = Gate.createGate(Direction.VERTICAL);
        addGate(gate, x, y);
    }

    private void addGate(Gate gate, int x, int y) {
        gates.add(gate);
        addGameObject(gate, x, y);
    }

    void spawnPaymentMachine(int x, int y) {
        PaymentMachine paymentMachine = PaymentMachine.create(Direction.HORIZONTAL);
        machines.add(paymentMachine);
        addGameObject(paymentMachine, x, y);
    }

    // add Game Object to main pane
    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());
    }

}
