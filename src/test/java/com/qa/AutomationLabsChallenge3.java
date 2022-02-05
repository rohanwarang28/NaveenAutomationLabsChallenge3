package com.qa;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AutomationLabsChallenge3 {
	static WebDriver driver;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		 driver = new ChromeDriver();
		//login to app
		driver.get("https://www.saucedemo.com/");
		driver.findElement(By.id("user-name")).sendKeys("standard_user");
		driver.findElement(By.id("password")).sendKeys("secret_sauce");
		driver.findElement(By.id("login-button")).click();
		List<WebElement> pricesElements = driver.findElements(By.cssSelector(".inventory_item_price"));
		//get the price and name of the costliest product
		double highestPrice = getHighestPrice(pricesElements);
		String highestPriceName = getCostliestProdName(pricesElements,highestPrice);
		
		//add to cart the highest price product
		addToCartCostliestProduct(pricesElements,highestPrice);
		
		//verify whether product is present in the cart
		driver.findElement(By.className("shopping_cart_link")).click();
		if(verifyProductAddedToCart(highestPriceName))
			System.out.println("Product is present in the cart");
		else
			System.out.println("Product is not present in the cart");
		
		driver.quit();
	}
		
	public static Double getHighestPrice(List<WebElement> pricesElements){
		
		List<Double> prices = pricesElements	
			.stream()
			.map(s->s.getText())
			.map(s->Double.parseDouble(s.replace("$", "")))
			.distinct()
			.collect(Collectors.toList());
			
		return Collections.max(prices);
				
	}
	
	public static void addToCartCostliestProduct(List<WebElement> pricesElements, double highestPrice) {
			List<WebElement> costliestElements = pricesElements
			.stream()
			.filter(s->s.getText()
			.equals("$"+highestPrice))
			.distinct()
			.collect(Collectors.toList());
			
			costliestElements.get(0).findElement(By.xpath("following-sibling::button")).click();
	}
	
	public static String getCostliestProdName(List<WebElement> pricesElements, double highestPrice) {
			 return pricesElements
			.stream()
			.filter(s->s.getText()
			.equals("$"+highestPrice))
			.distinct()
			.collect(Collectors.toList())
			.get(0).findElement(By.xpath("parent::div/preceding-sibling::div//div[@class='inventory_item_name']")).getText();
	}
	
	public static boolean verifyProductAddedToCart(String productName) {
	return driver.findElements(By.cssSelector(".inventory_item_name"))
		.stream()
		.anyMatch(s->s.getText().equals(productName));
	}

}
