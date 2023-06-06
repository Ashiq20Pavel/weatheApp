package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.service.UserInfoService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

@PageTitle("Registration")
@Route("register")
class RegistrationView extends VerticalLayout {

    @Autowired
    UserInfoService userInfoService;

    private TextField fullNameField;
    private EmailField emailField;
    private DatePicker dobField;
    private TextField usernameField;
    private PasswordField passwordField;

    public RegistrationView(UserInfoService userInfoService) {

        String username = (String) UI.getCurrent().getSession().getAttribute("username");
        if (username == null) {
            this.userInfoService = userInfoService;
            initView();
        } else {
            H2 message = new H2();
            message.getStyle()
                    .set("text-align", "center")
                    .set("font-weight", "bold")
                    .set("text-justify", "inter-word");
            message.setText("You are already logged in as a registered user!");

            RouterLink dashBoardLink = new RouterLink("Home", MainView.class);
            dashBoardLink.getStyle().set("text-decoration", "none");

            Div div = new Div();
            div.getStyle().set("text-align", "justify");
            div.add(dashBoardLink);

            message.add(div);

            setSizeFull();
            setJustifyContentMode(JustifyContentMode.CENTER);
            setAlignItems(Alignment.CENTER);

            Div container = new Div(message);
            container.getStyle()
                    .set("display", "flex")
                    .set("flex-direction", "column")
                    .set("height", "100%")
                    .set("justify-content", "center")
                    .set("align-items", "center");

            add(container);
        }
    }

    private void initView() {
        Header header = new Header(new H2("Weather Application"));
        H3 title = new H3("Signup");

        fullNameField = new TextField("Full Name");
        emailField = new EmailField("Email");

        HorizontalLayout row1Layout = new HorizontalLayout(fullNameField, emailField);
        row1Layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        row1Layout.setWidthFull();
        row1Layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        dobField = new DatePicker("Date of Birth");
        Div formatMessage = new Div();
        formatMessage.setText("Format: mm/dd/yyyy");
        formatMessage.getStyle().set("color", "gray");

        HorizontalLayout row2Layout = new HorizontalLayout(dobField);
        row2Layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        row2Layout.setWidthFull();
        row2Layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");

        HorizontalLayout row3Layout = new HorizontalLayout(usernameField, passwordField);
        row3Layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        row3Layout.setWidthFull();
        row3Layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button registerButton = new Button("Register", this::register);

        RouterLink loginLink = new RouterLink("Already Have an account? Login", LoginView.class);

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Div footer = new Div();
        footer.setText("Developed By Md. Ashiqur Rahman");
        footer.getStyle().set("margin", "auto");
        footer.getStyle().set("text-align", "center");

        layout.add(header, title, row1Layout, row2Layout, formatMessage,
                row3Layout, registerButton, loginLink);
        layout.setSizeFull();
        add(layout);
        add(footer);
    }

    private void register(ClickEvent<Button> event) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setFullName(fullNameField.getValue());
        userInfoEntity.setEmail(emailField.getValue());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dob = dobField.getValue().format(formatter);
        userInfoEntity.setDob(dob);

        userInfoEntity.setUsername(usernameField.getValue());
        userInfoEntity.setPassword(passwordField.getValue());

        userInfoService.addUser(userInfoEntity);

        Notification.show("Registration successful! Please Login").setPosition(Notification.Position.TOP_CENTER);
        UI.getCurrent().navigate(LoginView.class);
        clearFields();
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
}

