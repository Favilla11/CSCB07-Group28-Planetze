package review;

public class Activity {
	private String date;
    private String description;
    private String category;
    private String subCategory;
    private double emission;

    public Activity(String date, String description, String category, String subCategory, double emission) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.emission = emission;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    public String getDate() {
        return date;
    }
    public String getSubCategory(){
        return  subCategory;
    }
    public String getDescription() {
        return description;
    }
    public String getCategory(){
        return category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
