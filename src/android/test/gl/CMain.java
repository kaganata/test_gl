package android.test.gl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;

public class CMain extends Activity implements GLSurfaceView.Renderer{
	/** Called when the activity is first created. */
	//======================================================
	// 変数
	private GLSurfaceView	m_glSurfaceView;	// GLSurfaceView
	private CTestPolygon	m_trianglePolygon;	// テストポリゴン
	private CTestBox		m_boxPolygon;
	
	private int				m_afterX;			// 現在x座標
	private int				m_afterY;			// 現在y座標
	private int				m_beforeX;			// 前x座標
	private int				m_beforeY;			// 前y座標
	
	private int				m_motionAction;			// 
	
	//======================================================
	// 関数
	
	/**------------------------------------------------------
	 * func:	アクティビティ生成時呼び出し
	 * param1:	遷移状態 
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		
		// タイトルバーを消す
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// GLSurfaceViewを生成
		m_glSurfaceView = new GLSurfaceView(this);
		// レンダラーを生成してセット
		m_glSurfaceView.setRenderer(this);
		
		// レイアウトのリソース参照は渡さず,直接Viewオブジェクトを渡す
		// setContentView(R.layout.main);
		setContentView(m_glSurfaceView);
		
		// ポリゴンの初期化
		//m_trianglePolygon = new CTestPolygon();
		m_boxPolygon = new CTestBox();
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		// Log.d(this.getClass().getName(), Thread.currentThread().getStackTrace()[0].getMethodName());
	}

	/**------------------------------------------------------
	 * func:	サーフェイスが生成される際、または再生成される際に呼ばれる
	 * param1:	OpenGL10
	 * param2:	EGL(依存部分と非依存部分のブリッジのような役割)
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		
		// ディザ(ギザギザを目立たなくさせる)を無効化
		gl.glDisable(GL10.GL_DITHER);
		// カラーとテクスチャ座標の補間制度を、最も効率的なものに指定
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		// バッファ初期化時のカラー情報をセット
		gl.glColor4x(0, 0, 0, 1);
		// 片面表示を有効に
		gl.glEnable(GL10.GL_CULL_FACE);
		// カリング設定をCCWに
		gl.glFrontFace(GL10.GL_CCW);
		
		// 深度テストを有効に
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// スムースシェーディングにセット
		gl.glShadeModel(GL10.GL_SMOOTH);
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}
	
	/**------------------------------------------------------
	 * func:	サーフェイスのサイズ変更時に呼ばれる
	 * param1:	OpenGL10
	 * param2:	サイズ変更後の横幅
	 * param3:	サイズ変更後の縦幅
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		
		// ビューポートをサイズに合わせてセットしなおす
		gl.glViewport(0, 0, width, height);
		
		// アスペクトレート
		float ratio = (float)width / height;
		
		// 射影行列を選択
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// 現在選択されている行列(射影行列)に、単位行列をセット
		gl.glLoadIdentity();
		// 透視投影用の錐台のパラメータをセット
		gl.glFrustumf(-ratio, ratio, -1.f, 1.f, 1.f, 10.f);
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}

	/**------------------------------------------------------
	 * func:	描画の為に毎フレーム呼び出されるイベント
	 * param1:	OpenGL10
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	public void onDrawFrame(GL10 gl) {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		// 描画用バッファをクリア
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// カメラ位置をセット
		GLU.gluLookAt(gl, 
					0.f, 2.f, 5.f, 		// カメラ位置座標
					0.f, 0.f, 0.f, 		// 注視点座標
					0.f, 1.f, 0.f);		// 上方向ベクトル
		
		// 頂点配列を使うことを宣言
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// 色情報配列を使うことを宣言
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		// 2Dテクスチャを無効に
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
		// モデルビュー行列を指定
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// 現在選択されている行列(モデルビュー行列)に、単位行列をセット
		gl.glLoadIdentity();

		/* モデルの回転
		long time = SystemClock.uptimeMillis() % 4000L;
		float angle = 0.090f * ((int)time);
		gl.glRotatef(angle, 0, 1.f, 0);
		 //*/
		
		// ポリゴンの描画メソッドを呼ぶ
		//m_trianglePolygon.draw(gl);
		m_boxPolygon.draw(gl);

		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}

	/**------------------------------------------------------
	 * func:	ポーズ状態からの復旧時や、アクティビティ生成時などに呼ばれる
	 * param1:	none
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	protected void onResume() {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		
		super.onResume();
		m_glSurfaceView.onResume();
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}

	/**------------------------------------------------------
	 * func:	アクティビティ一時停止時や、終了時に呼ばれる
	 * param1:	none
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	protected void onPause() {
		Log.d(this.getClass().getName(), "enter"+new Throwable().getStackTrace()[0].getMethodName()+"()");
		
		super.onPause();
		m_glSurfaceView.onPause();
		
		Log.d(this.getClass().getName(), "exit"+new Throwable().getStackTrace()[0].getMethodName()+"()");
	}
	/**------------------------------------------------------
	 * func:	タッチイベント(画面、トラックボール等の操作を行なったとき
	 * param1:	イベント情報
	 * ret:		none
	 ------------------------------------------------------*/
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
/*
		// 座標の取得
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
*/
		m_motionAction = event.getAction();
		
		return super.onTouchEvent(event);
	}

}

