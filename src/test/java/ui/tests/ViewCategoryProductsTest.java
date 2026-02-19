package ui.tests;

import config.TestConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.base.BaseUiTest;
import ui.pages.CategoryPage;
import ui.pages.HomePage;

public class ViewCategoryProductsTest extends BaseUiTest {

    @Test
    public void viewCategoryProducts() {
        String baseUrl = TestConfig.baseUrl();

        HomePage home = new HomePage(driver);
        home.open(baseUrl);

        CategoryPage womenDress = home.openWomenDressCategory();
        womenDress.waitTitleVisible();

        String womenTitle = womenDress.getTitleText().toUpperCase().replaceAll("\\s+", " ").trim();
        Assert.assertTrue(womenTitle.contains("WOMEN") && womenTitle.contains("DRESS"),
                "Unexpected category title: " + womenDress.getTitleText());

        // второй переход — заново на Home (проще и стабильнее)
        CategoryPage menTshirts = home.openMenTshirtsCategory();
        menTshirts.waitTitleVisible();
        String menTitle = menTshirts.getTitleText().toUpperCase();

        Assert.assertTrue(menTitle.contains("MEN") && menTitle.contains("TSHIRTS"),
                "Unexpected category title: " + menTshirts.getTitleText());
    }
}
