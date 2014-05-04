package GreatDouBaba;

public class Historyhtml {
		
	public static String Generatehtml(GDB_DTB_High_Interface gdb){
		String[][] tmp=gdb.getHistory();
		String htmlcontext="";
		String htmlhead="<!DOCTYPE html> <html> <head> <title>History</title> </head>";
		String htmlbodystarts="<body>";
		for(int i=0;i<tmp.length;i++){
			htmlcontext="<tr><td>"+ "<a href=\""+tmp[i][0]+"\"> </td>"+"<td>"+tmp[i][2]+"</td>"+"<td>"+tmp[i][1]+"</td></tr>";
		}
		String htmlbodyends="</body>";	
		String htmlend="</html>";
		String finalstring=htmlhead+htmlbodystarts+htmlcontext+htmlbodyends+htmlend;
		return finalstring;
	}
}
