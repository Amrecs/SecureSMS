package com.wcs.sms.tools;

import java.math.BigInteger;
import java.util.Random;

/*
 * Diffie-Hellman安全算法的工具类实现 
 * 算法描述:
 * 假设用户A和B希望交换一个密钥，用户A选择一个作为私有密钥的随机数XA(XA<q)，并计算公开密钥YA=g^XA mod q。
 * A对XA的值保密存放而使YA能被B公开获得。类似地，用户B选择一个私有的随机数XB<q，并计算公开密钥YB=g^XB mod q。
 * B对XB的值保密存放而使YB能被A公开获得.
 * 用户A产生共享秘密密钥的计算方式是K = (YB)^XA mod q.同样，用户B产生共享秘密密钥的计算是K = (YA)^XB mod q. 
 * 
 * g：参数，接口采用固定的数字，方便接口使用
 * 
 * x：私钥，采用用户输入口令方式
 * 
 * q：参数，使用Java接口获取大整数
 * 
 * y:公钥，工具类产生
 * 
 * k:密钥：工具类产生
 */
public class DhKey {
	
	private BigInteger g;
	private BigInteger x;
	private BigInteger q;
	private BigInteger y;
	private BigInteger k;
	
	public DhKey(String x){
		this.q = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF",16);
		this.g = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA237327FFFFFFFFFFFFFFFF",16);
		this.x =new BigInteger(x,16);
	}
	/*
	 * 生成公钥，根据自己的私钥
	 *
	 */
	public BigInteger getY(){
		y = g.modPow(x, q);
		return y;
	}
	/*
	 * 生成对称密钥
	 *  @param y
	 *           对方传来的公钥
	 */
	public BigInteger getK(String y){
		
		BigInteger y2 = new BigInteger(y);
		
		k = y2.modPow(this.x, this.q);
		
		return k;
	}
	
	

}
