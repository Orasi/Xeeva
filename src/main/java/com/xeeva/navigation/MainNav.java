package com.xeeva.navigation;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;

public class MainNav {
	
	private OrasiDriver driver = null;
	

	/**Page Elements**/
	
	@FindBy(linkText = "Logout") private Link lnkLogout;
	
	/**Constructor**/
	
	public MainNav(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	/**Page Interactions**/
	
	public boolean isLogoutDisplayed(){
		return lnkLogout.syncVisible(10, false);
	}
}
