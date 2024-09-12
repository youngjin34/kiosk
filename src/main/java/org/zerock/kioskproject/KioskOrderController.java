package org.zerock.kioskproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class KioskOrderController {
  private KioskMain mainApp;

  @FXML
  private Label quantityLabel1, quantityLabel2, quantityLabel3, quantityLabel4, quantityLabel5;

  @FXML
  private void handleBackButton() {
    if (mainApp != null) {
      mainApp.switchToMainScene();  // 메인 화면으로 전환
    } else {
      System.err.println("MainApp is null");
    }
  }

  @FXML
  public void decreaseQuantity(ActionEvent event) {
    Button source = (Button) event.getSource();
    Label quantityLabel = getQuantityLabelByButtonId(source.getId());

    if (quantityLabel != null) {
      int quantity = Integer.parseInt(quantityLabel.getText());
      if (quantity > 1) {
        quantity--;
        quantityLabel.setText(String.valueOf(quantity));
      }
    }
  }

  @FXML
  public void increaseQuantity(ActionEvent event) {
    Button source = (Button) event.getSource();
    Label quantityLabel = getQuantityLabelByButtonId(source.getId());

    if (quantityLabel != null) {
      int quantity = Integer.parseInt(quantityLabel.getText());
      quantity++;
      quantityLabel.setText(String.valueOf(quantity));
    }
  }

  private Label getQuantityLabelByButtonId(String buttonId) {
    switch (buttonId) {
      case "decreaseButton1":
      case "increaseButton1":
        return quantityLabel1;
      case "decreaseButton2":
      case "increaseButton2":
        return quantityLabel2;
      case "decreaseButton3":
      case "increaseButton3":
        return quantityLabel3;
      case "decreaseButton4":
      case "increaseButton4":
        return quantityLabel4;
      case "decreaseButton5":
      case "increaseButton5":
        return quantityLabel5;
      default:
        return null;
    }
  }

  public void setMainApp(KioskMain mainApp) {
    this.mainApp = mainApp;
  }
}
