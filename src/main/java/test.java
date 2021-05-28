public class test {
    static public void main(String[] argv)
    {
        Boolean[] key=new Boolean[64];
        for(int i=0;i<64;++i)
        {
            key[i]=Math.random()>0.5;
        }
        Boolean[] s1=new Boolean[64];
        for(int i=0;i<64;++i)
        {
            s1[i]=Math.random()>0.5;
        }
        DES des=new DES();
        long t1=System.nanoTime();
        Boolean[] s2=des.DESDecrypt(s1,1,key);
        long t2=System.nanoTime();
        Boolean[] s3=des.DESDecrypt(s2,-1,key);
        long t3=System.nanoTime();
        printBits(s1,"原文");
        printBits(s2,"密文");
        System.out.println(t2-t1+"ns");
        printBits(s3,"解密");
        System.out.println(t3-t2+"ns");
    }

    static void printBits(Boolean[] bits,String tip)
    {
        System.out.print(tip+": ");
        for(int i=0;i<bits.length;++i)
        {
            int n=(bits[i])?1:0;
            System.out.print(n);
        }
        System.out.println();
    }
}
