/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import server.Info;

public class LoginController implements Initializable {
	/*
	 * public class SaveSocket {
	 *
	 * public void set(Socket s) { System.out.println(s); S = s;
	 * System.out.println(S); } }
	 */
	private static Socket S;
	@FXML
	private Button signIn;
	@FXML
	private PasswordField password;
	@FXML
	private TextField email;
	@FXML
	private Label label1;
	@FXML
	private Label label2;
	@FXML
	private Button signUp;
	@FXML
	private PasswordField SUpassword;
	@FXML
	private TextField SUemail;
	@FXML
	private TextField SUname;
	@FXML
	private Label label3;
	@FXML
	private Label label4;
	@FXML
	private Label label5;
	@FXML
	private Label label6;
	private boolean initializeInputThread;
	Thread_InputListen TIL;
	private static String userName;
	private Socket clientSocket;// = new Socket("localhost", 6789);
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	private Info info = new Info();

	@FXML
	private void handleButtonSignIn(ActionEvent event) throws IOException {
		Stage stage;
		Parent root;
		if (!email.getText().trim().equals("")) {
			label1.setText("");
			if (!password.getText().trim().equals("")) {
				label2.setText("");
				String Semail = email.getText(), Spassword = password.getText();

				clientSocket = new Socket("localhost", 6789);
				System.out.println(clientSocket);
				// Save.set(clientSocket);
				S = clientSocket;
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String method = "signin", reply;
				userName = Semail;
				outToServer.writeBytes(method + "\n");
				outToServer.writeBytes(Semail + "\n");
				outToServer.writeBytes(Spassword + "\n");
				reply = inFromServer.readLine();
				if (reply.equals("True")) {

					stage = (Stage) signIn.getScene().getWindow();

					root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
					stage.setOnCloseRequest(event1 -> {

						try {
							outToServer.writeBytes("EXIT" + '\n');
						} catch (IOException ex) {
							Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
						}
					});

					Scene sc = new Scene(root);

					sc.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
					// sc.getStylesheets().add("stylesheet.css");
					stage.setScene(sc);
					stage.show();

				} else
					label1.setText("Login Failed");
			} else
				label2.setText("Password field is empty");
		} else
			label1.setText("email field is empty");
	}

	@FXML
	private void handleButtonSignUP(ActionEvent event) throws IOException {
		if (!SUname.getText().trim().equals("")) {
			label3.setText("");
			if (!SUemail.getText().trim().equals("")) {
				label4.setText("");
				if (!SUpassword.getText().trim().equals("")) {
					label5.setText("");
					clientSocket = new Socket("localhost", 6789);
					outToServer = new DataOutputStream(clientSocket.getOutputStream());
					inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

					String method = "signup";
					String reply = "";
					String email = SUemail.getText();
					String password = SUpassword.getText();
					String name = SUname.getText();

					outToServer.writeBytes(method + "\n");
					outToServer.writeBytes(name + "\n");
					outToServer.writeBytes(email + "\n");
					outToServer.writeBytes(password + "\n");
					System.out.println("method " + method);
					System.out.println("name " + name);
					System.out.println("email  " + email);
					System.out.println(" pass " + password);
					reply = inFromServer.readLine();
					if (!reply.equals("True")) {
						label6.setText("You successful created an account");
						info.makeUser(name, email, password);
					} else {
						label6.setText("SignUp Failed");
						System.out.println("faield");
					}
					clientSocket.close();
				} else
					label5.setText("Password field is empty");
			} else
				label4.setText("email field is empty");
		} else
			label3.setText("Name field is empty");
	}

	@FXML
	private void handleButtonChat(ActionEvent event) {

	}

	@FXML
	private Button b1 = new Button();
	@FXML
	private Button send = new Button();
	@FXML
	private VBox vBox1;
	@FXML
	private TextField msgTxt;
	@FXML
	private VBox chat;
	@FXML
	private VBox chatScreen;
	@FXML
	private Label myName;
	@FXML
	private Label l1;
	Vector<VBox> chatScreens = new Vector<VBox>();
	Vector<Button> chatButtons = new Vector<Button>();

	static boolean threadStarted = true;

