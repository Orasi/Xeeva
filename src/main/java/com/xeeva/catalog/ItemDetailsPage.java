package com.xeeva.catalog;

	import java.util.ResourceBundle;

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
	 * @author  Praveen Namburi, @version: Created 07-09-2016.
	 */
	public class ItemDetailsPage {

		private OrasiDriver driver = null;
		private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

		/**Page Elements**/
		@FindBy(linkText = "lnkShowPopup") private Link lnkCartItem;
		@FindBy(id = "txtBasicSearchCriteria")	private Textbox catalogSearch;
		@FindBy(xpath = "//select[@class='textFieldList width90Px']") private Listbox lstSelectUOM;
		@FindBy(xpath="//div[@class='add-to-cart-box']") private Button btnAddToCart;
		@FindBy(xpath="//div[@id='divAppInfoMsg'][@class='addMessage']") private Label lblCartItemAddedMessage;
		
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
			Sleeper.sleep(2000);
			lblCartItemAddedMessage.syncVisible(15, false);
			String getCartItemAddedMessage = lblCartItemAddedMessage.getText();
			System.out.println("Message after adding item to cart : "+ getCartItemAddedMessage);
			TestReporter.assertTrue(getCartItemAddedMessage.contains("added successfully!"), "Item added to the cart.");
		}
		
		/**
		  * @summary: Method to select UOMValue and Add-Item-To-Cart for Non-Price agreement item.
		  * @author: Praveen Namburi, @version: Created 09-09-2016,@param strUOMValue
		  */
		 public void selectUOMValueAndAddItemToCart(String strUOMValue){
		  if(lstSelectUOM.isDisplayed()){
			   lstSelectUOM.select(strUOMValue);
			   btnAddToCart.jsClick();
			   Sleeper.sleep(2000);
			   lblCartItemAddedMessage.syncVisible(15, false);
			   String getCartItemAddedMessage = lblCartItemAddedMessage.getText();
			   TestReporter.logStep("Message after adding item to cart : "+ getCartItemAddedMessage);
			   TestReporter.assertTrue(getCartItemAddedMessage.contains("added successfully!"), "Item added to the cart.");
		  }else{
			   TestReporter.logStep("Selected Item is Price aggrement Item!!");

		  }
		
	     }
		 
	}

