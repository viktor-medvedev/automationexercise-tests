## UI Test Coverage (AutomationExercise Test Cases)

- [x] **TC01 — Register User** — `src/test/java/ui/tests/RegisterUserTest.java`
- [x] **TC02 — Login User with correct email and password** — `src/test/java/ui/tests/LoginUserWithCorrectCredentialsTest.java`
- [x] **TC03 — Login User with incorrect email and password** — `src/test/java/ui/tests/LoginUserWithInvalidCredentialsTest.java`
- [x] **TC04 — Logout User** — `src/test/java/ui/tests/LogoutUserTest.java`
- [x] **TC05 — Register User with existing email** — `src/test/java/ui/tests/RegisterUserWithExistingEmailTest.java`
- [x] **TC06 — Contact Us Form** — `src/test/java/ui/tests/ContactUsFormTest.java`
- [x] **TC07 — Test Cases Page** — `src/test/java/ui/tests/VerifyTestCasesPageTest.java`
- [x] **TC08 — Products Page & Product Details** — `src/test/java/ui/tests/ProductsAndDetailsTest.java`
- [x] **TC09 — Search Product** — `src/test/java/ui/tests/SearchProductTest.java`
- [x] **TC10 — Verify Subscription (Home Page)** — `src/test/java/ui/tests/VerifySubscriptionHomeTest.java`
- [x] **TC11 — Verify Subscription (Cart Page)** — `src/test/java/ui/tests/VerifySubscriptionCartTest.java`
- [x] **TC12 — Add Products in Cart** — `src/test/java/ui/tests/AddTwoProductsToCartTest.java` *(и/или `AddProductToCartTest.java` если он у тебя есть)*
- [x] **TC13 — Verify Product Quantity in Cart** — `src/test/java/ui/tests/VerifyQuantityInCartTest.java`
- [x] **TC14 — Place Order: Register while Checkout** — `src/test/java/ui/tests/PlaceOrderRegisterWhileCheckoutTest.java`
- [x] **TC15 — Place Order: Register before Checkout** — `src/test/java/ui/tests/PlaceOrderRegisterBeforeCheckoutTest.java`
- [x] **TC16 — Place Order: Login before Checkout**
    - `src/test/java/ui/tests/ProceedToCheckoutAfterLoginTest.java`
    - `src/test/java/ui/tests/OpenPaymentFromCheckoutTest.java`
    - `src/test/java/ui/tests/PlaceOrderWithPaymentTest.java`
- [x] **TC17 — Remove Products From Cart** — `src/test/java/ui/tests/RemoveProductsFromCartTest.java`
- [x] **TC18 — View Category Products** — `src/test/java/ui/tests/ViewCategoryProductsTest.java`
- [x] **TC19 — View & Cart Brand Products** — `src/test/java/ui/tests/ViewBrandProductsTest.java`
- [x] **TC20 — Search Products and Verify Cart After Login** — `src/test/java/ui/tests/SearchProductsVerifyCartAfterLoginTest.java`
- [x] **TC21 — Add Review on Product** — `src/test/java/ui/tests/AddReviewOnProductTest.java`
- [x] **TC22 — Add to Cart from Recommended Items** — `src/test/java/ui/tests/AddRecommendedItemToCartTest.java`
- [x] **TC23 — Verify Address Details in Checkout** — `src/test/java/ui/tests/VerifyAddressDetailsInCheckoutTest.java`
- [x] **TC24 — Download Invoice after purchase** — `src/test/java/ui/tests/DownloadInvoiceAfterPurchaseTest.java`
- [x] **TC25 — Scroll Up using Arrow button** — `src/test/java/ui/tests/ScrollUpArrowButtonTest.java`
- [x] **TC26 — Scroll Up without Arrow button** — `src/test/java/ui/tests/ScrollUpWithoutArrowButtonTest.java`

### How to run
- Run all UI tests:
  ```bash
  mvn test
# automationexercise-tests