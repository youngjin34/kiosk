package org.zerock.kioskproject;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KioskMain extends Application {
  private Stage primaryStage;
  private Scene mainScene;
  private Scene orderScene;
  private Scene paymentScene;
  private Scene infoScene;

  private KioskOrderController orderController;
  private KioskPaymentController paymentController;

  @Override
  public void start(Stage stage) throws IOException {
    this.primaryStage = stage;

    // 메인 화면 로드
    FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("KioskMain.fxml"));
    mainScene = new Scene(mainLoader.load(), 700, 950);

    // 주문 화면 로드
    FXMLLoader orderLoader = new FXMLLoader(getClass().getResource("KioskOrder.fxml"));
    orderScene = new Scene(orderLoader.load(), 700, 950);
    orderController = orderLoader.getController();

    // 결제 화면 로드
    FXMLLoader paymentLoader = new FXMLLoader(getClass().getResource("KioskPayment.fxml"));
    paymentScene = new Scene(paymentLoader.load(), 700, 950);
    paymentController = paymentLoader.getController();

    // 정보 화면 로드
    FXMLLoader infoLoader = new FXMLLoader(getClass().getResource("KioskInfo.fxml"));
    infoScene = new Scene(infoLoader.load(), 700, 950);

    // 메인 화면 컨트롤러 설정
    KioskMainController mainController = mainLoader.getController();
    mainController.setMainApp(this);

    // 주문 화면 컨트롤러 설정
    orderController.setMainApp(this);

    // 결제 화면 컨트롤러 설정
    paymentController.setMainApp(this);

    // 정보 화면 컨트롤러 설정
    KioskInfoController infoController = infoLoader.getController();
    infoController.setMainApp(this);

    // 초기 화면 설정
    stage.setTitle("Kiosk Application");
    stage.setScene(mainScene);
    stage.show();
  }

  public KioskPaymentController getPaymentController() {
    return paymentController;
  }

  public KioskOrderController getOrderController() {
    return orderController; // 주문 화면 컨트롤러 반환
  }

  public void switchToOrderScene() {
    primaryStage.setScene(orderScene);
  }

  public void switchToPaymentScene() {
    primaryStage.setScene(paymentScene);
    paymentController.updatePaymentView(); // 장바구니 아이템 업데이트
  }

  public void switchToInfoScene() {
    primaryStage.setScene(infoScene);
  }

  public void switchToMainScene() {
    primaryStage.setScene(mainScene); // 메인 화면으로 전환
  }

  // 주문 화면 컨트롤러를 통해 장바구니 아이템 가져오기
  public ObservableList<CartItem> getCartItems() {
    return orderController.getCartItems(); // OrderController에서 장바구니 아이템 반환
  }

  public static void main(String[] args) {
    launch();
  }
}
