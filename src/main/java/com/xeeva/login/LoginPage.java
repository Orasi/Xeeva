package com.xeeva.login;

import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Constants;
import com.orasi.utils.OrasiDriver;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Sleeper;

public class LoginPage {
	PageLoaded pl = new PageLoaded();
	private OrasiDriver driver = null;
	private ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

	/**Page Elements**/
	@FindBy(id = "txtUserName")	private Textbox txtUsername;
	@FindBy(id = "txtPassword")	private Textbox txtPassword;
	@FindBy(id = "btnLogin")	private Button btnLogin;
	@FindBy(id = "ddlLocation")	private Listbox lstLocation;
	@FindBy(id="Email") private Textbox txtEmail;
	@FindBy(id="password") private Textbox txtPasswrd;
	@FindBy(id="button_Login") private Button btnlogin;
	@FindBy(className="dropdown-toggle") private Link lnkLogin;
	@FindBy(css=".dropdown-menu>li>a") private Link lnkSubLogin;

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
		if (lstLocation.syncVisible(30, false)){
			lstLocation.select(location);
		}
		btnLogin.syncVisible(30, false);
		btnLogin.jsClick();
	}

	/**
	 * This method logins to the application.  Note -  User name provided to this method is reading at runtime
	 * @author Lalitha Banda
	 * @param username
	 * @param location
	 * @param password
	 * 
	 */
	public void loginWithRuntimeUsername(String role, String location){
		txtUsername.set(role);
		txtPassword.set(userCredentialRepo.getString("PASSWORD"));

		if (lstLocation.syncVisible(20, false)){
			lstLocation.select(location);
		}
		btnLogin.syncVisible(20, false);
		btnLogin.jsClick();
	}

	
	/**
	 * This method logins to the Market Place application.  
	 * @author Lalitha Banda
	 * @param username
	 * @param password
	 */
	public void loginWithSupplierCredentials(String role){
		pl.isDomComplete(driver);
		Sleeper.sleep(5000);
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.elementToBeClickable(By.className("dropdown-toggle")));
		driver.findElement(By.cssSelector(".dropdown-menu>li>a")).jsClick();
		pl.isDomComplete(driver);
		txtEmail.set(userCredentialRepo.getString(role));
		txtPasswrd.set(userCredentialRepo.getString("PASSWORD"));
		btnlogin.syncVisible(10, false);
		btnlogin.click();
	}
	
	/**
	 * @Summary: This method logins to the Market Place application with captured Supplier credentials.  
	 * @author Praveen Namburi,@Version: Created 17-10-2016
	 * @param SupplierRole
	 * @param location
	 */
	public void loginWithRuntimeSupplierCredentials(String SupplierRole, String location){
		pl.isDomComplete(driver);
		Sleeper.sleep(5000);
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.elementToBeClickable(By.className("dropdown-toggle")));
		driver.findElement(By.cssSelector(".dropdown-menu>li>a")).jsClick();
		pl.isDomComplete(driver);
		txtEmail.set(SupplierRole);
		txtPasswrd.set(userCredentialRepo.getString("PASSWORD"));
		btnlogin.syncVisible(10, false);
		btnlogin.click();
	}
	
}

