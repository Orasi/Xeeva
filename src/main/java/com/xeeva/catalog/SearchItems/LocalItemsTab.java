package com.xeeva.catalog.SearchItems;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;

/**
 * @summary This page contains Local Items Tab objects
 * @author  Lalitha Banda
 * @date    06/09/16
 */
public class LocalItemsTab {
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "aTab1")	private Link localItemsTab;
	@FindBy(xpath = ".//*[@id='aTab1']/span[2]") private Label localCount;
	@FindBy(xpath = ".//*[@id='gvLocalSearch']/tbody/tr/td/span") private  List<WebElement> localItemsGrid;
	@FindBy(xpath="//div[@id='countrydivcontainer']") private Label lblItemDescriptionTable;
	
	//Add-to-cart-Item button
	@FindBy(xpath="//div[@class='add-to-cart-box']") private Button btnAddItemToCart;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private String lblCartItemAddedMessage;

	//Cart-Item link
	@FindBy(css="#lnkShowPopup") private Link lnkCartItem;
	
	/**Constructor**/

	public LocalItemsTab(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	private void pageLoaded(){
		localItemsTab.syncVisible(30, false);
	}

	/**Page Interactions**/

	// Clicks on local Item Tab 
	public void click_localItemsTab(){
		localItemsTab.click();
	}

	// Method to read Local Item Numbers 	
	public String  getLocalItemNumber(){
		String ItemNumber = null;

		click_localItemsTab();
		List<WebElement> localItems= localItemsGrid;
		if(localItems.size()>0){
			for(WebElement inputItem :localItems){
				ItemNumber = inputItem.getText();
				break;
			}
		}else{
			TestReporter.logStep("No Records Found !!");
		}

		return ItemNumber;
	}
	
	/**
	 * @summary: Method to add local-item to cart and verify.
	 * @author: Praveen Namburi,@version: Created 08-09-2016.
	 */
	public void addLocalItemToCartAndVerify(){
		//Wait until the item details are visible.
		lblItemDescriptionTable.syncVisible(60,false);
		List<WebElement> localItems = driver.findElements(By.xpath("//div[@class='add-to-cart-box']"));
		if(localItems.size()>0){
			for(WebElement inputItem :localItems){
				Sleeper.sleep(1000);
				inputItem.click();
				break;
			}
		}else{
			TestReporter.assertFalse(localItems.size() == 0, "No Records Found !!");
		}
		
		//Handle Alert if present
		if(AlertHandler.isAlertPresent(driver, 6)){
			AlertHandler.handleAlert(driver, 6);
		}
		
		//Added wait statement to wait until the Cart item added successfull message to be displayed.
		WebDriverWait wait = new WebDriverWait(driver,10);
		WebElement lblCartAddItemMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		 
		//lblCartItemAddedMessage.syncVisible(60,false);
		String getCartItemAddedMessage = lblCartAddItemMessage.getText();
		System.out.println("Message after adding item to cart : "+ getCartItemAddedMessage);
		TestReporter.assertTrue(getCartItemAddedMessage.contains("added successfully!"), "Item added to the cart.");
	}
	
	public void clickCartItemsLink(){
		pageLoaded();
		lnkCartItem.syncVisible(5, false);
		lnkCartItem.click();
		//Sleeper.sleep(1000);
	}

}
