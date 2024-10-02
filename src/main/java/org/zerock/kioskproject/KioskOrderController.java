package org.zerock.kioskproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.sql.*;

public class KioskOrderController {

  private KioskMain mainApp;

  @FXML
  private ListView<HBox> cartListView;

  @FXML
  private Label totalLabel;

  @FXML
  private ImageView americanoImage, latteImage, mocaImage, cappuccinoImage, viennaImage, strawberryLatteImage;

  private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

  @FXML
  private void initialize() {
    setImageViewCursor(americanoImage, latteImage, mocaImage, cappuccinoImage, viennaImage, strawberryLatteImage);

    // 메뉴 데이터베이스에서 가져오기
    loadMenuDataFromDatabase();
  }

  @FXML
  private void handleBackButton() {
    if (mainApp != null) {
      mainApp.switchToMainScene(); // 메인 화면으로 전환
    } else {
      System.err.println("MainApp is null");
    }
  }


  private CartItem findCartItemByName(String itemName) {
    return cartItems.stream()
            .filter(item -> item.getName().equals(itemName))
            .findFirst()
            .orElse(null);
  }

  // 추가된 메서드: 이미지 뷰에 커서 설정
  private void setImageViewCursor(ImageView... images) {
    for (ImageView image : images) {
      image.setCursor(Cursor.HAND); // 각 이미지에 HAND 커서를 설정
    }
  }

  // 추가된 메서드: 이미지 ID로 메뉴 이름 반환
  private String getItemNameByImageId(String imageId) {
    switch (imageId) {
      case "americanoImage":
        return "아메리카노";
      case "latteImage":
        return "카페라떼";
      case "mocaImage":
        return "카페모카";
      case "cappuccinoImage":
        return "카푸치노";
      case "viennaImage":
        return "비엔나";
      case "strawberryLatteImage":
        return "딸기라떼";
      default:
        return null;  // 이미지 ID가 없을 경우
    }
  }

  private void loadMenuDataFromDatabase() {
    String url = "jdbc:mariadb://localhost:3305/kiosk";
    String user = "root";
    String password = "root";

    String query = "SELECT menuName, menuPrice FROM kiosk";


    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {

      while (rs.next()) {
        String menuName = rs.getString("menuName");
        int menuPrice = rs.getInt("menuPrice");
        System.out.println("메뉴: " + menuName + ", 가격: " + menuPrice);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onImageClick(MouseEvent event) {
    String itemName = getItemNameByImageId(((ImageView) event.getSource()).getId());
    if (itemName != null) {
      addItemToCart(itemName);
    }
  }

  private void addItemToCart(String itemName) {
    CartItem item = findCartItemByName(itemName);
    if (item != null) {
      item.setQuantity(item.getQuantity() + 1);
    } else {
      int price = getItemPriceFromDatabase(itemName);
      cartItems.add(new CartItem(itemName, 1, price));
    }
    updateCartView();
  }

  private int getItemPriceFromDatabase(String itemName) {
    String url = "jdbc:mariadb://localhost:3305/kiosk";
    String user = "root";
    String password = "root";
    String query = "SELECT menuPrice FROM kiosk WHERE menuName = ?";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, itemName);

      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("menuPrice");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return 0;  // 해당 메뉴가 없으면 0원을 반환
  }

  void updateCartView() {
    ObservableList<HBox> cartDisplayItems = FXCollections.observableArrayList();
    int totalPrice = 0;

    for (CartItem item : cartItems) {
      HBox itemBox = new HBox(10);
      Label nameLabel = new Label(item.getName());
      Button decreaseButton = new Button("-");
      Button increaseButton = new Button("+");
      Button cancelButton = new Button("취소");
      Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
      Label priceLabel = new Label(item.getTotalPrice() + "원");

      decreaseButton.setId("decreaseButton" + item.getName());
      increaseButton.setId("increaseButton" + item.getName());
      cancelButton.setId("cancelButton" + item.getName());
      decreaseButton.setOnAction(this::changeQuantity);
      increaseButton.setOnAction(this::changeQuantity);
      cancelButton.setOnAction(this::removeItemFromCart);

      itemBox.getChildren().addAll(nameLabel, priceLabel, decreaseButton, quantityLabel, increaseButton, cancelButton);
      cartDisplayItems.add(itemBox);

      totalPrice += item.getTotalPrice();
    }

    cartListView.setItems(cartDisplayItems);
    totalLabel.setText("총 가격: " + totalPrice + "원");
  }

  @FXML
  public void changeQuantity(ActionEvent event) {
    Button source = (Button) event.getSource();
    String buttonId = source.getId();
    String itemName = buttonId.startsWith("increase") ? buttonId.replace("increaseButton", "") : buttonId.replace("decreaseButton", "");

    CartItem targetItem = findCartItemByName(itemName);

    if (targetItem != null) {
      if (buttonId.startsWith("increase")) {
        targetItem.setQuantity(targetItem.getQuantity() + 1);
      } else if (buttonId.startsWith("decrease") && targetItem.getQuantity() > 1) {
        targetItem.setQuantity(targetItem.getQuantity() - 1);
      }
      updateCartView();
    }
  }

  @FXML
  public void removeItemFromCart(ActionEvent event) {
    Button source = (Button) event.getSource();
    String buttonId = source.getId();
    String itemName = buttonId.replace("cancelButton", "");

    CartItem itemToRemove = findCartItemByName(itemName);

    if (itemToRemove != null) {
      cartItems.remove(itemToRemove); // 장바구니에서 아이템 제거
      updateCartView(); // 장바구니 UI 업데이트
    }
  }

  @FXML
  private void handleOrderButton(ActionEvent event) {
    if (mainApp != null) {
      KioskPaymentController paymentController = mainApp.getPaymentController();
      if (paymentController != null) {
        paymentController.setCartItems(cartItems);
      }
      mainApp.switchToPaymentScene(); // 결제 화면으로 전환
    } else {
      System.err.println("MainApp is null");
    }
  }

  public void clearCart() {
    cartItems.clear(); // 장바구니 초기화
    updateCartView(); // 장바구니 UI 업데이트
  }

  public ObservableList<CartItem> getCartItems() {
    return cartItems;
  }

  public void setMainApp(KioskMain mainApp) {
    this.mainApp = mainApp;
  }
}

