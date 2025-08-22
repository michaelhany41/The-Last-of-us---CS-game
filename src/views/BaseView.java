package views;
import java.awt.Point;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.util.Duration;
import model.characters.Character;
import model.characters.Direction;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;
import javafx.scene.control.Tooltip;
import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.animation.ScaleTransition;
import javafx.scene.control.TextField;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.*;
//prefHeightProperty
public class BaseView extends Application {

	private boolean flag=false;
	private boolean heroSelected = false;
	private boolean heroShown = false;
	private VBox buttonVBox = new VBox();
	private static final int GRID_SIZE = 15;
	private GridPane gridPane = new GridPane(); 
	private Hero player =null;
	private BorderPane root1 = new BorderPane();
	private Button moveUP = new Button("UP");
	private Button moveDOWN = new Button("DOWN");
	private Button moveLEFT = new Button("LEFT");
	private Button moveRIGHT = new Button("RIGHT");
	private Button attack = new Button ("ATTACK");
	private Button cure = new Button("CURE");
	private Button useSpecial = new Button("USE SPECIAL");
	private Button endTurn = new Button("END TURN");
	private VBox vboxCharacter = new VBox();
	private HBox heroesAlive = new HBox();
	private Image vaccine = new Image("./views/vaccine.png");
	private Image supply = new Image("./views/supply.png");
	private Image tile = new Image("./views/tile.png");
	private Image zombie = new Image("./views/zombie.png");
	private Image trap = new Image("./views/Exit.png");
	private Image clouds = new Image("./views/clouds.png");
	private Image lose = new Image("./views/lose.jpg");
	private Image win = new Image("./views/win.jpg");
	private Label rootlose = new Label();
	private Label rootwin = new Label();
	private Scene scenelose = new Scene(rootlose,800,600);
	private Scene scenewin = new Scene(rootwin,800,600);
	private HBox heroVBOx = new HBox();
	private List<VBox> heroInfoList = new ArrayList<>();
	

	public void start(Stage stage) throws Exception {
		for(int i = 0; i<Game.heroes.size();i++) {
			Image hero = new Image("./views/"+Game.availableHeroes.get(i).getName()+".png") ;
			ImageView x2 = new ImageView(hero);
			x2.setFitWidth(50);
			x2.setFitHeight(50);
			heroesAlive.getChildren().add(x2);
		}
		ImageView lose2 = new ImageView(lose);
		BackgroundImage backgroundloss = new BackgroundImage(lose, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,true));
		Background backgroundloss1 = new Background(backgroundloss);
		rootlose.setBackground(backgroundloss1);
		
