/*
 * VisualModeLineWrappingDialog.java
 *
 * Copyright (C) 2020 by RStudio, PBC
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */


package org.rstudio.studio.client.workbench.views.source.editors.text.visualmode.dialogs;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import org.rstudio.core.client.widget.ModalDialog;
import org.rstudio.core.client.widget.Operation;
import org.rstudio.core.client.widget.OperationWithInput;
import org.rstudio.studio.client.common.HelpLink;
import org.rstudio.studio.client.workbench.prefs.model.UserPrefsAccessor;
import org.rstudio.studio.client.workbench.views.source.editors.text.visualmode.VisualModeConfirm;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VisualModeConfirmLineWrappingDialog extends ModalDialog<VisualModeConfirm.LineWrappingAction>
{   
   public VisualModeConfirmLineWrappingDialog(
      String detectedLineWrapping,
      String configuredLineWrapping,
      boolean isProjectConfig,
      boolean haveProject,
      OperationWithInput<VisualModeConfirm.LineWrappingAction> onConfirm,
      Operation onCancel)
   {
      super("Line Wrapping Mismatch", 
            Roles.getDialogRole(), 
            onConfirm, 
            onCancel);
   
      
      
      mainWidget_ = new VerticalPanel();
      mainWidget_.setSpacing(10);
      mainWidget_.addStyleName(RES.styles().confirmLineWrappingDialog());
      
      Label mismatch = new Label("Line wrapping mismatch detected:");
      mainWidget_.add(mismatch);
      
      SafeHtmlBuilder builder = new SafeHtmlBuilder();
      builder.appendHtmlConstant("<ul>");
      builder.appendHtmlConstant("<li style=\"margin-bottom: 10px;\">");
      builder.appendEscaped("This document appears to use ");
      builder.appendEscaped(detectedLineWrapping);
      builder.appendEscaped("-based line wrapping"); 
      builder.appendHtmlConstant("</li>");
      builder.appendHtmlConstant("<li style=\"margin-bottom: 3px;\">");
      if (isProjectConfig)
         builder.appendEscaped("The current project is configured with ");
      else
         builder.appendEscaped("The current global option is set to ");
      if (configuredLineWrapping.equals(UserPrefsAccessor.VISUAL_MARKDOWN_EDITING_WRAP_NONE))
         builder.appendEscaped("no ");
      else 
         builder.appendEscaped(configuredLineWrapping + "-based");
      builder.appendEscaped("line wrapping");
      builder.appendHtmlConstant("</li>");
      builder.appendHtmlConstant("</ul>");
      
      
      mainWidget_.add(new HTML(builder.toSafeHtml()));
      
      StringBuilder msg = new StringBuilder();
      msg.append("You can either continue using ");
      msg.append(detectedLineWrapping + "-based line wrapping, ");
      msg.append("or alternatively adopt the ");
      msg.append(isProjectConfig ? "project" : "global");
      msg.append(" default.");
      
      Label choiceLabel = new Label("Select your preference for line wrapping below:");
      mainWidget_.add(choiceLabel);
            
    
      chkConfigureFile_ = lineWrappingRadio( 
         "Use " + detectedLineWrapping + "-based line wrapping for this document"
      );
      chkConfigureFile_.setValue(true);
      mainWidget_.add(chkConfigureFile_);
      chkConfigureProject_= lineWrappingRadio(
         "Use " + detectedLineWrapping + "-based line wrapping for this project"
      );      
      if (haveProject)
         mainWidget_.add(chkConfigureProject_);
      
      chkConfigureNone_ = lineWrappingRadio(
         "Use the " + (isProjectConfig ? "project" : "global") + " default (" + 
         (configuredLineWrapping.equals(UserPrefsAccessor.VISUAL_MARKDOWN_EDITING_WRAP_NONE) 
           ? "no"
           : configuredLineWrapping + "-based") +
         " line wrapping) for this document"
      );
      mainWidget_.add(chkConfigureNone_);
      
      
      HelpLink lineWrappingHelp = new HelpLink(
         "Learn more about visual mode line wrapping options",
         "visual_markdown_editing-line-wrapping",
         false
      );
      lineWrappingHelp.addStyleName(RES.styles().lineWrappingHelp());
      mainWidget_.add(lineWrappingHelp);
      
   }
   
  
   @Override
   protected Widget createMainWidget()
   {
      return mainWidget_;
   }
   
  
   
   @Override
   protected VisualModeConfirm.LineWrappingAction collectInput()
   {
      if (chkConfigureFile_.getValue())
         return VisualModeConfirm.LineWrappingAction.SetFileLineWrapping;
      else if (chkConfigureProject_.getValue())
         return VisualModeConfirm.LineWrappingAction.SetProjectLineWrapping;
      else
         return VisualModeConfirm.LineWrappingAction.SetNothing;
   }


   @Override
   protected boolean validate(VisualModeConfirm.LineWrappingAction result)
   {
      return true;  
   }
   
  
   private RadioButton lineWrappingRadio(String caption)
   {
      final String kRadioGroup = "DDFEDF81-87F7-45E8-B5FC-E021FC41FC69";
      RadioButton radio = new RadioButton(kRadioGroup, caption);
      radio.addStyleName(RES.styles().lineWrappingRadio());
      return radio;
   }
   
   private VerticalPanel mainWidget_; 
   private RadioButton chkConfigureFile_;
   private RadioButton chkConfigureProject_;
   private RadioButton chkConfigureNone_;
   
   
   private static VisualModeDialogsResources RES = VisualModeDialogsResources.INSTANCE;
   
}
