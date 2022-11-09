package Models;

import java.util.List;

public class Category {
    private String name;
    private List<Category> subCategories;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, List<Category> subCategories) {
        this.name = name;
        this.subCategories = subCategories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void addSubCategory(Category category) {
        this.subCategories.add(category);
    }

}
