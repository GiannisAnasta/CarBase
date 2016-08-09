package view;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import model.Car;
import service.CarService;

@Named
@SessionScoped
public class AddCarBean implements Serializable {

    @Inject
    private CarService service;

    private String model;
    private int year;
    private String manufacturer;
    private String color;
    private double price;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void addAction() {
        Car ordercar = new Car(this.model, this.year, this.manufacturer, this.color, this.price);
        service.addCar(ordercar);
        flush();
    }

    private void flush() {
        year = 0;
        model = "";
        color = "";
        price = 0.0;
    }
}
