package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;



/**
 * @summary This page contains Product comparison screen page objects
 * @author  Lalitha Banda
 * @date 	12/09/16
 */
public class ProductComparisonTab {
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(css = ".GridsubHeading>div") private Label lblProductComparison;
	@FindBy(xpath = "//i[@title='Add To Cart']") private List<WebElement> lstAddToCart;
	@FindBy(xpath = "//select[@class='textFieldList width90Px']") private Listbox ddSelectUOM;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartItemAddedMessage;


	/**Constructor**/

	public ProductComparisonTab(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		lblProductComparison.syncVisible(20, false);
	}

	/**Page Interactions**/
	public void select_UOM(String UOM){
		ddSelectUOM.select(UOM);
	}

	// Method to Click on Add to Cart - Product Comparison Screen
	public void click_AddToCart(String UOM){
		pageLoaded();
		select_UOM(UOM);
		driver.executeJavaScript("arguments[0].click();", lstAddToCart.get(0));
		Sleeper.sleep(3000);
		lblCartItemAddedMessage.isDisplayed();
		TestReporter.logStep( "Confirmation Message : " +lblCartItemAddedMessage.getText());
		TestReporter.assertTrue(lblCartItemAddedMessage.getText().contains("added successfully!"), "Item Added to Cart !!");
	}

}
