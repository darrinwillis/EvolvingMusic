package em.application.view;

import javafx.collections.transformation.SortedList;
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

		SortedList<RoundResult> sortedData = new SortedList<RoundResult>(
				ma.getRoundData());

		sortedData.comparatorProperty().bind(
				this.roundTable.comparatorProperty());

		this.roundTable.setItems(sortedData);
	}

	private void playTree(RoundResult rr)
	{
		if (rr != null && rr.getTree() != null)
		{
			this.mainApp.playTree(rr.getTree());
		}
	}

	@FXML
	private void startRun()
	{
		this.mainApp.startRun();
	}

}