	@FXML
	private void handleButton(ActionEvent event) throws IOException, ClassNotFoundException {

		myName.setAlignment(Pos.CENTER);
		myName.setText(userName);
		System.out.println("User Name ");

		inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
		outToServer = new DataOutputStream(S.getOutputStream());
		outToServer.writeBytes("SENDFRIENDS" + '\n');

	}

	String musicFile = "C:\\Users\\Hp\\Desktop\\1.mp3"; // Bell
	// tone

	Media sound = new Media(new File(musicFile).toURI().toString());
	MediaPlayer mediaPlayer = new MediaPlayer(sound);

	@FXML
	private void handleButtonSend(ActionEvent event) throws IOException {
		// Msg msg1 = new Msg();
		if (!initializeInputThread) {
			// inFromServer = new BufferedReader(new
			// InputStreamReader(S.getInputStream()));
			/* final Thread_InputListen */
			TIL = new Thread_InputListen(inFromServer, S, l1, chatScreens, mediaPlayer, b1, chat, vBox1);
			// Platform.runLater(TIL);
			TIL.start();

			initializeInputThread = true;
			// System.out.println("Thread Started");
		}
		if (!msgTxt.getText().trim().equals("") && !l1.getText().trim().equals("")) {
			// msg1.from = userName;
			// msg1.to = l1.getText();
			// msg1.text = msgTxt.getText();
			// System.out.println("masdfalsdfhasdf is " + );
			System.out.println(msgTxt.getText());
			outToServer = new DataOutputStream(S.getOutputStream());
			outToServer.writeBytes("MSG" + '\n');
			outToServer.writeBytes(userName + '\n');
			outToServer.writeBytes(l1.getText() + '\n');
			String saveMsg = msgTxt.getText();
			outToServer.writeBytes(msgTxt.getText() + '\n');
			msgTxt.setText("");
			System.out.println("msg send");
			Label recvMsg = new Label(), sendMsg = new Label();
			recvMsg.setTextAlignment(TextAlignment.LEFT);
			sendMsg.setTextAlignment(TextAlignment.LEFT);
			sendMsg.setAlignment(Pos.CENTER_RIGHT);
			sendMsg.setWrapText(true);
			recvMsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			sendMsg.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			sendMsg.getStyleClass().add("send");

			HBox hBox = new HBox();
			hBox.getChildren().add(sendMsg);
			hBox.setAlignment(Pos.BASELINE_RIGHT);
			for (int i = 0; i < chatScreens.size(); i++) {
				if (chatScreens.get(i).getId().equals(l1.getText())) {
					sendMsg.setText(saveMsg);
					// sendMsg.setWrapText(true);
					// sendMsg.setStyle("-fx-background-color: blue;
					// -fx-text-fill: white;");
					chatScreens.get(i).getChildren().add(hBox);
					// chat.getChildren().add(sendMsg);
					// chat = chatScreens.get(i);

				}
			}

		}

	}

	@FXML
	private Button CancelRequest;

	// is to be set
	@FXML
	private void handleButtonAttachFile(ActionEvent event) throws IOException {

	}

	@FXML
	private Button submitFeedback;

	@FXML
	private void handleButtonStartGroupChat(ActionEvent event) throws IOException {
		/// feedback code

	}

	@FXML
	Button Cancle = new Button();

	@FXML
	private Button addFriend;
	@FXML
	private Button cancel;
	@FXML
	private TextField friendUserName;

	@FXML
	private void handleButtonAddFriendWindow(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("Call CareTaker.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 600, 400);
		Stage stage = new Stage();
		stage.setTitle("New Window");
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	private Label friendAdded;

	@FXML
	private void handleButtonAddFriend(ActionEvent event) throws IOException {
		inFromServer = new BufferedReader(new InputStreamReader(S.getInputStream()));
		outToServer = new DataOutputStream(S.getOutputStream());
		outToServer.writeBytes("ADDFRIEND" + '\n');
		String FUN = friendUserName.getText();
		friendUserName.setText("");
		outToServer.writeBytes(FUN + '\n');
		String reply = inFromServer.readLine();
		if (reply.equals("True"))
			friendAdded.setText("You successfully added a new friend");
		else
			friendAdded.setText("Error");

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		send.fire();
		// vBox1 = new VBox();
		b1.setVisible(false);////////////////////////////////////////
		Cancle.setVisible(false);
	}

}
