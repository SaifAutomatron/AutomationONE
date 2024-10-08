/*
 * package V1;
 * 
 * import java.io.FileInputStream; import java.util.List;
 * 
 * import org.apache.poi.xwpf.usermodel.XWPFDocument; import
 * org.apache.poi.xwpf.usermodel.XWPFParagraph;
 * 
 * public class MSWordDocReadTEst {
 * 
 * public static String readDocFile(String filePath) { String paragraphText="";
 * try { FileInputStream fis=new FileInputStream(filePath); XWPFDocument
 * docx=new XWPFDocument(fis);
 * 
 * List<XWPFParagraph> paragraphList = docx.getParagraphs();
 * 
 * 
 * for(XWPFParagraph para:paragraphList) { System.out.println(para.getText());
 * paragraphText=paragraphText+para;
 * 
 * 
 * 
 * }
 * 
 * docx.close(); } catch (Exception e) { e.printStackTrace(); }
 * 
 * return paragraphText; }
 * 
 * public static void main(String[] args) {
 * 
 * System.out.println(readDocFile("./Data/Resume.docx")); }
 * 
 * }
 */