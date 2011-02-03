package auditor.model;

import java.io.Serializable;

import org.eclipse.core.resources.IFile;

import at.ac.tuwien.inso.eclipse.i18n.extensions.ILocation;

public class SLLocation implements Serializable, ILocation {
	
	private static final long serialVersionUID = 1L;
	private IFile file = null;
	private int startPos = -1;
	private int endPos = -1;
	private String literal;
	private Object data;
	
	public SLLocation(IFile file, int startPos, int endPos, String literal) {
		super();
		this.file = file;
		this.startPos = startPos;
		this.endPos = endPos;
		this.literal = literal;
	}
	public IFile getFile() {
		return file;
	}
	public void setFile(IFile file) {
		this.file = file;
	}
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
	public int getEndPos() {
		return endPos;
	}
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}
	public String getLiteral() {
		return literal;
	}
	public Object getData () {
		return data;
	}
	public void setData (Object data) {
		this.data = data;
	}
	
}
