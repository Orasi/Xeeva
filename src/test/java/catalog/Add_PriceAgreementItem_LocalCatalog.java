package catalog;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.xeeva.catalog.CartInformationPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.LocalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requestor is able to add the item from local catalog. 
 * @author praveen varma, @version: Created 08-09-2016
 */
 public class Add_PriceAgreementItem_LocalCatalog extends TestEnvironment {
			
	 public String RequisitionType = "serviceRequestGeneral";
		
		// **************
		// Data Provider
		// **************
		@DataProvider(name = "dataScenario")
		public Object[][] scenarios() {
			try {
				Object[][] excelData = new ExcelDataProvider("/datasheets/Catalog.xlsx","AddPriceAgrmnt_LocalCatalog").getTestData();
				return excelData;
			}
			catch (RuntimeException e){
				TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
			}
			return new Object[][] {{}};
	    }
		
		/**
		 * @description: To initialize the driver and setup the environment.
		 * @param runLocation,browserUnderTest,@param browserVersion
		 * @param operatingSystem,@param environment
		 */
		@BeforeTest
		@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
		public void setup(@Optional String runLocation, String browserUnderTest,String browserVersion, String operatingSystem, String environment) {
			setApplicationUnderTest("XEEVA");
			setBrowserUnderTest(browserUnderTest);
			setBrowserVersion(browserVersion);
			setOperatingSystem(operatingSystem);
			setRunLocation(runLocation);
			setTestEnvironment(environment);
			testStart("Add_PriceAgreementItem_InCart");
		}

		/**
		 * @description: Close the driver instance.
		 * @param testResults
		 */
		/*@AfterTest
		public void close(ITestContext testResults){
			endTest("TestAlert", testResults);
		}*/
		
		/**
		 * @Description: Main business-logic of the test-case resides here.
		 * @param role,location,selectUOM
		 */
		@Test(dataProvider = "dataScenario")
		public void add_PriceAgreementItem_LocalCatalog(String role, String location,String ItemType,
			String itemNumber){
			
			// Application Login 
			LoginPage loginPage = new LoginPage(getDriver());
			TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
			loginPage.loginWithCredentials(role,location);
			
			// Requisition Page  - Navigating to requisition page to create Smart Form Request
			RequisitioningPage reqPage = new RequisitioningPage(getDriver());
			TestReporter.logStep("Navigate to Requisitioning Page and Search for Local catalog Item.");
			reqPage.click_ReqTab();
			reqPage.verify_SearchItems(ItemType, itemNumber);
			
			//Navigate to Local-Items page and Add-Item to Cart.
			LocalItemsTab localItemsPage = new LocalItemsTab(getDriver());
			TestReporter.logStep("Navigate to Local-Items page and Click on Cart-Items link.");
			localItemsPage.clickCartItemsLink();
			
			//Navigate to CartInformation page and Verify Added item Unit-Price is not editable.
			TestReporter.logStep("Navigating to CartInformation page.");
			CartInformationPage cartInfoPage = new CartInformationPage(getDriver());
			//Verify whether the quantity can be increased for already added item in the cart.
			TestReporter.log("Verify whether the quantity can be increased for already added item in the cart.");
			//String getQuantityBefore = cartInfoPage.verifyQuantitybeforeAddingItemToCart(itemNumber);
			//TestReporter.log("Quantity value before addign item to cart : "+getQuantityBefore);
			cartInfoPage.closeCartInfoPage();
			
			//Navigate to Local-Items page and Add-Item-To-Cart.
			TestReporter.logStep("Navigate to Local-Items page and Add-Item-To-Cart.");
			localItemsPage.addLocalItemToCartAndVerify();
			localItemsPage.clickCartItemsLink();
			
			//Validating that the Unit-Price is not editable for the added item in the cart.
			TestReporter.log("Validating that the Unit-Price is not editable for the added item in the cart.");
			cartInfoPage.verifyUnitPriceIsNotEditable(itemNumber);
			cartInfoPage.closeCartInfoPage();
			
			// Application Logout
			MainNav mainNav = new MainNav(getDriver());
			TestReporter.logStep("Log-Out of the application.");
			mainNav.clickLogout();
			
		}
			
   }

