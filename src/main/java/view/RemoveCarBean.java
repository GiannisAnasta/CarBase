package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Car;
import service.CarService;

@Named
@SessionScoped
public class RemoveCarBean implements Serializable {

    @Inject
    private CarService service;

    public void remove(Car car) {
        try {
            service.removeCar(car);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
