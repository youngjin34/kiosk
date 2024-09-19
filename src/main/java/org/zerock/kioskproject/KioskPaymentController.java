package org.zerock.kioskproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.animation.PauseTransition;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KioskPaymentController {

  private KioskMain mainApp;
  private ObservableList<CartItem> cartItems;

  @FXML
  private ListView<HBox> paymentListView; // 결제 화면에서 장바구니 아이템을 보여줄 리스트

  @FXML
  private Label totalPriceLabel; // 총 결제 금액을 보여줄 라벨

  @FXML
  private void initialize() {
    // 결제 화면 초기화 로직
    updatePaymentView();
  }

  public void setCartItems(ObservableList<CartItem> cartItems) {
    this.cartItems = cartItems;
    updatePaymentView();
  }

  public void updatePaymentView() {
    if (cartItems != null) {
      ObservableList<HBox> paymentDisplayItems = FXCollections.observableArrayList();
      int totalPrice = 0;

      for (CartItem item : cartItems) {
        HBox itemBox = new HBox(10);
        Label nameLabel = new Label(item.getName());
        Label quantityLabel = new Label("수량: " + item.getQuantity());
        Label priceLabel = new Label(item.getTotalPrice() + "원");

        itemBox.getChildren().addAll(nameLabel, quantityLabel, priceLabel);
        paymentDisplayItems.add(itemBox);

        totalPrice += item.getTotalPrice();
      }

      paymentListView.setItems(paymentDisplayItems);
      totalPriceLabel.setText("총 결제 금액: " + totalPrice + "원");
    }
  }

  @FXML
  private void handleBackButton(ActionEvent event) {
    if (mainApp != null) {
      mainApp.switchToOrderScene(); // 주문 화면으로 돌아가기
    } else {
      System.err.println("MainApp is null");
    }
  }

  @FXML
  private void handlePaymentButton(ActionEvent event) {
    // 결제 버튼 클릭 시 처리할 로직
    showPaymentConfirmation();
  }

  private void showPaymentConfirmation() {
    // 확인 이미지를 별도의 Stage로 표시하기
    Stage confirmationStage = new Stage();
    confirmationStage.initModality(Modality.APPLICATION_MODAL);
    confirmationStage.initOwner(paymentListView.getScene().getWindow());

    ImageView confirmationImage = new ImageView(new Image(getClass().getResourceAsStream("/org/zerock/kioskproject/images/payment.gif")));
    VBox confirmationBox = new VBox(confirmationImage);
    confirmationBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

    Scene confirmationScene = new Scene(confirmationBox, 450, 450); // 이미지 사이즈에 맞게 조절
    confirmationStage.setScene(confirmationScene);
    confirmationStage.show();

    // 잠시 동안 확인 이미지를 보여줍니다
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(e -> {
      confirmationStage.close(); // 확인 이미지가 사라진 후 홈 화면으로 이동
      if (mainApp != null) {
        mainApp.switchToMainScene(); // 홈 화면으로 돌아가기
        System.out.println("결제되었습니다.");
      } else {
        System.err.println("MainApp is null");
      }
    });
    pause.play(); // 3초 동안 확인 이미지 보여주기
  }


  public void setMainApp(KioskMain mainApp) {
    this.mainApp = mainApp;
  }
}
