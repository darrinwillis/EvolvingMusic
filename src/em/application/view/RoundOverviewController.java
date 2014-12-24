package em.application.view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import em.application.MainApp;
import em.application.model.GPParameters;
import em.application.model.RoundResult;
import em.geneticProgram.GPRoundResult;
import em.geneticProgram.GeneticProgram;
import em.representation.MusicTree;
import em.util.Player;

public class RoundOverviewController implements GeneticProgram.Reporter {
	// Start run button text
	private static final String startRunDisabledText = "Running...";
	private static final String startRunEnabledText = "Start Run";
	// Play button text
	private static final String startPlayText = "Play";
	private static final String stopPlayText = "Stop";

	private static final int defaultNumGens = 100;
	private static final int defaultPopSize = 1000;
	private static final double defaultMutRate = 0.01d;
	private static final int maxDepth = 6;

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
	private ObservableList<Series<Number, Number>> chartData = FXCollections.observableArrayList();
	
	// The series for the best tree
	private Series<Number, Number> bestTreeSeries = new Series<Number, Number>();
	private Series<Number, Number> selectedTreeSeries = new Series<Number, Number>();

	private GPParameters params = new GPParameters(defaultNumGens, defaultPopSize, defaultMutRate);
	
	// Status Properties
	private DoubleProperty currentRunMaxGenerations = new SimpleDoubleProperty(1);
	private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
	private BooleanProperty playing = new SimpleBooleanProperty(false);

	private Stage stage;

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
		bestTreeSeries.setName("Best Tree of Generation");
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
									new Data<Number, Number>(roundResult.getGeneration(),
											roundResult.getFitness())));
				}

			}
		});
		
		selectedTreeSeries.setName("Selected Tree");
		Rectangle rect = new Rectangle(5, 5);
		rect.setFill(Color.GREEN);
		selectedTreeSeries.setNode(rect);
		chartData.add(selectedTreeSeries);
		this.roundTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
		{
			this.selectedTreeSeries.getData().clear();
			if (newValue != null)
			{
				this.selectedTreeSeries.getData().add(
						new Data<Number, Number>(
								newValue.getGeneration(), 
								newValue.getFitness()));
			}
		});
		
	}

	private void setupTable()
	{
		// Initialize the person table with the two columns.
		generationColumn.setCellValueFactory(cellData -> cellData.getValue().generationProperty());
		fitnessColumn.setCellValueFactory(cellData -> cellData.getValue().fitnessProperty());

		// Set table comparator to keep the data sorting the same as the table
		// sorting
		SortedList<RoundResult> sortedData = new SortedList<RoundResult>(roundData);

		sortedData.comparatorProperty().bind(this.roundTable.comparatorProperty());

		this.roundTable.setItems(sortedData);
	}

	private void setupControls()
	{
		// Progress bar
		// Set progress bar binding
		this.progressBar.progressProperty().unbind();

		BooleanBinding showBar = Bindings.and(getRunning(), getNumGenerations().isNotEqualTo(0));
		this.progressBar.visibleProperty().bind(showBar);
		
		NumberBinding barPercentage = Bindings.divide(roundData.sizeProperty(), currentRunMaxGenerations);
		this.progressBar.progressProperty().bind(barPercentage);

		// BUTTONS

		// Start Run Button
		StringBinding startRunButtonText = 
				Bindings.when(this.getRunning())
				.then(startRunDisabledText)
				.otherwise(startRunEnabledText);
		
		this.startRunButton.textProperty().bind(startRunButtonText);
		this.startRunButton.disableProperty().bind(this.getRunning());
		
		// Play Button
		StringBinding playButtonText =
				Bindings.when(this.playing)
				.then(stopPlayText)
				.otherwise(startPlayText);
		this.playButton.textProperty().bind(playButtonText);

		//Button is initially disabled
		this.playButton.setDisable(true);
		BooleanBinding playDisabled = Bindings.and(
				Bindings.lessThan(
						this.roundTable.getSelectionModel().selectedIndexProperty(), 
						0), 
				Bindings.not(playing));
		this.playButton.disableProperty().bind(playDisabled);
	}

	public void setMainApp(MainApp ma)
	{
		this.mainApp = ma;
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent t) {
				player.stop();
				player.close();
			}
		});
	}

	private ObservableList<Series<Number, Number>> getChartData()
	{
		return this.chartData;
	}

	public ObservableBooleanValue getRunning()
	{
		return this.running;
	}

	public IntegerProperty getNumGenerations()
	{
		return this.params.numGenerations();
	}

	public GPParameters getParameters()
	{
		return this.params;
	}
	
	@FXML
	private void handlePlay()
	{
		if (playing.get())
		{
			playing.set(false);
			Platform.runLater(() -> 
			{
				player.stop();
				player.close();
			});
		} else {
			playing.set(true);
			playTree();
			//playing is set to false in the task
		}
	}
	
	private void playTree()
	{
		int selectedIndex = roundTable
			.getSelectionModel()
			.getSelectedIndex();
		if (selectedIndex >= 0)
		{
			MusicTree mt = this.roundTable.getItems().get(selectedIndex).getTree();
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
						Platform.runLater(() -> playing.set(false));
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
		this.roundData.clear();
		this.bestTreeSeries.getData().clear();

		GeneticProgram gp = makeBuilder(this.params).createGP();

		gp.setReportDelegate(this);
		this.currentRunMaxGenerations.set(params.getNumGenerations());

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
	
	private GeneticProgram.GeneticProgramBuilder makeBuilder(GPParameters params)
	{
		GeneticProgram.GeneticProgramBuilder builder = new GeneticProgram
				.GeneticProgramBuilder(params.getNumGenerations())
				.populationSize(params.getPopulationSize())
				.initialMaxDepth(maxDepth)
				.doMutationProb(params.getMutationProbability());
		return builder;
	}

	public void addRound(GPRoundResult gprr)
	{
		RoundResult rr = new RoundResult(gprr);

		Platform.runLater(() -> roundData.add(rr));
	}

}
