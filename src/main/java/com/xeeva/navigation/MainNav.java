package com.xeeva.navigation;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestReporter;

public class MainNav {

	private OrasiDriver driver = null;

	/**Page Elements**/

	@FindBy(linkText = "Logout") private Link lnkLogout;
	@FindBy(id = "btnSaveCart") private Button btnSaveCart;
	@FindBy(id = "lnkShowPopup") private Link lnkCartItem;
	@FindBy(linkText = "tblCartInfo") private Webtable cartTable;
	@FindBy(xpath = ".//*[@id='tblCartInfo']/tbody/tr") private  List<WebElement> cartItemGridRows;
	@FindBy(id = "txtQuantity")	private Textbox txtQuantity;
	@FindBy(id = "ddlUOM")	private Listbox unitOfMeasure;
	@FindBy(id = "txtUnitPrice")private Textbox txtUnitPrice;
	@FindBy(id = "btnSubmit")	private Button btnSubmit;
	@FindBy(xpath = ".//*[@id='txtItem']")	private Textbox itemDescription;

	// Update Cart Items - before adding to cart
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[2]/td[2]/input") private Textbox txtQuantityRFQ;
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[1]/td[4]/select") private Listbox unitOfMeasureRFQ;
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[1]/td[2]/input[1]")private Textbox txtUnitPriceRFQ;
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[2]/td[2]/a/div") private Button btnAddToCart;


	/**Constructor**/

	public MainNav(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	/**Page Interactions**/

	public void pageLoaded(){
		lnkLogout.syncVisible(10, false);
	}

	public boolean isLogoutDisplayed(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lnkLogout.syncVisible(20, false);
	}

	// Method to click on Cart-Items Link 
	public void clickCartItemsLink(){
		Sleeper.sleep(2000);
		lnkCartItem.syncVisible(10, false);
		lnkCartItem.isEnabled();
		lnkCartItem.click();
		Sleeper.sleep(2000);
	}


	// Method for Application Logout 
	public void SaveCart(){
		if(btnSaveCart.isDisplayed()){
			btnSaveCart.click();
		}else{
			TestReporter.logStep("Cart Empty to Save");
		}
	}


	// Method to verify Smart Form Item Added to Cart 
	public void verify_CartItem(String ItemDescription,int cartItemRows){
		// Verifies Item added in the Cart
		String Expected = driver.findElement(By.xpath(".//*[@id='tblCartInfo']/tbody/tr["+(cartItemRows-1)+"]/td[2]/span")).getText();
		TestReporter.logStep("Actual :"+ ItemDescription + ": " + "Expected : "+ Expected);
		TestReporter.assertTrue(Expected.contains(ItemDescription), "Smart Form Item Verified!!");
	}


	// Verifies cart items updated with UOM ,Quantity,Unit Price
	public void verify_UpdateCart(String UP,String UOM,String Qty,int cartItemRows){

		// Clicking on Edit
		driver.findElement(By.xpath(".//*[@id='tblCartInfo']/tbody/tr["+(cartItemRows -1)+"]/td/div/a[2]")).click();
		Sleeper.sleep(3000);

		// Providing Update Values to Smart Form 
		TestReporter.logStep(itemDescription.getAttribute("value"));
		txtQuantity.safeSet(Qty);
		unitOfMeasure.select(UOM);
		txtUnitPrice.safeSet(UP);
		btnSubmit.click();

		// Clicking on Cart Link
		clickCartItemsLink();

		// Clicking on Edit
		driver.findElement(By.xpath(".//*[@id='tblCartInfo']/tbody/tr["+(cartItemRows -1)+"]/td/div/a[2]")).click();
		Sleeper.sleep(3000);

		// Verifying Cart Update
		TestReporter.assertTrue(txtQuantity.getAttribute("value").equalsIgnoreCase(Qty), "Smart Item Qunatity Updated!!");
		TestReporter.assertTrue(txtUnitPrice.getAttribute("value").equalsIgnoreCase(UP), "Unit Price Updated!!");
		TestReporter.assertTrue(unitOfMeasure.getFirstSelectedOption().getText().equals(UOM), "Unit Of measure Updated!!");

	}


	public void verifyCartItem(String ItemDescription,String UnitPrice,String UOM,String Quantity){

		// clicking cart link 
		clickCartItemsLink();

		// Reading Rows/Columns Count from Cart Item grid 
		List<WebElement> cartItemRows = driver.findWebElements(By.xpath(".//*[@id='tblCartInfo']/tbody/tr"));
		TestReporter.logStep("Total Rows available in Cart :" +cartItemRows.size());
		List<WebElement> cartItemCols = driver.findElements(By.xpath(".//*[@id='tblCartInfo']/tbody/tr["+(cartItemRows.size() -1)+"]/td"));
		TestReporter.logStep("Total Columns available in Cart :" +cartItemCols.size());

		// Verification - Smart Form Item Added to Cart
		verify_CartItem(ItemDescription,cartItemRows.size());

		// Verification - Smart form Item Updated with Quantity, UOM and UnitPrice
		verify_UpdateCart(UnitPrice,UOM,Quantity,cartItemRows.size() );


	}

	// Method for Application Logout 
	public void clickLogout(){
		isLogoutDisplayed();
		lnkLogout.click();
		SaveCart();
	}


}