		ImageView win2 = new ImageView(win);
		BackgroundImage backgroundwin = new BackgroundImage(win, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,true));
		Background backgroundwin1 = new Background(backgroundwin);
		rootlose.setBackground(backgroundwin1);
		createGridPane();

		// Create the buttons


		// Apply styling to the buttons
		moveUP.setStyle("-fx-font-size: 16px; -fx-font-family: Arial;");
		moveDOWN.setStyle("-fx-font-size: 16px; -fx-font-family: Arial;");
		moveLEFT.setStyle("-fx-font-size: 16px; -fx-font-family: Arial;");
		moveRIGHT.setStyle("-fx-font-size: 16px; -fx-font-family: Arial;");

		// Create an HBox for the buttons and set alignment
		HBox buttonBox = new HBox(moveLEFT, moveDOWN, moveRIGHT);
		buttonBox.setAlignment(Pos.BOTTOM_LEFT);
		buttonBox.setSpacing(5);

		// Create a VBox for the buttons and add the "UP" button
		buttonVBox.getChildren().addAll(moveUP, buttonBox);
		buttonVBox.setAlignment(Pos.CENTER);
		buttonVBox.setSpacing(5);

		//create a vbox for control cure attack endturn and use special\
		VBox buttonBox2 = new VBox(buttonVBox,attack,cure,useSpecial,endTurn);
		buttonBox2.setAlignment(Pos.CENTER);
		buttonBox2.setSpacing(5);

		// Create the root BorderPane and set the GridPane and buttonVBox
		root1.getChildren().add(heroesAlive);
		heroVBOx.setAlignment(Pos.BOTTOM_LEFT);
		heroVBOx.setSpacing(5);
		

		root1.setCenter(gridPane);
		root1.setBottom(heroVBOx);
		root1.setRight(buttonBox2);

		// Create the scene with the root BorderPane
		Scene nextScene = new Scene(root1, 800, 600);



		// Set column and row constraints to evenly distribute the cells
		for (int i = 0; i < 15; i++) {
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setHalignment(HPos.CENTER);
			gridPane.getColumnConstraints().add(colConstraints);

			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setValignment(VPos.CENTER);
			gridPane.getRowConstraints().add(rowConstraints);
		}


		//move
		gridPane.setOnMouseClicked(event -> {
			int numRows = gridPane.getRowConstraints().size();
			int numCols = gridPane.getColumnConstraints().size();

			double cellWidth = gridPane.getWidth() / numCols;
			double cellHeight = gridPane.getHeight() / numRows;

			int clickedRow = (int) (event.getY() / cellHeight);
			int clickedCol = (int) (event.getX() / cellWidth);

			System.out.println("Clicked cell: Row " + clickedRow + ", Column " + clickedCol);
			if( Game.map[clickedRow][clickedCol].isVisible()) {
				if(Game.map[clickedRow][clickedCol] instanceof CharacterCell) {
					if(((CharacterCell) Game.map[clickedRow][clickedCol]).getCharacter() instanceof Hero) {
						if (event.getButton() == MouseButton.PRIMARY) {
							player = SelectedHero(clickedRow,clickedCol);
							updateVBoxCharacter(player);
							System.out.println("HEro clicked");
						} 
						if(event.getButton() == MouseButton.SECONDARY) {
							player.setTarget(SelectedHero(clickedRow,clickedCol));

						}
						//moveeeeeeeeeeeeeeeeeee
						moveUP.setOnAction(e -> {
							// Perform action when moveUP button is clicked
							System.out.println("UP button clicked");
							try {
								if(newLocationTrap(player,Direction.RIGHT)) {
									Alert alert = new Alert(AlertType.ERROR);
									alert.setTitle("Trap");
									alert.setHeaderText("YOU ENTERED A TRAP TAKE CARE NEXT TIMEEE!!!");
									alert.getButtonTypes().setAll(ButtonType.OK);
									alert.showAndWait();
								}

								player.move(Direction.RIGHT);
								updateVBoxCharacter(player);
								gridPaneUpdate();							

							} catch (MovementException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								showExceptionPopup(e1);
							} catch (NotEnoughActionsException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								showExceptionPopup(e1);
							}
							if(Game.checkWin()) {
								stage.setScene(scenewin);



							}
							if(Game.checkGameOver()) {
								stage.setScene(scenelose);


							}
						});
					}

					moveDOWN.setOnAction(e -> {
						// Perform action when moveDOWN button is clicked
						System.out.println("DOWN button clicked");
						try {
							if(newLocationTrap(player,Direction.LEFT)) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Trap");
								alert.setHeaderText("YOU ENTERED A TRAP TAKE CARE NEXT TIMEEE!!!");
								alert.getButtonTypes().setAll(ButtonType.OK);
								alert.showAndWait();
							}
							player.move(Direction.LEFT);
							updateVBoxCharacter(player);
							gridPaneUpdate();	
						} catch (MovementException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						}
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);
						}
					});

					moveLEFT.setOnAction(e -> {
						// Perform action when moveLEFT button is clicked
						System.out.println("LEFT button clicked");
						try {
							if(newLocationTrap(player,Direction.DOWN)) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Trap");
								alert.setHeaderText("YOU ENTERED A TRAP TAKE CARE NEXT TIMEEE!!!");
								alert.getButtonTypes().setAll(ButtonType.OK);
								alert.showAndWait();
							}
							player.move(Direction.DOWN);
							updateVBoxCharacter(player);
							gridPaneUpdate();	
						} catch (MovementException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						}
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);


						}
					});

					moveRIGHT.setOnAction(e -> {
						// Perform action when moveRIGHT button is clicked
						System.out.println("RIGHT button clicked");
						try {
							if(newLocationTrap(player,Direction.UP)) {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Trap");
								alert.setHeaderText("YOU ENTERED A TRAP TAKE CARE NEXT TIMEEE!!!");
								alert.getButtonTypes().setAll(ButtonType.OK);
								alert.showAndWait();
							}
							player.move(Direction.UP);
							updateVBoxCharacter(player);
							gridPaneUpdate();	
						} catch (MovementException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						}
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);


						}
					});

				}
				if (((CharacterCell) Game.map[clickedRow][clickedCol]).getCharacter() instanceof Zombie) {
					Zombie selected =SelectedZombie(clickedRow,clickedCol);
					player.setTarget(selected);

					//attackkkkkkkkkkkkkkkkkkkkkkkk
					attack.setOnAction(e ->{

						System.out.println("ATTACK button clicked");

						try {
							int x = selected.getLocation().x;
							int y = selected.getLocation().y;
							player.attack();
							
							updateVBoxCharacter(player);
							System.out.println(selected.getCurrentHp());
							if(selected.getCurrentHp()==0) {
								Game.map[x][y].setVisible(true);
								player.setTarget(null);
								gridPaneUpdate();
								
							}
							gridPaneUpdate();
						}
						catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (InvalidTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						}
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);


						}

					});

					cure.setOnAction(e ->{
						try {
							player.cure();
							updateVBoxCharacter(player);
							gridPaneUpdate();
						} catch (NoAvailableResourcesException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (InvalidTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} 
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);


						}
					});

				}
				else {
					if(((CharacterCell) Game.map[clickedRow][clickedCol]).getCharacter() instanceof Medic) {
						Hero selected =SelectedHero(clickedRow,clickedCol);
						player.setTarget(selected);}


					attack.setOnAction(e ->{
						System.out.println("ATTACK button clicked");
						updateVBoxCharacter(player);
						try {
							player.attack();
							updateVBoxCharacter(player);


						} catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (InvalidTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						}
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);


						}

					});
					cure.setOnAction(e ->{
						try {
							player.cure();
							updateVBoxCharacter(player);
						} catch (NoAvailableResourcesException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (InvalidTargetException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} catch (NotEnoughActionsException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							showExceptionPopup(e1);
						} 
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);


						}
					});
					useSpecial.setOnAction(e ->{
						try {

							player.useSpecial();
							updateVBoxCharacter(player);
							gridPaneUpdate();
						} catch (NoAvailableResourcesException e1) {
							showExceptionPopup(e1);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							showExceptionPopup(e1);
							e1.printStackTrace();
						}
						if(Game.checkWin()) {
							stage.setScene(scenewin);


						}
						if(Game.checkGameOver()) {
							stage.setScene(scenelose);
						}

					});
				}
				endTurn.setOnAction(e ->{

					try {
						Game.endTurn();
						updateVBoxCharacter(player);
						gridPaneUpdate();
					} catch (NotEnoughActionsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(Game.checkWin()) {
						stage.setScene(scenewin);


					}
					if(Game.checkGameOver()) {
						stage.setScene(scenelose);


					}

				});

			}


		});

		Button Start = new Button();
		Image startButton = new Image("./views/startgame.png") ;
		ImageView x = new ImageView(startButton);
		Start.setGraphic(x);

		Button Exit = new Button();
		Image ExitButton = new Image("./views/Exit.png") ;
		ImageView y = new ImageView(ExitButton);
		Exit.setGraphic(y);

		Button options = new Button();
		Image optionsButton = new Image("./views/options.png") ;
		ImageView z = new ImageView(optionsButton);
		options.setGraphic(z); 

		//		//gifbackground2
		//		Image gifImage = new Image("file:path/to/your/image.gif");
		//        ImageView imageView = new ImageView(gifImage);
		//        BackgroundImage backgroundImageg = new BackgroundImage(gifImage, BackgroundRepeat.NO_REPEAT,
		//				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,true));
		//		Background backgroundg = new Background(backgroundImageg);



		Image image =new Image("./views/back.jpg");
		Image image2 =new Image("./views/back2.jpg");
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,true));
		Background background = new Background(backgroundImage);

		//background game
		Image image1 =new Image("./views/backgroundgame.jpg");
		BackgroundImage backgroundImage1 = new BackgroundImage(image1, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,true));
		Background background1 = new Background(backgroundImage1);
		root1.setBackground(background1);




		VBox layout = new VBox(Start,options,Exit);
		layout.setBackground(background);
		layout.setAlignment(Pos.BOTTOM_LEFT);
		layout.setSpacing(10);
		Scene scene = new Scene(layout, 800, 600);
		VBox vbox = new VBox();

		HBox heroes = new HBox();
		Game.loadHeroes("Heroes.csv");
		Tooltip tooltip = new Tooltip();
		Tooltip.install(heroes, tooltip);
		for(int i = 0; i<8;i++) {
			Hero player =Game.availableHeroes.get(i);
			System.out.println(player.getName());
			Button x1 = new Button();
			Image hero = new Image("./views/"+Game.availableHeroes.get(i).getName()+".png") ;
			ImageView x2 = new ImageView(hero);
			x2.setFitWidth(60);
			x2.setFitHeight(60);
			x1.setGraphic(x2);
			heroes.getChildren().add(x1);

			Popup popup = new Popup();
			popup.setAutoHide(true);

			x1.setOnMouseEntered(event -> {
				String text = "";
				if(player instanceof Fighter) {
					text = "Type: Fighter "+ "\n"+text;
				}
				if(player instanceof Medic) {
					text = "Type: Medic "+ "\n"+text;
				}
				if(player instanceof Explorer) {
					text = "Type: Explorer "+ "\n"+text;
				}

				Label tooltipLabel = new Label(
						text+ "Name: " + player.getName() +
						"\nMax HP: " + player.getMaxHp() +
						"\nAttack Dmg: " + player.getAttackDmg()
						+  "\nMax Actions: " + player.getMaxActions()
						);
				StackPane tooltipPane = new StackPane(tooltipLabel);
				tooltipPane.setStyle("-fx-background-color: #F8F8F8;"); // Replace with your desired bright color
				tooltipLabel.setPadding(new Insets(5)); // Optional: Add padding to the label
				popup.getContent().clear();
				popup.getContent().add(tooltipPane);
				popup.show(x1, event.getScreenX(), event.getScreenY());
			});

			x1.setOnMouseExited(event -> {
				popup.hide();
			});
			x1.setOnAction(event -> {
				System.out.println(player.getName());
				if(!heroSelected) {
					Button Select = selectButtonMaker();
					heroSelected = true;
					vbox.getChildren().addAll(Select);
					Select.setOnAction(e -> {

						Game.startGame(player);
						stage.setScene(nextScene);

						mapStartGame(player);

					});
				}
			});


		}
		StackPane stackPane = new StackPane();
		vbox.getChildren().add(heroes);
		heroes.setSpacing(15);
		heroes.setAlignment(Pos.CENTER);
		vbox.setSpacing(15);
		vbox.setAlignment(Pos.CENTER);

		stackPane.getChildren().add(vbox);

		TextField textBox = new TextField("PLEASE CHOOSE A HERO");
		textBox.setEditable(false); // To prevent editing the text
		textBox.setAlignment(Pos.TOP_CENTER);
		textBox.setStyle("-fx-background-color: black;"); // Set the background color to transparent
		textBox.setStyle("-fx-font-size: 20px;"); // Increase the font size

		stackPane.getChildren().add(textBox);
		stackPane.setAlignment(textBox, Pos.TOP_CENTER);


		BorderPane root = new BorderPane();

		root.setCenter(stackPane);
		root.setBackground(background1);






		Scene scene2 = new Scene(root,800,600);
		stage.setTitle("d3flt the last of last");		
		stage.getIcons().add(image2);
		stage.setScene(scene);
		stage.show();
		options.setOnAction(event -> {
			System.out.println("Button clicked");
			// Perform any actions you want when the button is clicked
		});
		Exit.setOnAction(event -> {
			Platform.exit();
		});
		Start.setOnAction(event -> {
			BackgroundImage backgroundImage2 = new BackgroundImage(image2, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100,100,true,true,true,true));

			Background background2 = new Background(backgroundImage2);
			vbox.setBackground(background2);

			stage.setScene(scene2);
			stage.setTitle("Heroes Selection");	

		});



	}
	public Hero SelectedHero(int x, int y) {
		return (Hero) ((CharacterCell) Game.map[x][y]).getCharacter();

	}
	private Zombie SelectedZombie(int x,int y) {
		return (Zombie) ((CharacterCell) Game.map[x][y]).getCharacter();
	}


	public Button selectButtonMaker() {
		Button select = new Button();
		Image hero = new Image("./views/next.png") ;
		ImageView x2 = new ImageView(hero);
		x2.setFitWidth(60);
		x2.setFitHeight(60);
		select.setGraphic(x2);
		select.setLayoutX(10);
		select.setLayoutY(10);
		return select;

	}
	public void Move(Hero h, Direction d) {

	}



	public void updateVisibile() {

		for(int i = 0; i<15;i++) {
			for(int j = 14; j>=0; j--) {
				if(!(Game.map[i][j].isVisible())) {
					ImageView clouds2 = new ImageView(clouds);

					clouds2.setFitWidth(30);
					clouds2.setFitHeight(30);
					gridPane.add(clouds2, i, j);

				}
				else {
					ImageView clouds2 = new ImageView(clouds);
					gridPane.getChildren().remove(clouds2);
				}
			}
		}
	}




	public void mapStartGame(Hero h) {
		Image hero = new Image("./views/"+h.getName()+".png") ;
		ImageView x3 = new ImageView(hero);
		x3.setFitWidth(30);
		x3.setFitHeight(30);
		x3.setRotate(90);
		gridPane.add(x3, 0, 0);


		for(int i = 0; i<15;i++) {
			for ( int j = 0;j<15;j++) {


				if(Game.map[i][j] instanceof TrapCell) {
					ImageView vaccine2 = new ImageView(trap);
					vaccine2.setFitWidth(30);
					vaccine2.setFitHeight(30);
					vaccine2.setRotate(90);
					gridPane.add(vaccine2, j, i);

				}
				if(Game.map[i][j] instanceof CollectibleCell) {
					if(((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Vaccine) {
						ImageView vaccine2 = new ImageView(vaccine);
						vaccine2.setFitWidth(30);
						vaccine2.setFitHeight(30);
						vaccine2.setRotate(90);
						gridPane.add(vaccine2, j, i);

					}
					else {
						ImageView supply2 = new ImageView(supply);
						supply2.setFitWidth(30);
						supply2.setFitHeight(30);
						supply2.setRotate(90);
						gridPane.add(supply2, j, i);

					}
				}
				if(Game.map[i][j] instanceof CharacterCell) {
					if((((CharacterCell) Game.map[i][j]).getCharacter() instanceof Zombie)) {
						ImageView zombie2 = new ImageView(zombie);
						zombie2.setFitWidth(30);
						zombie2.setFitHeight(30);
						zombie2.setRotate(90);
						gridPane.add(zombie2, j, i);
					}

				}


			}

		}

		updateVisibile();
	}




	public static VBox createDetailsPanelForHero(Hero hero) {
		VBox panel = new VBox();
		//        panel.setId("details-panel");

		HashMap<String, Object> heroDetails = new LinkedHashMap<>();

		heroDetails.put("Max HP", hero.getMaxHp());
		heroDetails.put("Attack Damage", hero.getAttackDmg());
		heroDetails.put("Max Action Points", hero.getMaxActions());


		panel.getChildren().add(ViewHelper.detailsBox(hero.getName(),heroDetails));

		return panel;
	}


	private void createGridPane() {
		//gridPane = new GridPane();

		// Create the grid map
		for (int row = 0; row <15; row++) {
			for (int col = 0; col < GRID_SIZE; col++) {
				// Create a square node for each grid cell
				Button button = new Button();
				gridPane.add(button, row, 14-col);
			}
		}

		gridPane.setAlignment(Pos.CENTER);
		for (int row = 0; row < 15; row++) {
			for (int col = 0; col < 15; col++) {
				ImageView tile2 = new ImageView(tile); // Create a new instance of ImageView
				tile2.setFitWidth(30);
				tile2.setFitHeight(30);
				tile2.setRotate(90);
				gridPane.add(tile2, col, row);

			}
		}
		gridPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		gridPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		Pane gridPaneWrapper = new Pane(gridPane);
		gridPaneWrapper.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
		gridPane.setRotate(270);


	}
	//	public void updateVBoxCharacter(Hero player) {
	//		VBox vbox = new VBox();
	//		String text="";
	//		Label textLabel = new Label(text);
	//		
	//		if (flag)
	//			 vbox.getChildren().remove(textLabel);
	//
	//		
	//		vbox.setStyle("-fx-background-color: lightblue;");
	//		text = "Name: " + player.getName() +
	//				"\nCurrent HP: " + player.getCurrentHp() +
	//				"\nAttack Dmg: " + player.getAttackDmg()
	//				+  "\nAvailable Actions: " + player.getActionsAvailable()
	//				+ "\nNumber of Vaccines: "+ player.getVaccineInventory().size()+
	//				"\nNumber of Supplies: "+ player.getSupplyInventory().size();
	//		if(player instanceof Fighter) {
	//			text = "Type: Fighter "+ "\n"+text;
	//		}
	//		if(player instanceof Medic) {
	//			text = "Type: Medic "+ "\n"+text;
	//		}
	//		if(player instanceof Explorer) {
	//			text = "Type: Explorer "+ "\n"+text;
	//		}
	//		// Create a Label for the text
	//		 textLabel = new Label(text);
	//		textLabel.setStyle("-fx-font-size: 18px;");
	//		// Add the label to the VBox
	//		vbox.getChildren().add(textLabel);
	//		vbox.setAlignment(Pos.TOP_LEFT);
	//		buttonVBox.getChildren().add(vbox);
	//		flag=true;
	//	}
	private VBox vbox; // Declare the VBox as a class member

	public void updateVBoxCharacter(Hero player) {
		String text = "";
		Label textLabel;

		// Check if the VBox for the current player already exists
		VBox currentPlayerVBox = findVBoxForPlayer(player);

		if (currentPlayerVBox == null) {
			// Create a new VBox for the current player
			currentPlayerVBox = new VBox();
			currentPlayerVBox.setStyle("-fx-background-color: lightblue;");
			currentPlayerVBox.setAlignment(Pos.CENTER);
			heroVBOx.getChildren().add(currentPlayerVBox);

			// Add the VBox to the list
			heroInfoList.add(currentPlayerVBox);
		} else {
			// The VBox for the current player already exists, so don't clear its content
			currentPlayerVBox.getChildren().clear();
		}

		// Clear the content of other VBox nodes
		// clearOtherVBoxContent(currentPlayerVBox);
		if(player.getCurrentHp()!=0) {
			ProgressBar hpProgressBar = new ProgressBar((double) player.getCurrentHp() / (double) player.getMaxHp());
			hpProgressBar.setStyle("-fx-accent: green;");

			ProgressBar actionsProgressBar = new ProgressBar((double) player.getActionsAvailable() / (double) player.getMaxActions());
			actionsProgressBar.setStyle("-fx-accent: blue;");

			// Update the labels and progress bars for the current player
			if (player instanceof Fighter) {
				text = "Type: Fighter\n" + text;
			} else if (player instanceof Medic) {
				text = "Type: Medic\n" + text;
			} else if (player instanceof Explorer) {
				text = "Type: Explorer\n" + text;
			}
			Label name = new Label("Name: " + player.getName());
			name.setStyle("-fx-font-size: 20px;");

			textLabel = new Label(text);
			textLabel.setStyle("-fx-font-size: 15px;");

			currentPlayerVBox.getChildren().addAll(name, textLabel);

			text = "Attack Damage: " + player.getAttackDmg() +
					"\nNumber of Vaccines: " + player.getVaccineInventory().size() +
					"\nNumber of Supplies: " + player.getSupplyInventory().size();
			textLabel = new Label(text);
			textLabel.setStyle("-fx-font-size: 13px;");

			currentPlayerVBox.getChildren().addAll(new Label("Current HP: " + player.getCurrentHp()), hpProgressBar,
					new Label("Available Actions: " + player.getActionsAvailable()), actionsProgressBar, textLabel);
		}
	}
	// Helper method to find the VBox associated with a player
	private VBox findVBoxForPlayer(Hero player) {
		for (VBox vbox : heroInfoList) {
			if (vbox.getChildren().size() > 0) {
				Node firstNode = vbox.getChildren().get(0);
				if (firstNode instanceof Label) {
					String playerName = ((Label) firstNode).getText().replace("Name: ", "");
					if (playerName.equals(player.getName())) {
						return vbox;
					}
				}
			}
		}
		return null;
	}
	//	public void updateVBoxCharacter(Hero player) {
	//	    String text = "";
	//	    Label textLabel;
	//
	//	    if (vbox == null) {
	//	        vbox = new VBox();
	//	        vbox.setStyle("-fx-background-color: lightblue;");
	//	        vbox.setAlignment(Pos.CENTER);
	//	        buttonVBox.getChildren().add(vbox);
	//	    } else {
	//	        vbox.getChildren().clear(); // Clear the existing content of the VBox
	//	    }
	//	    
	//	    ProgressBar hpProgressBar = new ProgressBar((double)player.getCurrentHp() / (double)player.getMaxHp());
	//        hpProgressBar.setStyle("-fx-accent: green;");
	//
	////        ProgressBar dmgProgressBar = new ProgressBar(player.getAttackDmg() / 100.0);
	////        dmgProgressBar.setStyle("-fx-accent: red;");
	//
	//        ProgressBar actionsProgressBar = new ProgressBar((double)player.getActionsAvailable() /(double)player.getMaxActions());
	//        actionsProgressBar.setStyle("-fx-accent: blue;");
	//
	//        if (player instanceof Fighter) {
	//	        text = "Type: Fighter\n" + text;
	//	    } else if (player instanceof Medic) {
	//	        text = "Type: Medic\n" + text;
	//	    } else if (player instanceof Explorer) {
	//	        text = "Type: Explorer\n" + text;
	//	    }
	//        Label name=new Label("Name: "+player.getName());
	//        name.setStyle("-fx-font-size: 20px;");
	//
	//        textLabel = new Label(text);
	//	    textLabel.setStyle("-fx-font-size: 15px;");
	//	    vbox.getChildren().addAll(name,textLabel);
	//	    
	//	    text = "Attack Damage: "+player.getAttackDmg()+
	//	    		"\nNumber of Vaccines: " + player.getVaccineInventory().size() +
	//	            "\nNumber of Supplies: " + player.getSupplyInventory().size();
	//	    textLabel = new Label(text);
	//	    textLabel.setStyle("-fx-font-size: 13px;");
	//	    
	//        vbox.getChildren().addAll( new Label("Current HP: "+player.getCurrentHp()), hpProgressBar,
	//                new Label("Available Actions: "+player.getActionsAvailable()), actionsProgressBar,textLabel);
	//	}


	public void gridPaneUpdate() {
		//gridPane = null;
		root1.getChildren().remove(gridPane);
		createGridPane();
		root1.getChildren().add(gridPane);
		for(int i = 0; i<15;i++) {
			for ( int j = 0;j<15;j++) {
				if(Game.map[i][j] instanceof TrapCell) {
					ImageView vaccine2 = new ImageView(trap);
					vaccine2.setFitWidth(30);
					vaccine2.setFitHeight(30);
					vaccine2.setRotate(90);
					gridPane.add(vaccine2, j, i);

				}

				if(Game.map[i][j] instanceof CollectibleCell) {
					if(((CollectibleCell) Game.map[i][j]).getCollectible() instanceof Vaccine) {
						ImageView vaccine2 = new ImageView(vaccine);
						vaccine2.setFitWidth(30);
						vaccine2.setFitHeight(30);
						vaccine2.setRotate(90);
						gridPane.add(vaccine2, j, i);

					}
					else {
						ImageView supply2 = new ImageView(supply);
						supply2.setFitWidth(30);
						supply2.setFitHeight(30);
						supply2.setRotate(90);
						gridPane.add(supply2, j, i);

					}
				}
				else if(Game.map[i][j] instanceof CharacterCell) {
					if(((CharacterCell) Game.map[i][j]).getCharacter()==null){
						ImageView tile2 = new ImageView(tile);
						tile2.setFitWidth(30);
						tile2.setFitHeight(30);
						tile2.setRotate(90);
						gridPane.add(tile2, j, i);

					}
					else if((((CharacterCell) Game.map[i][j]).getCharacter() instanceof Zombie)) {
						ImageView zombie2 = new ImageView(zombie);
						zombie2.setFitWidth(30);
						zombie2.setFitHeight(30);
						zombie2.setRotate(90);
						gridPane.add(zombie2, j, i);
					}
					else {
						String heroName = ((CharacterCell) Game.map[i][j]).getCharacter().getName();
						Hero x = (Hero)((CharacterCell) Game.map[i][j]).getCharacter();
						Image hero1 = (( new Image("./views/"+x.getName()+".png")));
						ImageView hero2 = new ImageView(x.getName());
						hero2.setFitWidth(30);
						hero2.setFitHeight(30);
						hero2.setRotate(90);
						gridPane.add(hero2, j, i);

					}

				}
				if(!(Game.map[i][j].isVisible())) {
					ImageView clouds2 = new ImageView(clouds);
					clouds2.setFitWidth(30);
					clouds2.setFitHeight(30);
					clouds2.setRotate(90);
					gridPane.add(clouds2, j, i);
				}


			}

		}
	}
	private void showExceptionPopup(Exception exception) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception");
		alert.setHeaderText("An exception occurred");
		alert.setContentText(exception.getMessage());

		// Add "OK" button to the alert
		alert.getButtonTypes().setAll(ButtonType.OK);

		// Handle button click event
		alert.setOnCloseRequest(e -> {
			// Remove the alert when the button is clicked
			alert.close();
		});

		alert.showAndWait();
	}
	public boolean newLocationTrap(Hero h,Direction d) {
		int tX = h.getLocation().x;
		int tY = h.getLocation().y;
		switch (d) {
		case DOWN:
			tX--;
			break;
		case LEFT:
			tY--;
			break;
		case RIGHT:
			tY++;
			break;
		case UP:
			tX++;
			break;
		}
		if (tX < 0 || tY < 0 || tX > Game.map.length - 1 || tY > Game.map.length - 1)
			return false;
		else if(Game.map[tX][tY] instanceof TrapCell) {
			return true ;
		}
		return false;
	}




	public static void main(String[] args) {
		launch(args);
	}
}
