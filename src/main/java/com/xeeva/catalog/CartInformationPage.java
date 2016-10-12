package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.xslf.usermodel.Placeholder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.AlertHandler;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.xeeva.catalog.SearchItems.LocalItemsTab;

/**
 * @summary: This page class contains all the methods and locators of CartInformation page.
 * @author praveen varma,@version: Created 8-9-2016
 */
public class CartInformationPage {
	PageLoaded pageLoad = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id="fancybox-outer") private Element eleCartInformationPage;	
	@FindBy(xpath="//*[@id='tblCartInfo']/tbody") private WebElement tblCartInfo;
	@FindBy(xpath="//a[@id='fancybox-close']") private Link lnkCloseCartInfo;
	@FindBy(xpath ="//div/table[@id='customfa2']/tbody/tr/td") private Label lblCartEmptyText;
	@FindBy(xpath="//input[@name='Button'][@value='Save Cart']") private Button btnSaveCart;
	@FindBy(xpath="//table[@id='tblCartInfo']/tbody/tr") private List<WebElement> tblCartInformation;
	@FindBy(xpath="//table[@id='tblCartInfo']/tbody/tr/td[5]/input") private List<WebElement> cartItemsList;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartAddItemMessage;
	
	/**Constructor**/
	public CartInformationPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	
	public void pageLoaded(){
		eleCartInformationPage.syncVisible(30, false);
	}

	/**Page Interactions**/
	
	/**
	 * @summary: Method to verify UnitPrice is not editable for added item into cart.
	 * @author: Praveen Namburi, @version: Created 13-09-2016,@param itemNumber
	 */
	public void verifyUnitPriceIsNotEditable(String itemNumber){
		pageLoaded();
		List<WebElement> cartInfoTableRows = tblCartInfo.findElements(By.tagName("tr"));
		int getRowsCount = cartInfoTableRows.size();
		TestReporter.log("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=1; rows<=getRowsCount; rows++){
			String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/"
					+ "tbody/tr["+ ((rows+1)) +"]/td[1]")).getText();

			if(getItemNumber.trim().equalsIgnoreCase(itemNumber)){
			    driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ ((rows+1)) +"]/td[6]"));
				List<WebElement> getInputTags = driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/"
						+ "tr["+ ((rows+1)) +"]/td[6]/input"));
				int inputTagsCount = getInputTags.size();
				TestReporter.assertTrue(inputTagsCount == 0, "Unit Price is not editable for the added item -"
						+ " "+"["+itemNumber+"]");
				break;
			}
		}

	}
	
	/**
	 * @summary: Method to capture the quantity value for added item in cart info page.
	 * @author: Praveen Namburi, @version: Created 13-09-2016
	 * @param itemNumber* @return itemQuantityValue
	 */
	public String getQuantityForAddedItemToCart(String itemNumber){
		String quantityValue = null;
		
		pageLoaded();
		List<WebElement> cartInfoTableRows = tblCartInfo.findElements(By.tagName("tr"));
		int getRowsCount = cartInfoTableRows.size();
		TestReporter.log("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=2; rows<=getRowsCount-1; rows++){
			String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ rows +"]/td[1]")).getText();

			if(getItemNumber.trim().equalsIgnoreCase(itemNumber)){
				driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ rows +"]/td[5]"));
				String itemQuantityValue = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
						+ "tr["+ rows +"]/td[5]/input")).getAttribute("value");
				//System.out.println("Captured Item Quantity value: "+itemQuantityValue);
				quantityValue = itemQuantityValue;
				break;
			}
		}
		return quantityValue;	

	}
	
	/**
	 * @Summary: Method to close the cart-Info page.
	 * @author: Praveen Namburi, @version: Created 12-09-2016
	 */
	public void closeCartInfoPage(){
		lnkCloseCartInfo.isDisplayed();
		//driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
		//driver.executeJavaScript("arguments[0].click();", lnkCloseCartInfo);
		lnkCloseCartInfo.syncVisible(20);
		lnkCloseCartInfo.click();
	}
	
	/**
	 * @summary:Method to get Quantity from cart items.
	 * @author: praveen namburi, @version: Created 16-09-2016
	 * @param itemNumber
	 * @return
	 */
	public String grabQuantityForAddedItem(String itemNumber){
		String quantityValue = null;
		
		pageLoaded();
		List<WebElement> cartInfoTableRows = tblCartInfo.findElements(By.tagName("tr"));
		int getRowsCount = cartInfoTableRows.size();
		TestReporter.log("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=2; rows<=getRowsCount-1; rows++){
			String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ rows +"]/td[1]")).getText();
			if(getItemNumber.trim().equalsIgnoreCase(itemNumber)){
				driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ rows +"]/td[5]"));
				String itemQuantityValue = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
						+ "tr["+ rows +"]/td[5]/input")).getAttribute("value");
				//System.out.println("Captured Item Quantity value: "+itemQuantityValue);
				quantityValue = itemQuantityValue;
				break;
			}
		}
		return quantityValue;	

	}
	
	/**
	 * @summary: Method to verify the rejected item is added to cart.
	 * @author: Praveen Namburi, @Version: Created 27-09-2016
	 * @param itemNumber
	 */
	public void verifyAddedCartItem_ForRejectedOrder(String itemNumber){
		int evenNum = 0;
		pageLoaded();
		List<WebElement> cartInfoTableRows = driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr"));
		int getRowsCount = cartInfoTableRows.size();
		TestReporter.log("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=1; rows<=getRowsCount-1; rows++){
			if(rows % 2 == 0){
	    		evenNum = rows;
	    		Sleeper.sleep(2000);
	    		String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+rows+"]/td[1]/a")).getText();
				if(getItemNumber.trim().equalsIgnoreCase(itemNumber)){
					TestReporter.assertEquals(getItemNumber, itemNumber, 
							"Rejected Item number added to cart successfully.");
					break;
			    }
		     }

	     }
	
	}
	
	/**
	 * @Sumamry: Method to update the Quantity and verify save cart functionality.
	 * @author: Praveen Namburi, @Version: 28-09-2016
	 * @param: Quantity
	 */
	public void updateQuantityAndVerifySaveCartFunc(String Quantity){
		int evenNum = 0;  
		pageLoaded();
		List<WebElement> cartInfoTableRows = driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr"));
		int getRowsCount = cartInfoTableRows.size();
		TestReporter.log("Total no. of rows in Cart Info table : " + getRowsCount);
		//Iterate even number rows	
		for(int rows=1; rows<=getRowsCount-1; rows++){
			if(rows % 2 == 0){
		       evenNum = rows;
		       String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
		    		  + "tr["+rows+"]/td[1]/a")).getText();
		       TestReporter.log("Updating the quantity for the item number: " + getItemNumber);
		       driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
			       		+ "tr["+rows+"]/td[5]/input")).clear();
		       driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
		       		+ "tr["+rows+"]/td[5]/input")).sendKeys(Quantity);
		       btnSaveCart.syncEnabled(20);
		       btnSaveCart.click();
		       //Handle Alert if present
			   if(AlertHandler.isAlertPresent(driver, 6)){
				   AlertHandler.handleAlert(driver, 6);
			   }
			   verify_UpdatedQuantity();
		       break;
			 }
		}
		
	}

	/**
	 * @summary: Method to verify the message after updating the quantity in cart-Information page.
	 * @author: Praveen Namburi, @version: Created 28-09-2016.
	 */
	public void verify_UpdatedQuantity(){
		//Added wait statement to wait till the timeout for updated quantity 
		//successfull message to be displayed.
		WebDriverWait wait = new WebDriverWait(driver,10);
		WebElement lblCartAddItemMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getUpdatedQuantityMessage = lblCartAddItemMessage.getText();
		TestReporter.logStep("Message after updating the quantity in cartInfo page: "+ getUpdatedQuantityMessage);
		TestReporter.assertTrue(getUpdatedQuantityMessage.contains("updated successfully") || getUpdatedQuantityMessage.contains("saved successfully"), 
				"Quantity updated successfully!");

	}
	
	/**
	 * @Sumamry: Method to update the Quantity and verify save cart functionality.
	 * @author: Praveen Namburi, @Version: 28-09-2016
	 */
	public void deleteExistingCartItems(){
		int evenNum = 0;  
		pageLoaded();
		List<WebElement> cartInfoTableRows = driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr"));
		int getRowsCount = cartInfoTableRows.size();
		TestReporter.log("Total no. of rows in Cart Info table : " + getRowsCount);
		//Iterate even number rows	
		for(int rows=1; rows<=getRowsCount-1; rows++){
			if(rows % 2 == 0){
		       evenNum = rows;
		       String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
		    		  + "tr["+rows+"]/td[1]")).getText();
		       TestReporter.log("Deleting the existing Item - [" + getItemNumber + "] from cart.");
		       driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+rows+"]/td[8]/div/a[3]/i")).click();
		       //Handle Alert if present
			   if(AlertHandler.isAlertPresent(driver, 6)){
				   AlertHandler.handleAlert(driver, 6);
			   }
			   verify_DeleteCartItems();
			 }
		   //driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
		}
		
	}
	
	/**
	 * @summary: Method to verify the message after deleting the item from cart.
	 * @author: Praveen Namburi, @version: Created 28-09-2016.
	 */
	public void verify_DeleteCartItems(){
		//Added wait statement to wait till the timeout for Removed item 
		//successfull message to be displayed.
		WebDriverWait wait = new WebDriverWait(driver,10);
		WebElement lblCartAddItemMessage =wait.until(ExpectedConditions.
				visibilityOfElementLocated(By.xpath("//div[@id='divAppInfoMsg'][@class='addMessage']")));
		String getItemRemovedMessage = lblCartAddItemMessage.getText();
		TestReporter.logStep("Message after deleting the items from cart : "+ getItemRemovedMessage);
		TestReporter.assertTrue(getItemRemovedMessage.contains("removed successfully"), 
				"The item has been removed successfully!");

	}
	
	/**
	 * @summary: Method to update the quantity values for cart-items in cart-info page.
	 * @author: Praveen Namburi, @version: Created 29-09-2016
	 */
	public void UpdateQuantityAndSaveCart(String Quantity){
		int evenNum=0;
		pageLoaded();
		List<WebElement> cartInfoTblRows = tblCartInformation;
		int getRows = cartInfoTblRows.size();
		TestReporter.log("Total no. of rows in Cart-Info table: " + getRows);
		List<WebElement> cartItems = cartItemsList;
		TestReporter.log("Total Items to be updated in cart: " + cartItems.size());
		//Iterate even number rows	
		for(int rows=1; rows<=getRows-1; rows++){
			if(rows % 2 == 0){
		       evenNum = rows;
		       String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/"
		    		  + "tr["+rows+"]/td[1]")).getText();
		       TestReporter.log("Updating the quantity value for the Item Number - [" + getItemNumber + "].");
		       driver.setElementTimeout(Constants.ELEMENT_TIMEOUT);
		       driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+rows+"]/td[5]/input")).clear();
		       driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+rows+"]/td[5]/input")).sendKeys(Quantity);
		       btnSaveCart.syncEnabled(30);
		       btnSaveCart.click();
		       //Handle Alert if present
			   if(AlertHandler.isAlertPresent(driver, 2)){
				   AlertHandler.handleAlert(driver, 2);
			   }
		       verify_UpdatedQuantity();
			}
		}
	}
	
	
	
}
