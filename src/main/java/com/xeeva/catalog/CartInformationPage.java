package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;
import com.xeeva.catalog.SearchItems.LocalItemsTab;

/**
 * @summary: This page class contains all the methods and locators of CartInformation page.
 * @author praveen varma,@version: Created 8-9-2016
 */
public class CartInformationPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id="fancybox-outer") private Element eleCartInformationPage;	
	@FindBy(xpath="//*[@id='tblCartInfo']/tbody") private WebElement tblCartInfo;
	@FindBy(xpath="//a[@id='fancybox-close']") private Link lnkCloseCartInfo;
	@FindBy(xpath ="//div/table[@id='customfa2']/tbody/tr/td") private Label lblCartEmptyText;
	
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
	
}
