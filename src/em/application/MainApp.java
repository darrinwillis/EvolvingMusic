package em.application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private ObservableList<RoundResult> roundData = FXCollections
			.observableArrayList();

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

	public ObservableList<RoundResult> getRoundData()
	{
		return roundData;
	}

	public void playTree(MusicTree mt)
	{

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

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception
			{
				gp.run();
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
