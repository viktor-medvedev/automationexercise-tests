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

        // Women -> Dress
        CategoryPage womenDress = home.openWomenDressCategory();
        womenDress.waitTitleVisible();

        String womenTitle = womenDress.getTitleText().toUpperCase().replaceAll("\\s+", " ").trim();
        Assert.assertTrue(womenTitle.contains("WOMEN") && womenTitle.contains("DRESS"),
                "Unexpected category title: " + womenDress.getTitleText());

        // IMPORTANT: go back to Home before second navigation (stability)
        home.open(baseUrl);

        // Men -> Tshirts
        CategoryPage menTshirts = home.openMenTshirtsCategory();
        menTshirts.waitTitleVisible();

        String menTitle = menTshirts.getTitleText().toUpperCase().replaceAll("\\s+", " ").trim();
        Assert.assertTrue(menTitle.contains("MEN") && (menTitle.contains("TSHIRTS") || menTitle.contains("TSHIRT")),
                "Unexpected category title: " + menTshirts.getTitleText());
    }
}
