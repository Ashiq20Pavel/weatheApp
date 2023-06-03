package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.repository.UserInfoRepository;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDate;

@Route("forgotPass")
public class ForgetPasswordView extends Div {

    @Autowired
    UserInfoRepository userInfoRepository;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button resetButton;

    public ForgetPasswordView() {
        Header header = new Header(new H2("Weather Application"));
        H3 title = new H3("Forget Password");

        usernameField = new TextField("Username");
        passwordField = new PasswordField("New Password");
        resetButton = new Button("Reset Password", this::resetPassword);

        RouterLink loginLink = new RouterLink("Login", LoginView.class);

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        Div footer = new Div();
        footer.setText("Developed By Md. Ashiqur Rahman");
        footer.getStyle().set("margin", "auto");
        footer.getStyle().set("text-align", "center");

        layout.add(header, title, usernameField, passwordField, resetButton, loginLink);
        layout.setSizeFull();
        add(layout);
        add(footer);
    }

    private void resetPassword(ClickEvent<Button> event) {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        UserInfoEntity userInfoEntity = userInfoRepository.getUserByUsername(username);

        if (userInfoEntity != null) {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());
            String encryptedPassword = bCryptPasswordEncoder.encode(password);

            userInfoEntity.setPassword(encryptedPassword);
            userInfoEntity.setuUsr(username);
            userInfoEntity.setuDt(LocalDate.now());
            userInfoRepository.save(userInfoEntity);

            Notification.show("Password has been reset!");
            UI.getCurrent().navigate(LoginView.class);
        } else {
            Notification.show("Username not found. Please check your input.");
        }
    }

}
