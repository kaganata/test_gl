package android.test.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public class CTestBox {
	//======================================================
	// 変数
	private IntBuffer	m_vertexBuffer;
	private IntBuffer	m_colorBuffer;
//	private ShortBuffer	m_indexBuffer;
	private ByteBuffer	m_indexBuffer;
	
	//======================================================
	// 関数
	//------------------------------------------------------
	// コンストラクタ
	public CTestBox() {
		// 固定小数点値で1.0
		int one = 0x10000;
		
		// 頂点座標
		int vertices[] = {
///*
			-one,	one,	one,
			-one,	-one,	one,
			one,	one,	one,
			one,	-one,	one,
			
			-one,	one,	-one,
			-one,	-one,	-one,
			one,	one,	-one,
			one,	-one,	-one,
//*/
		};
		// 頂点カラー
		int colors[] = {
				one,	0,		one,	1,
				one,	one,	0,		1,
				0,		one,	one,	1,
				one,	0,		one,	1,
				one,	one,	0,		1,
				0,		one,	one,	1,
				one,	0,		one,	1,
				one,	one,	0,		1,

		};
		// インデックス
		byte indeces[] = {
				// 
				0,	1,	2,
				1,	3,	2,
				// 
				0,	4,	1,
				4,	5,	1,
				// 
				1,	5,	3,
				5,	7,	3,
				// 
				3,	7,	2,
				7,	6,	2,
				// 
				0,	2,	4,
				2,	6,	4,
				// 
				5,	4,	6,
				5,	6,	7,
		};
		
		// ネイティブヒープにバッファ作成
		ByteBuffer	vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		// 作成したバッファをセットし、バッファに頂点情報を書き込む
		m_vertexBuffer = vbb.asIntBuffer();
		m_vertexBuffer.put(vertices);
		m_vertexBuffer.position(0);
		
		ByteBuffer	cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		m_colorBuffer = cbb.asIntBuffer();
		m_colorBuffer.put(colors);
		m_colorBuffer.position(0);
		
		//ByteBuffer	ibb = ByteBuffer.allocateDirect(indeces.length);
		m_indexBuffer = ByteBuffer.allocateDirect(indeces.length);
		//ibb.order(ByteOrder.nativeOrder());
		//m_indexBuffer = ibb.asShortBuffer();
		m_indexBuffer.put(indeces);
		m_indexBuffer.position(0);
	}
	
	//------------------------------------------------------
	// 
	public void draw(GL10 gl) {
		// 
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, m_colorBuffer);
		// 描画
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, m_indexBuffer);
	}
}
