import sun.security.util.BitArray;

import java.util.Vector;

public class DES {
    private Tables tables;
    private Boolean[] key;
    private Boolean[][] keys;
    private Boolean[] Lbits;
    private Boolean[] Rbits;

    public DES()
    {
        tables=new Tables();
        Lbits=new Boolean[32];
        Rbits=new Boolean[32];
    }

    //DES
    public Boolean[] DESDecrypt(Boolean[] bits,int mode,Boolean[] key)
    {
        //获取子钥
        this.key=key;
        this.keys=GetKeys(key);


        int keyStart=(mode==1)?0:15;

        //初始IP置换
        bits=Replace(bits,tables.getFirstIPTable());
        //切分bits
        for(int i=0;i<32;++i)
        {
            Lbits[i]=bits[i];
        }
        for(int i=0;i<32;++i)
        {
            Rbits[i]=bits[i+32];
        }
        //16轮变换
        for(int i=0;i<16;++i)
        {
            int temp=mode*i+keyStart;
            Feistel(keys[temp]);
        }
        //组合子串
        for(int i=0;i<32;++i)
        {
            bits[i]=Rbits[i];
        }
        for(int i=0;i<32;++i)
        {
            bits[i+32]=Lbits[i];
        }
        //逆初始IP置换
        bits=Replace(bits,tables.getLastReplaceTable());
        return bits;
    }

    //置换
    private Boolean[] Replace(Boolean[] bits,int[] table)
    {
        Boolean[] res=new Boolean[table.length];
        for(int i=0;i<res.length;++i)
        {
            res[i]=bits[table[i]-1];
        }
        return res;
    }

    //一轮变换
    private void Feistel(Boolean[] key)
    {
        Boolean[] tempRbits=Rbits.clone();
        Boolean[] fRes=FFuction(key);
        Rbits=XOR(Lbits,fRes);
        Lbits=tempRbits;
    }

    //轮函数
    private Boolean[] FFuction(Boolean[] key)
    {
        Boolean[] temp=XOR(key,Replace(Rbits,tables.getFTable()));
        //分组
        Boolean[][] teams=new Boolean[8][6];
        for (int i=0;i<8;++i)
        {
            for(int j=0;j<6;++j)
            {
                teams[i][j]=temp[i*6+j];
            }
        }
        //输入S盒子
        Boolean[][] res=new Boolean[8][4];
        for(int i=0;i<8;++i)
        {
            res[i]=SBox(teams[i],i);
        }
        //组合
        for(int i=0;i<8;++i)
        {
            for(int j=0;j<4;++j)
            {
                Rbits[i*4+j]=res[i][j];
            }
        }
        //置换
        return Replace(Rbits,tables.getSBoxPTable());
    }

    //获取子钥
    private Boolean[][] GetKeys(Boolean[] key)
    {
        Boolean[][] res=new Boolean[16][48];
        //第一次置换
        Boolean[] realKey=Replace(key,tables.getKeyTable1());
        //切割realKey
        Boolean[] Lkey=new Boolean[28];
        for(int i=0;i<28;++i)
        {
            Lkey[i]=realKey[i];
        }
        Boolean[] Rkey=new Boolean[28];
        for(int i=0;i<28;++i)
        {
            Rkey[i]=realKey[i+28];
        }
        //循环获取子钥
        Boolean[] tempKey=new Boolean[56];
        for(int i=0;i<16;++i)
        {
            //移位
            Lkey=KeyShift(Lkey,tables.getKeyShiftTable()[i]);
            Rkey=KeyShift(Rkey,tables.getKeyShiftTable()[i]);
            //合并
            for(int j=0;j<28;++j)
            {
                tempKey[j]=Lkey[j];
            }
            for(int j=28;j<56;++j)
            {
                tempKey[j]=Rkey[j-28];
            }
            //置换
            res[i]=Replace(tempKey,tables.getKeyTable2());
        }
        return res;
    }

    //移位
    private Boolean[] KeyShift(Boolean[] key,int n)
    {
        if(key==null)
        {
            return null;
        }
        Boolean[] res=new Boolean[28];
        for(int i=0;i<28;++i)
        {
            res[i]=key[(i+n)%28];
        }
        return res;
    }

    //异或
    private Boolean[] XOR(Boolean[] a,Boolean[] b)
    {
        if(a.length!=b.length)
        {
            return null;
        }
        int l=a.length;
        Boolean[] res=new Boolean[l];
        for(int i=0;i<l;++i)
        {
            res[i]=(a[i].equals(b[i]))?false:true;
        }
        return res;
    }

    //S盒子
    Boolean[] SBox(Boolean[] team,int n)
    {
        int[] table=tables.getSBoxTable(n);
        int a=((team[0])?2:0)+((team[5])?1:0);
        int b=((team[1])?8:0)+((team[2])?4:0)+((team[3])?2:0)+((team[4])?1:0);
        int r=table[a*16+b];
        return NumberToBits(r);
    }

    //数字转bit
    Boolean[] NumberToBits(int num)
    {
        Boolean[] res={false,false,false,false};
        int n=num;
        for(int i=3;i>=0;--i)
        {
            res[i]=n%2==1;
            n/=2;
        }
        return res;
    }

    void printBits(Boolean[] bits,String tip)
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
