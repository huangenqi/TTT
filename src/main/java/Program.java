import sun.security.util.BitArray;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.Scanner;
import java.util.Vector;

public class Program {
    static public void main(String[] argv)
    {
        Boolean[] key=new Boolean[64];
        for(int i=0;i<64;++i)
        {
            key[i]=Math.random()>0.5;
        }
        Decrypt decrypt=new Decrypt();
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入明文：");
        String s=sc.nextLine();
        byte[] b1=null;
        try {
            b1=s.getBytes("UTF-8");
            byte[] b2=decrypt.Decrypting(b1,1,key);
            System.out.println("密文："+new String(b2,"UTF-8"));
            byte[] b3=decrypt.Decrypting(b2,-1,key);
            System.out.println("解密："+new String(b2,"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
