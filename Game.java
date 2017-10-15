

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class Game extends Application{

    private GridPane gridPaneMatriz;
    private Label[][] matriz;
    private Calcular calcular;
    private Button buttonStart, buttonPausa, buttonRestaurar; //buttonRedefine;
    private String colorLife, colorDeath;
    private Timeline animacion;
    private ComboBox<String> comboBox;
    private Rectangle2D primaryScreenBounds;
    private int x;
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Ingrese tamaño de tablero");
        
        primaryScreenBounds = Screen.getPrimary().getVisualBounds();   
        
        colorLife = "#000000";
        colorDeath = "#FFFAFA";
        
        BorderPane root = new BorderPane();
        root.getStylesheets().addAll(getClass().getResource("StyleGame.css").toExternalForm());
        
        //Matrix
        gridPaneMatriz = new GridPane();
        gridPaneMatriz.setPrefSize(primaryScreenBounds.getWidth() * 0.60, primaryScreenBounds.getHeight() * 0.75);
        gridPaneMatriz.setPadding(new Insets(5, 20, 5, 20));
        gridPaneMatriz.setAlignment(Pos.CENTER);
        gridPaneMatriz.setVisible(true);
       
        String tamano = JOptionPane.showInputDialog("Ingrese tamaño del tablero");
        x=Integer.parseInt(tamano);
        
        definirMatriz(x, x);
        
        //Animation y velocidad
        

     
        animacion = new Timeline(new KeyFrame(Duration.millis(70), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                calcular.crear();
                calcular.incrementargeneracion(1);
                actualizargeneracion();
                actualizarpoblacion();
            }
        }));
        
        animacion.setCycleCount(Timeline.INDEFINITE);
        
        calcular = new Calcular(matriz, colorDeath, colorLife);      
        
        //Buttons
        HBox boxTop = new HBox();
        boxTop.setPadding(new Insets(30, 10, 10, 10));
        boxTop.setSpacing(15);
        boxTop.setAlignment(Pos.CENTER);
        boxTop.setStyle("-fx-background-color: #D3D3D3;");
        
        buttonStart = new Button("Start Game");
        buttonStart.setPrefSize(145, 40);
        buttonStart.getStyleClass().add("button");
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (Integer.valueOf(calcular.obtenerpoblacion()) > 0) {
                    animacion.play();
                    ((Button)e.getSource()).setDisable(true);
                    buttonPausa.setDisable(false);
                    buttonRestaurar.setDisable(true);
                    //buttonRedefine.setDisable(true);
                    comboBox.setDisable(true);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Esta vacio");
                    alert.setHeaderText(null);
                    alert.setContentText("No se ha activado ninguna vida");
                    alert.showAndWait();
                }
            }
        });
        
        buttonPausa = new Button("Pausa");
        buttonPausa.setDisable(true);
        buttonPausa.setPrefSize(145, 40);
        buttonPausa.getStyleClass().add("button");
        buttonPausa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                animacion.stop();
                ((Button) event.getSource()).setDisable(true);
                buttonStart.setDisable(false);
                buttonRestaurar.setDisable(false);
            }
        });
        
        
        buttonRestaurar = new Button("Restaurar");
        buttonRestaurar.setDisable(false);
        buttonRestaurar.setPrefSize(145, 40);
        buttonRestaurar.getStyleClass().add("button");
        buttonRestaurar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {               
                calcular.restaurarMatriz();
                calcular.setgeneracion(0l);
                calcular.setpoblacion(0l);
                actualizargeneracion();
                actualizarpoblacion();
                //buttonRedefine.setDisable(false);
                comboBox.setDisable(false);
                ((Button)event.getSource()).setDisable(true);
            }
        });
        
          Label LabelDefineGrid = new Label("Game of Life");
          LabelDefineGrid.getStyleClass().add("label-information");
           
        
        boxTop.getChildren().addAll(LabelDefineGrid);
        
       HBox boxBottom = new HBox();
       boxBottom.setPadding(new Insets(10, 10, 50, 10));
       boxBottom.setSpacing(10);
       boxBottom.setAlignment(Pos.BOTTOM_CENTER);
       boxBottom.setStyle("-fx-background-color: #D3D3D3;");
        
      // comboBox = new ComboBox<>();
      // comboBox.getItems().addAll("80x40", "70x35", "60x30", "50x25", "40x20");      
      // comboBox.setValue("60x30");
        
       // Label LabelDefineGrid = new Label("Redefine Grid: ");
      //  LabelDefineGrid.getStyleClass().add("label-information");
        
       // buttonRedefine = new Button("Redefine");
      //  buttonRedefine.getStyleClass().add("button");
      //  buttonRedefine.setPrefSize(145, 40);
     //   buttonRedefine.setOnMouseClicked(new EventHandler<MouseEvent>() {
       //     @Override
       //     public void handle(MouseEvent event) {   
         //       gridPaneMatrix.getChildren().clear();
       //         String[] coordinates = comboBox.getValue().split("x");
                
        //        defineMatrix(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]));
       //         calculator.redefineReplic(matrix);
       //         root.setCenter(gridPaneMatrix);
        //    }
      //  });
        
      boxBottom.getChildren().addAll(buttonStart, buttonPausa, buttonRestaurar);
        
        root.setTop(boxTop);
        root.setCenter(gridPaneMatriz);
        root.setBottom(boxBottom);
        root.getStyleClass().add("border-pane");
               
        Scene scene = new Scene(root, (primaryScreenBounds.getWidth() * 0.5625), (primaryScreenBounds.getHeight() * 0.867));

        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);      
        primaryStage.centerOnScreen();
        primaryStage.show();         
    }
    
    private void definirMatriz(int rows, int cols) {  
        
        matriz = new Label[rows][cols];
        
        for (int i = 0; i < matriz.length; i++) 
            for (int j = 0; j < matriz[i].length; j++) {
                
                matriz[i][j] = new Label();
                matriz[i][j].setAccessibleHelp(i+","+j);
                matriz[i][j].getStyleClass().add("classic-label");
                matriz[i][j].setStyle("-fx-background-color: "+colorDeath+";");
                matriz[i][j].setMinWidth(primaryScreenBounds.getWidth() * 0.0150);
                matriz[i][j].setMinHeight(primaryScreenBounds.getWidth() * 0.015);
                matriz[i][j].setMaxWidth(primaryScreenBounds.getWidth() * 0.0090);
                matriz[i][j].setMaxHeight(primaryScreenBounds.getWidth() * 0.001);
                matriz[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        String[] coords = ((Label)event.getSource()).getAccessibleHelp().split(",");
                        
                        int x = calcular.modificarReplica(Integer.valueOf(coords[0]) + 1, Integer.valueOf(coords[1]) + 1);
                        ((Label)event.getSource()).setStyle("-fx-background-color: "+((x == 1) ? colorLife : colorDeath)+";");
                        calcular.modificarpoblacion(x == 1);
                        actualizarpoblacion();
                    }
                });
                
                gridPaneMatriz.add(matriz[i][j], i, j);
                
            }
    }
    
    private void actualizargeneracion() {
        
    }
    
    private void actualizarpoblacion() {
       
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }

   // private void JOptionPaneshowInputDialog(String mensaje) {
   //     JOptionPane.showInputDialog("Ingrese tamaño tablero");
        
  
   // }
}

 class Calcular {
    
    private int[][] replica;
    private String colorDeath, colorLife;
    private long poblacion, generacion;
    private Label[][] matriz;
    
    public Calcular(Label[][] matriz, String ColorDeath, String ColorLife) {       
        this.matriz = matriz;
        this.colorLife = ColorLife;
        this.colorDeath = ColorDeath;
        this.poblacion = 0l;
        this.generacion = 0l;
        //This variable is initialized by adding 2 rows and 2 columns, thus the edges are not ignored
        this.replica = new int[matriz.length + 2][matriz[0].length + 2];
    }
    
    /*        
        1 = cell alive
        0 = cell dead

        This method clones the array, however clone () does not clone itself 
        into all elements of the array so I have had to do it manually. 
    */
    private int[][] copiarReplica() {
        int[][] copy = new int[replica.length][];
        
        for (int i = 0; i < copy.length; i++) 
            copy[i] = replica[i].clone();       
        
        return copy;
    }
    
    public int modificarReplica(int x, int y) {
        replica[x][y] = (replica[x][y] == 0) ? 1 : 0;
        
        return replica[x][y];
    }
    
    //This method returns the new pattern.
    public void crear() {
        evaluarReplica(copiarReplica());
    }
    
    private void evaluarReplica(int[][] copia) {
        for (int i = 1; i < copia.length-1; i++) 
            for (int j = 1; j < copia[i].length-1; j++) 
                evaluarCell(i, j, copia);  
    }
    /*   
        This method receives a block (coordinates) and evaluates the block around them. 
        From the counter it is determined whether the block is still alive, 
        dies or not their status is altered ...
    */
    private void evaluarCell(int row, int col, int[][] copy) {
        int count = 0;
        
        for (int i = row - 1; i < (row + 2); i++) 
            for (int j = col - 1; j < (col + 2); j++) 
                if (copy[i][j] == 1 && (i != row || j != col)) 
                    count+=1;             
        
        if ((count < 2 && copy[row][col] == 1) || (count > 3 && copy[row][col] == 1)) {
            poblacion -= 1;
            replica[row][col] = 0;
            matriz[row - 1][col - 1].setStyle("-fx-background-color: "+this.colorDeath+";");
        } else 
            if (count == 3 && copy[row][col] == 0) {
                poblacion += 1;
                replica[row][col] = 1;
                matriz[row - 1][col - 1].setStyle("-fx-background-color: "+this.colorLife+";");                
            }
    }
    
    //Clean the grid
    public void restaurarMatriz() {
        if (poblacion > 0) {
            for (int i = 0, c = 0; i < matriz.length && c < poblacion; i++) 
                for (int j = 0; j < matriz[i].length && c < poblacion; j++) 
                    if (replica[i + 1][j + 1] == 1) {
                        c++;
                        replica[i + 1][j + 1] = 0;
                        matriz[i][j].setStyle("-fx-background-color: "+this.colorDeath+";");
                    }                        
        }
    }
    
    public void redefinirReplica(Label[][] matrix) {
        this.matriz = matrix;
        this.replica = new int[matrix.length + 2][matrix[0].length + 2];
    }
    
    /*        
        "r" is a variable that evaluates whether the block is dead the population is 
        subtracted or otherwise adds. This has more to do in the event that the game itself ...
    */
    public void modificarpoblacion(boolean r) {
        poblacion += (r) ? 1 : -1;
    }
    
    public void incrementargeneracion(int x) {
        generacion += x;
    }
    
    public String obtenerpoblacion() {
        return String.valueOf(poblacion);
    }
    
    public String obtenergeneracion() {
        return String.valueOf(generacion);
    }
    
    public void setgeneracion(long x) {
        generacion = x;
    }
    
    public void setpoblacion(long x) {
        poblacion = x;
    }
    
}
