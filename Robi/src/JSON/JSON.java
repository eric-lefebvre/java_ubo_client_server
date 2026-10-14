package JSON;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {

	public static String Java2Json(Object o) {
		String res = null;
		try {
			StringWriter writer = new StringWriter();
			JsonGenerator generator = new JsonFactory().
				createGenerator(writer);
			generator.setCodec(new ObjectMapper());
			generator.writeObject(o); 
			generator.close();
			res = writer.toString();

		}
		catch (Exception e) {
			System.out.println("Erreur JSON.Java2Json : "+e.getMessage());
		}
		return res;
	}
	
	public static Object Json2Java(String json, Class c) {
		Object res = null;
		try {
			Reader reader = new StringReader(json); // flot pour lire le JSON
			res  = new ObjectMapper().readValue(reader, c);
		}
		catch (Exception e) {
			System.out.println("Erreur JSON.Json2Java : "+e.getMessage());
		}
		return res;
	}
}
