
public class Test {

	
	public int test() throws Exception{
		String s = null;
		try{
			s ="HEY BABY";
			System.out.println("try "+s);
			return 1;
		}catch(Exception e){
			System.out.println("exception "+s);
			throw new Exception("");
		}finally{
			System.out.println("finally "+s);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		Test t = new Test();
		t.test();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
