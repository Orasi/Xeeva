package com.xeeva.login;

import java.util.ResourceBundle;

import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;

public class LoginPage {
	
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);
	
	/**Page Elements**/
	@FindBy(id = "txtUserName")	private Textbox txtUsername;
	@FindBy(id = "txtPassword")	private Textbox txtPassword;
	@FindBy(id = "btnLogin")	private Button btnLogin;
	@FindBy(id = "ddlLocation")	private Listbox lstLocation;
	
	/**Constructor**/
	public LoginPage(OrasiDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	/**Page Interactions**/
	
	public void verifyPageIsLoaded(){
		
		
			
	}
	

	public void login(){
	}
	
			

	
	/**
	 * This method logins to the application.  Note - the location drop down field only
	 * displays if the user has more than 1 location associated to it.  So if the location
	 * drop down does not display, then will not attempt to select a location
	 * @author
	 * @param username
	 * @param location
	 * @param password
	 */
	public void loginWithCredentials(String role, String location){
		txtUsername.set(userCredentialRepo.getString(role));
		txtPassword.set(userCredentialRepo.getString("PASSWORD"));
		
		if (lstLocation.syncVisible(1, false)){
			lstLocation.select(location);
		}
		btnLogin.click();

	}
	
	
}
