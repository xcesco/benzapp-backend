package it.insiel.innovazione.poc.benzapp.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import it.insiel.innovazione.poc.benzapp.BenzappApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = BenzappApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
