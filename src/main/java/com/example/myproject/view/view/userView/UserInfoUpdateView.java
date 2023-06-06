package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.repository.UserInfoRepository;
import com.example.myproject.backend.service.UserInfoService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Route("updateinfo")
public class UserInfoUpdateView extends VerticalLayout {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserInfoService userInfoService;

    private TextField userIdField;
    private TextField fullNameField;
    private EmailField emailField;
    private DatePicker birthDateField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField iUsrField;
    private TextField iDtField;
    private TextField roleField;
    private TextField activeField;
    private Button updateButton;

    private Avatar avatar;
    private ContextMenu dropdown;

    public UserInfoUpdateView(UserInfoService userInfoService, UserInfoRepository userInfoRepository) {
        String username = (String) UI.getCurrent().getSession().getAttribute("username");
        if (username != null) {
            this.userInfoService = userInfoService;
            this.userInfoRepository = userInfoRepository;

            init(username);
            updateButton.addClickListener(this::updateEmployeeInfo);
        } else {
            H2 message = new H2();
            message.getStyle()
                    .set("text-align", "center")
                    .set("font-weight", "bold")
                    .set("text-justify", "inter-word");
            message.setText("Please log in to update your account information!");

            RouterLink loginLink = new RouterLink("Login", LoginView.class);
            loginLink.getStyle().set("text-decoration", "none");

            Div div = new Div();
            div.getStyle().set("text-align", "justify");
            div.add(loginLink);

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

    private void init(String username) {

        UserInfoEntity userInfoEntity = userInfoRepository.getUserByUsername(username);

        avatar = new Avatar(username);
        HorizontalLayout avatarLayout = new HorizontalLayout(avatar);
        avatarLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        avatarLayout.setWidthFull();
        avatarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        avatarLayout.add(new Label("Hi! " + userInfoEntity.getFullName()), avatar);

        dropdown = new ContextMenu();
        dropdown.setOpenOnClick(true);
        dropdown.setTarget(avatar);

        dropdown.addItem("Home", e -> {
            home();
        });
        dropdown.addItem("Logout", e -> {
            logout();
        });

        Header header = new Header(new H2("Weather Application"));
        H3 title = new H3("Account Information");


        userIdField = new TextField("User ID");
        userIdField.setValue(String.valueOf(userInfoEntity.getUserId()));
        userIdField.setReadOnly(true);

        usernameField = new TextField("Username");
        usernameField.setValue(userInfoEntity.getUsername());
        usernameField.setReadOnly(true);

        HorizontalLayout row1Layout = new HorizontalLayout(userIdField, usernameField);
        row1Layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        row1Layout.setWidthFull();
        row1Layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        fullNameField = new TextField("Full Name");
        fullNameField.setValue(userInfoEntity.getFullName());

        emailField = new EmailField("Email");
        emailField.setValue(userInfoEntity.getEmail());

        HorizontalLayout row2Layout = new HorizontalLayout(fullNameField, emailField);
        row1Layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        row1Layout.setWidthFull();
        row1Layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate storedDob = LocalDate.parse(userInfoEntity.getDob(), formatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDob = storedDob.format(outputFormatter);

        birthDateField = new DatePicker("Date of Birth");
        Div formatMessage = new Div();
        formatMessage.setText("Format: mm/dd/yyyy");
        formatMessage.getStyle().set("color", "gray");
        birthDateField.setValue(LocalDate.parse(formattedDob, outputFormatter));

        HorizontalLayout row3Layout = new HorizontalLayout(birthDateField);
        row1Layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        row1Layout.setWidthFull();
        row1Layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        passwordField = new PasswordField("Password");
        passwordField.setValue(userInfoEntity.getPassword());
        passwordField.setVisible(false);

        iUsrField = new TextField("Created By");
        iUsrField.setValue(userInfoEntity.getiUsr());
        iUsrField.setVisible(false);

        iDtField = new TextField("Created Date");
        iDtField.setValue(String.valueOf(userInfoEntity.getiDt()));
        iDtField.setVisible(false);

        roleField = new TextField("Role");
        roleField.setValue(userInfoEntity.getRole());
        roleField.setVisible(false);

        activeField = new TextField("Status");
        activeField.setValue(userInfoEntity.getActive());
        activeField.setVisible(false);

        updateButton = new Button("Update");

        RouterLink dashBoardLink = new RouterLink("Home", MainView.class);

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Div footer = new Div();
        footer.setText("Developed By Md. Ashiqur Rahman");
        footer.getStyle().set("margin", "auto");
        footer.getStyle().set("text-align", "center");

        add(avatarLayout);
        layout.add(header, title, row1Layout, row2Layout, row3Layout, formatMessage, updateButton, dashBoardLink);
        layout.setSizeFull();
        add(layout);
        add(footer);
    }

    private void updateEmployeeInfo(ClickEvent<Button> event) {
        UserInfoEntity userInfoEntity = new UserInfoEntity();

        Long userId = Long.valueOf(userIdField.getValue());
        String username = usernameField.getValue();
        String firstName = fullNameField.getValue();
        String lastName = emailField.getValue();
        LocalDate birthDate = birthDateField.getValue();
        String password = passwordField.getValue();
        String iUsr = iUsrField.getValue();
        String iDt = iDtField.getValue();
        String role = roleField.getValue();
        String active = activeField.getValue();

        userInfoEntity.setUserId(userId);
        userInfoEntity.setUsername(username);
        userInfoEntity.setFullName(firstName);
        userInfoEntity.setEmail(lastName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dob = birthDate.format(formatter);
        userInfoEntity.setDob(dob);

        userInfoEntity.setPassword(password);
        userInfoEntity.setiUsr(iUsr);
        userInfoEntity.setiDt(LocalDate.parse(iDt));
        userInfoEntity.setRole(role);
        userInfoEntity.setActive(active);

        userInfoService.updateUser(userInfoEntity);

        Notification.show("Account information updated successfully");
    }

    private void home() {
        UI.getCurrent().navigate(MainView.class);
    }

    private void logout() {
        UI.getCurrent().getSession().setAttribute("username", null);
        UI.getCurrent().navigate(LoginView.class);
    }
}