package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

public class mainController implements Initializable{
	
    @FXML
    private JFXButton homeButton,encrpButton,decrpButton,logout;
    @FXML
    private AnchorPane homeAnc,encrpAnc,decrpAnc,logoutAnc;
    @FXML
    private JFXTextField decKey,encKey;
    @FXML
    private JFXTextArea decArea,encArea;
    
	double x = 0;
	double y = 0;
	String alphaU = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String alphaL = "abcdefghijklmnopqrstuvwxyz";
	
    @FXML
    public void encryptionAction(ActionEvent event) {
    	decArea.setText("");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mazin\\Desktop"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files","*.txt"));
		File selected = fileChooser.showOpenDialog(null);
		
		encArea.setText(encryption(selected));
    }
    
    @FXML
    public void decryptionAction(ActionEvent event) {
    	encArea.setText("");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Mazin\\Desktop"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files","*.txt"));
		File selected = fileChooser.showOpenDialog(null);

		decArea.setText(decryption(selected));
    }
	@FXML
	public void dragged(MouseEvent e) {
		Node node = (Node) e.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		stage.setX(e.getScreenX()-x);
		stage.setY(e.getScreenY()-y);
	}
	@FXML
	public void pressed(MouseEvent e) {
		x = e.getSceneX();
		y = e.getSceneY();
	}

    @FXML
    private void menueButtonAction(ActionEvent event) {
    	if(event.getSource() == homeButton) {
    		visibility(homeAnc,encrpAnc,decrpAnc,logoutAnc);
    	}
    	if(event.getSource() == encrpButton) {
    		visibility(encrpAnc,homeAnc,decrpAnc,logoutAnc);
    	}
    	if(event.getSource() == decrpButton) {
    		visibility(decrpAnc,homeAnc,encrpAnc,logoutAnc);
    	}
    	if(event.getSource() == logout) {
    		visibility(logoutAnc,homeAnc,encrpAnc,decrpAnc);
    	}
    }
    
    @FXML
    private void shuttdownProg(ActionEvent event) {
    	Platform.exit();
    	System.exit(0);
    }
    
    public String readFromFile(File name){
		String readed = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(name));
			String line = null;
			StringBuilder strb = new StringBuilder();
			while((line = reader.readLine()) != null){
				strb.append(line);
			}
			readed = strb.toString();
			reader.close();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "The file "+name.getName()+" could not be found","Open File",JOptionPane.ERROR_MESSAGE);
		}
		return readed;
	}
	public String encryption(File select) {
		String readedtext;
		readedtext = readFromFile(select);
		int length = readedtext.length();
		String key = "";
		if(!decKey.getText().equals(""))
			key = decKey.getText();
		else
			key = randomAlpha(length);
		encKey.setText(key);
		String ciphertext = "";
		for(int i=0; i<length; i++) {
			try {
				char textget = readedtext.charAt(i);
				char keyget = key.charAt(i);
				if(Character.isUpperCase(textget)) {
					int textIndex = alphaU.indexOf(textget);
					int keyIndex = alphaU.indexOf(Character.toUpperCase(keyget));
					int total = (textIndex + keyIndex) % 26;
					ciphertext += alphaU.charAt(total);
				}
				else if(Character.isLowerCase(textget)) {
					int textIndex = alphaL.indexOf(textget);
					int keyIndex = alphaL.indexOf(Character.toLowerCase(keyget));
					int total = (textIndex + keyIndex) % 26;
					ciphertext += alphaL.charAt(total);				
				}
				else
					ciphertext += textget;
				
			}catch (StringIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(null, "Length of File text must equals Length of Key","Matching",JOptionPane.WARNING_MESSAGE);
				return "";
			}
		}
		writeInToFile(ciphertext,select);
		return ciphertext;
	}
	public String decryption(File select) {
		
		String readedtext;
		readedtext = readFromFile(select);
		int length = readedtext.length();
		String key = "";
		if(!encKey.getText().equals(""))
			key = encKey.getText();
		else
			key = randomAlpha(length);
		decKey.setText(key);
		String plaintext = "";
		for(int i=0; i<length; i++) {
			try {
			char textget = readedtext.charAt(i);
			char keyget = key.charAt(i);
			if(Character.isUpperCase(textget)) {
				int textIndex = alphaU.indexOf(textget);
				int keyIndex = alphaU.indexOf(Character.toUpperCase(keyget));
				int total = (textIndex - keyIndex) % 26;
				total = (total<0)?total + 26:total;
				plaintext += alphaU.charAt(total);
			}
			else if(Character.isLowerCase(textget)) {
				int textIndex = alphaL.indexOf(textget);
				int keyIndex = alphaL.indexOf(Character.toLowerCase(keyget));
				int total = (textIndex - keyIndex) % 26;
				total = (total<0)?total + 26:total;
				plaintext += alphaL.charAt(total);				
			}
			else
				plaintext += textget;
			
			}catch (StringIndexOutOfBoundsException e1) {
				JOptionPane.showMessageDialog(null, "Length of File text must equals Length of Key","Matching",JOptionPane.WARNING_MESSAGE);
				return "";
			}
		}
		writeInToFile(plaintext,select);
		return plaintext;
	}
	private String randomAlpha(int length) {
		Random ran = new Random();
		String key="";
		for(int i=0; i<length; i++) {
			key = key + (char)(ran.nextInt(26) + 'A');
		}
		return key;
	}

	public void writeInToFile(String text,File name){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(name));
			writer.write(text);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void visibility(AnchorPane needed, AnchorPane not1, AnchorPane not2, AnchorPane not3) {
		FadeTransition transition = new FadeTransition();
		transition.setNode(needed);
		needed.setVisible(true);
		transition.setDuration(Duration.millis(500));
		transition.setFromValue(0.0);
		transition.setToValue(1.0);
		transition.setAutoReverse(false);
		transition.setCycleCount(1);
		transition.play();
		
		not1.setVisible(false);
		not2.setVisible(false);
		not3.setVisible(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeButton.setTooltip(new Tooltip("Home"));
		encrpButton.setTooltip(new Tooltip("Encryption"));
		decrpButton.setTooltip(new Tooltip("Decryption"));
		logout.setTooltip(new Tooltip("Shuttdown"));
		
	}
}
