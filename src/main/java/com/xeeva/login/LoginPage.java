package com.xeeva.login;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.OrasiDriver;

public class LoginPage {
	
	private OrasiDriver driver = null;
	
	/**Page Elements**/
	@FindBy(id = "txtUserName")	private Textbox txtUsername;
	@FindBy(id = "txtPassword")	private Textbox txtPassword;
	@FindBy(id = "btnLogin")	private Button btnLogin;
	
	//Constructor
	public LoginPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	/**Page Interactions**/
	
	public void verifyPageIsLoaded(){
		
		
			
	}
	
	public void login(){
			
	}
}
