package em.application.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import em.application.MainApp;
import em.application.model.RoundResult;

public class RoundOverviewController {
	@FXML
	private TableView<RoundResult> roundTable;
	@FXML
	private TableColumn<RoundResult, Number> generationColumn;
	@FXML
	private TableColumn<RoundResult, Number> fitnessColumn;

	private MainApp mainApp;

	public RoundOverviewController() {

	}

	@FXML
	private void initialize()
	{
		// Initialize the person table with the two columns.
		generationColumn.setCellValueFactory(cellData -> cellData.getValue()
				.generationProperty());
		fitnessColumn.setCellValueFactory(cellData -> cellData.getValue()
				.fitnessProperty());

		roundTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> playTree(newValue));
	}

	public void setMainApp(MainApp ma)
	{
		this.mainApp = ma;

		this.roundTable.setItems(ma.getRoundData());
	}

	private void playTree(RoundResult rr)
	{
		this.mainApp.playTree(rr.getTree());
	}

}
