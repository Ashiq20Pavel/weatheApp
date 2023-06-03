package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.service.UserInfoService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
class RegistrationView extends VerticalLayout {

    @Autowired
    UserInfoService userInfoService;

    private TextField fullNameField;
    private EmailField emailField;
    private TextField dobField;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button registerButton;

    public RegistrationView(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
        initView();
    }

    private void initView() {
        Header header = new Header(new H2("Weather Application"));
        H3 title = new H3("Signup");

        fullNameField = new TextField("Full Name");
        emailField = new EmailField("Email");
        dobField = new TextField("Date of Birth");
        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        registerButton = new Button("Register", this::register);

        RouterLink loginLink = new RouterLink("Already Have an account? Login", LoginView.class);

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Div footer = new Div();
        footer.setText("Developed By Md. Ashiqur Rahman");
        footer.getStyle().set("margin", "auto");
        footer.getStyle().set("text-align", "center");

        layout.add(header, title, fullNameField, emailField, dobField, usernameField, passwordField, registerButton, loginLink);
        layout.setSizeFull();
        add(layout);
        add(footer);

//        add(fullNameField, emailField, dobField, usernameField, passwordField, registerButton);
    }

    private void register(ClickEvent<Button> event) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setFullName(fullNameField.getValue());
        userInfoEntity.setEmail(emailField.getValue());
        userInfoEntity.setDob(dobField.getValue());
        userInfoEntity.setUsername(usernameField.getValue());
        userInfoEntity.setPassword(passwordField.getValue());

        userInfoService.addUser(userInfoEntity);

        Notification.show("Registration successful!");
        clearFields();
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
}
