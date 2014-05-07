package GreatDouBaba;

public class Bookmarkhtml {
		
	public static String Generatehtml(GDB_DTB_High_Interface gdb){
		String[][] tmp=gdb.getBookmark();
		String htmlcontext="";
		String htmlhead="<!DOCTYPE html> <html> <head> <title>Bookmarks</title>"
				+ "<style>table.dataintable {   border: 1px solid #888888;border-collapse: collapse;   font-family: Arial,Helvetica,sans-serif;   margin-top: 10px;   width: 100%;}table.dataintable th {   background-color: #CCCCCC;   border: 1px solid #888888;   padding: 5px 15px 5px 5px;   text-align: left;   vertical-align: baseline;}table.dataintable td {   background-color: #EFEFEF;   border: 1px solid #AAAAAA;   padding: 5px 15px 5px 5px;   vertical-align: text-top;}</style>"
				+ " </head>";
		String htmlbodystarts="<body><h2>Bookmarks</h2><table class=\"dataintable\">";
		htmlcontext = "<tr>	<th>Title</th></tr>";
		for(int i=0;i<tmp.length;i++){
			htmlcontext=htmlcontext+"<tr><td>"+ "<a href=\""+tmp[i][0]+"\">"+tmp[i][1]+"</a> </td></tr>";
		}
		String htmlbodyends="</table></body>";	
		String htmlend="</html>";
		String finalstring=htmlhead+htmlbodystarts+htmlcontext+htmlbodyends+htmlend;
		return finalstring;
	}
}
