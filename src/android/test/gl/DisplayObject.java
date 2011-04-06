package android.test.gl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 
 */
public class DisplayObject {
	//======================================================
	// 変数
	protected	IntBuffer	m_vertexBuffer;
	protected	IntBuffer	m_colorBuffer;
	protected	IntBuffer	m_transformBuffer;
	protected	ByteBuffer	m_indexBuffer;
	private int	m_x = 0;
	private int	m_y = 0;
	private int	m_z = 0;
	private boolean	m_visible = true;
	private boolean	m_picking = true;
	private String	m_name = "";
	protected float	m_scaleX = 1.f;
	protected float	m_scaleY = 1.f;
	protected float	m_scaleZ = 1.f;
	protected CMatrix	m_transform = new CMatrix();
	protected boolean	m_transformDirty = true;
	private float	m_rotationX = 0.f;
	private float	m_rotationY = 0.f;
	private float	m_rotationZ = 0.f;
	
	//======================================================
	// 関数
	//------------------------------------------------------
	// 
	public void update() {
		if (m_transformDirty){
			updateTransform();
		}
	}
	//------------------------------------------------------
	// 
	public void draw(GL10 gl) {
		// if (m_transformDirty)	{ updateTransform(); }
		// gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_vertexBuffer);
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_transformBuffer);
		gl.glColorPointer(4, GL10.GL_FIXED, 0, m_colorBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, m_indexBuffer.capacity(), GL10.GL_UNSIGNED_BYTE, m_indexBuffer);
	}
	//------------------------------------------------------
	// 
	public void reset() {
		m_x = 0;
		m_y = 0;
		m_z = 0;
		m_scaleX = 1.f;
		m_scaleY = 1.f;
		m_scaleZ = 1.f;
		m_rotationX = 0.f;
		m_rotationY = 0.f;
		m_rotationZ = 0.f;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void move(int x, int y, int z) {
		this.m_x = x;
		this.m_y = y;
		this.m_z = z;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void translate(int x, int y, int z) {
		this.m_x += x;
		this.m_y += y;
		this.m_z += z;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public int getX() { return m_x; }
	public int getY() { return m_y; }
	public int getZ() { return m_z; }
	//------------------------------------------------------
	// 
	public void setX(int x) {
		this.m_x = x;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void setY(int y) {
		this.m_y = y;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void setZ(int z) {
		this.m_z = z;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// ====rotation=====
	public float getRotationX()	{ return m_rotationX; }
	public float getRotationY()	{ return m_rotationY; }
	public float getRotationZ()	{ return m_rotationZ; }
	//------------------------------------------------------
	// 
	public void rotate(float ax, float ay, float az) {
		m_rotationX += ax;
		m_rotationY += ay;
		m_rotationZ += az;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void setRotationX(float r) {
		m_rotationX = r;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void setRotationY(float r) {
		m_rotationY = r;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// 
	public void setRotationZ(float r) {
		m_rotationZ = r;
		
		m_transformDirty =true;
	}
	//------------------------------------------------------
	// ====scale====
	public float getScale()	{ return m_scaleX; }
	public float getScaleX()	{ return m_scaleX; }
	public float getScaleY()	{ return m_scaleY; }
	public float getScaleZ()	{ return m_scaleZ; }
	//------------------------------------------------------
	// 
	public void setScale(float scale) {
		m_scaleX = m_scaleY = m_scaleZ = scale;
		
		m_transformDirty = true;
	}
	//------------------------------------------------------
	// ====visibility====
	public boolean getVisibility()	{ return m_visible; }
	public void setVisibility(boolean visible) {
		this.m_visible = visible;
	}
	//------------------------------------------------------
	// ====picking====
	public boolean getPicking()	{ return m_picking; }
	public void setPicking(boolean picking) {
		this.m_picking = picking;
	}
	//------------------------------------------------------
	// ====name====
	public String getName()	{ return m_name; }
	public void setName(String name) {
		this.m_name = name;
	}
	//------------------------------------------------------
	// ====transform====
	public void updateTransform() {
		CMatrix	m = CMatrix.euler2Quaternion(m_rotationX, m_rotationY, m_rotationZ);
		m = CMatrix.quaternion2Euler(m.get(11), m.get(12), m.get(13), m.get(14));
		m.set(44, 1.f);
		
		CMatrix	transform = new CMatrix(CMatrix.IDENTITY);
		transform.set(41, m_x);
		transform.set(42, m_y);
		transform.set(43, m_z);
		transform.calc(m, transform);
		
		CMatrix	scaleM = new CMatrix(CMatrix.IDENTITY);
		scaleM.set(11, m_scaleX);
		scaleM.set(22, m_scaleY);
		scaleM.set(33, m_scaleZ);
		scaleM.set(44, 1.f);
		
		this.m_transform.calc(transform, scaleM);
		
		CMatrix	vertexM = new CMatrix();
		CMatrix	r = new CMatrix();
		vertexM.set(14, 1.f);
		for (int i=0; i<m_vertexBuffer.capacity(); i+=3) {
			vertexM.set(11, (float)m_vertexBuffer.get(i));
			vertexM.set(12, (float)m_vertexBuffer.get(i+1));
			vertexM.set(13, (float)m_vertexBuffer.get(i+2));
			
			r.calc(vertexM, scaleM);
			
			m_transformBuffer.put(i, (int)r.get(11));
			m_transformBuffer.put(i+1, (int)r.get(12));
			m_transformBuffer.put(i+2, (int)r.get(13));
		}
		m_transformDirty = false;
	}
	//------------------------------------------------------
	// ====string====
	public String toString() {
		String	s;
		
		s = "x:" + m_x + "y:" + m_y + "z:" + m_z + "\n";
		s += "scaleX:" + m_scaleX + "scaleY:" + m_scaleY + "m_scaleZ:" + m_scaleZ + "\n";
		s += "rotationX:" + m_rotationX + "rotationY:" + m_rotationY + "rotationZ:" + m_rotationZ + "\n";
		
		return s;
	}
}
