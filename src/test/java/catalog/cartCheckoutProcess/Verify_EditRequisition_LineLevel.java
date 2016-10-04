package catalog.cartCheckoutProcess;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.xeeva.catalog.CostCenterPage;
import com.xeeva.catalog.ItemDetailsPage;
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.catalog.SearchItems.GlobalItemsTab;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;


public class Verify_EditRequisition_LineLevel extends TestEnvironment{
	
	public String RequisitionType = "serviceRequestGeneral";
	public String itemType = "EditItem";
	
		// **************
			// Data Provider
			// **************
			@DataProvider(name = "dataScenario")
			public Object[][] scenarios() {
				try {
					Object[][] excelData = new ExcelDataProvider("/datasheets/SmartForm.xlsx","SmartForm").getTestData();
					return excelData;
				}
				catch (RuntimeException e){
					TestReporter.assertTrue(false, "An error occured with accessing the data provider: " + e);
				}
				return new Object[][] {{}};
			}


			@BeforeTest
			@Parameters({ "runLocation", "browserUnderTest", "browserVersion","operatingSystem", "environment" })
			public void setup(@Optional String runLocation, String browserUnderTest,
					String browserVersion, String operatingSystem, String environment) {
				setApplicationUnderTest("XEEVA");
				setBrowserUnderTest(browserUnderTest);
				setBrowserVersion(browserVersion);
				setOperatingSystem(operatingSystem);
				setRunLocation(runLocation);
				setTestEnvironment(environment);
				testStart("Verify_CopiedItem_LineLevel");
			}

			@AfterTest
			public void close(ITestContext testResults){
				//endTest("TestAlert", testResults);
			}

			@Test(dataProvider = "dataScenario")
			public void CopiedItem(String role, String location,String ItemDescription,String UNSPSCCode,String SS,
					String CategoryType,String Category,String SubCategory,String MN,String MPN,
					String Quantity,String UnitofMeasure,String Price){

				String[] QuantityArray = Quantity.split(";");
				String[] UOMArray = UnitofMeasure.split(";");
				String[] UPArray = Price.split(";");
				
				
				// Application Login 
				TestReporter.logStep("Application Login");
				LoginPage loginPage = new LoginPage(getDriver());
				loginPage.loginWithCredentials(role,location);
				
				// Requisition Page   
				TestReporter.logStep("Navigating to requisition page to perform catalog search");
				RequisitioningPage reqPage = new RequisitioningPage(getDriver());
				reqPage.click_ReqTab();
				
				//Creating Smart Form Request
				TestReporter.logStep("Creating Smart Form Request");
				reqPage.createSmartFormRequest( RequisitionType,ItemDescription, UNSPSCCode,SS,CategoryType, 
						Category, SubCategory, MN, MPN, QuantityArray[0], UOMArray[0],UPArray[0]);

				//Navigating to Cart CheckOut Page
				TestReporter.logStep("Navigating to Cart CheckOut Page ");
				MainNav mainNav = new MainNav(getDriver());
				mainNav.cart_CheckOut();
				
				//Clicking EditItem Link in CostCenter Page and verifying Edit link
				TestReporter.logStep("Clicking Edit Item Link");
				CostCenterPage costCenterPage = new CostCenterPage(getDriver());
				costCenterPage.verifyEDITlinkEnabledOrNot();
				costCenterPage.verifyCostCenter(itemType,null,null,null,ItemDescription);
				
				// Application Logout
				TestReporter.logStep("Application Logout");
				mainNav.clickLogout();
			}
}

