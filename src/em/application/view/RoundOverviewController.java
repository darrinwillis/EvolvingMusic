package em.application.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
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
	@FXML
	private ScatterChart<Number, Number> scatterChart;

	private MainApp mainApp;
	private ListProperty<RoundResult> roundData;
	private ObservableList<Series<Number, Number>> chartData = FXCollections
			.observableArrayList();
	private Series<Number, Number> bestTreeSeries = new Series<Number, Number>();

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
		scatterChart.setData(getChartData());
	}

	public void setMainApp(MainApp ma)
	{
		this.mainApp = ma;

		this.roundData = ma.getRoundData();

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

		chartData.add(bestTreeSeries);
		// Set up the chart
		this.roundData.addListener(new ListChangeListener<RoundResult>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends RoundResult> c)
			{
				while (c.next())
				{
					c.getAddedSubList().forEach(
							roundResult -> bestTreeSeries.getData().add(
									new Data<Number, Number>(roundResult
											.getGeneration(), roundResult
											.getFitness())));

				}

			}
		});
	}

	private ObservableList<Series<Number, Number>> getChartData()
	{
		return this.chartData;
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
