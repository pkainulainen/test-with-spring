package com.testwithspring.intermediate.example;

public class FormDTO {

    private boolean checkbox;
    private String message;
    private Integer number;
    private boolean radioButton;

    public FormDTO() {
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public String getMessage() {
        return message;
    }

    public Integer getNumber() {
        return number;
    }

    public boolean isRadioButton() {
        return radioButton;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setRadioButton(boolean radioButton) {
        this.radioButton = radioButton;
    }
}
