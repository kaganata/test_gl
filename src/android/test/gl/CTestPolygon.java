package android.test.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

//import android.graphics.Canvas.VertexMode;
import android.util.Log;

/**
 * テストポリゴンクラス
 */
public class CTestPolygon {
	//======================================================
	// 変数
	private IntBuffer	m_vertexBuffer;
	private IntBuffer	m_colorBuffer;
	
	//======================================================
	// 関数
	//------------------------------------------------------
	// コンストラクタ
	public CTestPolygon() {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		
		// 固定小数点値で1.0
		int	one = 0x10000;

		// 頂点座標
		int	vertices[] = {
				-one,	one,	0,
				-one,	-one,	0,
				one,	-one,	0,
		};
		// 頂点カラー
		int	colors[] = {
				one,	0,		0,		1,
				0,		one,	0,		1,
				0,		0,		one,	1,
		};
		// ネイティブヒープ(ガベージコレクタが働かない領域)にバッファを作成
		ByteBuffer	vbb = ByteBuffer.allocateDirect(vertices.length * 4);	// 4はintのバイト数
		vbb.order(ByteOrder.nativeOrder());		// ネイティブのバイト順序の取得
		// 作成したバッファをセットし、バッファに頂点情報を書き込む
		m_vertexBuffer = vbb.asIntBuffer();
		m_vertexBuffer.put(vertices);
		m_vertexBuffer.position(0);
		
		ByteBuffer	cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		m_colorBuffer = cbb.asIntBuffer();
		m_colorBuffer.put(colors);
		m_colorBuffer.position(0);
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}
	
	//------------------------------------------------------
	// 
	public void draw(GL10 gl) {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		//			1:座標情報数 2:配列の型 3:ｵﾌｾｯﾄ指定(0だと隙間なく収まっている 4:配列へのﾎﾟｲﾝﾀ)
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, m_colorBuffer);
		// 描画
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 3);
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}
}
