package com.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a button and a click listener.
 */
@Route("") 	//The @Route annotation tells Vaadin 
			//to direct the root URL to this view. 
			//The URL parameter is optional and is derived from the class name, if not given.
@PWA(name = "Project Base for Vaadin Flow", shortName = "Project Base")
//The @PWA annotation tells Vaadin to activate automatic PWA features. This annotation is optional.
public class MainView extends VerticalLayout {

	private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>(Customer.class);
    private TextField filterText = new TextField();
    
    private CustomerForm form = new CustomerForm(this);
	
    public MainView() {
        /*Button button = new Button("Click me",
                event -> Notification.show("Clicked!"));
        add(button);*/
    	
    	filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);//ensures that change events are fired immediately when the user types
        filterText.addValueChangeListener(e -> updateList());//adds a value change listener that reacts to changes in the value of the text field
        /*
         The value change listener calls the updateList() method, but this method doesn’t yet use the value in the filter. 
         To configure it to use this value, change the line in the updateList() method 
         to send the value to the service (backend) call          
         */
        
        Button addCustomerBtn = new Button("Add new customer");
        addCustomerBtn.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCustomer(new Customer());
        });
    	
        HorizontalLayout toolbar = new HorizontalLayout(filterText,
        	    addCustomerBtn);
        
    	grid.setColumns("firstName", "lastName", "status");
    	//setColumns configures the Grid to show the firstName, lastName, and status properties of the Customer class

        //add(grid);
        //add(grid) adds the Grid to the VerticalLayout

        setSizeFull();
        //setSizeFull sets the height and width of the VerticalLayout to 100%, i.e. to use all the space available in the browser
        
        updateList();
        
        
        //add(filterText, grid);
        
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();

        //add(filterText, mainContent);
        add(toolbar,mainContent);
        
        form.setCustomer(null);
        //when no customer is selected in the Grid, the form should be hidden. 
        //Hide the form by setting a null customer in the constructor of the MainView class 
        
        grid.asSingleSelect().addValueChangeListener(event ->
        form.setCustomer(grid.asSingleSelect().getValue()));
        /*
         	addValueChangeListener adds a listener to the Grid. 
         	The Grid component supports multi and single-selection modes. 
         	This example uses the single-select mode through the asSingleSelect() method.

			setCustomer sets the selected customer in the CustomerForm. This line also uses the single-select mode.

			The getValue() method returns the Customer in the selected row or null if there’s no selection,
			effectively showing or hiding the form accordingly. 
         */
        
    }
    
    public void updateList() {
        //grid.setItems(service.findAll());
    	grid.setItems(service.findAll(filterText.getValue())); //filterText.getValue() returns the current string in the text field
    }
}
