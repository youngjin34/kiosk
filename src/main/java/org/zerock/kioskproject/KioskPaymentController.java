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
  private KioskOrderController orderController;
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

    // 결제 후 장바구니 초기화
    cartItems.clear();  // 결제 화면의 장바구니 리스트 비우기

    // 주문 화면의 장바구니도 초기화
    if (mainApp != null) {
      KioskOrderController orderController = mainApp.getOrderController(); // 주문 화면 컨트롤러 가져오기
      if (orderController != null) {
        orderController.clearCart(); // 주문 화면의 장바구니 초기화
      }
    }

    // UI 갱신
    updatePaymentView(); // 장바구니가 비워진 상태를 결제 화면에 반영
  }


  private void showPaymentConfirmation() {
    Stage confirmationStage = new Stage();
    confirmationStage.initModality(Modality.APPLICATION_MODAL);
    confirmationStage.initOwner(paymentListView.getScene().getWindow());

    ImageView confirmationImage = new ImageView(new Image(getClass().getResourceAsStream("/org/zerock/kioskproject/images/payment.gif")));
    VBox confirmationBox = new VBox(confirmationImage);
    confirmationBox.setStyle("-fx-alignment: center; -fx-padding: 20;");

    Scene confirmationScene = new Scene(confirmationBox, 450, 450);
    confirmationStage.setScene(confirmationScene);
    confirmationStage.show();

    // 3초 동안 확인 이미지를 보여준 후 결제 완료 처리
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(e -> {
      confirmationStage.close(); // 확인 이미지 닫기
      if (mainApp != null) {
        cartItems.clear(); // 장바구니 비우기
        mainApp.switchToMainScene(); // 홈 화면으로 이동
        System.out.println("결제가 완료되었습니다. 장바구니를 비웁니다.");
      } else {
        System.err.println("MainApp is null");
      }
    });
    pause.play();
  }

  public void switchToOrderScene() {
    // 기존 화면 전환 로직

    // 장바구니가 비워졌으므로 주문 화면에서 UI 갱신
    orderController.updateCartView();
  }


  public void setMainApp(KioskMain mainApp) {
    this.mainApp = mainApp;
  }
}
