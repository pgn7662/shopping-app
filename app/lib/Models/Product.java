package Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import app.helpers.Pair;

public class Product {
    protected final UUID id;
    protected String name, info;
    protected double mrp;
    protected int discountPercent;
    private int quantity;
    protected Category category;
    private List<Pair<ValueTag<String>, String>> valueTags;
    protected List<DetailTag> detailTags;

    public Product(UUID uuid, String name, String info, double mrp, int discountPercent, int quantity,
            Category category) {
        this.id = uuid;
        this.name = name;
        this.mrp = mrp;
        this.discountPercent = discountPercent;
        this.quantity = quantity;
        this.category = category;
        this.valueTags = new LinkedList<>();
        this.detailTags = new LinkedList<>();
    }

    public Product(String name, String info, double mrp, int discountPercent, int quantity, Category category) {
        this(UUID.randomUUID(), name, info, mrp, discountPercent, quantity, category);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getCurrentPrice() {
        return mrp - mrp * discountPercent / 100;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void addDetailTag(DetailTag detailTag) {
        detailTags.add(detailTag);
    }

    public List<DetailTag> getDetailTags() {
        return detailTags;
    }

    public void setValuesForDetailTag(String detailTagName, List<String> values) {
        getDetailTag(this.detailTags, detailTagName).setValues(values);
    }

    private DetailTag getDetailTag(List<DetailTag> detailTags, String detailTagName) {
        for (DetailTag detailTag : detailTags) {
            if (detailTag.getName().equals(detailTagName))
                return detailTag;
            DetailTag detailTag2 = getDetailTag(detailTag.getSubDetailTags(), detailTagName);
            if (detailTag2 != null)
                return detailTag2;
        }
        return null;
    }

    public List<Pair<ValueTag<String>, String>> getValueTags() {
        return valueTags;
    }

    public void setValuesForValueTag(String valueTagName, List<String> values) {
        for (Pair<ValueTag<String>, String> pair : this.valueTags) {
            if (pair.getFirst().getName().equals(valueTagName)) {
                pair.getFirst().setValues(values);
                for (String val : values) {
                    if (val.equals(pair.getSecond()))
                        return;
                }
                pair.setSecond(values.get(0));
            }
        }
    }

    public String getValueForValueTag(ValueTag<String> valueTag) {
        for (Pair<ValueTag<String>, String> pair : valueTags)
            if (valueTag.getName().equals(pair.getFirst().getName()))
                return pair.getSecond();
        return null;
    }

    public void setValueForValueTag(ValueTag<String> valueTag, String value) {
        for (Pair<ValueTag<String>, String> pair : valueTags) {
            if (valueTag.getName().equals(pair.getFirst().getName())) {
                pair.setSecond(value);
            }
        }
    }

    public void addValueTag(ValueTag<String> valueTag) {
        valueTags.add(new Pair<>(valueTag, valueTag.getValues().get(0)));
    }

    public void changeDetailedTagsOrder(String[] names) {
        List<DetailTag> newDetailTags = new ArrayList<>(names.length);
        for (String name : names)
            for (DetailTag detailTag : this.detailTags)
                if (detailTag.getName().equals(name))
                    newDetailTags.add(detailTag);
        this.detailTags.clear();
        this.detailTags.addAll(newDetailTags);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Product && ((Product) object).id.equals(this.id);
    }

}