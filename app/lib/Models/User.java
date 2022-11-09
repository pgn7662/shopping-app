package Models;

public abstract class User {
    private String name;
    private String email;
    private String password;
    private Long phone;
    private Address address;

    protected User(String name, String email, String password, long phone, Address address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phone = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

}
