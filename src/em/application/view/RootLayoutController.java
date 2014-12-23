package em.application.view;

import javafx.fxml.FXML;
import em.application.MainApp;

public class RootLayoutController {

	private MainApp mainApp;

	public RootLayoutController(){
		
	}
	
	@FXML
	private void initialize()
	{

	}
	
	public void setMainApp(MainApp ma)
	{
		this.mainApp = ma;
	}
	
	@FXML
	public void handleShowEditDialog()
	{
		mainApp.showEditDialog();
	}
}
