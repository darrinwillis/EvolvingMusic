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
		this.params = params;
		numGenerationsTextField.setText(Integer.toString(params.getNumGenerations()));
		populationSizeTextField.setText(Integer.toString(params.getPopulationSize()));
		mutationProbTextField.setText(String.format("%.2f%%", params.getMutationProbability() * 100));
	}

	@FXML
	public void handleOk()
	{
		if (checkInput())
		{
			params.setNumGenerations(Integer.parseInt(numGenerationsTextField.getText()));
			params.setPopulationSize(Integer.parseInt(populationSizeTextField.getText()));
			params.setMutationProbability(parsePercentage(mutationProbTextField.getText()));
			dialogStage.close();
		}
	}

	public boolean checkInput()
	{
		Integer newNumGens, newPopSize;
		Double newMutProb;

		String errorMessage = "";

		try{
			newNumGens = Integer.parseInt(numGenerationsTextField.getText());
		} catch (NumberFormatException e) {
			errorMessage += "No valid number of generations\n";
		}
		try{
			newPopSize = Integer.parseInt(populationSizeTextField.getText());
		} catch (NumberFormatException e) {
			errorMessage += "No valid population size\n";
		}
		try{
			newMutProb = parsePercentage(mutationProbTextField.getText());
		} catch (NumberFormatException e) {
			errorMessage += "No valid mutation probability\n";
		}

		if (errorMessage.length() == 0)
		{
			return true;
		} else
		{
            Dialogs.create()
            	.title("Invalid Fields")
            	.masthead("Please correct invalid fields")
            	.message(errorMessage)
            	.showError();
			return false;
		}
	}
	
	public double parsePercentage(String s) throws NumberFormatException
	{
		double value;
		if (s.endsWith("%"))
		{
			value = Double.parseDouble(s.trim().replace("%", "")) / 100;
		} else {
			value = Double.parseDouble(s);
		}
		return value;
	}
}
