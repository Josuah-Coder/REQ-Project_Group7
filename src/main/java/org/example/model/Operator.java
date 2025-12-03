package org.example.model;

public class Operator {
    private String id;
    private String name;
    private String email;
    private String role;

    public Operator(String email, String name) {
        this.id = "OP-" + System.currentTimeMillis();
        this.email = email;
        this.name = name;
        this.role = "Operator";
    }

    public Operator withRole(String role) {
        this.role = role;
        return this;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return String.format("Operator{id='%s', name='%s', email='%s', role='%s'}",
                id, name, email, role);
    }
}