package org.zerock.kioskproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class KioskOrderController {
  private KioskMain mainApp;

  // 장바구니에 담긴 아이템 리스트
  private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

  @FXML
  private ListView<String> cartListView;  // 장바구니 아이템을 보여주는 리스트

  @FXML
  private Label totalLabel;  // 총 가격을 보여줄 라벨

  @FXML
  private void handleBackButton() {
    if (mainApp != null) {
      mainApp.switchToMainScene();  // 메인 화면으로 전환
    } else {
      System.err.println("MainApp is null");
    }
  }

  @FXML
  public void onImageClick(MouseEvent event) {
    ImageView source = (ImageView) event.getSource();
    String itemName = getItemNameByImageId(source.getId());
    if (itemName != null) {
      addItemToCart(itemName);
    }
  }

  // 장바구니에 아이템 추가
  private void addItemToCart(String itemName) {
    CartItem existingItem = findCartItemByName(itemName);

    if (existingItem != null) {
      // 이미 장바구니에 있는 경우 수량만 증가
      existingItem.setQuantity(existingItem.getQuantity() + 1);
    } else {
      // 새로운 아이템 추가
      double price = getItemPriceByName(itemName);  // 가격은 각 아이템에 따라 다름
      cartItems.add(new CartItem(itemName, 1, price));
    }

    updateCartView();  // 장바구니 UI 업데이트
  }

  // 장바구니에 있는 아이템 찾기
  private CartItem findCartItemByName(String itemName) {
    for (CartItem item : cartItems) {
      if (item.getName().equals(itemName)) {
        return item;
      }
    }
    return null;
  }

  private String getItemNameByImageId(String imageId) {
    // 이미지 ID에 따라 아이템 이름 반환
    switch (imageId) {
      case "americanoImage":
        return "아메리카노";
      case "latteImage":
        return "라떼";
      case "mocaImage":
        return "모카";
      default:
        return null;
    }
  }

  // 아이템 이름에 따라 가격 반환 (여기서는 예시로 단순하게 설정)
  private double getItemPriceByName(String itemName) {
    switch (itemName) {
      case "아메리카노":
        return 5000;
      case "라떼":
        return 5500;
      case "모카":
        return 5500;
      default:
        return 0;
    }
  }

  // 장바구니 UI 업데이트 및 총 가격 계산
  private void updateCartView() {
    ObservableList<String> cartDisplayItems = FXCollections.observableArrayList();
    double totalPrice = 0;

    for (CartItem item : cartItems) {
      cartDisplayItems.add(item.getName() + " x" + item.getQuantity() + " - " + item.getTotalPrice() + "원");
      totalPrice += item.getTotalPrice();
    }

    cartListView.setItems(cartDisplayItems);
    totalLabel.setText("총 가격: " + totalPrice + "원");
  }

  // 수량 증가/감소 버튼 이벤트 핸들러
  @FXML
  public void changeQuantity(ActionEvent event) {
    Button source = (Button) event.getSource();
    String buttonId = source.getId();
    CartItem targetItem = getItemByButtonId(buttonId);

    if (targetItem != null) {
      if (buttonId.startsWith("increase")) {
        targetItem.setQuantity(targetItem.getQuantity() + 1);
      } else if (buttonId.startsWith("decrease") && targetItem.getQuantity() > 1) {
        targetItem.setQuantity(targetItem.getQuantity() - 1);
      }
      updateCartView();
    }
  }

  // 버튼 ID에 따라 맞는 아이템 찾기 (수량 조절용)
  private CartItem getItemByButtonId(String buttonId) {
    switch (buttonId) {
      case "increaseButton1":
      case "decreaseButton1":
        return findCartItemByName("아메리카노");
      case "increaseButton2":
      case "decreaseButton2":
        return findCartItemByName("라떼");
      case "increaseButton3":
      case "decreaseButton3":
        return findCartItemByName("모카");
      default:
        return null;
    }
  }

  public void setMainApp(KioskMain mainApp) {
    this.mainApp = mainApp;
  }
}
