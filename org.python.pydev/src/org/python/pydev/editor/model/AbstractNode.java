/*
 * Author: atotic
 * Created on Apr 7, 2004
 * License: Common Public License v1.0
 */
package org.python.pydev.editor.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.python.pydev.plugin.PydevPlugin;

/**
 * ModelNode is a superclass of all nodes.
 * It knows its location, parent & children.
 * 
 * Nodes can have properties that are useful when querying
 * For example, clickable nodes have PROP_CLICKABLE set.
 */
public abstract class AbstractNode {

	static ArrayList emptyChildList = new ArrayList(); // we keep an empty list around for efficiency
	public static int PROP_CLICKABLE = 1;	// if the node can be hyperlinked
	public static int PROP_ANY = 0; // all properties
	
	protected Location start;	// line/offset start of the text that defines this node
	protected Location end;		// end of node text
	protected AbstractNode parent;
	protected ArrayList children;	
	int properties = 0;			// a multi-valued integer, can be PROP_CLICKABLE, or others later
								// properties should be set in the initializer

	/**
	 * @param parent can be null
	 */
	public AbstractNode(AbstractNode parent) {
		this.parent = parent;
		if (parent != null)
			parent.addChild(this);
	}
		
	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}
	
	/**
	 * @return an unmodifiable list of children.
	 * It is UNMODIFIABLE because if list is empty, we return a static empty list
	 * to save memory/speed.
	 */
	public ArrayList getChildren() {
		if (children == null)
			return emptyChildList;
		return children;
	}

	public void addChild(AbstractNode child) {
		if (children == null)
			children = new ArrayList();
		children.add(child);
	}
	
	public AbstractNode getParent() {
		return parent;
	}
	
	/**
	 * Scope is where the local variables are. Module/Class/Functions have scope,
	 * all others inherit from the parent.
	 */
	public Scope getScope() {
		if (parent != null)
			return parent.getScope();
		return null;
	}
	
	public int getProperties() {
		return properties;
	}
	
	public String toString() {
		return getClass().toString() + " " + start.toString() + end.toString();
	}

	/**
	 * Subclasses should override. This gets the python string.
	 */
	public abstract String getName();

	public IFile getFile() {
		return parent.getFile();
	}
	
	/**
	 * This function is a heuristic solution for the way jython & TextEditor
	 * deal with column numbers.
	 * 
	 * jython's parser converts tabs to spaces internally. 
	 * When it reports the column, it reports the column after the spaces
	 * have been converted to tabs. So in "\tFunc()", func starts in column
	 * 5 according to jython's parser, and in column 1 according to text editor.
	 * 
	 * This fix tries to convert jython's column numbers to those that work for editor.
	 * To do this, we get the whole line where location was defined, and
	 * substract the spaces assumed by jython (8 per tab).
	 */
	public void fixColumnLocation(Location loc, String lineText) {
		int where = 0;
		int tabCount = 0;
		where = lineText.indexOf("\t", where);
		while (where != -1 && where <= loc.column) {
			where = lineText.indexOf("\t", where+1);
			tabCount++;
		}
		if (tabCount > 0)
			loc.column = loc.column - tabCount * 7;
		if (loc.column < 0) {
			loc.column = 0;
			PydevPlugin.log(IStatus.ERROR, "Unexpected columnFixLocation error", null);
		}
	}
}
