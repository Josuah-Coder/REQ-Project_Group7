package org.example.manager;

import org.example.model.Operator;

public class OperatorManager {
    private static OperatorManager instance;
    private Operator currentOperator;

    private OperatorManager() {
        currentOperator = new Operator("admin@energy-sample.de", "Admin User");
    }

    public static synchronized OperatorManager getInstance() {
        if (instance == null) {
            instance = new OperatorManager();
        }
        return instance;
    }

    public Operator getCurrentOperator() {
        return currentOperator;
    }

    public void setCurrentOperator(Operator operator) {
        this.currentOperator = operator;
    }

    public Operator login(String email, String password) {
        if ("admin@energy-sample.de".equals(email)) {
            currentOperator = new Operator(email, "Administrator");
            return currentOperator;
        }
        return null;
    }
}
