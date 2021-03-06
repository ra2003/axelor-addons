package com.axelor.apps.rossum.web;

import com.axelor.apps.base.db.AppRossum;
import com.axelor.apps.base.db.repo.AppRossumRepository;
import com.axelor.apps.rossum.service.app.AppRossumService;
import com.axelor.apps.rossum.service.organisation.OrganisationService;
import com.axelor.apps.rossum.service.queue.QueueService;
import com.axelor.apps.rossum.service.schema.SchemaService;
import com.axelor.apps.rossum.service.workspace.WorkspaceService;
import com.axelor.exception.AxelorException;
import com.axelor.exception.ResponseMessageType;
import com.axelor.exception.service.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.io.IOException;
import org.apache.http.ParseException;
import wslite.json.JSONException;

public class AppRossumController {

  public void login(ActionRequest request, ActionResponse response) {
    AppRossum appRossum =
        Beans.get(AppRossumRepository.class)
            .find(request.getContext().asType(AppRossum.class).getId());

    try {
      Beans.get(AppRossumService.class).login(appRossum);
      Beans.get(OrganisationService.class).getOrganisations(appRossum);
      Beans.get(WorkspaceService.class).getWorkspaces(appRossum);
      Beans.get(SchemaService.class).getSchemas(appRossum);
      Beans.get(QueueService.class).getQueues(appRossum);

      response.setReload(true);
    } catch (ParseException | IOException | JSONException | AxelorException e) {
      TraceBackService.trace(response, e, ResponseMessageType.ERROR);
    }
  }

  public void reset(ActionRequest request, ActionResponse response) {
    AppRossum appRossum =
        Beans.get(AppRossumRepository.class)
            .find(request.getContext().asType(AppRossum.class).getId());

    Beans.get(AppRossumService.class).reset(appRossum);
    response.setReload(true);
  }
}
