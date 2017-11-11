import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
 
interface lab
{
	 digraph createDirectedGraph(String filename);//生成有向图 done

	 String queryBridgeWords(digraph G, String word1, String word2);// 查询桥接词done
	 String generateNewText(digraph G, String inputText);//根据bridge word生成新文本 done
	 String calcShortestPath(digraph G, String word1, String word2);//计算两个单词之间的最短路径 done
	 String randomWalk(digraph G);//随机游走done
	}
class digraph implements lab
{
	public String[] refrence;//存放单词，以下标为单词节点编号
	public int[][] list;//有向图的二维矩阵
	public static int[] ifvisited;//访问标记数组
	public static int times;//随机访问时的重复次数，作为全局变量
	public static int sign;//当前随机到位置
	public static int least;//最短路径的权值合
 	public digraph(int i)//定义一个字符串数组

	{
		this.refrence=new String[i];
		this.list=new int[i][i];
		
	}
	public static void refreshifvisited(int i)//刷新访问数组，在使用之前调用一次
	{
		ifvisited=new int[i];
	}
	public static void refreshtimes()//重置重复次数，需要时调用
	{times=0;}
	public static void refreshleast()//重置最短路径合
	{least=0;}
    public int GetLength()//得到数组的有效长度
	{
		for(int i=0;i<this.refrence.length;i++)
		{
			if(this.refrence[i]==null) return i;
		}
		return 0;
	}
	public boolean IfHaveChild(String str)//是否存在相邻边，存在返回true
	{
		for (int i=0;i<this.GetLength();i++)
		{
			if(this.list[this.GetNum(str)][i]>0) return true;
		}
		return false;
	}
	public int GetNum(String str)//通过字符串查找对应的编号值
	{
		for(int j=0;j<this.GetLength();j++)
		{   if(this.refrence[j]==null) return -1;
			if(this.refrence[j].equals(str)==true) return j;
		}
		return -1;
	}
	public void Add(int i,int j)//将对应的两个编号的权值+1，表示i->j有一条有向边
	{
		this.list[i][j]=this.list[i][j]+1;
	}
	public digraph createDirectedGraph(String filename)//创建有向图
	{   
		File file=new File(filename);//文件打开
        if (!file.exists())//检验是否存在
        {System.err.println("Not Fing the File!\n");}
       try//存在后进行文件操作
       {  
    	   FileReader fr=new FileReader(filename);//文件读取
    	   char[] buf=new char[102400];//存放文件中所有字符的字符串
    	   char[] word=new char[30];//存放每个单词的字符串    	   
    	   int i=0;int count=0;int index=0;
    	   int num=0;
    	   char[] preword=null;//前词
    	   try{//读取文件内容
    		   num=fr.read(buf);
    		   fr.close();
    		   while(count<=num)//从读取出的文件字符中筛选有效单词
    		   {  
    			  if(i==0&&(buf[count]<'a'||buf[count]>'z')&&(buf[count]<'A'||buf[count]>'Z'))
    			  {count++;}
    			  if((buf[count]>='a'&&buf[count]<='z')||(buf[count]>='A'&&buf[count]<='Z'))//条件
    			  {   
    				  word[i]=buf[count];  			      				  
				  i++;
				  count++;
    			  }
    			  else
    			  {   
    				  if(this.GetNum(new String(word).toLowerCase().trim())==-1)//如果没有储存这个单词，则存入
    				  { 
    				  this.refrence[index]=(new String(word).toLowerCase().trim());
    				  index++;
    				  }//有效单词存入参考数组中,同时大写转换
    				  
    			      if(preword!=null)//如果前一单词不为空，则前一单词到此单词的权值+1
    			      {  
    			         
    			    	  this.Add(this.GetNum(new String(preword).toLowerCase().trim()),this.GetNum(new String(word).toLowerCase().trim()) );
    			      }
    			      
    				  i=0;
    				  count++;
    				  preword=word;//更新前词
    				  word=new char[30];//更新当前词
    			  }
    		   }
    		    		      		      		       		   
    		 }
    	   catch(IOException e)
    	   {e.printStackTrace();}
    	   
    	  
       }
       catch (FileNotFoundException e) {
    	   e.printStackTrace();
    	  }
       return this;  
	}
	/*public void queryBridgeWords(digraph G,String str1,String str2)//寻找桥接词
	{   if(str1.equals(str2)==true) 
	{
		System.out.println("No bridge words from "+str1+" to "+str2+"! ");
		return;
	}
	if(G.GetNum(str1)==-1||G.GetNum(str2)==-1)
	{
		System.out.println("No "+str1+" or "+str2+" in the graph! ");
		return;
	}
	if(G.list[G.GetNum(str1)][G.GetNum(str2)]>0)
	{System.out.println("No bridge words from "+str1+" to "+str2+"! ");
	return;}
	
		Stack<String> stack=new Stack<String>();//定义一个栈，存放遍历过的节点
		Stack<String> stackroute=new Stack<String>();//定义一个栈，存放可能正确的路径，按顺序存放要输出的节点
		Stack<String> stackprint=new Stack<String>();//定义一个输出栈
		int index_1=G.GetNum(str1);
		int i;int judge,j;
		String temp;int temp_index;
		refreshifvisited(this.GetLength());//初始化访问数组
		ifvisited[index_1]=1;
		stack.push(str1);
		while(stack.isEmpty()==false)//遍历有向图
		{   judge=0;j=0;
			temp=stack.pop();//top
			stackroute.add(temp);//temp为当前访问到的节点，作为可能的正确路径之一加入到栈
			temp_index=G.GetNum(temp);
			if(temp.equals(str2))//判断是否为目标结点，是的话则找到目标路径，算法结束
			  	{    System.out.print("The bridge words from "+str1+" to "+str2+" are: ");
			  	stackroute.pop();
			  	String top=stackroute.peek();
				while(stackroute.isEmpty()==false)//打印路径,函数结束
			  			{
			  				stackprint.push(stackroute.pop());
			  			}
				stackprint.pop();
				 System.out.print(stackprint.pop());
			  		  while(stackprint.isEmpty()==false)
			  		    {    String sp= stackprint.pop();
			  		    if(sp.equals(top))
			  		    { System.out.print(" and "+sp);}
			  		    else
			  			  {System.out.print(" , "+sp);}
				
			  		    }
				return;
			    }
			for(i=0;i<G.GetLength();i++)//寻找存在的下一条边
			{
				if(G.list[temp_index][i]>0&&ifvisited[i]==0)//如果相邻并访问
				{
					stack.push(G.refrence[i]);//进栈
					ifvisited[i]=1;//标记已访问
					judge=1;//标记位，表示找到可行的下一节点
				}
			}
			if(judge==0)//未找到可行节点的情况，说明此路径不可行
			{   stackroute.pop();
			if(stack.isEmpty()==true)//栈已经为空，说明已经没有可行解
			{
				System.out.println("No bridge words from "+str1+" to "+str2+"! ");
				return;//没有桥接词
			}
				while(stackroute.isEmpty()==false&&j==0)//从队列尾端开始删除没有子节点的节点，说明必不是有效路径
				{
					for(i=0;i<G.GetLength();i++)//判断当前stackroute栈顶元素是否有可行路径
					{
						if(G.list[G.GetNum(stack.peek())][i]>0&&ifvisited[i]==0)
						{
							j=1;//如果还有可行路径，则stack栈中当前栈顶元素就为下一路径的可能正确解，继续上一步循环即可
						}
					}
					if(j==0)//当前stackroute栈顶元素没有可行解，删除当前栈顶元素
					{
						stackroute.pop();
					}
				}
			}	
		}
		System.out.println("No bridge words from "+str1+" to "+str2+"! ");
		return;//没有桥接词
		
		
	}*/
	public String queryBridgeWords(digraph G, String word1, String word2)// 查询桥接词
	{
		int index1=G.GetNum(word1);int index2=G.GetNum(word2);
		if(index1==-1||index2==-1) return "No in";//至少有一个词不存在
		else if(G.list[index1][index2]>0) return "No have";//相邻两词无桥接词
		else
		{			
			G.ifvisited[index1]=1;G.ifvisited[index2]=1;
			for(int i=0;i<G.GetLength();i++)//判断是否两次间有桥接词
			{
				if(G.list[index1][i]>0)
				{					
						if(G.list[i][index2]>0&&G.ifvisited[i]==0)//有且未被访问过
						{
							G.ifvisited[i]=1;
							return G.refrence[i];//输出有效词
						}
					
				}
			}
		for(int j=0;j<G.GetLength();j++)//判断是否出现过有效词
		{
			if(j==index1||j==index2);
			else if(G.ifvisited[j]==1) return "No more";//出现过
			
		}
		return "No have";//未出现过
		}
	}
	public String generateNewText(digraph G, String inputText)//根据bridge word生成新文本 
	{
		String sentence[]=inputText.split("\\s+");//分割输入的字符串	
		String strout=new String();//最终输出的字符串
		String strbridge;//存放当前桥接词
		String strconnect;//得出的有效桥接词
		for(int i=0;i<sentence.length-1;i++)
		{   G.refreshifvisited(G.GetLength());
			strout=strout+sentence[i]+" ";
			strbridge=G.queryBridgeWords(G, sentence[i], sentence[i+1]);
			if(strbridge.equals("No in")||strbridge.equals("No have")) strconnect=" ";//如果两次间无桥接词，不需要插入新单词
			else //如果两次间有桥接词，随机插入一个桥接词
			{
				String[] bridgewords=new String[G.GetLength()];
                int index=0;
                Random random=new Random();
				while(!strbridge.equals("No more"))//未找全所有桥接词时
				{
					bridgewords[index]=strbridge;
					index++;
					strbridge=G.queryBridgeWords(G, sentence[i], sentence[i+1]);
				}
				strconnect=bridgewords[random.nextInt(index)]+" ";
				 
				
				
			}
			strout=strout+strconnect;
			
			
		}
		strout=strout+sentence[sentence.length-1];
		return strout;
		
	}
    public String randomWalk(digraph G)//随机游走
	{   int temp;
 		Random index=new Random();//生成一个范围内的随机数
		temp=index.nextInt(G.GetLength());
		if(G.IfHaveChild(G.refrence[G.sign])==false) return "end";//当前节点已无子结点，又走游走结束
		if(G.list[G.sign][temp]>0&&G.ifvisited[temp]==0)//当前字符存在邻接字符并且未被访问
		{
			G.ifvisited[temp]=1;//标记访问
			this.sign=temp;
			this.refreshtimes();
			return G.refrence[temp];//当前字符为可输出字符，返回当前字符
		}
		else if(G.list[this.sign][temp]>0&&G.ifvisited[temp]==1&&(times==0||times==1))//当前已经访问过但未重复，说明只出现重复单词，未必是出现重复句子，继续游走
		{
			G.ifvisited[temp]=1;//标记访问
			this.sign=temp;
			times++;
			return G.refrence[temp];//当前字符为可输出字符，返回当前字符
		}
		else if(G.list[this.sign][temp]>0&&G.ifvisited[temp]==1&&times==2)//当前已经访问过并且已重复，说明出现重复句子，游走结束
		{
			return "end";
		}
		else//当前生成随机数不是下一单词的下标但当前走到的节点还有子节点，重新执行函数，直到生成有效下标
		{
			return "continue";
		}
	}
    public void showDirectedGraph(digraph G)//展示有向图
    {
    	/*GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        int index=G.GetLength();
        for(int i=0;i<index;i++)
        {
        	for(int j=0;j<index;j++)
        	{
        		if(G.list[i][j]>0)
        		{
        			System.out.println(G.list[i][j]);
        		    gv.addln(G.refrence[i]+"->"+G.refrence[j]+"[label=\""+G.list[i][j]+"\"];");
        		}
        	}
        }
        
        gv.addln(gv.end_graph());
        System.out.println(gv.getDotSource());       
        String type = "gif";
//        String type = "dot";
//        String type = "fig";    // open with xfig
//        String type = "pdf";
//        String type = "ps";
//        String type = "svg";    // open with inkscape
//        String type = "png";
//        String type = "plain";
//        File out = new File("/tmp/out." + type);   // Linux
       // File out = new File("c:/eclipse.ws/graphviz-java-api/out ." + type);    // Windows
        File out = new File("D:\\eclipse\\Lab1"+type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );*/
    }
    public String calcShortestPath(digraph G, String word1, String word2)//计算两个单词之间的最短路径 
    {       int num=G.GetLength();
    int index1=G.GetNum(word1);int index2=G.GetNum(word2);
    if(index1==-1||index2==-1) return "No way";
    	   int[][] A=new int[num][num];
    	   int[][] path=new int [num][num];
    	   int i,j,k,n=num;
    	   for(i=0;i<n;i++)
    	   {
    		   for(j=0;j<n;j++)
    		   {   if(i==j) A[i][j]=0;
    		   else if(G.list[i][j]==0) A[i][j]=999;//无路径时权值定义为999 
    		   else A[i][j]=G.list[i][j];
    			   path[i][j]=-1;
    		   }
    	   }
    	  for(k=0;k<n;k++)
    	  {
    		  for(i=0;i<n;i++)
    		  {
    			  for(j=0;j<n;j++)
    			  {
    				  if(A[i][j]>(A[i][k]+A[k][j]))
    				  {
    					  A[i][j]=A[i][k]+A[k][j];
    					  path[i][j]=k;
    				  }
    			  }
    		  }
    	  }
    	  G.least=A[index1][index2];//更新最短路径值
    	  String str;
    	  str=G.ReturnBetweenWords(path, index1, index2);
    	  return str;
    	  
    	  
    }
    public String ReturnBetweenWords(int[][] A,int index1,int index2)//递归返回字符串，辅助函数
    {
    	if(A[index1][index2]==-1) return this.refrence[index1]+"->";
    	else return this.ReturnBetweenWords(A, index1, A[index1][index2])+this.ReturnBetweenWords(A,A[index1][index2], index2);
    	}
}
public class Lab1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 Scanner input=new Scanner(System.in);
        String filename=new String("D:\\eclipse\\Lab1\\test.txt");
        String str1,str2,str3;
        digraph nl=new digraph(30);//定义新的有向图类,同时初始化二维矩阵
        nl=nl.createDirectedGraph(filename);//创建有向图
        nl.showDirectedGraph(nl);
        /*查询桥接词*/
        System.out.println("Please input the words you want to search: ");
        str1=input.next();
        input.next();
        str2=input.next();
        input.next();
        nl.refreshifvisited(nl.GetLength());   
        str3=nl.queryBridgeWords(nl, str1, str2);       
        if(str3.equals("No in")) System.out.println("No "+str1+" or "+str2+" in the graph !");
        else if(str3.equals("No have")) System.out.println("No bridge words from "+str1+" to "+str2+" !");
        else 
        {
        	System.out.print("The bridge words from "+str1+" to "+str2+" are :"+str3);
        	 str3=nl.queryBridgeWords(nl, str1, str2);    
        	while(!str3.equals("No more")) 
        	{        		
        		String str4=new String(nl.queryBridgeWords(nl, str1, str2));        		
        		if(!str3.equals("No more")&&!str4.equals("No more")) 
        			{System.out.print(","+str3);str3=str4;}
        		else if(!str3.equals("No more")&&str4.equals("No more"))
        			{System.out.print("and "+str3);str3=str4;}
        	}
        }
        System.out.println("\n**********************************************");
        /*随机游走*/
        System.out.println("Now begin the random waklind.");
        Random random=new Random();
        String result=new String();
        String strin=new String("Yes");//输入字符，判断是否继续遍历
        nl.sign=random.nextInt(nl.GetLength());//生成一个初始游走位置
        nl.refreshifvisited(nl.GetLength());
        nl.refreshtimes();
        nl.ifvisited[nl.sign]=1;//标记为已访问
        System.out.print(nl.refrence[nl.sign]+" ");
        while(!result.equals("end"))//未出现结束标志时
        {    result=nl.randomWalk(nl);
        	if(!result.equals("continue")&&!result.equals("end")) 
        	{
        		System.out.print(result+" ");//当得到有效输出时，输出字符}
        		} }
       /* while(!result.equals("end")&&!strin.equals("No"))//未出现结束标志时
        {    result=nl.randomWalk(nl);
             if(!strin.equals("Yes")&&!strin.equals("No"))
             {
            	 System.out.println("Wrong inputs!");
            	 break;
             }        
        	if(!result.equals("continue")&&!result.equals("end")&&strin.equals("Yes")) 
        	{
        		System.out.print(result+" \n");//当得到有效输出时，输出字符}
        		System.out.println("Would you like to continue ?(inout Yes to continue, No to stop)");
        		strin=input.next();}        		             
        }*/
        System.out.println("\nComplete random walk!");
        System.out.println("**********************************************");
        /*根据bridgewords生成新文本*/
        System.out.println("Please input the new text: ");
        String inputText=new String(input.nextLine());
        System.out.println(nl.generateNewText(nl, inputText));
        System.out.println("**********************************************");
        /*最短路径搜索*/
        System.out.println("Please input the words you want to search for the least cost: ");
        String str5,str6;
        str5=input.next();
        str6=input.next();
        nl.refreshleast();
        String strcheapestroute=new String();
        strcheapestroute=nl.calcShortestPath(nl, str5, str6);
        if(strcheapestroute.equals("No way"))//如果路径不存在
        {
        	System.out.println("No route bewteen the words!");
        }
        else{
        System.out.println("The least cost way is :"+strcheapestroute+str6);
        System.out.println("The least cost is: " +nl.least);}
        System.out.println("**********************************************");
        input.close();
        System.out.println("\nHello World!");
     

	}

}
