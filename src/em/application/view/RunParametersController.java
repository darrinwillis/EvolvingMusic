package em.application.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import em.application.model.GPParameters;

public class RunParametersController {

	@FXML
	private TextField numGenerationsTextField;
	@FXML
	private TextField populationSizeTextField;
	@FXML
	private TextField mutationProbTextField;

	private Stage dialogStage;
	private GPParameters params;

	public RunParametersController() {

	}

	@FXML
	private void initialize()
	{

	}

	public void setDialogStage(Stage stage)
	{
		this.dialogStage = stage;
	}

	public void setParameters(GPParameters params)
	{
		numGenerationsTextField.setText(Integer.toString(params.getNumGenerations()));
		populationSizeTextField.setText(Integer.toString(params.getPopulationSize()));
		mutationProbTextField.setText(String.format("%f%", params.getMutationProbability() * 100));
	}

	@FXML
	public void handleOk()
	{
		if (checkInput())
		{
			params.setNumGenerations(Integer.parseInt(numGenerationsTextField.getText()));
			params.setPopulationSize(Integer.parseInt(populationSizeTextField.getText()));
			params.setMutationProbability(Double.parseDouble(mutationProbTextField.getText()));
			dialogStage.close();
		}
	}

	public boolean checkInput()
	{
		Integer newNumGens, newPopSize;
		Double newMutProb;

		String errorMessage = "";

		newNumGens = Integer.parseInt(numGenerationsTextField.getText());
		if (newNumGens == null)
		{
			errorMessage += "No valid number of generations\n";
		}
		newPopSize = Integer.parseInt(populationSizeTextField.getText());
		if (newPopSize == null)
		{
			errorMessage += "No valid population size\n";
		}
		newMutProb = Double.parseDouble(mutationProbTextField.getText());
		if (newMutProb == null)
		{
			errorMessage += "No valid mutation probability%n";
		}

		if (errorMessage.length() == 0)
		{
			return true;
		} else
		{
			//Show error @formatter:off
            Dialogs.create()
            	.title("Invalid Fields")
            	.masthead("Please correct invalid fields")
            	.message(errorMessage)
            	.showError();
            //@formatter:on
			return false;
		}
	}
}
