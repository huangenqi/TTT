import java.util.Vector;

public class Decrypt {
    private DES des;
    public Decrypt()
    {
        des=new DES();
    }

    //加密解密函数，Mode（1）加密，Mode（-1）解密
    public byte[] Decrypting(byte[] str,int mode,Boolean[] key)
    {
        if(str==null||!((mode==1)||(mode==-1)))
        {
            return null;
        }
        else if(mode==1||mode==-1)   //加密,解密
        {
            Vector<Boolean> bits=StringBitsChange.StringToBits(str);
            //加密,解密
            Boolean[] bitArray=new Boolean[64];
            for(int i=0;i<bits.size();++i)
            {
                int k=i/64;
                int n=i%64;
                bitArray[n]=bits.get(i);
                if(n==63)
                {
                    bitArray=des.DESDecrypt(bitArray,mode,key);
                    for(int j=0;j<64;++j)
                    {
                        bits.set(64*k+j,bitArray[j]);
                    }
                }
            }
            return StringBitsChange.BitsToString(bits,mode);
        }
        else
        {
            return null;
        }
    }


}

//字符串与bit转化类
class StringBitsChange
{
    static public Vector<Boolean> StringToBits(byte[] s)
    {
        Vector<Boolean> bits=new Vector<Boolean>();
        try
        {
            byte[] bytes=s;
            //拆分填充字符串
            int addnum=7-bytes.length%7;
            int len=bytes.length+addnum+bytes.length/7+1;
            if(bytes.length%7==0)
            {
                addnum=0;
                len=bytes.length+addnum+bytes.length/7;
            }

            byte[] newBytes=new byte[len];
            for(int i=0;i<len;++i)
            {
                int k=i/8;
                if(i%8==0&&i!=len-8)//除最后一组外的每组第一个byte
                {
                     newBytes[i]=7;
                 }
                else if(i==len-8)//最后一组第一个byte
                {
                    newBytes[i]=(byte) (7-addnum);
                }
                else if(i<len-addnum)//真byte
                {
                    newBytes[i]=bytes[i-k-1];
                }
                else//假byte
                {
                    newBytes[i]=0;
                }
            }
            bytes=newBytes;

            for(int i=0;i<bytes.length;++i)
            {
                Boolean[] temp=ByteToBits(bytes[i]);
                int start=i*8;
                for(int j=0;j<8;++j)
                {
                    bits.add(temp[j]);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return bits;
    }

    static private Boolean[] ByteToBits(byte b)
    {
        int val=b;
        if(val<0)
        {
            val*=(-1);
        }
        Vector<Boolean> vector=new Vector<Boolean>();
        while(val>0)
        {
            int l=val%2;
            vector.add(l==1);
            val/=2;
        }
        Boolean[] bits=new Boolean[8];
        bits[0]=(b<0);
        for(int i=1;i<=7-vector.size();++i)
        {
            bits[i]=false;
        }
        for(int i=8-vector.size();i<8;++i)
        {
            bits[i]=vector.get(7-i);
        }
        return bits;
    }


    static public byte[] BitsToString(Vector<Boolean> bits,int mode)
    {
        if(bits.size()%64!=0)
        {
            return null;
        }
        byte[] bytes=new byte[bits.size()/8];
        Boolean[] temp=new Boolean[8];
        for(int i=0;i<bits.size();++i)
        {
            int j=i%8;
            int k=i/8;
            temp[j]=bits.get(i);
            if(j==7)
            {
                bytes[k]=BitsToByte(temp);
            }
        }
        if(mode==-1)
        {
            //还原原bytes
            int teams=bytes.length/8;
            int trueByteNum=bytes.length-(7-bytes[bytes.length-8]);
            byte[] endBytes=new byte[trueByteNum-teams];
            for(int i=0;i<endBytes.length;++i)
            {
                int k=i/7;
                endBytes[i]=bytes[i+k+1];
            }
            bytes=endBytes;
        }
        return bytes;
    }

    static private byte BitsToByte(Boolean[] bits)
    {
        int val=0;
        for(int i=1;i<=7;++i)
        {
            if(bits[i])
            {
                val+=Math.pow(2,7-i);
            }
        }
        if(val==0&&bits[0])  //特殊情况-128
        {
            val=-128;
        }
        val*=(bits[0])?(-1):1;
        return (byte)val;
    }
}
