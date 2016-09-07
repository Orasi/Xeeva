package com.xeeva.navigation;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.TestReporter;

public class MainNav {
	
	private OrasiDriver driver = null;
	

	/**Page Elements**/
	
	@FindBy(linkText = "Logout") private Link lnkLogout;
	@FindBy(id = "btnSaveCart") private Button btnSaveCart;
	
	
	/**Constructor**/
	
	public MainNav(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	/**Page Interactions**/
	
	public boolean isLogoutDisplayed(){
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lnkLogout.syncVisible(20, false);
	}
	
	 // Method for Application Logout 
		public void SaveCart(){
			
			if(btnSaveCart.isDisplayed()){
				btnSaveCart.click();
			}else{
				TestReporter.logStep("Cart Empty to Save");
			}
		}
	
	
	 // Method for Application Logout 
	public void clickLogout(){
		isLogoutDisplayed();
		lnkLogout.click();
		SaveCart();
	}
}
