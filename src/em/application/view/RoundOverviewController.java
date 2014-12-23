package em.application.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import em.application.MainApp;
import em.application.model.RoundResult;
import em.geneticProgram.GPRoundResult;
import em.geneticProgram.GeneticProgram;
import em.representation.MusicTree;
import em.util.Player;

public class RoundOverviewController implements GeneticProgram.Reporter {
	private static final String startRunDisabledText = "Running...";
	private static final String startRunEnabledText = "Start Run";

	// FXML Items
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
	@FXML
	private Button startRunButton;
	@FXML
	private Button playButton;

	private Player player = new Player();

	// The parent of this view
	private MainApp mainApp;
	// The current data from the GP run
	private ListProperty<RoundResult> roundData = new SimpleListProperty<RoundResult>(
			FXCollections.observableArrayList());
	// The view for the data from the perspective of the chart
	private ObservableList<Series<Number, Number>> chartData = FXCollections
			.observableArrayList();
	// The series for the best tree
	private Series<Number, Number> bestTreeSeries = new Series<Number, Number>();

	// Run parameters
	private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
	private SimpleIntegerProperty numGenerations = new SimpleIntegerProperty(1);

	public RoundOverviewController() {

	}

	@FXML
	private void initialize()
	{
		setupTable();
		setupChart();
		setupControls();
	}

	private void setupChart()
	{
		scatterChart.setData(getChartData());
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

	private void setupTable()
	{
		// Initialize the person table with the two columns.
		generationColumn.setCellValueFactory(cellData -> cellData.getValue()
				.generationProperty());
		fitnessColumn.setCellValueFactory(cellData -> cellData.getValue()
				.fitnessProperty());

		// Set table comparator to keep the data sorting the same as the table
		// sorting
		SortedList<RoundResult> sortedData = new SortedList<RoundResult>(
				roundData);

		sortedData.comparatorProperty().bind(
				this.roundTable.comparatorProperty());

		this.roundTable.setItems(sortedData);
	}

	private void setupControls()
	{
		// Progress bar
		// Set progress bar binding
		this.progressBar.progressProperty().unbind();

		BooleanBinding showBar = Bindings.and(getRunning(), getNumGenerations()
				.isNotEqualTo(0));

		// @formatter:off
		roundData.sizeProperty().addListener(
				(observable, oldValue, newValue) -> 
				this.progressBar.setProgress(
						newValue.doubleValue() / getNumGenerations().doubleValue()));
		// @formatter:on
		this.progressBar.visibleProperty().bind(showBar);

		// BUTTONS

		this.getRunning().addListener((observable, oldValue, newValue) ->
		{
			if (newValue)
			{
				// We are now running

				// Disable the button
				this.startRunButton.setDisable(true);
				this.startRunButton.setText(startRunDisabledText);

			} else
			{
				// We are no longer running

				// Enable the button
				this.startRunButton.setDisable(false);
				this.startRunButton.setText(startRunEnabledText);
			}
		});

	}

	public void setMainApp(MainApp ma)
	{
		this.mainApp = ma;
	}

	private ObservableList<Series<Number, Number>> getChartData()
	{
		return this.chartData;
	}

	public ObservableBooleanValue getRunning()
	{
		return this.running;
	}

	public SimpleIntegerProperty getNumGenerations()
	{
		return this.numGenerations;
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
			// Make a task to play the tree
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call()
				{
					MusicTree tree = mt;
					if (tree != null)
					{
						player.playTree(tree);
						try
						{
							Thread.sleep(500);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						player.close();
					}
					return null;
				}
			};

			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
	}

	@FXML
	private void startRun()
	{
		int maxDepth = 6;
		int populationSize = 1000;
		int generations = 100;

		this.roundData.clear();
		this.bestTreeSeries.getData().clear();

		// @formatter:off
		GeneticProgram gp = new GeneticProgram
				.GeneticProgramBuilder(generations)
				.populationSize(populationSize)
				.initialMaxDepth(maxDepth)
				.doMutationProb(0.01)
				.createGP();
		// @formatter:on

		gp.setReportDelegate(this);
		this.numGenerations.set(gp.getNumGenerations());

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception
			{
				Platform.runLater(() -> running.set(true));
				gp.run();
				Platform.runLater(() -> running.set(false));
				return null;
			}
		};

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}

	public void addRound(GPRoundResult gprr)
	{
		RoundResult rr = new RoundResult(gprr);

		Platform.runLater(() -> roundData.add(rr));
	}

}
