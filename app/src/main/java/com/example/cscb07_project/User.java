import com.example.cscb07_project.Information;

import java.util.Map;

public class User {
    private String userName;
    private String email;
    private String password;
    //    private List<Habit> habitList;
    private double totalFootprint, transportationFootprint, foodFootprint, houseFootprint, consumptionFootprint;
    private Map<String, Information> userInformation;

    public User(){}
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getTotalFootprint() {
        return totalFootprint;
    }

    public void setTotalFootprint(double totalFootprint) {
        this.totalFootprint = totalFootprint;
    }

    public double getTransportationFootprint() {
        return transportationFootprint;
    }

    public void setTransportationFootprint(double transportationFootprint) {
        this.transportationFootprint = transportationFootprint;
    }

    public double getConsumptionFootprint() {
        return consumptionFootprint;
    }

    public void setConsumptionFootprint(double consumptionFootprint) {
        this.consumptionFootprint = consumptionFootprint;
    }

    public double getFoodFootprint() {
        return foodFootprint;
    }

    public void setFoodFootprint(double foodFootprint) {
        this.foodFootprint = foodFootprint;
    }

    public double getHouseFootprint() {
        return houseFootprint;
    }

    public void setHouseFootprint(double houseFootprint) {
        this.houseFootprint = houseFootprint;
    }

    public Map<String, Information> getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(Map<String, Information> userInformation) {
        this.userInformation = userInformation;
    }

//    public List<Habit> getHabitList() {
//        return habitList;
//    }
//
//    public void setHabitList(List<Habit> habitList) {
//        this.habitList = habitList;
//    }
}