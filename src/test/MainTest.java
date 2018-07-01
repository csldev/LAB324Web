package test;

public class MainTest {

	public static void main(String args[]) {
		PathTest test = new PathTest();
		
		System.out.println(test.writeAbsoluteFile("pathTest1.txt"));
		System.out.println(test.writeRelativeFile("pathTest2.txt"));
		System.out.println(test.writeRelativeFile2("pathTest3.txt"));
	}
	
}
