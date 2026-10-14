package JSON;

public class TestJson {
	
 public static void main(String [] args) {
	 
	 Message m1 = new Message("robi","(space setColor \"black\")");
	 
	 System.out.println("message m1 = "+m1.toString());
	 
	 String json = JSON.Java2Json(m1);
	 
	 System.out.println("json = "+json);
	 
	 Message m2 = (Message)JSON.Json2Java(json, Message.class);
	 
	 System.out.println("message m2 = "+m2.toString());

	 
 }
 
}
