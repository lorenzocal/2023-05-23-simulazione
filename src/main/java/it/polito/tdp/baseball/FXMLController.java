package it.polito.tdp.baseball;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.baseball.model.Model;
import it.polito.tdp.baseball.model.People;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnesse;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnDreamTeam;

    @FXML
    private Button btnGradoMassimo;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtSalary;

    @FXML
    private TextField txtYear;

    
    
    @FXML
    void doCalcolaConnesse(ActionEvent event) {
    	
    }

    
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	try {
    		Integer anno = Integer.parseInt(this.txtYear.getText());
    		 if (this.model.getAllAnni().contains(anno)) {
    			try {
    				Integer salario = Integer.parseInt(this.txtSalary.getText())*1000000;
    				this.model.creaGrafo(anno, salario);
    				if (this.model.getnVertici() > 0) {
    					this.txtResult.appendText("Grafo creato correttamente.\n");
    					this.txtResult.appendText("Vertici: " + this.model.getnVertici() + "\n");
    					this.txtResult.appendText("Archi: " + this.model.getnArchi() + "\n");
    				 	this.btnGradoMassimo.setDisable(false);
    				 	this.btnDreamTeam.setDisable(false);
    				}
    				else {
    					this.txtResult.appendText("Non può essere creato alcun grafo dati i parametri inseriti.\n");
    				}
    			} catch (NumberFormatException nfe) {
    				this.txtResult.appendText("Il salario inserito non è in un formato valido.\n");
    			} catch (NullPointerException npe) {
    	    		this.txtResult.appendText("Non è stato inserito alcun salario.\n");
    	    	}
    		}
    		else {
    			this.txtResult.appendText("Non sono presenti dati per l'anno inserito.\n");
    		}
    	} catch (NumberFormatException nfe) {
    		this.txtResult.appendText("L'anno inserito non è in un formato valido.\n");
    	} catch (NullPointerException npe) {
    		this.txtResult.appendText("Non è stato inserito alcun anno.\n");
    	}
   
    }

    
    @FXML
    void doDreamTeam(ActionEvent event) {
    	this.model.inizializza();
    }

    
    @FXML
    void doGradoMassimo(ActionEvent event) {
    	if (this.model.getnArchi() == 0) {
    		this.txtResult.appendText("Occorre creare prima un grafo valido.\n");
    	}
    	this.model.gradoMassimo();
    	this.txtResult.appendText("Vertici di grado massimo:\n");
    	for (People p : this.model.getVerticiGradoMassimo()) {
    		this.txtResult.appendText(p + " | Grado: " + this.model.getGradoMassimo() + "\n");
   
    	}
    }

    
    @FXML
    void initialize() {
        assert btnConnesse != null : "fx:id=\"btnConnesse\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGradoMassimo != null : "fx:id=\"btnGradoMassimo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSalary != null : "fx:id=\"txtSalary\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYear != null : "fx:id=\"txtYear\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
    
}
