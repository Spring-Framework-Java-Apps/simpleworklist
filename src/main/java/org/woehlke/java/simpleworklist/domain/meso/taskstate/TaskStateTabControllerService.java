package org.woehlke.java.simpleworklist.domain.meso.taskstate;


import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.woehlke.java.simpleworklist.domain.db.data.Context;
import org.woehlke.java.simpleworklist.domain.meso.session.UserSessionBean;

import java.util.Locale;


public interface TaskStateTabControllerService {

  String getTaskStatePageInbox(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageToday(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageNext(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageWaiting(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageScheduled(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageFocus(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageSomeday(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageCompleted(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageTrash(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageDeleted(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

  String getTaskStatePageProjects(
    Context context,
    Pageable pageRequest,
    UserSessionBean userSession,
    Locale locale,
    Model model
  );

}
