package gui.redmine

import static org.viewaframework.util.ComponentFinder.find

import org.jdesktop.swingx.JXList

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.DefaultListModel
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.JTextField

import org.viewaframework.view.*
import org.viewaframework.util.*
import org.viewaframework.controller.*
import org.viewaframework.view.perspective.*
import org.viewaframework.widget.view.*
//import org.viewaframework.ioc.IOCContext

import com.taskadapter.redmineapi.RedmineManager
import com.taskadapter.redmineapi.RedmineManagerFactory
import com.taskadapter.redmineapi.bean.Project

import gui.controller.DefaultViewControllerWorker

class ListProjectController extends
    DefaultViewControllerWorker<ActionListener, ActionEvent, String, Project> {

    @Override
    Class<ActionListener> getSupportedClass() {
        return ActionListener
    }

    @Override
    void preHandlingView(ViewContainer view, ActionEvent event) {
        updateStatus("Loading list...", 50)
        viewManager.addView(new ProjectListView(), PerspectiveConstraint.RIGHT)
    }

    @Override
    void handleView(ViewContainer view, ActionEvent event) {
        def redmineAddress = getContextAttribute("redmineUrl")
        def redmineApiKey = getContextAttribute("redmineApiKey")
        def redmineManager = new RedmineManager(redmineAddress, redmineApiKey)

        publish(redmineManager.projects)
    }

    @Override
    void handleViewPublising(ViewContainer view, ActionEvent event, List<Project> chunks) {
        locate(ProjectListView)
            .named(ProjectListView.ID)
            .model
            .addAll(chunks.flatten())
    }

    @Override
    void postHandlingView(ViewContainer viewContainer, ActionEvent event) {
        def rows = locate(ProjectListView)
            .named(ProjectListView.ID)
            .model
            .rowCount

        updateStatus("Showing $rows Redmine projects ", 0)
    }

    void updateStatus(String message, Integer progress) {
        def progressBar = find(JProgressBar).in(viewManager.rootView).named(StatusBar.STATUS_BAR_NAME)
        def label = find(JLabel).in(viewManager.rootView).named(StatusBar.LEFT_PANEL_LABEL)

        progressBar.value = progress
        progressBar.stringPainted = true
        label.text = message
    }

    //def getSpringContext() {
    //    return viewManager.application.applicationContext.getAttribute(IOCContext.ID)
    //}

}
