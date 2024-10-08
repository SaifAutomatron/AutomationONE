package Utilities.Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import lombok.SneakyThrows;

/**
 * 
 * @author Saif
 *
 */
public class JavaUtilities {
	/**
	 * returns random integer number between 1-1000
	 * @return int Randomnumber
	 */
	public static int getRandomNumber()
	{
		Random rd=new Random();
		int randomnum=rd.nextInt(1000);

		return randomnum;
	}

	/**
	 * 
	 * @param key
	 * @return Returns value according to key from property file
	 * @throws Exception
	 */
	@SneakyThrows
	public static String getConfig(String configkey)
	{

		Properties p=new Properties();
		p.load(new FileInputStream(PathConst.PROPFILEPATH));
		return p.getProperty(configkey);
	}

	/**
	 * 
	 * @param String dataFilePath,HashMap hmap
	 * @param hMap
	 * @return Document content as String after replacing templates with desired values
	 */
	@SneakyThrows
	public static String replaceTemplatewithValues(String dataFilePath,HashMap<String, String> hMap)
	{
		String result="";
		StringBuffer sb;
		BufferedReader br=new BufferedReader(new FileReader(new File(dataFilePath)));
		String s;
		sb=new StringBuffer();

		while((s=br.readLine())!=null) {
			sb=sb.append(s);
			sb=sb.append("\n");

		}
		result=sb.toString();

		String[] kerArr=hMap.keySet().toArray(new String[hMap.size()]);

		for(String k:kerArr)
		{
			if(result.contains(k))
			{
				result=result.replace("{{"+k+"}}", hMap.get(k));
			}
		}
		br.close();

		return result;
	}

	@SneakyThrows
	public static void createCSV(String fileName,String filePath,String ...data)
	{
		StringBuilder stringBuilder=new StringBuilder();

		int size=data.length;

		for(int i=0;i<size;i++)
		{
			stringBuilder.append(data[i]);
		}

		try(FileWriter writer=new FileWriter(filePath+"/"+fileName+".csv"))
		{
           writer.write(stringBuilder.toString());
           System.out.println("CSV file created sucessfully");
		} catch (Exception e) {
            System.err.println("CSV file creation failed !!!");
			e.printStackTrace();
		}
	}
	
	@SneakyThrows
	public static void runWindowsCommand(String command,String logText) {
		
		String path=System.getProperty("user.dir");
		ProcessBuilder builder=new ProcessBuilder("cmd.exe","/c","cd \""+path+"\" && "+command);
		builder.redirectErrorStream(true);
		Process p=builder.start();
		BufferedReader r=new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while(true) {
			line=r.readLine();
			if(line.contains(logText)) {
				Thread.sleep(5000);
				System.out.println(line);
				break;
			}
		}
		
	}

}
