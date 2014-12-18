package em.application.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import em.application.MainApp;
import em.application.model.RoundResult;
import em.representation.MusicTree;

public class RoundOverviewController {
	@FXML
	private TableView<RoundResult> roundTable;
	@FXML
	private TableColumn<RoundResult, Number> generationColumn;
	@FXML
	private TableColumn<RoundResult, Number> fitnessColumn;
	@FXML
	private ProgressBar progressBar;

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
	}

	public void setMainApp(MainApp ma)
	{
		this.mainApp = ma;

		ListProperty<RoundResult> roundData = ma.getRoundData();

		// Set progress bar binding
		this.progressBar.progressProperty().unbind();

		BooleanBinding showBar = Bindings.and(ma.getRunning(), ma
				.getNumGenerations().isNotEqualTo(0));

		// @formatter:off
		roundData.sizeProperty().addListener(
				(observable, oldValue, newValue) -> 
				this.progressBar.setProgress(
						newValue.doubleValue() / ma.getNumGenerations().doubleValue()));
		// @formatter:on
		this.progressBar.visibleProperty().bind(showBar);

		// Set table comparator to keep the data sorting the same as the table
		// sorting
		SortedList<RoundResult> sortedData = new SortedList<RoundResult>(
				roundData);

		sortedData.comparatorProperty().bind(
				this.roundTable.comparatorProperty());

		this.roundTable.setItems(sortedData);
	}

	@FXML
	private void playTree()
	{
		// @formatter:off
		int selectedIndex = roundTable
			.getSelectionModel()
			.getSelectedIndex();
		// @formatter:on
		if (selectedIndex >= 0)
		{
			MusicTree mt = this.roundTable.getItems().get(selectedIndex)
					.getTree();
			this.mainApp.playTree(mt);
		}
	}

	@FXML
	private void startRun()
	{
		this.mainApp.startRun();
	}

}
