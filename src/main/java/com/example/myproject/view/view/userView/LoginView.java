package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.repository.UserInfoRepository;
import com.example.myproject.backend.service.UserInfoService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Optional;

@Route("login")
public class LoginView extends VerticalLayout {

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserInfoRepository userInfoRepository;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;

    public LoginView(UserInfoService userInfoService, UserInfoRepository userInfoRepository) {
        this.userInfoService = userInfoService;
        this.userInfoRepository = userInfoRepository;
        initView();
    }

    private void initView() {
        Header header = new Header(new H2("Weather Application"));
        H3 title = new H3("Login");

        usernameField = new TextField("Username");
        passwordField = new PasswordField("Password");
        loginButton = new Button("Login", this::login);

        RouterLink registerLink = new RouterLink("Create Account", RegistrationView.class);
        RouterLink forgetPasswordLink = new RouterLink("Forget Password", ForgetPasswordView.class);

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Div footer = new Div();
        footer.setText("Developed By Md. Ashiqur Rahman");
        footer.getStyle().set("margin", "auto");
        footer.getStyle().set("text-align", "center");

        layout.add(header, title, usernameField, passwordField, loginButton, forgetPasswordLink, registerLink);
        layout.setSizeFull();
        add(layout);
        add(footer);
    }

    private void login(ClickEvent<Button> event) {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        Optional<UserInfoEntity> userInfoEntity = userInfoRepository.getUserByUsernameLogin(username);
        if (userInfoEntity.isPresent()) {
            UserInfoEntity userInfoEntity1 = userInfoEntity.get();

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10, new SecureRandom());

            if (bCryptPasswordEncoder.matches(password, userInfoEntity1.getPassword())) {
                UI.getCurrent().getSession().setAttribute("username", username);
                UI.getCurrent().navigate(MainView.class);
            } else {
                Notification.show("Invalid username or password");
            }
        } else {
            Notification.show("Invalid username or password");
        }
        clearFields();
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
}
