package com.wasserwerkewesterzgebirge.permissiontracker.Views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import jakarta.servlet.http.HttpServletResponse;

@ParentLayout(MainLayout.class)
public class ErrorController
        extends RouteNotFoundError {

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<NotFoundException> parameter) {

        Div errorDiv = new Div();
        errorDiv.getStyle().set("display", "flex");
        errorDiv.getStyle().set("justify-content", "center");
        errorDiv.getStyle().set("align-items", "center");


        StreamResource logoStream = new StreamResource("404.gif", () -> getClass().getResourceAsStream("/static/gifs/404.gif"));
        Image errorImage = new Image(logoStream, "404.gif");
        errorDiv.add(errorImage);

        getElement().appendChild(errorDiv.getElement());

        return HttpServletResponse.SC_NOT_FOUND;
    }
}
