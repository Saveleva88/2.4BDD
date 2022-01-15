package test;

import data.DataHelper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.DashboardPage;
import page.LoginPage;
import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.getBalanceOfFirstCardAfterTransfer;
import static data.DataHelper.getBalanceOfSecondCardAfterTransfer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        LoginPage loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val balanceOfFirstCardBefore = dashboardPage.getCardBalanceFirstCard();
        val balanceOfSecondCardBefore = dashboardPage.getCardBalanceSecondCard();
        val transferPage = dashboardPage.topUpSecondCard();
        val cardInfo = DataHelper.getFirstCardInfo();
        transferPage.topUp(cardInfo, amount);
        val balanceAfterTransactionFirstCard = getBalanceOfFirstCardAfterTransfer(balanceOfFirstCardBefore, amount);
        val balanceAfterTransactionSecondCard = getBalanceOfSecondCardAfterTransfer(balanceOfSecondCardBefore, amount);
        val balanceOfFirstCardAfter = dashboardPage.getCardBalanceFirstCard();
        val balanceOfSecondCardAfter = dashboardPage.getCardBalanceSecondCard();
        assertEquals(balanceAfterTransactionFirstCard, balanceOfFirstCardAfter);
        assertEquals(balanceAfterTransactionSecondCard, balanceOfSecondCardAfter);
    }

    @Test
    void shouldTransferFromSecondToFirst() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val balanceOfFirstCardBefore = dashboardPage.getCardBalanceFirstCard();
        val balanceOfSecondCardBefore = dashboardPage.getCardBalanceSecondCard();
        val transferPage = dashboardPage.topUpFirstCard();
        val cardInfo = DataHelper.getSecondCardInfo();
        transferPage.topUp(cardInfo, amount);
        val balanceAfterTransactionFirstCard = getBalanceOfSecondCardAfterTransfer(balanceOfFirstCardBefore, amount);
        val balanceAfterTransactionSecondCard = getBalanceOfFirstCardAfterTransfer(balanceOfSecondCardBefore, amount);
        val balanceOfFirstCardAfter = dashboardPage.getCardBalanceFirstCard();
        val balanceOfSecondCardAfter = dashboardPage.getCardBalanceSecondCard();
        assertEquals(balanceAfterTransactionFirstCard, balanceOfFirstCardAfter);
        assertEquals(balanceAfterTransactionSecondCard, balanceOfSecondCardAfter);
    }

    @Test
    void shouldNotTransferIfBalanceNotEnough() {
        val dashboardPage = new DashboardPage();
        val amount = 100000;
        val transferPage = dashboardPage.topUpFirstCard();
        val cardInfo = DataHelper.getSecondCardInfo();
        transferPage.topUp(cardInfo, amount);
        transferPage.getError();
    }
}