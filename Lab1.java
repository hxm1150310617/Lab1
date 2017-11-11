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
	 digraph createDirectedGraph(String filename);//��������ͼ done

	 String queryBridgeWords(digraph G, String word1, String word2);// ��ѯ�ŽӴ�done
	 String generateNewText(digraph G, String inputText);//����bridge word�������ı� done
	 String calcShortestPath(digraph G, String word1, String word2);//������������֮������·�� done
	 String randomWalk(digraph G);//�������done
	}
class digraph implements lab
{
	public String[] refrence;//��ŵ��ʣ����±�Ϊ���ʽڵ���
	public int[][] list;//����ͼ�Ķ�ά����
	public static int[] ifvisited;//���ʱ������
	public static int times;//�������ʱ���ظ���������Ϊȫ�ֱ���
	public static int sign;//��ǰ�����λ��
	public static int least;//���·����Ȩֵ��
 	public digraph(int i)//����һ���ַ�������

	{
		this.refrence=new String[i];
		this.list=new int[i][i];
		
	}
	public static void refreshifvisited(int i)//ˢ�·������飬��ʹ��֮ǰ����һ��
	{
		ifvisited=new int[i];
	}
	public static void refreshtimes()//�����ظ���������Ҫʱ����
	{times=0;}
	public static void refreshleast()//�������·����
	{least=0;}
    public int GetLength()//�õ��������Ч����
	{
		for(int i=0;i<this.refrence.length;i++)
		{
			if(this.refrence[i]==null) return i;
		}
		return 0;
	}
	public boolean IfHaveChild(String str)//�Ƿ�������ڱߣ����ڷ���true
	{
		for (int i=0;i<this.GetLength();i++)
		{
			if(this.list[this.GetNum(str)][i]>0) return true;
		}
		return false;
	}
	public int GetNum(String str)//ͨ���ַ������Ҷ�Ӧ�ı��ֵ
	{
		for(int j=0;j<this.GetLength();j++)
		{   if(this.refrence[j]==null) return -1;
			if(this.refrence[j].equals(str)==true) return j;
		}
		return -1;
	}
	public void Add(int i,int j)//����Ӧ��������ŵ�Ȩֵ+1����ʾi->j��һ�������
	{
		this.list[i][j]=this.list[i][j]+1;
	}
	public digraph createDirectedGraph(String filename)//��������ͼ
	{   
		File file=new File(filename);//�ļ���
        if (!file.exists())//�����Ƿ����
        {System.err.println("Not Fing the File!\n");}
       try//���ں�����ļ�����
       {  
    	   FileReader fr=new FileReader(filename);//�ļ���ȡ
    	   char[] buf=new char[102400];//����ļ��������ַ����ַ���
    	   char[] word=new char[30];//���ÿ�����ʵ��ַ���    	   
    	   int i=0;int count=0;int index=0;
    	   int num=0;
    	   char[] preword=null;//ǰ��
    	   try{//��ȡ�ļ�����
    		   num=fr.read(buf);
    		   fr.close();
    		   while(count<=num)//�Ӷ�ȡ�����ļ��ַ���ɸѡ��Ч����
    		   {  
    			  if(i==0&&(buf[count]<'a'||buf[count]>'z')&&(buf[count]<'A'||buf[count]>'Z'))
    			  {count++;}
    			  if((buf[count]>='a'&&buf[count]<='z')||(buf[count]>='A'&&buf[count]<='Z'))//����
    			  {   
    				  word[i]=buf[count];  			      				  
				  i++;
				  count++;
    			  }
    			  else
    			  {   
    				  if(this.GetNum(new String(word).toLowerCase().trim())==-1)//���û�д���������ʣ������
    				  { 
    				  this.refrence[index]=(new String(word).toLowerCase().trim());
    				  index++;
    				  }//��Ч���ʴ���ο�������,ͬʱ��дת��
    				  
    			      if(preword!=null)//���ǰһ���ʲ�Ϊ�գ���ǰһ���ʵ��˵��ʵ�Ȩֵ+1
    			      {  
    			         
    			    	  this.Add(this.GetNum(new String(preword).toLowerCase().trim()),this.GetNum(new String(word).toLowerCase().trim()) );
    			      }
    			      
    				  i=0;
    				  count++;
    				  preword=word;//����ǰ��
    				  word=new char[30];//���µ�ǰ��
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
	/*public void queryBridgeWords(digraph G,String str1,String str2)//Ѱ���ŽӴ�
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
	
		Stack<String> stack=new Stack<String>();//����һ��ջ����ű������Ľڵ�
		Stack<String> stackroute=new Stack<String>();//����һ��ջ����ſ�����ȷ��·������˳����Ҫ����Ľڵ�
		Stack<String> stackprint=new Stack<String>();//����һ�����ջ
		int index_1=G.GetNum(str1);
		int i;int judge,j;
		String temp;int temp_index;
		refreshifvisited(this.GetLength());//��ʼ����������
		ifvisited[index_1]=1;
		stack.push(str1);
		while(stack.isEmpty()==false)//��������ͼ
		{   judge=0;j=0;
			temp=stack.pop();//top
			stackroute.add(temp);//tempΪ��ǰ���ʵ��Ľڵ㣬��Ϊ���ܵ���ȷ·��֮һ���뵽ջ
			temp_index=G.GetNum(temp);
			if(temp.equals(str2))//�ж��Ƿ�ΪĿ���㣬�ǵĻ����ҵ�Ŀ��·�����㷨����
			  	{    System.out.print("The bridge words from "+str1+" to "+str2+" are: ");
			  	stackroute.pop();
			  	String top=stackroute.peek();
				while(stackroute.isEmpty()==false)//��ӡ·��,��������
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
			for(i=0;i<G.GetLength();i++)//Ѱ�Ҵ��ڵ���һ����
			{
				if(G.list[temp_index][i]>0&&ifvisited[i]==0)//������ڲ�����
				{
					stack.push(G.refrence[i]);//��ջ
					ifvisited[i]=1;//����ѷ���
					judge=1;//���λ����ʾ�ҵ����е���һ�ڵ�
				}
			}
			if(judge==0)//δ�ҵ����нڵ�������˵����·��������
			{   stackroute.pop();
			if(stack.isEmpty()==true)//ջ�Ѿ�Ϊ�գ�˵���Ѿ�û�п��н�
			{
				System.out.println("No bridge words from "+str1+" to "+str2+"! ");
				return;//û���ŽӴ�
			}
				while(stackroute.isEmpty()==false&&j==0)//�Ӷ���β�˿�ʼɾ��û���ӽڵ�Ľڵ㣬˵���ز�����Ч·��
				{
					for(i=0;i<G.GetLength();i++)//�жϵ�ǰstackrouteջ��Ԫ���Ƿ��п���·��
					{
						if(G.list[G.GetNum(stack.peek())][i]>0&&ifvisited[i]==0)
						{
							j=1;//������п���·������stackջ�е�ǰջ��Ԫ�ؾ�Ϊ��һ·���Ŀ�����ȷ�⣬������һ��ѭ������
						}
					}
					if(j==0)//��ǰstackrouteջ��Ԫ��û�п��н⣬ɾ����ǰջ��Ԫ��
					{
						stackroute.pop();
					}
				}
			}	
		}
		System.out.println("No bridge words from "+str1+" to "+str2+"! ");
		return;//û���ŽӴ�
		
		
	}*/
	public String queryBridgeWords(digraph G, String word1, String word2)// ��ѯ�ŽӴ�
	{
		int index1=G.GetNum(word1);int index2=G.GetNum(word2);
		if(index1==-1||index2==-1) return "No in";//������һ���ʲ�����
		else if(G.list[index1][index2]>0) return "No have";//�����������ŽӴ�
		else
		{			
			G.ifvisited[index1]=1;G.ifvisited[index2]=1;
			for(int i=0;i<G.GetLength();i++)//�ж��Ƿ����μ����ŽӴ�
			{
				if(G.list[index1][i]>0)
				{					
						if(G.list[i][index2]>0&&G.ifvisited[i]==0)//����δ�����ʹ�
						{
							G.ifvisited[i]=1;
							return G.refrence[i];//�����Ч��
						}
					
				}
			}
		for(int j=0;j<G.GetLength();j++)//�ж��Ƿ���ֹ���Ч��
		{
			if(j==index1||j==index2);
			else if(G.ifvisited[j]==1) return "No more";//���ֹ�
			
		}
		return "No have";//δ���ֹ�
		}
	}
	public String generateNewText(digraph G, String inputText)//����bridge word�������ı� 
	{
		String sentence[]=inputText.split("\\s+");//�ָ�������ַ���	
		String strout=new String();//����������ַ���
		String strbridge;//��ŵ�ǰ�ŽӴ�
		String strconnect;//�ó�����Ч�ŽӴ�
		for(int i=0;i<sentence.length-1;i++)
		{   G.refreshifvisited(G.GetLength());
			strout=strout+sentence[i]+" ";
			strbridge=G.queryBridgeWords(G, sentence[i], sentence[i+1]);
			if(strbridge.equals("No in")||strbridge.equals("No have")) strconnect=" ";//������μ����ŽӴʣ�����Ҫ�����µ���
			else //������μ����ŽӴʣ��������һ���ŽӴ�
			{
				String[] bridgewords=new String[G.GetLength()];
                int index=0;
                Random random=new Random();
				while(!strbridge.equals("No more"))//δ��ȫ�����ŽӴ�ʱ
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
    public String randomWalk(digraph G)//�������
	{   int temp;
 		Random index=new Random();//����һ����Χ�ڵ������
		temp=index.nextInt(G.GetLength());
		if(G.IfHaveChild(G.refrence[G.sign])==false) return "end";//��ǰ�ڵ������ӽ�㣬�������߽���
		if(G.list[G.sign][temp]>0&&G.ifvisited[temp]==0)//��ǰ�ַ������ڽ��ַ�����δ������
		{
			G.ifvisited[temp]=1;//��Ƿ���
			this.sign=temp;
			this.refreshtimes();
			return G.refrence[temp];//��ǰ�ַ�Ϊ������ַ������ص�ǰ�ַ�
		}
		else if(G.list[this.sign][temp]>0&&G.ifvisited[temp]==1&&(times==0||times==1))//��ǰ�Ѿ����ʹ���δ�ظ���˵��ֻ�����ظ����ʣ�δ���ǳ����ظ����ӣ���������
		{
			G.ifvisited[temp]=1;//��Ƿ���
			this.sign=temp;
			times++;
			return G.refrence[temp];//��ǰ�ַ�Ϊ������ַ������ص�ǰ�ַ�
		}
		else if(G.list[this.sign][temp]>0&&G.ifvisited[temp]==1&&times==2)//��ǰ�Ѿ����ʹ��������ظ���˵�������ظ����ӣ����߽���
		{
			return "end";
		}
		else//��ǰ���������������һ���ʵ��±굫��ǰ�ߵ��Ľڵ㻹���ӽڵ㣬����ִ�к�����ֱ��������Ч�±�
		{
			return "continue";
		}
	}
    public void showDirectedGraph(digraph G)//չʾ����ͼ
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
    public String calcShortestPath(digraph G, String word1, String word2)//������������֮������·�� 
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
    		   else if(G.list[i][j]==0) A[i][j]=999;//��·��ʱȨֵ����Ϊ999 
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
    	  G.least=A[index1][index2];//�������·��ֵ
    	  String str;
    	  str=G.ReturnBetweenWords(path, index1, index2);
    	  return str;
    	  
    	  
    }
    public String ReturnBetweenWords(int[][] A,int index1,int index2)//�ݹ鷵���ַ�������������
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
        digraph nl=new digraph(30);//�����µ�����ͼ��,ͬʱ��ʼ����ά����
        nl=nl.createDirectedGraph(filename);//��������ͼ
        nl.showDirectedGraph(nl);
        /*��ѯ�ŽӴ�*/
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
        /*�������*/
        System.out.println("Now begin the random waklind.");
        Random random=new Random();
        String result=new String();
        String strin=new String("Yes");//�����ַ����ж��Ƿ��������
        nl.sign=random.nextInt(nl.GetLength());//����һ����ʼ����λ��
        nl.refreshifvisited(nl.GetLength());
        nl.refreshtimes();
        nl.ifvisited[nl.sign]=1;//���Ϊ�ѷ���
        System.out.print(nl.refrence[nl.sign]+" ");
        while(!result.equals("end"))//δ���ֽ�����־ʱ
        {    result=nl.randomWalk(nl);
        	if(!result.equals("continue")&&!result.equals("end")) 
        	{
        		System.out.print(result+" ");//���õ���Ч���ʱ������ַ�}
        		} }
       /* while(!result.equals("end")&&!strin.equals("No"))//δ���ֽ�����־ʱ
        {    result=nl.randomWalk(nl);
             if(!strin.equals("Yes")&&!strin.equals("No"))
             {
            	 System.out.println("Wrong inputs!");
            	 break;
             }        
        	if(!result.equals("continue")&&!result.equals("end")&&strin.equals("Yes")) 
        	{
        		System.out.print(result+" \n");//���õ���Ч���ʱ������ַ�}
        		System.out.println("Would you like to continue ?(inout Yes to continue, No to stop)");
        		strin=input.next();}        		             
        }*/
        System.out.println("\nComplete random walk!");
        System.out.println("**********************************************");
        /*����bridgewords�������ı�*/
        System.out.println("Please input the new text: ");
        String inputText=new String(input.nextLine());
        System.out.println(nl.generateNewText(nl, inputText));
        System.out.println("**********************************************");
        /*���·������*/
        System.out.println("Please input the words you want to search for the least cost: ");
        String str5,str6;
        str5=input.next();
        str6=input.next();
        nl.refreshleast();
        String strcheapestroute=new String();
        strcheapestroute=nl.calcShortestPath(nl, str5, str6);
        if(strcheapestroute.equals("No way"))//���·��������
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
