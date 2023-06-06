package com.example.myproject.view.view.userView;

import com.example.myproject.backend.domain.dto.WeatherDTO;
import com.example.myproject.backend.domain.entity.CityInfoEntity;
import com.example.myproject.backend.domain.entity.UserFavCityEntity;
import com.example.myproject.backend.domain.entity.UserInfoEntity;
import com.example.myproject.backend.repository.CityInfoRepository;
import com.example.myproject.backend.repository.UserFavCityRepository;
import com.example.myproject.backend.repository.UserInfoRepository;
import com.example.myproject.backend.repository.WeatherServiceRepository;
import com.example.myproject.backend.service.CityService;
import com.example.myproject.backend.util.WeatherInfoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    UserFavCityRepository userFavCityRepository;

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


    public MainView(CityInfoRepository cityInfoRepository, UserInfoRepository userInfoRepository, UserFavCityRepository userFavCityRepository) {

        String username = (String) UI.getCurrent().getSession().getAttribute("username");
        if (username != null) {
            this.cityInfoRepository = cityInfoRepository;
            this.userInfoRepository = userInfoRepository;
            this.userFavCityRepository = userFavCityRepository;
            initView(username);
            populateList();
        } else {
            H2 message = new H2();
            message.getStyle()
                    .set("text-align", "center")
                    .set("font-weight", "bold")
                    .set("text-justify", "inter-word");
            message.setText("Please log in to access the Main View.");

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

    private void initView(String username) {

        UserInfoEntity userInfoEntity = userInfoRepository.getUserByUsername(username);

        searchField = new TextField();
        searchField.setPlaceholder("Search...");
        searchButton = new Button("Search", event -> searchUser());
        clearButton = new Button("Clear", event -> clearSearch());

        ComboBox<String> filterComboBox = new ComboBox<>("Filter");
        filterComboBox.setItems("All", "Favorites");
        filterComboBox.setValue("All");
        filterComboBox.addValueChangeListener(event -> {
            String selectedFilter = event.getValue();
            if (selectedFilter.equals("Favorites")) {
                populateFavList(username);
            } else {
                populateList();

            }
        });


        HorizontalLayout searchLayout = new HorizontalLayout(filterComboBox, searchField, searchButton, clearButton);
        searchLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        searchLayout.setWidthFull();
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        avatar = new Avatar(username);
        HorizontalLayout avatarLayout = new HorizontalLayout(avatar);
        avatarLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        avatarLayout.setWidthFull();
        avatarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        avatarLayout.add(new Label("Hi! " + userInfoEntity.getFullName()), avatar);

        dropdown = new ContextMenu();
        dropdown.setOpenOnClick(true);
        dropdown.setTarget(avatar);

        dropdown.addItem("Profile", e -> {
            updateUserInformation();
        });
        dropdown.addItem("Logout", e -> {
            logout();
        });

        cityInfoEntityGrid = new Grid<>(CityInfoEntity.class);
        cityInfoEntityGrid.setColumns("city", "country");
        cityInfoEntityGrid.addComponentColumn(cityInfoEntity -> {
            Div weatherInfoDiv = createWeatherInfo(cityInfoEntity);
            return weatherInfoDiv;
        }).setHeader("Weather Current Info");

        /*******location favourite*******/

        List<UserFavCityEntity> favoriteCities = userFavCityRepository.findFavListByuserId(userInfoEntity.getUserId());
        cityInfoEntityGrid.addComponentColumn(city -> {
            Button favoritesButton = new Button();
            favoritesButton.setIcon(city.isFavorite() ? VaadinIcon.HEART.create() : VaadinIcon.HEART_O.create());
            favoritesButton.addClickListener(event -> {
                city.setFavorite(!city.isFavorite());
                favoritesButton.setIcon(city.isFavorite() ? VaadinIcon.HEART.create() : VaadinIcon.HEART_O.create());

                if (city.isFavorite()) {
                    UserFavCityEntity userFavCity = new UserFavCityEntity();

                    Long maxId = userFavCityRepository.findMaxUserFavCityId();
                    if (maxId == null) {
                        userFavCity.setUserFavCityId(1L);
                    } else userFavCity.setUserFavCityId(maxId + 1);

                    userFavCity.setUserId(userInfoEntity.getUserId());
                    userFavCity.setiUsr(username);
                    userFavCity.setiDt(LocalDate.now());
                    userFavCity.setCityInfo(city);

                    userFavCityRepository.save(userFavCity);
                } else {
                    UserFavCityEntity userFavCity = userFavCityRepository.findByUserIdAndCityInfo(userInfoEntity.getUserId(), city);
                    if (userFavCity != null) {
                        userFavCityRepository.delete(userFavCity);
                    }
                }
            });
            for (UserFavCityEntity favoriteCity : favoriteCities) {
                if (favoriteCity.getCityInfo().getId().equals(city.getId())) {
                    city.setFavorite(true);
                    favoritesButton.setIcon(VaadinIcon.HEART.create());
                    break;
                }
            }
            return favoritesButton;
        }).setHeader("Favorite");

        /**************end location favourite*****************/

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

        add(avatarLayout, searchLayout, cityInfoEntityGrid, buttonLayout, footer);
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
        nextButton.setEnabled(cityInfoEntityPage.hasNext() && cityInfoEntityList.size() == pageSize);
    }

    private void populateFavList(String username) {
        UserInfoEntity userInfoEntity = userInfoRepository.getUserByUsername(username);

        Page<CityInfoEntity> cityInfoEntityPage;
        if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            cityInfoEntityPage = cityInfoRepository.findByCityContainingIgnoreCase(currentSearchQuery, PageRequest.of(currentPage, pageSize));
        } else {
            cityInfoEntityPage = cityInfoRepository.findAll(PageRequest.of(currentPage, pageSize));
        }

        List<UserFavCityEntity> favoriteCities = userFavCityRepository.findFavListByuserId(userInfoEntity.getUserId());

        List<CityInfoEntity> filteredList = cityInfoEntityPage.getContent().stream()
                .filter(cityInfoEntity -> {
                    for (UserFavCityEntity favoriteCity : favoriteCities) {
                        if (favoriteCity.getCityInfo().getId().equals(cityInfoEntity.getId())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());

        cityInfoEntityGrid.setItems(filteredList);
        previousButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(cityInfoEntityPage.hasNext() && filteredList.size() == pageSize);
//        nextButton.setEnabled(cityInfoEntityPage.hasNext());
    }

    private Div createWeatherInfo(CityInfoEntity cityInfoEntity) {
        Long cityId = cityInfoEntity.getId();
        WeatherDTO weather = weatherInfoService.getWeatherByCity(cityId);

        Div weatherInfoDiv = new Div();
        weatherInfoDiv.setClassName("weather-info");

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
        UI.getCurrent().getSession().setAttribute("username", null);
        UI.getCurrent().navigate(LoginView.class);
    }
}