package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.dto.WeatherDTO;
import com.example.myproject.backend.domain.entity.CityInfoEntity;
import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.repository.CityInfoRepository;
import com.example.myproject.backend.repository.UserInfoRepository;
import com.example.myproject.backend.repository.WeatherServiceRepository;
import com.example.myproject.backend.service.CityService;
import com.example.myproject.backend.util.WeatherInfoService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.GeneratedVaadinContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.server.ThemeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@Route("dashboard")
public class MainView extends VerticalLayout {

    @Autowired
    CityInfoRepository cityInfoRepository;

    @Autowired
    CityService cityService;

    @Autowired
    WeatherInfoService weatherInfoService;

    @Autowired
    WeatherServiceRepository weatherServiceRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    private Grid<CityInfoEntity> cityInfoEntityGrid;

    private TextField searchField;
    private Button searchButton;
    private Button clearButton;

    private Button previousButton;
    private Button nextButton;

    private Integer currentPage = 0;
    private Integer pageSize = 10;

    private String currentSearchQuery;

    private Avatar avatar;
    private ContextMenu dropdown;


    public MainView(CityInfoRepository cityInfoRepository) {
        this.cityInfoRepository = cityInfoRepository;

        initView();
        populateList();
    }

    private void initView() {
        searchField = new TextField();
        searchField.setPlaceholder("Search...");
        searchButton = new Button("Search", event -> searchUser());
        clearButton = new Button("Clear", event -> clearSearch());

        avatar = new Avatar("User");
        HorizontalLayout avatarLayout = new HorizontalLayout();
        avatarLayout.setJustifyContentMode(JustifyContentMode.END); // Align component to the right
        avatarLayout.add(avatar);

        dropdown = new ContextMenu();
        dropdown.setOpenOnClick(true);
        dropdown.setTarget(avatar);

        dropdown.addItem("Profile", e -> {
            /*updateUserInformation();*/ });
        dropdown.addItem("Logout", e -> { logout(); });

        cityInfoEntityGrid = new Grid<>(CityInfoEntity.class);

        cityInfoEntityGrid.setColumns("city", "country");

        cityInfoEntityGrid.addComponentColumn(cityInfoEntity -> {
            Div weatherInfoDiv = createWeatherInfo(cityInfoEntity);
            return weatherInfoDiv;
        }).setHeader("Weather Current Info");

        cityInfoEntityGrid.addComponentColumn(this::createViewButton).setHeader("Actions");

        cityInfoEntityGrid.setPageSize(10);
        cityInfoEntityGrid.setHeight("550px");

        previousButton = new Button("Previous", event -> navigateToPage(currentPage - 1));
        nextButton = new Button("Next", event -> navigateToPage(currentPage + 1));

        HorizontalLayout buttonLayout = new HorizontalLayout(previousButton, nextButton);
        buttonLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);


        Div footer = new Div();
        footer.setText("Developed By Md. Ashiqur Rahman");
        footer.getStyle().set("margin", "auto");
        footer.getStyle().set("text-align", "center");

        add(new HorizontalLayout(avatarLayout, searchField, searchButton, clearButton), cityInfoEntityGrid, buttonLayout, footer);
    }

    private void populateList() {
        Page<CityInfoEntity> cityInfoEntityPage;
        if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            cityInfoEntityPage = cityInfoRepository.findByCityContainingIgnoreCase(currentSearchQuery, PageRequest.of(currentPage, pageSize));
        } else {
            cityInfoEntityPage = cityInfoRepository.findAll(PageRequest.of(currentPage, pageSize));
        }
        List<CityInfoEntity> cityInfoEntityList = cityInfoEntityPage.getContent();
        cityInfoEntityGrid.setItems(cityInfoEntityList);
        previousButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(cityInfoEntityPage.hasNext());
    }

    private Div createWeatherInfo(CityInfoEntity cityInfoEntity) {
        Long cityId = cityInfoEntity.getId();
        WeatherDTO weather = weatherInfoService.getWeatherByCity(cityId);

        Div weatherInfoDiv = new Div();
        weatherInfoDiv.setClassName("weather-info");

        /*if (weather != null) {
            String weatherStatus = weatherServiceRepository.findDesByCode(weather.getWeatherStatus());
            String weatherInfo = "Temperature: " + weather.getTemperature() + "°C" + "<br>"
                    + "Wind Speed: " + weather.getWindSpeed() + "km/h" + "<br>"
                    + "Weather Status: " + weatherStatus;
            weatherInfoDiv.getElement().setProperty("innerHTML", weatherInfo);
        } else {
            weatherInfoDiv.setText("No weather information");
        }*/

        if (weather != null) {
            String weatherStatus = weatherServiceRepository.findDesByCode(weather.getWeatherStatus());
            String[] statusLines = weatherStatus.split(",");
            StringBuilder weatherInfoBuilder = new StringBuilder();
            weatherInfoBuilder.append("<b>Temperature:</b> ").append(weather.getTemperature()).append("°C").append("<br>")
                    .append("<b>Wind Speed:</b> ").append(weather.getWindSpeed()).append("km/h").append("<br>")
                    .append("<b>Weather Status:</b> ");
            for (String statusLine : statusLines) {
                weatherInfoBuilder.append(statusLine.trim()).append("<br>");
            }
            String weatherInfo = weatherInfoBuilder.toString();
            weatherInfoDiv.getElement().setProperty("innerHTML", weatherInfo);
        } else {
            weatherInfoDiv.setText("No weather information");
        }

        return weatherInfoDiv;
    }

    private void searchUser() {
        currentSearchQuery = searchField.getValue().trim();
        navigateToPage(0);
    }

    private void clearSearch() {
        searchField.clear();
        currentSearchQuery = null;
        navigateToPage(0);
    }

    private void navigateToPage(Integer page) {
        currentPage = page;
        populateList();
    }

    private Button createViewButton(CityInfoEntity cityInfoEntity) {
        Button viewButton = new Button("View Hourly Info");
        viewButton.addClickListener(e -> {
            showDetails(cityInfoEntity);
        });
        return viewButton;
    }

    private void showDetails(CityInfoEntity cityInfoEntity) {
        Dialog dialog = new Dialog();
        dialog.setWidth("1000px");
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.add(
                new H2("City Hourly Weather Details"),
                new Paragraph("City: " + cityInfoEntity.getCity()),
                new Paragraph("Country: " + cityInfoEntity.getCountry()),
                new Paragraph("Latitude: " + cityInfoEntity.getLat()),
                new Paragraph("Longitude: " + cityInfoEntity.getLng())
        );
        content.add(new H3("Hourly Weather"));
        content.add(new H5("Units => Time Zone: GMT, Temperature: °C, Rain: mm, Wind Speed: km/h"));
        createWeatherInfoHourly(cityInfoEntity, content);
        dialog.add(content);
        dialog.open();
    }

    private void createWeatherInfoHourly(CityInfoEntity cityInfoEntity, VerticalLayout layout) {
        Long cityId = cityInfoEntity.getId();
        ArrayList<WeatherDTO> weather = weatherInfoService.getWeatherHourlyByCity(cityId);

        if (weather != null && !weather.isEmpty()) {
            Grid<WeatherDTO> grid = new Grid<>(WeatherDTO.class);
            grid.setItems(weather);
            grid.setColumns("time", "temperature", "rain", "windSpeed");
            layout.add(grid);
        } else {
            layout.add("No weather information");
        }
    }

    private void updateUserInformation() {
        UI.getCurrent().navigate(UserInfoUpdateView.class);
    }

    private void logout() {
        UI.getCurrent().navigate(LoginView.class);
    }
}
