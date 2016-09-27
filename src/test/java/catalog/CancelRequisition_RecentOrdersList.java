package catalog;

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
import com.xeeva.catalog.RequisitioningPage;
import com.xeeva.login.LoginPage;
import com.xeeva.navigation.MainNav;

/**
 * @Summary: To verify that requester is able to cancel the requisition from recent order list.
 * @author praveen varma, @version: Created 22-09-2016
 */
public class CancelRequisition_RecentOrdersList extends TestEnvironment{

		 public String RequisitionType = "serviceRequestGeneral";
			
			// **************
			// Data Provider
			// **************
			@DataProvider(name = "dataScenario")
			public Object[][] scenarios() {
				try {
					Object[][] excelData = new ExcelDataProvider("/datasheets/CancelRequisition_RecentOrdersList.xlsx",
							"CancelReq_RecentOrdersList").getTestData();
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
			@AfterTest
			public void close(ITestContext testResults){
				endTest("TestAlert", testResults);
			}
			
			/**
			 * @Description: Main business-logic of the test-case resides here.
			 * @param role,location,comments
			 */
			@Test(dataProvider = "dataScenario")
			public void cancelRequisition_RecentOrdersList(String role, String location,String comments){
				
				// Application Login 
				LoginPage loginPage = new LoginPage(getDriver());
				TestReporter.logStep("Launch the application and Login with valid Requestor credentials");
				loginPage.loginWithCredentials(role,location);
				
				// Requisition Page  - Navigating to requisition page to create Smart Form Request
				RequisitioningPage reqPage = new RequisitioningPage(getDriver());
				TestReporter.logStep("Navigate to Requisitioning Page and Click on Recent Orders Tab.");
				reqPage.click_ReqTab();
				reqPage.clickRecentOrdersTab();
				
				// Cancel Requisition in Recent Orders.
				TestReporter.logStep("Cancel Requisition from Recent Orders.");
				reqPage.cancelRequisitionFromRecentOrders(comments);
				
				// Application Logout
				MainNav mainNav = new MainNav(getDriver());
				TestReporter.logStep("Log-Out of the application.");
				mainNav.clickLogout();
				
			}
				
	   }


