package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

/**
 * @summary: This page class contains all the methods and locators of CartInformation page.
 * @author praveen varma,@version: Created 8-9-2016
 */
public class CartInformationPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(css="#fancybox-outer") private Element eleCartInformationPage;	
	@FindBy(xpath="//*[@id='tblCartInfo']/tbody") private WebElement tblCartInfo;
	@FindBy(xpath="//a[@id='fancybox-close']") private Link lnkCloseCartInfo;
	
	/**Constructor**/
	public CartInformationPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	
	public void pageLoaded(){
		eleCartInformationPage.syncVisible(15, false);
	}

	/**Page Interactions**/

	/**
	 * @summary: Method to verify UnitPrice is not editable for added item into cart.
	 * @author: Praveen Namburi, @version: Created 08-09-2016,@param strUOMValue
	 */
	public void verifyUnitPriceIsNotEditable(String itemNumber){
		pageLoaded();
		List<WebElement> cartInfoTableRows = tblCartInfo.findElements(By.tagName("tr"));
				//driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr"));
		int getRowsCount = cartInfoTableRows.size();
		System.out.println("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=1; rows<=getRowsCount; rows++){
			List<WebElement> itemNumLinks = driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ ((rows)+1) +"]/td[1]"));
			for(WebElement itemNumLink : itemNumLinks){
				//System.out.println(itemNumLink.getText().trim());
				if(itemNumLink.getText().trim().equalsIgnoreCase(itemNumber)){
					System.out.println(itemNumLink.getText().trim());
				   List<WebElement> unitPriceItems = itemNumLink.findElements(By.tagName("td[6]"));
				   for(WebElement unitPriceItem: unitPriceItems){
					   if(unitPriceItem.findElement(By.tagName("input")).isDisplayed()){
						  TestReporter.assertTrue(unitPriceItem.findElement(By.tagName("input")).isDisplayed(), 
								"Requestor is not able to change the price for the added item.");
					   }
					   break;
				   } 
				   break;
				}
			}
			
		}
	}
	
	/**
	 * @Summary: Method to verify Quantity before Adding Item To Cart.
	 * @author: Praveen Namburi, @version: Created 12-09-2016
	 * @param itemNumber
	 */
	public String verifyQuantitybeforeAddingItemToCart(String itemNumber){
		String quantityValue = null;
		pageLoaded();
		List<WebElement> cartInfoTableRows = tblCartInfo.findElements(By.tagName("tr"));
				//driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr"));
		int getRowsCount = cartInfoTableRows.size();
		System.out.println("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=1; rows<=getRowsCount; rows++){
			List<WebElement> itemNumLinks = driver.findElements(By.xpath("//table[@id='tblCartInfo']/"
					+ "tbody/tr["+ ((rows)+1) +"]/td[1]"));
			for(WebElement itemNumLink : itemNumLinks){
				//System.out.println(itemNumLink.getText().trim());
			    if(itemNumLink.getText().trim().equalsIgnoreCase(itemNumber)){
				   TestReporter.log("Added Item number is: "+itemNumLink.getText().trim());
				   List<WebElement> quantityCount = itemNumLink.findElements(By.tagName("td[5]"));
				   for(WebElement quantity: quantityCount){
					  if(quantity.findElement(By.tagName("input")).isDisplayed()){
						  String getQuantitybeforeAddItemToCart = quantity.findElement(By.tagName("input")).getText();
						  TestReporter.log("Captured Quantity value before adding item to cart : "+ getQuantitybeforeAddItemToCart );
						  quantityValue = getQuantitybeforeAddItemToCart;
					  }else{
						  TestReporter.log("Item should be added to the cart for verifying the quantity value.");
					  }
					 break;
				   } 
				 break;
			  }
		    }		
		  }
		return quantityValue;
	}
	
	/**
	 * @Summary: Method to close the cart-Info page.
	 * @author: Praveen Namburi, @version: Created 12-09-2016
	 */
	public void closeCartInfoPage(){
		lnkCloseCartInfo.syncVisible(15, false);
		lnkCloseCartInfo.click();
	}
	
}
	
		
 



