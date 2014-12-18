package em.application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import em.application.model.RoundResult;
import em.application.view.RoundOverviewController;
import em.geneticProgram.GPRoundResult;
import em.geneticProgram.GeneticProgram;
import em.representation.MusicTree;
import em.util.Player;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	// private ObservableList<RoundResult> roundData = FXCollections
	// .observableArrayList();
	private ListProperty<RoundResult> roundData = new SimpleListProperty<RoundResult>(
			FXCollections.observableArrayList());
	private SimpleBooleanProperty running = new SimpleBooleanProperty(false);
	private SimpleIntegerProperty numGenerations = new SimpleIntegerProperty(1);

	private Player player = new Player();

	public MainApp() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Evolving Music");
		initRootLayout();
		showRoundOverview();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout()
	{
		try
		{
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showRoundOverview()
	{
		try
		{
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RoundOverview.fxml"));
			AnchorPane roundOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(roundOverview);

			RoundOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ListProperty<RoundResult> getRoundData()
	{
		return roundData;
	}

	public ObservableBooleanValue getRunning()
	{
		return this.running;
	}

	public SimpleIntegerProperty getNumGenerations()
	{
		return this.numGenerations;
	}

	public void playTree(MusicTree mt)
	{
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

	public void startRun()
	{
		int maxDepth = 6;
		int populationSize = 1000;
		int generations = 100;

		// @formatter:off
		GeneticProgram gp = new GeneticProgram
				.GeneticProgramBuilder(generations)
				.populationSize(populationSize)
				.initialMaxDepth(maxDepth)
				.doMutationProb(0.01)
				.createGP();
		// @formatter:on

		gp.setMainApp(this);
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

	public static void main(String[] args)
	{
		launch(args);
	}
}
