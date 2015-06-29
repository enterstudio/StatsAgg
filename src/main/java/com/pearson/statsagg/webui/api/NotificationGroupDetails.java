/*
 * Copyright 2015 prashant kumar (prashant4nov).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pearson.statsagg.webui.api;

import com.pearson.statsagg.database.notifications.NotificationGroupsDao;
import com.pearson.statsagg.database.notifications.NotificationGroup;
import com.pearson.statsagg.utilities.StackTrace;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author prashant kumar (prashant4nov)
 */
@WebServlet(name="NotificationGroupDetails", urlPatterns={"/api/notificationgroup"})
public class NotificationGroupDetails extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationGroupDetails.class.getName());
    public static final String PAGE_NAME = "NotificationGroupDetails";
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return PAGE_NAME;
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("doGet");
        try {    
            JSONObject json = getNotificationGroup(request, new NotificationGroupsDao());       
            PrintWriter out = null;
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(json);
        }
        catch (Exception e) {
            logger.error(e.toString() + System.lineSeparator() + StackTrace.getStringFromStackTrace(e));
        }
    }

    private JSONObject getNotificationGroup(HttpServletRequest request, NotificationGroupsDao notificationGroupsDao) {
        logger.debug("getNotificationGroup");
        logger.debug(PAGE_NAME);
        JSONObject notificationGroupDetails = new JSONObject();
        int notificationGroupId = 0;
        try {
            if (request.getParameter(Common.notificationGroupId) != null) {
              notificationGroupId = Integer.parseInt(request.getParameter(Common.notificationGroupId));
            }
            NotificationGroup notificationGroup = notificationGroupsDao.getNotificationGroup(notificationGroupId);
            if (notificationGroup != null) {
              if (notificationGroup.getId() != null) {
                notificationGroupDetails.put("id", notificationGroup.getId());
              }
              if (notificationGroup.getName() != null) {
                notificationGroupDetails.put("name", notificationGroup.getName());
              }
              if (notificationGroup.getEmailAddresses() != null) {
                notificationGroupDetails.put("email_addresses", notificationGroup.getEmailAddresses());
              }
            } else {
                notificationGroupDetails.put(Common.error, Common.noResult);
            }
        } catch (Exception e) {
            logger.error(e.toString() + System.lineSeparator() + StackTrace.getStringFromStackTrace(e));
            notificationGroupDetails.put(Common.error, Common.errorMsg);
        }
        return notificationGroupDetails;
    }
}