package view;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Car;

@Named
@RequestScoped
public class CarsTableFilter implements Serializable {

    private List<Car> filteredCars;

    public List<Car> getFilteredCars() {
        return filteredCars;
    }

    public void setFilteredCars(List<Car> filteredCars) {
        this.filteredCars = filteredCars;
    }
}
