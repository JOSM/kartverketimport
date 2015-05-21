package org.openstreetmap.josm.plugins.kartverket;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.MainMenu;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.plugins.kartverket.actions.CheckDirectionAction;

public class KartverketPlugin extends Plugin {
	JMenuItem CheckDirection;
     /**
      * Will be invoked by JOSM to bootstrap the plugin
      *
      * @param info  information about the plugin and its local installation    
      */
      public KartverketPlugin(PluginInformation info) {
         super(info);
         JMenu toolsMenu = Main.main.menu.moreToolsMenu;
         CheckDirection = MainMenu.add(toolsMenu, new CheckDirectionAction());
      }
      
      @Override
      public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) { 
    	  boolean enabled = newFrame != null;
          enabled = false;
          CheckDirection.setEnabled(enabled);
      }
  }