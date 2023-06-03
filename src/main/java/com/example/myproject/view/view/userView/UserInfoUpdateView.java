package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.service.UserInfoService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.Optional;

@Route("updateinfo")
public class UserInfoUpdateView extends VerticalLayout {

    private UserInfoService userInfoService;
    private Optional<UserInfoEntity> loggedInUser;

    private TextField fullNameField;
    private EmailField emailField;
    private TextField birthDateField;
    private TextField usernameField;
    private Button updateButton;

    public UserInfoUpdateView(UserInfoService userInfoService, Optional<UserInfoEntity> loggedInUser) {
        this.userInfoService = userInfoService;
        this.loggedInUser = loggedInUser;

        initComponents();
        updateButton.addClickListener(this::updateEmployeeInfo);
    }

    private void initComponents() {
        // Initialize and configure UI components
        fullNameField = new TextField("Full Name", loggedInUser.get().getFullName());
        emailField = new EmailField("Email", loggedInUser.get().getEmail());
        birthDateField = new TextField("Date of Birth", loggedInUser.get().getDob());
        usernameField = new TextField("Username", loggedInUser.get().getUsername());
        updateButton = new Button("Update");

        // Add components to the layout
        add(usernameField, fullNameField, emailField, birthDateField, birthDateField, updateButton);
    }

    private void updateEmployeeInfo(ClickEvent<Button> event) {
        // Get the updated values from the form fields
        String username = usernameField.getValue();
        String firstName = fullNameField.getValue();
        String lastName = emailField.getValue();
        String birthDate = birthDateField.getValue();

        // Update the employee information
        loggedInUser.get().setUsername(username);
        loggedInUser.get().setFullName(firstName);
        loggedInUser.get().setEmail(lastName);
        loggedInUser.get().setDob(birthDate);

        // Save the updated employee information
        userInfoService.updateUser(loggedInUser.get());

        // Show a success message or navigate to a different view
        Notification.show("Employee information updated successfully");
    }
}
