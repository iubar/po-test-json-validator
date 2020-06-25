package it.iubar.json.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EveritTest {
	
	private static final Logger LOGGER = Logger.getLogger(EveritTest.class.getName());
	
	private static String schema1 = "/hello-schema.json"; // "additionalProperties": true, "required": ... 
	private static String schema2 = "/hello2-schema.json"; // "additionalProperties": false, "required": ... 

	@Test
	public void testRelaxedSyntax2()   {
		String strJson = "{\"hello\" : \"world\";}";  // relaxed syntax
		assertDoesNotThrow(() -> parseWithTheEveritLib(schema1 , strJson));
	}
	
	@Test
	public void testEveritWrongSyntax2()  {
		String strJson = "{\"hello\" : \"world\"\"}";  // wrong syntax
		int errorCount = assertDoesNotThrow(() -> parseWithTheEveritLib(schema1, strJson));
		assertEquals(1, errorCount);
	}
	
	@Test
	public void testEveritRightSyntax2()   {
		String strJson = "{\"hello\" : \"world\"}";
		assertDoesNotThrow(() -> parseWithTheEveritLib(schema1, strJson));
	}
			
	@Test
	public void testEveritRequired1()   {
		String strJson = "{\"missingKeyFromSchema\" : \"should fail because the hello key is required\"}";
		int errorCount = assertDoesNotThrow(() -> parseWithTheEveritLib(schema1, strJson));
		assertEquals(1, errorCount);
	}
	
	@Test
	public void testEveritAdditionalProperties1()   {
		String strJson = "{\"hello\" : \"world\", \"missingKeyFromSchema\" : \"should pass because additionalProperties is true\"}";
		int errorCount = assertDoesNotThrow(() -> parseWithTheEveritLib(schema1, strJson));
		assertEquals(0, errorCount);		
	}	
	
	@Test	
	public void testEveritRequired2()   {
		String strJson = "{\"missingKeyFromSchema2\" : \"should fail because the hello key is required and additionalProperties is false \"}";
		int errorCount = assertDoesNotThrow(() -> parseWithTheEveritLib(schema2, strJson));
		assertEquals(2, errorCount);
	}
	
	@Test
	public void testEveritAdditionalProperties2()   {
		String strJson = "{\"hello\" : \"world\", \"missingKeyFromSchema\" : \"should fail because additionalProperties is false\"}";
		int errorCount = assertDoesNotThrow(() -> parseWithTheEveritLib(schema2, strJson));
		assertEquals(1, errorCount);
	}	
	
	@Test
	public void testEveritRightSyntax3()   {
		String strJson = "{\"hello\" : \"world\"}";
		assertDoesNotThrow(() -> parseWithTheEveritLib(schema2, strJson));
	}	


	private int parseWithTheEveritLib(String schemaPath, String strJson) throws FileNotFoundException {				
		EveritStrategy strategy = new EveritStrategy();
		File schemaFile = new File(EveritTest.class.getResource(schemaPath).getFile());
		strategy.setSchema(schemaFile);
		return strategy.validate(strJson);
	
	}
}
