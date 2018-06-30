package utils;


public class DataHandler {

	public String handle(String dateString) {
		String string;
		int index = 0;
		String date = dateString.substring(index, dateString.indexOf("....",index)).replace("-","");
		index = dateString.indexOf("....",index)+8;
		String item = dateString.substring(index,dateString.indexOf("....",index));
		index = dateString.indexOf("....",index)+8;
		String change = dateString.substring(index,dateString.indexOf("....",index));
		index = dateString.indexOf("....",index)+8;
		String remain = dateString.substring(index);
		
		string = "{"
				+ "\"date\":\""+date+"\","
				+ "\"item\":\""+item+"\","
				+ "\"change\":\""+change+"\","
				+ "\"remain\":\""	+remain+"\""
				+ "},";
		
		return string;
	}
	
}
