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
	@FindBy(xpath ="//div/table[@id='customfa2']/tbody/tr/td") private Label lblCartEmptyText;
	
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
	/*public String verifyQuantitybeforeAddingItemToCart(String itemNumber){
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
						  String getQuantitybeforeAddItemToCart = quantity.findElement(By.tagName("input")).getAttribute("value").trim();
						  TestReporter.log("Captured Quantity value before adding item to cart : "+ getQuantitybeforeAddItemToCart );
						  quantityValue = getQuantitybeforeAddItemToCart;
					  }else{
						  TestReporter.log("Item should be added to the cart for verifying the quantity value.");
					  }
					 break;
				   } 
			  }
			  break;
		    }		
		  }
		return quantityValue;
	}*/
	public String verifyQuantitybeforeAddingItemToCart(String itemNumber){
		String quantityValue = null;
		pageLoaded();
		List<WebElement> cartInfoTableRows = tblCartInfo.findElements(By.tagName("tr"));
				//driver.findElements(By.xpath("//table[@id='tblCartInfo']/tbody/tr"));
		
		int getRowsCount = cartInfoTableRows.size();
		System.out.println("Total no. of rows in Cart Info table : " + getRowsCount);
		
		for(int rows=1; rows<=getRowsCount; rows++){
			String getItemNumber = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ ((rows+1)) +"]/td[1]")).getText();

			if(getItemNumber.trim().equalsIgnoreCase(itemNumber)){
				/* driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr["+ ((rows+1)) +"]/td[5]"));*/
				String itemQuantityValue = driver.findElement(By.xpath("//table[@id='tblCartInfo']/tbody/tr[2]/td[5]/input")).getAttribute("value");
				System.out.println("====================> "+itemQuantityValue);
				quantityValue = itemQuantityValue ;
				break;
			}
		}
		return quantityValue;	
			
	}		
			
			/*for(WebElement itemNumLink : itemNumLinks){
				System.out.println(itemNumLink.findElement(By.tagName("td[1]")).getText().trim());
				
			    if(itemNumLink.getText().trim().equalsIgnoreCase(itemNumber)){
				   TestReporter.log("Added Item number is: "+itemNumLink.getText().trim());
				   List<WebElement> quantityCount = itemNumLink.findElements(By.tagName("td[5]"));
				   for(WebElement quantity: quantityCount){
					  if(quantity.findElement(By.tagName("input")).isDisplayed()){
						  String getQuantitybeforeAddItemToCart = quantity.findElement(By.tagName("input")).getAttribute("value").trim();
						  TestReporter.log("Captured Quantity value before adding item to cart : "+ getQuantitybeforeAddItemToCart );
						  quantityValue = getQuantitybeforeAddItemToCart;
					  }else{
						  TestReporter.log("Item should be added to the cart for verifying the quantity value.");
					  }
					 break;
				   } 
			  }
			  break;
		    }		
		  }return quantityValue;*/ //}
		
/*		}
		return quantityValue;
	}*/
	
	/**
	 * @Summary: Method to close the cart-Info page.
	 * @author: Praveen Namburi, @version: Created 12-09-2016
	 */
	public void closeCartInfoPage(){
		Sleeper.sleep(2000);
		lnkCloseCartInfo.isDisplayed();
		lnkCloseCartInfo.click();
	}
	
	/**
	 * This Method performs all Verifications For  Local,Global,Fav,ComparisonSceen Cart Items
	 **/
	public void perform_CartItemVerifications(String Quantity) {
		// Reading Rows Count from CartItem grid
		List<WebElement> cartItemRows = driver.findWebElements(By
				.xpath(".//*[@id='tblCartInfo']/tbody/tr"));
		TestReporter.logStep("Total Rows available in Cart :"+ cartItemRows.size());

		// Click on Edit
		//click_CartItemEdit(cartItemRows.size());

		// Update - Unit Price , UOM
		//TestReporter.logStep("Item Qty befor Update : "+ txtItemQuantity.getAttribute("value"));
		//String actualQty = txtItemQuantity.getAttribute("value");
		
		/*txtItemUnitPrice.isDisplayed();
		txtItemUnitPrice.safeSet(UnitPrice);
		txtItemUnitPrice.isDisplayed();
		lstItemunitOfMeasure.select(UOM);*/

		//btnAddToCart.jsClick();

		// clicking cart link
		//clickCartItemsLink();

		// Reading Quantity after changing UOM
		String expectedQty = driver.findElement(
				By.xpath(".//*[@id='tblCartInfo']/tbody/tr["
						+ (cartItemRows.size() - 1) + "]/td[5]/input"))	.getAttribute("value");
		TestReporter.logStep("Item Qty after Update : "+ expectedQty);

		// Click on Edit
		//click_CartItemEdit(cartItemRows.size());

		/*String expectedUP = txtItemUnitPrice.getAttribute("value");
		String expectedUOM = lstItemunitOfMeasure.getFirstSelectedOption().getText();*/


		// Verify -  Quantity changed as per UOM
		//TestReporter.assertTrue(!actualQty.equalsIgnoreCase(expectedQty),"Quantity Updated !!");
		/*// Verify - updated UOM
		TestReporter.assertTrue(UOM.equalsIgnoreCase(expectedUOM),"Unit of Measure Updated !!");
		// Verify - Updated UP
		TestReporter.assertTrue(UnitPrice.equalsIgnoreCase(expectedUP),"Unit Price Updated !!");*/

	}
	
}
	
		
 



=======
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
