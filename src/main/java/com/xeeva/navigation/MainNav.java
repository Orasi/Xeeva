package com.xeeva.navigation;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
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

	/** Page Elements **/
	@FindBy(linkText = "Logout") private Link lnkLogout;
	@FindBy(id = "btnSaveCart")	private Button btnSaveCart;
	@FindBy(id = "lnkShowPopup")	private Link lnkCartItem;
	@FindBy(linkText = "tblCartInfo")	private Webtable cartTable;
	@FindBy(xpath = ".//*[@id='tblCartInfo']/tbody/tr")	private List<WebElement> cartItemGridRows;
	@FindBy(id = "txtQuantity")	private Textbox txtQuantity;
	@FindBy(id = "ddlUOM")	private Listbox unitOfMeasure;
	@FindBy(id = "txtUnitPrice")	private Textbox txtUnitPrice;
	@FindBy(id = "btnSubmit")	private Button btnSubmit;
	@FindBy(xpath = ".//*[@id='txtItem']")	private Textbox itemDescription;
	@FindBy(xpath = ".//*[@id='spanCartValue']")	private Element lblCartValue;


	// Update Cart Items - for non price agreement cart Items verifications
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[2]/td[2]/input")
	private Textbox txtItemQuantity;
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[1]/td[4]/select")
	private Listbox lstItemunitOfMeasure;
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[1]/td[2]/input[1]")
	private Textbox txtItemUnitPrice;
	@FindBy(xpath = ".//*[@id='tblItemDetails']/tbody/tr[2]/td[2]/a/div")
	private Button btnAddToCart;
	@FindBy(xpath = ".//tr[2]/td/table/tbody/tr/td/input") private Button btnsaveCartPopUp;
	@FindBy(xpath = "//*[@id='btnPopupCP']") private Button btnCheckOut;
	

	/** Constructor **/

	public MainNav(OrasiDriver driver) {
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	/** Page Interactions **/

	public void pageLoaded() {
		lnkLogout.syncVisible(10, false);
	}

	public boolean isLogoutDisplayed() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lnkLogout.syncVisible(20, false);
	}


	/**
	 * @summary  Method to click on Cart-Items Link
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/

	public void clickCartItemsLink() {
		driver.executeJavaScript("arguments[0].click();", lnkCartItem);
		driver.setPageTimeout(4);
	}

	/**
	 * @summary Method for Application Logout
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void SaveCart() {
		if (btnSaveCart.isDisplayed()) {
			btnSaveCart.syncVisible(3, false);
			btnSaveCart.click();
		} else {
			TestReporter.logStep("Cart Empty to Save");
		}
	}

	/**
	 * @summary  Method to Click on Cart Item Edit icon
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void click_CartItemEdit(String itemNumber) {
		List<WebElement> cartRows = driver.findElements(By.xpath(".//*[@id='tblCartInfo']/tbody/tr"));
		for(int row =1;row<cartRows.size();row++)
			if(cartRows.get(row).getAttribute("id").contains("trItemRow_")){
				List<WebElement> links =  cartRows.get(row).findElements(By.tagName("a"));
				TestReporter.logStep( "Item Number Fronm Cart Rows : "+links.get(0).getText());
				if(links.get(0).getText().equalsIgnoreCase(itemNumber)){
					// clicking on Cart Item Edit
					driver.setPageTimeout(3);
					driver.executeJavaScript("arguments[0].click();", links.get(2));
					break;
				}
			}
	}



	/**
	 * @summary  Method to verify Smart Form Item Added to Cart
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void verify_CartItem(String ItemDescription, int cartItemRows) {

		// Verifies Item added in the Cart
		String Expected = driver.findElement(
				By.xpath(".//*[@id='tblCartInfo']/tbody/tr["+ (cartItemRows - 1) + "]/td[2]/span")).getText();
		TestReporter.logStep("Actual :" + ItemDescription + ": "+ "Expected : " + Expected);
		TestReporter.assertTrue(Expected.contains(ItemDescription),"Smart Form Item Verified!!");
	}



	/**
	 * @summary This Method Verifications For Smart Form Items updated with UOM
	 * Quantity,Unit Price
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void verify_UpdateCart(String UP, String UOM, String Qty,
			int cartItemRows) {

		// Clicking on Edit
		driver.findElement(	By.xpath(".//*[@id='tblCartInfo']/tbody/tr["+ (cartItemRows - 1) + "]/td/div/a[2]")).click();
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
		driver.findElement(
				By.xpath(".//*[@id='tblCartInfo']/tbody/tr["
						+ (cartItemRows - 1) + "]/td/div/a[2]")).click();
		Sleeper.sleep(3000);

		// Verifying Cart Update
		TestReporter.assertTrue(txtQuantity.getAttribute("value").equalsIgnoreCase(Qty), "Smart Item Qunatity Updated!!");
		TestReporter.assertTrue(txtUnitPrice.getAttribute("value").equalsIgnoreCase(UP), "Unit Price Updated!!");
		TestReporter.assertTrue(unitOfMeasure.getFirstSelectedOption().getText().equals(UOM), "Unit Of measure Updated!!");

	}

	/**@summary This Method performs Verifications For Smart Form Items 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void verifyCartItem(String ItemDescription, String UnitPrice,
			String UOM, String Quantity) {

		// clicking cart link
		clickCartItemsLink();

		// Reading Rows/Columns Count from Cart Item grid
		List<WebElement> cartItemRows = driver.findWebElements(By.xpath(".//*[@id='tblCartInfo']/tbody/tr"));
		TestReporter.logStep("Total Rows available in Cart :"+ cartItemRows.size());
		List<WebElement> cartItemCols = driver.findElements(By.xpath(".//*[@id='tblCartInfo']/tbody/tr["
				+ (cartItemRows.size() - 1) + "]/td"));
		TestReporter.logStep("Total Columns available in Cart :"+ cartItemCols.size());

		// Verification - Smart form Item Updated with Quantity, UOM and UnitPrice
		verify_UpdateCart(UnitPrice, UOM, Quantity, cartItemRows.size());

	}

	/**
	 * @summary This Method performs all Verifications For  Local,Global,Favorite,ComparisonSceen Cart Items
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void perform_CartItemVerifications(String UnitPrice,String UOM,String Quantity,String itemNumber) {
		// clicking cart link
		clickCartItemsLink();

		// Reading Rows Count from CartItem grid
		List<WebElement> cartItemRows = driver.findWebElements(By
				.xpath(".//*[@id='tblCartInfo']/tbody/tr"));
		TestReporter.logStep("Total Rows available in Cart :"+ cartItemRows.size());

		// Click on Edit
		click_CartItemEdit(itemNumber);

		// Update - Unit Price , UOM
		TestReporter.logStep("Item Qty befor Update : "+ txtItemQuantity.getAttribute("value"));
		String actualQty = txtItemQuantity.getAttribute("value");

		txtItemUnitPrice.syncVisible(5, false);
		txtItemUnitPrice.clear();
		txtItemUnitPrice.safeSet(UnitPrice);
		lstItemunitOfMeasure.select(UOM);

		btnAddToCart.jsClick();

		// clicking cart link
		clickCartItemsLink();

		// Reading Quantity after changing UOM
		String expectedQty = driver.findElement(
				By.xpath(".//*[@id='tblCartInfo']/tbody/tr["
						+ (cartItemRows.size() - 1) + "]/td[5]/input"))	.getAttribute("value");
		TestReporter.logStep("Item Qty after Update : "+ expectedQty);

		// Click on Edit
		click_CartItemEdit(itemNumber);

		String expectedUP = txtItemUnitPrice.getAttribute("value");
		String expectedUOM = lstItemunitOfMeasure.getFirstSelectedOption().getText();


		// Verify -  Quantity changed as per UOM
		TestReporter.assertTrue(!actualQty.equalsIgnoreCase(expectedQty),"Quantity Updated !!");
		// Verify - updated UOM
		TestReporter.assertTrue(UOM.equalsIgnoreCase(expectedUOM),"Unit of Measure Updated !!");
		// Verify - Updated UP
		TestReporter.assertTrue(UnitPrice.equalsIgnoreCase(expectedUP),"Unit Price Updated !!");


	}


	/**
	 * @summary Verify Cart Items 
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public boolean verifyCartValue(String ItemType){
		boolean statsuFlag = false;
		Sleeper.sleep(15000);
		TestReporter.logStep("Cart Having [" + lblCartValue.getText()+"] Items!!");
		if(Integer.parseInt(lblCartValue.getText())>=2){
			TestReporter.logStep("Cart Having [" + lblCartValue.getText()+"] Items!!");
			statsuFlag = true;
		}else{
			statsuFlag = false;
		}
		return statsuFlag;
	}


	/**
	 * @summary Method for Application Logout
	 * @author Lalitha Banda
	 * @date 14/9/16
	 **/
	public void clickLogout() {
		isLogoutDisplayed();
		lnkLogout.click();
		driver.setPageTimeout(2);
		SaveCart();
	}


	// Method for Checkout the Cart
	public void saveCartPopUp(){
		pageLoaded();
		btnsaveCartPopUp.isDisplayed();
		btnsaveCartPopUp.jsClick();
		driver.setPageTimeout(2);
	}

	// Method for Checkout the Cart
	public void CheckOut(){
		pageLoaded();
		btnCheckOut.isDisplayed();
		btnCheckOut.click();
	}


	/**
	 * @summary Method 'to click on Cart Check Out 
	 * @author Lalitha Banda
	 * @date 15/9/16
	 **/
	public void cart_CheckOut(){
		clickCartItemsLink();
		saveCartPopUp();
		CheckOut();
	}
	
}
