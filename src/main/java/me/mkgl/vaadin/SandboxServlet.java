package me.mkgl.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@WebServlet(value = "/VAADIN/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = UI.class)
public class SandboxServlet extends VaadinServlet {
}
