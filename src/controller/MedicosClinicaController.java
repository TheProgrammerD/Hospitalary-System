package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import excepciones.NotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Clinica;
import model.MedicoEspecialista;

public class MedicosClinicaController implements Initializable {
	
	
	
	private ObservableList<MedicoEspecialista> medicos;
	@FXML
	private TextField nit;
	
	@FXML
	private Button back;
	
	@FXML
	private Button delete;

	@FXML
	private BorderPane panel;
	
	@FXML
	private Label label;
	
	private TableView<MedicoEspecialista> listMed;
	

	void init() {
		listMed = new TableView<MedicoEspecialista>();
		listMed.setMaxWidth(540);
		listMed.setMaxHeight(360);

		medicos = FXCollections.observableArrayList();
		listMed.setItems(medicos);

		TableColumn<MedicoEspecialista, String> colName = new TableColumn<>("Nombre");
		colName.setCellValueFactory(new PropertyValueFactory<MedicoEspecialista, String>("nombre_apellidos"));
		colName.setMinWidth(listMed.getMaxWidth()/4);
		
		TableColumn<MedicoEspecialista, String> colId = new TableColumn<>("Identificacion");
		colId.setCellValueFactory(new PropertyValueFactory<MedicoEspecialista, String>("id"));
		colId.setMinWidth(listMed.getMaxWidth()/4);
		
		TableColumn<MedicoEspecialista, String> colEspe = new TableColumn<>("Especialidad");
		colEspe.setCellValueFactory(new PropertyValueFactory<MedicoEspecialista, String>("medico_especialista"));
		colEspe.setMinWidth(listMed.getMaxWidth()/4);
		
		TableColumn<MedicoEspecialista, Integer> colLi = new TableColumn<>("No. Licencia");
		colLi.setCellValueFactory(new PropertyValueFactory<MedicoEspecialista, Integer>("no_licencia"));
		colLi.setMinWidth(listMed.getMaxWidth()/4);
		
		
		
		listMed.getColumns().addAll(colName,colId,colEspe,colLi);
		panel.setCenter(listMed);
		
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {

				try {
					Parent root = FXMLLoader.load(getClass().getResource("/application/clinica.fxml"));
					Scene scene = new Scene(root);
					Stage stage = (Stage) ((Node) t.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.centerOnScreen();
					stage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		
		delete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent t) {

				if (listMed.getSelectionModel().isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText(null);
					alert.setContentText("Seleccione un elemento de la tabla");
					alert.showAndWait();
				} else {
					int licencia = medicos.get(listMed.getSelectionModel().getSelectedIndex()).getNo_licencia();
					int n = Integer.parseInt(nit.getText());
					medicos.remove(listMed.getSelectionModel().getSelectedItem());
					try {
						Main.getEPS().borrarMedicoClinica(licencia, n);
					} catch (NotFoundException e) {
						Alert alert = new Alert(AlertType.INFORMATION);
			    		alert.setTitle("Aviso");
			    		alert.setHeaderText(null);
						alert.setContentText("La clinica buscada no existe");
						alert.showAndWait();
					}
				}

			}
		});
		
	}
	
	@FXML
	public void search(ActionEvent e) {
		if (nit.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
			alert.setContentText("El campo de busqueda esta vacio!");
			alert.showAndWait();
		} else {
			int n = Integer.parseInt(nit.getText());
			
				try {
					medicos = FXCollections.observableArrayList(Main.getEPS().buscarClinica(n).getMedicos());
					listMed.setItems(medicos);
					
					if (medicos.isEmpty()) {
						Alert alert = new Alert(AlertType.INFORMATION);
			    		alert.setTitle("Aviso");
			    		alert.setHeaderText(null);
						alert.setContentText("La clinica no cuenta con medicos registrados, puede a�adir uno en el menu a su izquierda");
						alert.showAndWait();
					}
				} catch (NotFoundException e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
		    		alert.setTitle("Aviso");
		    		alert.setHeaderText(null);
					alert.setContentText("La clinica buscada no existe");
					alert.showAndWait();
				}
		}
	}
	
	@FXML
	public void addMed(ActionEvent e) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/application/registrarMedEsp.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		init();
	}

}
