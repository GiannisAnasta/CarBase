package service;

import java.io.Serializable;
import java.util.ArrayList;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.Car;

@Named
@SessionScoped
public class CarService implements Serializable {

    private static final ArrayList<Car> entities = new ArrayList<>();

    public ArrayList<Car> getList() {
        return entities;
    }

    public void addCar(Car car) {
        entities.add(car);
    }

    public void removeCar(Car car) {
        entities.remove(car);
    }

}
