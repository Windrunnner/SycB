package GreatDouBaba;

public class Historyhtml {
		
	public static String Generatehtml(GDB_DTB_High_Interface gdb){
		String[][] tmp=gdb.getHistory();
		String htmlcontext="";
		String htmlhead="<!DOCTYPE html> <html> <head> <title>History</title>"
				+ "<style>table.dataintable {   border: 1px solid #888888;border-collapse: collapse;   font-family: Arial,Helvetica,sans-serif;   margin-top: 10px;   width: 100%;}table.dataintable th {   background-color: #CCCCCC;   border: 1px solid #888888;   padding: 5px 15px 5px 5px;   text-align: left;   vertical-align: baseline;}table.dataintable td {   background-color: #EFEFEF;   border: 1px solid #AAAAAA;   padding: 5px 15px 5px 5px;   vertical-align: text-top;}</style>"
				+ " </head>";
		String htmlbodystarts="<body><h2>History</h2><table class=\"dataintable\">";
		htmlcontext = "<tr>	<th>Title</th><th>Date</th></tr>";
		for(int i=0;i<tmp.length;i++){
			htmlcontext=htmlcontext+"<tr><td>"+ "<a href=\""+tmp[i][1]+"\">"+tmp[i][2]+"</a> </td>"+"<td>"+tmp[i][0]+"</td></tr></br>";
		}
		String htmlbodyends="</table></body>";	
		String htmlend="</html>";
		String finalstring=htmlhead+htmlbodystarts+htmlcontext+htmlbodyends+htmlend;
		return finalstring;
	}
}
