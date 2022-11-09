package Models;

import java.util.LinkedList;
import java.util.List;

import app.helpers.Pair;

public final class CartProduct extends Product {
    private int quantity;
    private List<Pair<ValueTag<String>, String>> valueTags;

    public CartProduct(Product product) {
        super(product.getId(), product.getName(), product.getInfo(), product.getMrp(), product.getDiscountPercent(),
                product.getQuantity(), product.getCategory());
        this.quantity = product.getQuantity();
        this.valueTags = new LinkedList<>();
        for (Pair<ValueTag<String>, String> pair : product.getValueTags())
            this.valueTags.add(new Pair<>(pair.getFirst(), pair.getSecond()));
    }

    public CartProduct(Product product, int quantity, List<Pair<String, String>> chosenValues) {
        super(product.getId(), product.getName(), product.getInfo(), product.getMrp(), product.getDiscountPercent(),
                product.getQuantity(), product.getCategory());
        this.quantity = Math.min(product.getQuantity(), quantity);
        this.valueTags = new LinkedList<>();
        for (Pair<ValueTag<String>, String> pTag : product.getValueTags()) {
            for (Pair<String, String> vTag : chosenValues) {
                if (vTag.getFirst().equals(pTag.getFirst().getName())) {
                    this.valueTags.add(new Pair<>(pTag.getFirst(), pTag.getFirst().isValuePresent(vTag.getSecond())
                            ? vTag.getSecond()
                            : pTag.getSecond()));
                    break;
                }
            }
        }
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = Math.min(quantity, super.getQuantity());
    }

    @Override
    public double getCurrentPrice() {
        return super.getCurrentPrice() * this.quantity;
    }

    @Override
    public List<Pair<ValueTag<String>, String>> getValueTags() {
        return this.valueTags;
    }

    @Override
    public String getValueForValueTag(ValueTag<String> valueTag) {
        for (Pair<ValueTag<String>, String> pair : this.valueTags)
            if (pair.getFirst().getName().equals(valueTag.getName()))
                return pair.getSecond();
        return null;
    }

    @Override
    public void setValueForValueTag(ValueTag<String> valueTag, String value) {
        for (Pair<ValueTag<String>, String> pair : this.valueTags)
            if (pair.getFirst().getName().equals(valueTag.getName())) {
                pair.setSecond(value);
                break;
            }
    }

}
