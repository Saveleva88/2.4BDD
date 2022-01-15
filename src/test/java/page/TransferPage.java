package page;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transferButton = $("[data-test-id=action-transfer]");


    public void topUp(DataHelper.CardInfo CardInfo, int amount) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(CardInfo.getCardNumber());
        transferButton.click();
        new DashboardPage();
    }

    public void getError() {
        $(byText("Недостаточно средств для перевода")).shouldBe(visible);
    }
}