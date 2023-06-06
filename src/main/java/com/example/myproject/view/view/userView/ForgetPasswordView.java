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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDate;

@PageTitle("Forget Password")
@Route("forgotPass")
public class ForgetPasswordView extends VerticalLayout {

    @Autowired
    UserInfoRepository userInfoRepository;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button resetButton;

    public ForgetPasswordView(UserInfoRepository userInfoRepository){
        String username = (String) UI.getCurrent().getSession().getAttribute("username");

        if (username == null) {
            this.userInfoRepository = userInfoRepository;
            initView();
        } else {
            H2 message = new H2();
            message.getStyle()
                    .set("text-align", "center")
                    .set("font-weight", "bold")
                    .set("text-justify", "inter-word");
            message.setText("You are not permitted to access this page as a logged in user!");

            RouterLink dashBoardLink = new RouterLink("Home", MainView.class);
            dashBoardLink.getStyle().set("text-decoration", "none");

            Div div = new Div();
            div.getStyle().set("text-align", "justify");
            div.add(dashBoardLink);

            message.add(div);

            setSizeFull();
            setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            setAlignItems(FlexComponent.Alignment.CENTER);

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

            Notification.show("Password has been reset!").setPosition(Notification.Position.TOP_CENTER);
            UI.getCurrent().navigate(LoginView.class);
        } else {
            Notification.show("Username not found. Please check your input.").setPosition(Notification.Position.TOP_CENTER);
        }
    }

}
