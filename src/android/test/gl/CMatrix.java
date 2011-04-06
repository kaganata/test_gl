package android.test.gl;

import android.graphics.Matrix;

/**
 * 行列
 */
public class CMatrix {
	//======================================================
	// 定数
	public static	float[] IDENTITY = {
		1.f,	0.f,	0.f,	0.f,
		0.f,	1.f,	0.f,	0.f,
		0.f,	0.f,	1.f,	0.f,
		0.f,	0.f,	0.f,	1.f,
	};
	
	//======================================================
	// 変数
	private float	m_n11 = 0.f;
	private float	m_n12 = 0.f;
	private float	m_n13 = 0.f;
	private float	m_n14 = 0.f;
	private float	m_n21 = 0.f;
	private float	m_n22 = 0.f;
	private float	m_n23 = 0.f;
	private float	m_n24 = 0.f;
	private float	m_n31 = 0.f;
	private float	m_n32 = 0.f;
	private float	m_n33 = 0.f;
	private float	m_n34 = 0.f;
	private float	m_n41 = 0.f;
	private float	m_n42 = 0.f;
	private float	m_n43 = 0.f;
	private float	m_n44 = 0.f;
	
	//======================================================
	// 関数
	//------------------------------------------------------
	// コンストラクタ
	public CMatrix() {
	}
	
	public CMatrix(float[] ar) {
		set(ar);
	}
	//------------------------------------------------------
	// set(個々セット)
	public void set(int index, float n) {
		switch (index) {
		case 11 :	m_n11 = n;	break;
		case 12 :	m_n12 = n;	break;
		case 13 :	m_n13 = n;	break;
		case 14 :	m_n14 = n;	break;
		case 21 :	m_n21 = n;	break;
		case 22 :	m_n22 = n;	break;
		case 23 :	m_n23 = n;	break;
		case 24 :	m_n24 = n;	break;
		case 31 :	m_n31 = n;	break;
		case 32 :	m_n32 = n;	break;
		case 33 :	m_n33 = n;	break;
		case 34 :	m_n34 = n;	break;
		case 41 :	m_n41 = n;	break;
		case 42 :	m_n42 = n;	break;
		case 43 :	m_n43 = n;	break;
		case 44 :	m_n44 = n;	break;
		}
	}
	// set(配列セット)
	public void set (float [] ar) {
		if (ar.length > 12) {
			m_n11 = ar[0];	m_n12 = ar[1];	m_n13 = ar[2];	m_n14 = ar[3];
			m_n21 = ar[4];	m_n22 = ar[5];	m_n23 = ar[6];	m_n24 = ar[7];
			m_n31 = ar[8];	m_n32 = ar[9];	m_n33 = ar[10];	m_n34 = ar[11];
			m_n41 = ar[12];	m_n42 = ar[13];	m_n43 = ar[14];	m_n44 = ar[15];
		}
	}
	//------------------------------------------------------
	// get(個々ゲット)
	public float get(int index) {
		switch (index) {
		case 11 :	return m_n11;
		case 12 :	return m_n12;
		case 13 :	return m_n13;
		case 14 :	return m_n14;
		case 21 :	return m_n21;
		case 22 :	return m_n22;
		case 23 :	return m_n23;
		case 24 :	return m_n24;
		case 31 :	return m_n31;
		case 32 :	return m_n32;
		case 33 :	return m_n33;
		case 34 :	return m_n34;
		case 41 :	return m_n41;
		case 42 :	return m_n42;
		case 43 :	return m_n43;
		case 44 :	return m_n44;
		}
		return 0;
	}
	// get(配列ゲット)
	public float[] get() {
		return new float[] {m_n11,m_n12,m_n13,m_n14,m_n21,m_n22,m_n23,m_n24,m_n31,m_n32,m_n33,m_n34,m_n41,m_n42,m_n43,m_n44};
	}
	//------------------------------------------------------
	// calc
	public void calc(CMatrix a, CMatrix b) {
		float[]	m1 = a.get();
		float[]	m2 = b.get();
		float[]	r = new float[m1.length];
		
		for (int i=0; i<4; ++i) {
			for (int j=0; j<4; ++j) {
				int n = i*4;
				r[n+j] = m1[n]*m2[j]+m1[n+1]*m2[j+4]+m1[n+2]*m2[j+8]+m1[n+3]*m2[j+12];
			}
		}
		set(r);
	}

	//------------------------------------------------------
	// rotation
	public static CMatrix euler2Quaternion(float ax, float ay, float az) {
		float	sinX = (float)Math.sin(ax/2);
		float	cosX = (float)Math.cos(ax/2);
		float	sinY = (float)Math.sin(ay/2);
		float	cosY = (float)Math.cos(ay/2);
		float	sinZ = (float)Math.sin(az/2);
		float	cosZ = (float)Math.cos(az/2);
		float	a = sinY * sinX;
		float	b = cosY * cosX;
		float	c = sinY * cosX;
		float	d = cosY * sinX;
		
		CMatrix	m = new CMatrix();
		
		m.set(11, cosZ * d - sinZ * c);
		m.set(12, sinZ * d + cosZ * c);
		m.set(13, sinZ * b - cosZ * a);
		m.set(14, cosZ * b + sinZ * a);
		
		return m;
	}
	//------------------------------------------------------
	// 
	public static CMatrix quaternion2Euler(float x, float y, float z, float w) {
		float xx = x * x;
		float xy = x * y;
		float xz = x * z;
		float xw = x * w;

		float yy = y * y;
		float yz = y * z;
		float yw = y * w;

		float zz = z * z;
		float zw = z * w;

		CMatrix	m = new CMatrix();
		
		m.set(11, 1 - 2 * (yy + zz));
		m.set(12, 2 * (xy - zw));
		m.set(13, 2 * (xz + yw));
		m.set(21, 2 * (xy + zw));
		m.set(22, 1 - 2 * (xx + zz));
		m.set(23, 2 * (yz - xw));
		m.set(31, 2 * (xz - yw));
		m.set(32, 2 * (yz + xw));
		m.set(33, 1 - 2 * (xx + yy));
		
		for (int i=0; i<4; ++i) {
			for (int j=0; j<4; ++j) {
				float n = m.get(i * 10 + j);
				if (Math.abs(n) < 1.0e-6)	{ m.set(i * 10 + j, 0.f); }
			}
		}
		return m;
	}
	//------------------------------------------------------
	// string
	public String toString() {
		String	s;
		s = ""+m_n11+","+m_n12+","+m_n13+","+m_n14+"\n";
		s += ""+m_n21+","+m_n22+","+m_n23+","+m_n24+"\n";
		s += ""+m_n31+","+m_n32+","+m_n33+","+m_n34+"\n";
		s += ""+m_n41+","+m_n42+","+m_n43+","+m_n44+"\n";
		
		return s;
	}
}

