package com.xeeva.catalog;

import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

/**
 * @summary This page contains Item Details page objects.
 * @author  Praveen Namburi
 *  @version: Created 07-09-2016.
 */
public class ItemDetailsPage {

	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(linkText = "lnkShowPopup") private Link lnkCartItem;
	@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
	@FindBy(xpath = "//select[@class='textFieldList width90Px']") private Listbox lstSelectUOM;
	@FindBy(xpath = ".//*[@class='textFieldList']") private List<Listbox> lstUOMGlobalCart;
	@FindBy(className="add-to-cart-box") private List<WebElement> btnIAddToCartItems;
	@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartItemAddedMessage;
	@FindBy(xpath = "//table/tbody/tr[6]/td/table/tbody/tr/td[4]/select") private Listbox lstunitofMeasure;
	@FindBy(xpath = "//tr[1]/td[3]//tr[7]//td[4]/a/div")
	private Button btnAddToCart;

	// global Items , UnitPrice ,Quantity
	@FindBy(xpath = "//table/tbody/tr[6]/td/table/tbody/tr/td[2]/input") 
	private Textbox txtunitPrice;
	@FindBy(xpath = "//table/tbody/tr[7]/td/table/tbody/tr/td[4]/input") 
	private Textbox txtQuantity;



	/**Constructor**/
	public ItemDetailsPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	public void pageLoaded(){
		catalogSearch.syncVisible(10, false);
	}

	/**Page Interactions**/

	/**
	 * @summary: Method to add Price Agreement Item to Cart.
	 * @author: Praveen Namburi, @version: Created 08-09-2016
	 */
	public void addPriceAgrmntItemToCart(){
		btnAddToCart.syncEnabled();
		btnAddToCart.click();
	}

	/**
	 * @summary Method to add non price agreement item to cart 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void selectUOMValueAndAddNonPriceItemToCart(String strUOMValue){
		if(lstSelectUOM.isDisplayed()){
			lstSelectUOM.select(strUOMValue);
			btnAddToCart.jsClick();
			Sleeper.sleep(2000);
			lblCartItemAddedMessage.syncVisible(15, false);
			String getCartItemAddedMessage = lblCartItemAddedMessage.getText();
			System.out.println("Message after adding item to cart : "+ getCartItemAddedMessage);
			TestReporter.assertTrue(getCartItemAddedMessage.contains("added successfully!"), "Item added to the cart.");
		}else{
			TestReporter.assertFalse(!lstSelectUOM.isDisplayed(),"Selected Random Item is Price aggrement Item!!");
		}
	}

	/**
	 * @summary: Method to Add-Item-To-Cart for Price agreement item.
	 * @author: Praveen Namburi, @version: Created 12-09-2016
	 */
	public void addPriceAgreementItemToCart_And_Verify(){
		btnAddToCart.jsClick();
		Sleeper.sleep(2000);
		lblCartItemAddedMessage.syncVisible(15, false);
		String getCartItemAddedMessage = lblCartItemAddedMessage.getText();
		TestReporter.logStep("Message after adding item to cart : "+ getCartItemAddedMessage);
		TestReporter.assertTrue(getCartItemAddedMessage.contains("added successfully!"), "Item added to the cart.");

	}

	/**
	 * @summary Method to modify Item details for UOM,UP and quantity 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/

	public void  modify_ItemDetails(String UP,String Qty,String UOM){
		txtunitPrice.clear();
		txtunitPrice.isDisplayed();
		txtunitPrice.safeSet(UP);
		txtQuantity.clear();
		txtQuantity.safeSet(Qty);
		lstunitofMeasure.select(UOM);
	}


	/**
	 * @summary Method modify Item details - Global Search Records
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/

	public void modifyItemDetails(String UnitPrice, String Quantity, String strUOMValue){
		modify_ItemDetails(UnitPrice,Quantity,strUOMValue);
		btnAddToCart.isDisplayed();
		btnAddToCart.click();
		Sleeper.sleep(3000);
		lblCartItemAddedMessage.syncVisible(15, false);
		String getCartItemAddedMessage = lblCartItemAddedMessage.getText();
		//The item has been added successfully!
		TestReporter.assertTrue(getCartItemAddedMessage.equalsIgnoreCase("The item has been added successfully!"), "Item added to the cart.");
	}


	/**
	 * @summary Method To add two different items to cart to process Cost Center update
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void add_TwoDiffrent_ItemsToCart(String UP,String Qty,String UOM){
		TestReporter.logStep("UOM "+lstUOMGlobalCart.size());
		TestReporter.logStep("Add To Cart buttons "+btnIAddToCartItems.size());
		lstUOMGlobalCart.get(0).select(UOM);
		driver.executeJavaScript("arguments[0].click();", btnIAddToCartItems.get(0));
		Sleeper.sleep(5000);
		lstUOMGlobalCart.get(1).select(UOM);
		driver.executeJavaScript("arguments[0].click();", btnIAddToCartItems.get(1));

	}

}

