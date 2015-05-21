package org.openstreetmap.josm.plugins.kartverket.actions;

import static org.openstreetmap.josm.gui.help.HelpUtil.ht;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.ExtendedDialog;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.plugins.kartverket.CheckDirectionDialog;
import org.openstreetmap.josm.plugins.kartverket.CheckNextWayI;

public class CheckDirectionAction extends JosmAction implements CheckNextWayI {
	private int nWaysCompleted;
	private int nWaysFixme;
	Collection<Way> ways;
	Iterator<Way> waysIterator;
	Way w;
	
	public CheckDirectionAction() {
		super(tr("Check direction of streams"), null,
				tr("Check direction of streams and rivers"), null, true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isEnabled())
			return;
		ways = getCurrentDataSet().getWays();
		nWaysFixme = 0;
		nWaysCompleted = 0;
		for (Way w : ways) {
			if (w.hasTag("FIXME", "Check direction of river/stream")) {
				nWaysFixme ++;
			}
		}
		
		waysIterator = ways.iterator();
		nextWay();

	}
	
	private void nextWay() {
		while (waysIterator.hasNext())
		{
			w = waysIterator.next();
			if (w.hasTag("FIXME", "Check direction of river/stream")) {
				getCurrentDataSet().clearSelection();
				getCurrentDataSet().addSelected(w.getPrimitiveId());
				BoundingXYVisitor boundingVisitor = new BoundingXYVisitor();
				boundingVisitor.visit(w);
				boundingVisitor.enlargeToMinSize(2000.);
				Main.map.mapView.zoomTo(boundingVisitor);
				CheckDirectionDialog dialog = new CheckDirectionDialog(this,nWaysCompleted/(1.*nWaysFixme));
				dialog.makeVisible();
				break;
			}
		}
		Notification note = new Notification(tr("No more directions to check!"));
		note.show();
		
	}
	

	@Override
	public void wayDirectionIsCorrect() {
		w.remove("FIXME");
		nWaysCompleted++;
		nextWay();
	}

	@Override
	public void wayDirectionIsWrong() {
		List<Node> nd = w.getNodes();
		Collections.reverse(nd);
		w.setNodes(nd);
		wayDirectionIsCorrect();
	}

	@Override
	public void wayDirectionIgnore() {
		nWaysCompleted++;
		nextWay();
	}
	
    @Override
    protected void updateEnabledState() {
        if (getCurrentDataSet() == null) {
            setEnabled(false);
        } else {
        	setEnabled(true);
        }
    }

}
