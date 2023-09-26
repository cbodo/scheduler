package com.cbodo.schedule.controller;

import com.cbodo.schedule.DAO.ReportsDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * This class handles the logic for displaying the data reports in the UI.
 */
public class ReportsController {
    /**
     * TextArea for displaying the report for each customer's totals by month.
     */
    @FXML private TextArea reportTextArea;
    /**
     * TextArea for displaying the report for each customer's average meeting length.
     */
    @FXML private TextArea durationTextArea;

    /**
     * Initializes the reports and displays the results in the UI.
     * @param controller MainViewController instance.
     */
    public void init(MainViewController controller) throws Exception {
        controller.setReportsController(this);
        reportTextArea.setText(ReportsDAO.buildReport());
        durationTextArea.setText(ReportsDAO.durationReport());
    }

    /**
     * Updates reports after changes in database.
     */
    public void updateReports() throws Exception {
        reportTextArea.setText(ReportsDAO.buildReport());
        durationTextArea.setText(ReportsDAO.durationReport());
    }

}
